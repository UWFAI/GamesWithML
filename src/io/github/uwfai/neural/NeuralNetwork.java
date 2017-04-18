package io.github.uwfai.neural;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

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
	/*
	* These enums provide the outside functionality for selecting the cost type, activation
	* type, initialization type, and regularization type. Thsee are four important factors
	* for building an efficient network. It depends on your problem and your network, but
	* the below implementations of these interfaces are the core of fundamental neural
	* networking.
	*/
	
	public static enum CostType { QUADRATIC, CROSSENTROPY };
	public static enum ActivationType { SIGMOID, RELU };
	public static enum InitializeType { DUMB, SMART };
	public static enum RegularizationType { NONE, L2 };
	
	private class CostFunction {
		public double fn(Matrix y, Matrix a) { return 0.0d; }
		public Matrix delta(Matrix y, Matrix a) { return new Matrix(0.0d); }
      public CostFunction() { }
	}
	
	private class ActivationFunction {
		public double fn(double z) { return 0.0d; }
		public double prime(double z) { return 0.0d; }
      public ActivationFunction() { }
	}
	
	private class InitializeFunction {
		public double weight(Random gen, int n) { return 0.0d; }
		public double bias(Random gen, int n) { return 0.0d; }
      public InitializeFunction() { }
	}
	
	private class RegularizationFunction {
		public double reg(Matrix weights) { return 0.0d; }
		public Matrix dv(Matrix weights) { return new Matrix(0.0d); }
      public RegularizationFunction() { }
	}
	
	/*
	* To implement our layers, we have the default functions defined here. They are fairly
	* basic, but because, specifically, our Feedforward and Output layers share most traits
	* about how they operate, we define our LayerClass with basic definitions in order to ensure
	* that the Layer interface is implemented for all layers, even if custom feedforward and
	* initializations functions aren't specifically defined for each class of layer.
	*/
	
	private class LayerClass {
		Matrix weights;
		Matrix biases;
		int width;
		int height;
		
		public int size() {
			return this.width*this.height;
		}
		
		public int[] dimensions() {
			int[] dim = { this.width, this.height };
			return dim;
		}
		
		public void initialize(InitializeFunction init, LayerClass previous, Random gen, int n) {
			this.biases = new Matrix();
			this.weights = new Matrix();
			for (int neuron = 0; neuron < this.size(); ++neuron) {
				Matrix nweights = new Matrix();
				this.biases.append(init.bias(gen, n));
				for (int prev = 0; prev < previous.size(); ++prev) {
					nweights.append(init.weight(gen, n));
				}
				this.weights.append(nweights);
			}
		}
		
		public Matrix getWeights() {
			return new Matrix(this.weights);
		}

		public Matrix getBiases() { return new Matrix(this.biases); }
		
		public Matrix feedforward(Matrix activations, ActivationFunction activation, Matrix zs, Matrix as) {
			Matrix result = new Matrix();
			zs.append(new Matrix());
			as.append(new Matrix());
			for (int neuron = 0; neuron < this.size(); ++neuron) {
				double z = activations.hadamard(this.weights.getm(neuron)).sum()+this.biases.getd(neuron);
				double a = activation.fn(z);
				result.append(a);
				((Matrix)zs.get(zs.size()-1)).append(z);
				((Matrix)as.get(as.size()-1)).append(a);
			}
			return result;
		}
		
		public void update(Matrix nabla_b, Matrix nabla_w, double eta, double lambda, int n, int v) {
			for (int neuron = 0; neuron < this.size(); ++neuron) {
				for (int weight = 0; weight < this.weights.getm(neuron).size(); ++weight) {
					this.weights.getm(neuron).set(weight,
						((1.0-(eta*lambda/(double)n))*this.weights.getm(neuron).getd(weight))-((eta/(double)v)*nabla_w.getm(neuron).getd(weight))
					);
				}
				this.biases.set(neuron,
					(double)this.biases.get(neuron)
					-((eta/(double)v)*(double)nabla_b.get(neuron))
				);
			}
		}

		public void check() {
         this.weights.check();
      }

		public String json() {
         Gson gson = new Gson();
         return gson.toJson(this);
		}
		
		LayerClass(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}

   private class LayerClassInstanceCreator implements InstanceCreator<LayerClass> {
      public LayerClass createInstance(Type type) {
         return new LayerClass(1, 1);
      }
   }
	
	/*
	* The feeforward layer is the core part of our neural networks. Feedforward layers do exactly
	* what the name says - they push the input through the network to deliver your output. They
	* implement the default functions in the LayerClass, only providing the constructor which
	* tells the LayerClass that it is of 1 width (only one column) and a certain height, as
	* defined by the user with the constructor Layer(height).
	*/
	
	private class FeedforwardLayer extends LayerClass {
		FeedforwardLayer(int height) {
			super(1, height);
		}
	}

   private class FeedforwardLayerCreator implements InstanceCreator<FeedforwardLayer> {
      public FeedforwardLayer createInstance(Type type) {
         return new FeedforwardLayer(1);
      }
   }
	
	/*
	* TODO: convolutional neural network layers. The implementation is simple: for each grid of
	* neurons that fits the weights structure (defined through the constructor as Convolutional(
	* width, height)), calculate the difference from the "filter"/"lens" and create a new layer
	* based on those differences. This reduces a large layer to a smaller layer and provides the
	* functionality of looking for an overall feature of, say, an image, providing more accurate
	* networks. Generally, we won't update the filter/lens of our CNN automatically.
	*/
	
	private class ConvolutionalLayer extends LayerClass {
		public Matrix feedforward(Matrix activations, ActivationFunction activation, Matrix as, Matrix zs) {
			
			return new Matrix();
		}
		
		public void initialize(InitializeFunction init, LayerClass previous, Random gen, int n) {
			return;
		}
		
		public void setFilter(Matrix filter) {
			try {
				if (filter.similar(this.weights)) {
					this.weights = filter;
				} else {
					throw new Exception(String.format("filter size doesn't match %dx%d", this.width, this.height));
				}
			} catch (Exception e) {
				System.err.println("Error setting convolutional filter: %s".format(e.getMessage()));
				e.printStackTrace();
			}
		}
		
		ConvolutionalLayer(int width, int height) {
			super(width, height);
		}
	}

   private class ConvolutionalLayerCreator implements InstanceCreator<ConvolutionalLayer> {
      public ConvolutionalLayer createInstance(Type type) {
         return new ConvolutionalLayer(1, 1);
      }
   }
	
	/*
	* The InputLayer is the first layer defined for our network and gives us the functionality of
	* pushing through a set of values and answers. It is largely different from the Feedforward and
	* Output layers in that it doesn't provide weights and biases in the same way; the weights are
	* all 1.0 and the biases are 0.0, since we don't want to modify our inputs through our Input
	* layer, but rather pass them along into our Feeforward/Convolutional layers.
	*/
	
	private class InputLayer extends LayerClass {
		public Matrix feedforward(Matrix activations, ActivationFunction activation, Matrix zs, Matrix as) {
			as.append(activations);
			zs.append(activations);
			return activations;
		}
		
		public void initialize(InitializeFunction init, LayerClass previous, Random gen, int n) {
			this.weights = new Matrix();
			this.biases = new Matrix();
			for (int neuron = 0; neuron < this.size(); ++neuron) {
				this.weights.append(new Matrix());
            for (int i = 0; i < this.size(); ++i) {
               if (neuron == i) {
                  this.weights.getm(neuron).append(1.0d);
               } else {
                  this.weights.getm(neuron).append(0.0d);
               }
            }
				this.biases.append(0.0d);
			}
			return;
		}
		
		InputLayer(int width, int height) {
			super(width, height);
		}
		
		InputLayer(int height) {
			this(1, height);
		}
	}

   private class InputLayerCreator implements InstanceCreator<InputLayer> {
      public InputLayer createInstance(Type type) {
         return new InputLayer(1);
      }
   }

	/*
	* The Output layer functions similary to the Feedforward layers, but it must always come last
	* in your NN because it differentiates for the change in cost due to each output neuron. This
	* differs from inner layers, and so a special class is necessary. It is linked with the NN
	* class constructor Output(height).
	*/

	private class OutputLayer extends LayerClass {
		OutputLayer(int height) {
			super(1, height);
		}
	}

	private class Quadratic extends CostFunction {
		public double fn(Matrix y, Matrix a) {
			double cost = 0.0;
			for (int i = 0; i < y.size(); ++i) {
				double v = (double)0.5*Math.pow(y.getd(i)-a.getd(i),2.0);
				cost += v;
			}
			return cost;
		}

		public Matrix delta(Matrix y, Matrix a) {
			return a.subtract(y);
		}

		Quadratic() {
			return;
		}
	}

	/*private class CrossEntropy implements CostFunction {
		public double fn(Matrix y, Matrix a) {
			double cost = 0.0;
			for (int i = 0; i < y.size(); ++i) {
				double v = (y.getd(i)*(double)Math.log(a.getd(i)))+((1.0d-y.getd(i))*Math.log(1.0d-a.getd(i)));
				cost += v;
			}
			return cost;
		}
	}*/

	private class Sigmoid extends ActivationFunction {
		public double fn(double z) {
			return 1.0/(1.0+Math.exp(-z));
		}

		public double prime(double z) {
			return this.fn(z)*(1.0-this.fn(z));
		}

		Sigmoid() {
			return;
		}
	}

	private class ReLU extends ActivationFunction {
      public double fn(double z) { return Math.log(1+Math.exp(z)); }

      public double prime(double z) { return 1.0/(1.0+Math.exp(-z)); }

      ReLU() { return; }
   }

	private class Dumb extends InitializeFunction {
		public double weight(Random gen, int n) {
			return gen.nextDouble()-gen.nextDouble();
		}
		
		public double bias(Random gen, int n) {
			return gen.nextDouble()-gen.nextDouble();
		}
	}

	private class Smart extends InitializeFunction {
		public double weight(Random gen, int n) {
			return (gen.nextDouble()-gen.nextDouble())/Math.sqrt(n);
		}
		
		public double bias(Random gen, int n) {
			return gen.nextDouble()-gen.nextDouble();
		}
	}
	
	private ArrayList<LayerClass> layers = new ArrayList<LayerClass>();
	private CostFunction cost;
	private ActivationFunction activation;
   private CostType costtype;
   private ActivationType actitype;
	private double eta;
	private double lambda;
	private Random gen = new Random();
	
	/*
	* Our network, of course, must be initialized. We initialize our weights and biases based on
	* the initialization function we choose in the NeuralNetwork constructor.
	*/
	
	public void initialize(InitializeFunction init) {
		int size = this.size();
		for (int layer = 0; layer < this.layers.size(); ++layer) {
			LayerClass prev = this.layers.get(0);
			if (layer > 0) { prev = this.layers.get(layer-1); }
			this.layers.get(layer).initialize(init, prev, this.gen, size);
		}
	}
	
	/*
	* This function simply feeds the inputs forward through the network and returns the output
	* vaues. This is primarily for our testing purposes to determine the accuracy of our network.
	*/
	
	public Matrix feedforward(Matrix activations, Matrix zs, Matrix as) {
		for (LayerClass layer : this.layers) {
			activations = layer.feedforward(activations, this.activation, zs, as);
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
		
		error.prepend(cost.delta(answers, as.getm(as.size()-1)));
		
		for (int layer = this.layers.size()-1; layer > 0; --layer) {
			Matrix adjust = new Matrix();
			for (int neuron = 0; neuron < this.layers.get(layer).size(); ++neuron) {
				adjust.append(this.activation.prime(zs.getm(layer).getd(neuron)));
			}
			error.set(0, error.getm(0).hadamard(adjust));
			
			if (layer > 0) {
				Matrix result = new Matrix();
				for (int neuron = 0; neuron < this.layers.get(layer-1).size(); ++neuron) {
					result.append(error.getm(0)
							.hadamard(
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
	
	public void batch(Matrix data, Matrix answers, int datasize) {
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
			this.layers.get(layer).update(nabla_b.getm(layer), nabla_w.getm(layer-1), this.eta, this.lambda, datasize, data.size());
		}
	}
	
	/*
	* An epoch is a set of batches to train our network. It is here that the training data is
	* separated from the test data, to ensure that our network isn't tested against the same
	* examples that it used to train. This ensures the network isn't being tested for what it
	* already knows, but rather what it should have learned based on its extrapolation from the
	* test data.
	*/
	
	public void epoch(Matrix data, Matrix answers, int batchsize) {
		int batches = (int)Math.floor((double)data.size()/(double)batchsize);
		for (int batch = 0; batch < batches; ++batch) {
			Matrix bdata = new Matrix();
			Matrix badata = new Matrix();
			
			for (int i = 0; i < batchsize; ++i) {
				bdata.append(new Matrix(data.getm((batch*batchsize)+i)));
				badata.append(new Matrix(answers.getm((batch*batchsize)+i)));
			}
			
			this.batch(bdata, badata, data.size());
		}
	}
	
	/*
	* If we don't need direct control over our network, we can use this train function. It
	* performs all of the above and gives us how accurate our network is without too much effort.
	*/
	
	public void train(Matrix data, Matrix answers, int epochs, int batchsize) {
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
			
			this.epoch(tdata, tadata, batchsize);
		}
	}

	public double evaluate(Matrix data, Matrix answers) {
      double total = 0.0d;
      for (int i = 0; i < data.size(); ++i) {
         Matrix a = this.feedforward(data.getm(i));
         total += this.cost.fn(answers.getm(i), a);
      }
      return total/data.size();
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

	public int size() {
		int num = this.layers.get(0).size();
		for (int layer = 1; layer < this.layers.size(); ++layer) {
			num *= this.layers.get(layer).size();
		}
		return num;
	}

	public void setEta(double neta) {
      this.eta = neta;
   }

   public void setLambda(double nlambda) {
      this.lambda = nlambda;
   }

   public void setCost(CostType ncosttype) {
      this.costtype = ncosttype;
      this.Refresh();
   }

   public void setActivation(ActivationType nactitype) {
      this.actitype = nactitype;
      this.Refresh();
   }

   public double getEta() {
      return this.eta;
   }

   public double getLambda() {
      return this.lambda;
   }
	
	/*
	* The rest of the functions are what we use on the frontend to define our network. Each network
	* follows the same construction patterned, all packaged together in one line:
	*
	* new NeuralNetwork(long seed)
	*	.Input(int height)
	*	.Feedforward(int height)
	*	...
	*	.Output(int height)
	*	.Build(double eta,
	*			double lambda,
	*			CostType cost,
	*			ActivationType activation,
	*			InitializeType init);
	*
	* You MUST keep track of the connections you need. Avoiding the counterintuitive design of DL4J,
	* there is no function/class for connecting layers. Be aware of what sizes of weights you need in
	* order to properly connect to the previous layers. (This is mostly relevant for CNNs - Feedforward
	* layers do all of the connecting for you.)
	*/
	
	public NeuralNetwork(long seed) {
		this.gen.setSeed(seed);
	}
	
	public NeuralNetwork() {
		this((long)0);
	}
	
	public NeuralNetwork Input(int height) {
		layers.add(new InputLayer(1, height));
		return this;
	}
	
	public NeuralNetwork Input(int width, int height) {
		layers.add(new InputLayer(width, height));
		return this;
	}
	
	public NeuralNetwork Feedforward(int height) {
		layers.add(new FeedforwardLayer(height));
		return this;
	}
	
	public NeuralNetwork Convolutional(int width, int height) {
		layers.add(new ConvolutionalLayer(width, height));
		return this;
	}
	
	public NeuralNetwork Filter(Matrix filter) {
		try {
			if (this.layers.get(this.layers.size()-1) instanceof ConvolutionalLayer) {
				((ConvolutionalLayer)this.layers.get(this.layers.size()-1)).setFilter(filter);
			} else {
				throw new Exception("most recent layer not convolutional");
			}
		} catch (Exception e) {
			System.err.println("Error filtering convolutional layer: %s".format(e.getMessage()));
			e.printStackTrace();
		}
		return this;
	}
	
	public NeuralNetwork Output(int height) {
		layers.add(new OutputLayer(height));
		return this;
	}
	
	public NeuralNetwork Build(double eta, double lambda, CostType cost, ActivationType activation, InitializeType init) {
		this.eta = eta;
		this.lambda = lambda;
      this.costtype = cost;
      this.actitype = activation;
		switch (cost) {
			default:
			case QUADRATIC:
			{
				this.cost = new Quadratic();
				break;
			}
		}
		switch (activation) {
			default:
			case SIGMOID:
			{
				this.activation = new Sigmoid();
				break;
			}
         case RELU:
         {
            this.activation = new ReLU();
            break;
         }
		}
		switch (init) {
			default:
			case DUMB:
			{
				this.initialize(new Dumb());
				break;
			}
			case SMART:
			{
				this.initialize(new Smart());
				break;
			}
		}
		return this;
	}

	public NeuralNetwork Refresh() {
      switch (this.costtype) {
      default:
         case QUADRATIC:
         {
            this.cost = new Quadratic();
            break;
         }
      }
      switch (this.actitype)
      {
         default:
         case SIGMOID:
         {
            this.activation = new Sigmoid();
            break;
         }
         case RELU:
         {
            this.activation = new ReLU();
            break;
         }
      }
      for (int l = 0; l < this.layers.size(); ++l) {
         this.layers.get(l).check();
      }
      return this;
   }

	public NeuralNetwork Load(String JSON) {
      Gson gson = new Gson();
      return ((NeuralNetwork)gson.fromJson(JSON, new TypeToken<NeuralNetwork>(){}.getType())).Refresh();
   }
}