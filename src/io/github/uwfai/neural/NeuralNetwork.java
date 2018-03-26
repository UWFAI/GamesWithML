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
import java.util.List;
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
	
	public Matrix feedforward(Matrix activations, List<Matrix> zs, List<Matrix> as)
   {
		for (Layer layer : this.layers)
		{
			activations = layer.feedforward(activations, zs, as);
		}

		return activations;
	}
	
	/*
	* If no activation and input matrices are defined, this function produces new matrices that
	* are then forgotten/emptied when the feedforward is complete, simply returning the output
	* layer matrix.
	*/
	
	public Matrix feedforward(Matrix activations)
   {
		return this.feedforward(activations, new ArrayList<>(), new ArrayList<>());
	}
	
	/*
	* This is where the magic happens. In this function, our neural network actually learns how
	* drastically it needs to change each weight in bias in order to reduce our cost. It returns
	* this 'gradient' to be used by the parent function, normally the 'batch' function.
	*/
	
	public ArrayList<Matrix>[] set(Matrix data, Matrix answers)
   {
		ArrayList<Matrix> zs = new ArrayList<>();
		ArrayList<Matrix> as = new ArrayList<>();
		Matrix rs = this.feedforward(data, zs, as);
		ArrayList<Matrix> error = new ArrayList<>();

		error.add(0, cost.derivative(answers, as.get(as.size()-1)));
		
		for (int layer = this.layers.size()-1; layer > 0; --layer)
		{
			error.set(0, error.get(0).multiply(this.layers.get(layer).getActivation().derivative(zs.get(layer))));

			error.add(0, this.layers.get(layer).getWeights().transpose().dot(error.get(0)).sum(1));
		}

		ArrayList<Matrix> nabla_w = new ArrayList<>();

		for (int layer = 1; layer < this.layers.size(); ++layer)
		{
			nabla_w.add(new Matrix(this.layers.get(layer).getWeights().getRows(), this.layers.get(layer).getWeights().getColumns()));

			for (int neuron = 0; neuron < this.layers.get(layer).size(); ++neuron)
			{
				for (int weight = 0; weight < this.layers.get(layer).getWeights().getColumns(); ++weight)
				{
					nabla_w.get(nabla_w.size()-1).set(neuron, weight, as.get(layer-1).get(weight, 0)* error.get(layer).get(neuron, 0));
				}
			}
		}

		ArrayList[] ret = new ArrayList[2];
      ret[0] = error;
      ret[1] = nabla_w;

      return ret;
	}
	
	/*
	* For each of a set of training examples and answers, this functions calculates the gradient
	* and updates each layer with it. We always want to take the average over a batch of inputs,
	* since feeding the network one example at a time can overtrain a network to one specific
	* example accidentally.
	*/
	
	public void batch(List<Matrix> data, List<Matrix> answers, int datasize, double eta, double lambda) {
		List<Matrix> nabla_b = new ArrayList<>();
		List<Matrix> nabla_w = new ArrayList<>();

		for (int set = 0; set < data.size(); ++set)
		{
			List<Matrix>[] sgradient = this.set(data.get(set), answers.get(set));

			if (set == 0)
			{
				nabla_b = sgradient[0];
				nabla_w = sgradient[1];
			}
			else
			{
				for (int nabla = 0; nabla < sgradient[0].size(); ++nabla)
				{
					nabla_b.set(nabla, nabla_b.get(nabla).add(sgradient[0].get(nabla)));
				}

				for (int nabla = 0; nabla < sgradient[1].size(); ++nabla)
				{
					nabla_w.set(nabla, nabla_w.get(nabla).add(sgradient[1].get(nabla)));
				}
			}
		}

		for (int layer = 1; layer < this.layers.size(); ++layer)
		{
			Matrix w = this.layers.get(layer).getWeights();
			Matrix nb = nabla_b.get(layer).multiply(nabla_b.get(layer).shape().fill(eta/(double)datasize));
         Matrix nw = nabla_w.get(layer-1).multiply(nabla_w.get(layer-1).shape().fill(eta/(double)datasize)).add(this.regularization.derivative(w).multiply(w.shape().fill(lambda*eta/(double)datasize)));
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
	
	public void epoch(List<Matrix> data, List<Matrix> answers, int batchsize, double eta, double lambda)
   {
		int batches = (int)Math.floor((double)data.size()/(double)batchsize);

		for (int batch = 0; batch < batches; ++batch)
		{
			List<Matrix> bdata = new ArrayList<>();
			List<Matrix> badata = new ArrayList<>();
			
			for (int i = 0; i < batchsize; ++i)
			{
				bdata.add(new Matrix(data.get((batch*batchsize)+i)));
				badata.add(new Matrix(answers.get((batch*batchsize)+i)));
			}
			
			this.batch(bdata, badata, data.size(), eta, lambda);
		}
	}
	
	/*
	* If we don't need direct control over our network, we can use this train function. It
	* performs all of the above and gives us how accurate our network is without too much effort.
	*/
	
	public void train(List<Matrix> data, List<Matrix> answers, int epochs, int batchsize, double eta, double lambda)
   {
		for (int epoch = 0; epoch < epochs; ++epoch)
		{
			List<Matrix> tdata = new ArrayList<>(data);
			List<Matrix> tadata = new ArrayList<>(answers);
			
			for (int i = 0; i < tdata.size(); ++i)
			{
				int index1 = (int)Math.floor(this.gen.nextDouble()*tdata.size());
				int index2 = (int)Math.floor(this.gen.nextDouble()*tdata.size());
				Matrix swap = new Matrix(tdata.get(index1));
				tdata.set(index1, new Matrix(tdata.get(index2)));
				tdata.set(index2, swap);
				swap = new Matrix(tadata.get(index1));
				tadata.set(index1, new Matrix(tadata.get(index2)));
				tadata.set(index2, swap);
			}
			
			this.epoch(tdata, tadata, batchsize, eta, lambda);
		}
	}

	public double evaluate(List<Matrix> data, List<Matrix> answers)
   {
      double total = 0.0d;

      for (int i = 0; i < data.size(); ++i)
      {
         Matrix a = this.feedforward(data.get(i));
         total += this.cost.cost(answers.get(i), a);
      }

      double reg = 0.0d;

      for (int l = 0; l < this.layers.size(); ++l)
      {
	      total += this.regularization.regularize(this.layers.get(l).getWeights());
      }

      return (total/data.size())+reg;
   }

	public String json()
   {
      Gson gson = new Gson();
      return gson.toJson(this);
	}

	public void save(String path, boolean append)
   {
		try
		{
			File f = new File(path);

         if (!f.exists())
         {
            f.createNewFile();
         }

			FileWriter fw = new FileWriter(f, append);
         BufferedWriter bw = new BufferedWriter(fw);

         bw.write(this.json());

         bw.close();
         fw.close();
		}
		catch (Exception e)
      {

		}
	}
	
	public NeuralNetwork(long seed, int inputSize, InitializationFunction initialization, RegularizationFunction regularization, CostFunction cost, Layer[] layers)
	{
		this.gen.setSeed(seed);

		this.cost = cost;
		this.regularization = regularization;

		this.layers = new ArrayList<>(Arrays.asList(layers));

		int totalSize = 0;
		for (Layer layer : this.layers)
		{
			totalSize += layer.size();
		}

		this.layers.get(0).initialize(initialization, new int[]{1, inputSize}, gen, totalSize);

		for (int layer = 1; layer < this.layers.size(); ++layer)
		{
			this.layers.get(layer).initialize(initialization, this.layers.get(layer-1).dimensions(), gen, totalSize);
		}
	}
	
	public NeuralNetwork(int inputSize, InitializationFunction initialization, RegularizationFunction regularization, CostFunction cost, Layer[] layers)
	{
		this(0L, inputSize, initialization, regularization, cost, layers);
	}

	public static NeuralNetwork Load(String JSON)
	{
      Gson gson = new Gson();
      return ((NeuralNetwork)gson.fromJson(JSON, new TypeToken<NeuralNetwork>(){}.getType()));
   }

	static String readFile(String path, Charset encoding)
			throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

   public static NeuralNetwork LoadFromFile(String FILE)
   {
	   Gson gson = new Gson();
	   String JSON = "";

	   try
	   {
		   JSON = readFile(FILE, Charset.defaultCharset());
	   }
	   catch (IOException e)
	   {
			System.err.format("Failed to load NeuralNetwork from file: %s\n", e.getMessage());
		   e.printStackTrace();
	   }

	   return ((NeuralNetwork)gson.fromJson(JSON, new TypeToken<NeuralNetwork>(){}.getType()));
   }
}