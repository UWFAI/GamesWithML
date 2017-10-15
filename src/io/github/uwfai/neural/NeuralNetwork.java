package io.github.uwfai.neural;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.uwfai.neural.function.CostFunction;
import io.github.uwfai.neural.function.InitializationFunction;
import io.github.uwfai.neural.function.RegularizationFunction;
import io.github.uwfai.neural.layer.Layer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
* NeuralNetwork library
* 
* Created for the UWF AIRG, this library implements the fundamentals
* of neural networks - simple feedforward networks, the backpropogation
* and weight/bias update algorithms, and (soon) Convolutional Neural
* Networks (CNNS.)
* 
* @author UWF AI Research Group
* @version 1.0
* @since 2017-3-3
*/
public class NeuralNetwork {
	private ArrayList<Layer> layers = new ArrayList<>();
	private CostFunction cost;
	private RegularizationFunction regularization;
	private Random gen = new Random();
	
	/*
	* This function simply feeds the inputs forward through the network and returns the output
	* vaues. This is primarily for our testing purposes to determine the accuracy of our network.
	*/
	
	public Matrix feedforward(Matrix activations, Matrix zs, Matrix as) {
		for (Layer layer : this.layers) {
			activations = layer.feedforward(activations, zs, as);
		}
		return activations;
	}
	
	/*
	* If no activation and input matrices are defined, this function produces new matrices that
	* are then forgotten/emptied when the feedforward is complete, simply returning the output
	* layer matrix.
	*/
	
	public Matrix feedforward(Matrix activations) {
		return this.feedforward(activations, new Matrix(), new Matrix());
	}
	
	/*
	* This is where the magic happens. In this function, our neural network actually learns how
	* drastically it needs to change each weight in bias in order to reduce our cost. It returns
	* this 'gradient' to be used by the parent function, normally the 'batch' function.
	*/
	
	public ArrayList set(Matrix data, Matrix answers) {
		Matrix zs = new Matrix();
		Matrix as = new Matrix();
		Matrix rs = this.feedforward(data, zs, as);
		Matrix error = new Matrix();

		error.prepend(cost.derivative(answers, as.getm(as.size()-1)));
		
		for (int layer = this.layers.size()-1; layer > 0; --layer) {
			Matrix adjust = new Matrix();
			for (int neuron = 0; neuron < this.layers.get(layer).size(); ++neuron) {
				adjust.append(this.layers.get(layer).activationDerivative(zs.getm(layer).getd(neuron)));
			}
			error.set(0, error.getm(0).product(adjust));
			
			if (layer > 0) {
				Matrix result = new Matrix();
				for (int neuron = 0; neuron < this.layers.get(layer-1).size(); ++neuron) {
					result.append(error.getm(0)
							.product(
								this.layers.get(layer).getWeights().column(neuron)
							)
							.sum());
				}
				error.prepend(result);
			}
		}
		
		ArrayList gradient = new ArrayList();
		Matrix nabla_b = new Matrix(error);
		Matrix nabla_w = new Matrix();
		for (int layer = 1; layer < this.layers.size(); ++layer) {
			nabla_w.append(new Matrix());
			for (int neuron = 0; neuron < this.layers.get(layer).size(); ++neuron) {
				nabla_w.getm(nabla_w.size()-1).append(new Matrix());
				for (int weight = 0; weight < ((Matrix)this.layers.get(layer).getWeights().get(neuron)).size(); ++weight) {
					nabla_w.getm(nabla_w.size()-1).getm(nabla_w.getm(nabla_w.size()-1).size()-1).append(
						as.getm(layer-1).getd(weight)*error.getm(layer).getd(neuron)
					);
				}
			}
		}
		gradient.add(nabla_b);
		gradient.add(nabla_w);
		return gradient;
	}
	
	/*
	* For each of a set of training examples and answers, this functions calculates the gradient
	* and updates each layer with it. We always want to take the average over a batch of inputs,
	* since feeding the network one example at a time can overtrain a network to one specific
	* example accidentally.
	*/
	
	public void batch(Matrix data, Matrix answers, int datasize, double eta, double lambda) {
		Matrix nabla_b = new Matrix();
		Matrix nabla_w = new Matrix();
		for (int set = 0; set < data.size(); ++set) {
			ArrayList sgradient = this.set(data.getm(set), answers.getm(set));
			if (set == 0) {
				nabla_b = (Matrix)sgradient.get(0);
				nabla_w = (Matrix)sgradient.get(1);
			} else {
				nabla_b = nabla_b.add((Matrix)sgradient.get(0));
				nabla_w = nabla_w.add((Matrix)sgradient.get(1));
			}
		}
		for (int layer = 1; layer < this.layers.size(); ++layer) {
			Matrix w = this.layers.get(layer).getWeights();
			Matrix nb = nabla_b.getm(layer).product(nabla_b.getm(layer).shape().fill(eta/(double)datasize));
         Matrix nw = nabla_w.getm(layer-1).product(nabla_w.getm(layer-1).shape().fill(eta/(double)datasize)).add(w.mapply((j) -> this.regularization.derivative(j).product(j.shape().fill(lambda*eta/(double)datasize))));
         this.layers.get(layer).update(nb, nw);
		}
	}
	
