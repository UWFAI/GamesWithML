package io.github.uwfai.neural.layer;

import com.google.gson.Gson;
import io.github.uwfai.neural.Matrix;
import io.github.uwfai.neural.function.ActivationFunction;
import io.github.uwfai.neural.function.InitializationFunction;

import java.util.List;
import java.util.Random;

public class Layer
{
   protected Matrix weights;
   protected Matrix biases;
   protected int width;
   protected int height;
   protected ActivationFunction activation;

   public Layer(int width, int height, ActivationFunction activation)
   {
      this.width = width;
      this.height = height;
      this.activation = activation;
   }

   public Layer(int height, ActivationFunction activation)
   {
      this(1, height, activation);
   }

   public int size()
   {
      return this.width*this.height;
   }

   public int[] dimensions()
   {
      return new int[] { this.width, this.height };
   }

   public void initialize(InitializationFunction init, int[] previousSize, Random gen, int n)
   {
      this.biases = new Matrix(size(), 1);
      this.weights = new Matrix(size(), previousSize[0]*previousSize[1]);

      for (int row = 0; row < height; ++row)
      {
         for (int column = 0; column < width; ++column)
         {
            this.biases.set(row, column, init.bias(gen, n));

            for (int prev = 0; prev < previousSize[0] * previousSize[1]; ++prev)
            {
               this.weights.set((row*width)+column, prev, init.weight(gen, n));
            }
         }
      }
   }

   public Matrix getWeights()
   {
      return new Matrix(this.weights);
   }

   public Matrix getBiases()
   {
      return new Matrix(this.biases);
   }

   public ActivationFunction getActivation()
   {
      return this.activation;
   }

   public Matrix feedforward(Matrix activations, List<Matrix> zs, List<Matrix> as)
   {
      Matrix input = weights.dot(activations).add(biases);

      Matrix output = activation.activate(input);

      zs.add(input);
      as.add(output);

      return output;
   }

   public void update(Matrix nabla_b, Matrix nabla_w)
   {
      this.weights = this.weights.subtract(nabla_w);
      this.biases = this.biases.subtract(nabla_b);
   }

   public String json()
   {
      Gson gson = new Gson();
      return gson.toJson(this);
   }
}