	/*
	* An epoch is a set of batches to train our network. It is here that the training data is
	* separated from the test data, to ensure that our network isn't tested against the same
	* examples that it used to train. This ensures the network isn't being tested for what it
	* already knows, but rather what it should have learned based on its extrapolation from the
	* test data.
	*/
	
	public void epoch(Matrix data, Matrix answers, int batchsize, double eta, double lambda) {
		int batches = (int)Math.floor((double)data.size()/(double)batchsize);
		for (int batch = 0; batch < batches; ++batch) {
			Matrix bdata = new Matrix();
			Matrix badata = new Matrix();
			
			for (int i = 0; i < batchsize; ++i) {
				bdata.append(new Matrix(data.getm((batch*batchsize)+i)));
				badata.append(new Matrix(answers.getm((batch*batchsize)+i)));
			}
			
			this.batch(bdata, badata, data.size(), eta, lambda);
		}
	}
	
	/*
	* If we don't need direct control over our network, we can use this train function. It
	* performs all of the above and gives us how accurate our network is without too much effort.
	*/
	
	public void train(Matrix data, Matrix answers, int epochs, int batchsize, double eta, double lambda) {
		for (int epoch = 0; epoch < epochs; ++epoch) {
			Matrix tdata = new Matrix(data);
			Matrix tadata = new Matrix(answers);
			
			for (int i = 0; i < tdata.size(); ++i) {
				int index1 = (int)Math.floor(this.gen.nextDouble()*tdata.size());
				int index2 = (int)Math.floor(this.gen.nextDouble()*tdata.size());
				Matrix swap = new Matrix(tdata.getm(index1));
				tdata.set(index1, new Matrix(tdata.getm(index2)));
				tdata.set(index2, swap);
				swap = new Matrix(tadata.getm(index1));
				tadata.set(index1, new Matrix(tadata.getm(index2)));
				tadata.set(index2, swap);
			}
			
			this.epoch(tdata, tadata, batchsize, eta, lambda);
		}
	}

	public double evaluate(Matrix data, Matrix answers) {
      double total = 0.0d;
      for (int i = 0; i < data.size(); ++i) {
         Matrix a = this.feedforward(data.getm(i));
         total += this.cost.cost(answers.getm(i), a);
      }
      double reg = 0.0d;
      for (int l = 0; l < this.layers.size(); ++l) {
	      Matrix w = this.layers.get(l).getWeights();
	      for (int n = 0; n < w.size(); ++n) {
		      total += this.regularization.regularize(w.getm(n));
	      }
      }
      return (total/data.size())+reg;
   }

	public String json() {
      Gson gson = new Gson();
      return gson.toJson(this);
	}

	public void save(String path, boolean append) {
		try
		{
			File f = new File(path);
         if (!f.exists()) {
            f.createNewFile();
         }
			FileWriter fw = new FileWriter(f, append);
         BufferedWriter bw = new BufferedWriter(fw);
         bw.write(this.json());
         bw.close();
         fw.close();
		} catch (Exception e) {

		}
	}
	
	public NeuralNetwork(long seed, int inputSize, InitializationFunction initialization, RegularizationFunction regularization, CostFunction cost, Layer[] layers) {
		this.gen.setSeed(seed);

		this.cost = cost;
		this.regularization = regularization;

		this.layers = new ArrayList<>(Arrays.asList(layers));

		int totalSize = 0;
		for (Layer layer : this.layers) {
			totalSize += layer.size();
		}

		this.layers.get(0).initialize(initialization, new int[]{inputSize, 1}, gen, totalSize);

		for (int layer = 1; layer < this.layers.size(); ++layer) {
			this.layers.get(layer).initialize(initialization, this.layers.get(layer-1).dimensions(), gen, totalSize);
		}
	}
	
	public NeuralNetwork(int inputSize, InitializationFunction initialization, RegularizationFunction regularization, CostFunction cost, Layer[] layers) {
		this(0L, inputSize, initialization, regularization, cost, layers);
	}

	public static NeuralNetwork Load(String JSON) {
      Gson gson = new Gson();
      return ((NeuralNetwork)gson.fromJson(JSON, new TypeToken<NeuralNetwork>(){}.getType()));
   }

	static String readFile(String path, Charset encoding)
			throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

   public static NeuralNetwork LoadFromFile(String FILE) {
	   Gson gson = new Gson();
	   String JSON = "";
	   try {
		   JSON = readFile(FILE, Charset.defaultCharset());
	   } catch (IOException e) {
			System.err.format("Failed to load NeuralNetwork from file: %s\n", e.getMessage());
		   e.printStackTrace();
	   }
	   return ((NeuralNetwork)gson.fromJson(JSON, new TypeToken<NeuralNetwork>(){}.getType()));
   }
}