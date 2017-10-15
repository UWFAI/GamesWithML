package io.github.uwfai.neural.layer;

import com.google.gson.Gson;
import io.github.uwfai.neural.Matrix;
import io.github.uwfai.neural.function.ActivationFunction;
import io.github.uwfai.neural.function.InitializationFunction;
import io.github.uwfai.neural.function.RegularizationFunction;

import java.util.Random;

public class Layer
{
   protected Matrix weights;
   protected Matrix biases;
   protected int width;
   protected int height;
   protected ActivationFunction activation;

   public Layer(int width, int height, ActivationFunction activation) {
      this.width = width;
      this.height = height;
      this.activation = activation;
   }

   public Layer(int height, ActivationFunction activation) {
      this(1, height, activation);
   }

   public int size() {
      return this.width*this.height;
   }

   public int[] dimensions() {
      return new int[] { this.width, this.height };
   }

   public void initialize(InitializationFunction init, int[] previousSize, Random gen, int n) {
      this.biases = new Matrix();
      this.weights = new Matrix();
      for (int neuron = 0; neuron < this.size(); ++neuron) {
         Matrix nweights = new Matrix();
         this.biases.append(init.bias(gen, n));
         for (int prev = 0; prev < previousSize[0] * previousSize[1]; ++prev) {
            nweights.append(init.weight(gen, n));
         }
         this.weights.append(nweights);
      }
   }

   public Matrix getWeights() {
      return new Matrix(this.weights);
   }

   public Matrix getBiases() {
      return new Matrix(this.biases);
   }

   public Matrix feedforward(Matrix activations, Matrix zs, Matrix as) {
      Matrix result = new Matrix();
      zs.append(new Matrix());
      as.append(new Matrix());
      for (int neuron = 0; neuron < this.size(); ++neuron) {
         double z = activations.product(this.weights.getm(neuron)).sum()+this.biases.getd(neuron);
         double a = activation.activate(z);
         result.append(a);
         ((Matrix)zs.get(zs.size()-1)).append(z);
         ((Matrix)as.get(as.size()-1)).append(a);
      }
      return result;
   }

   public void update(Matrix nabla_b, Matrix nabla_w) {
      this.weights = this.weights.subtract(nabla_w);
      this.biases = this.biases.subtract(nabla_b);
   }

   public final double activationDerivative(double activation) {
      return this.activation.derivative(activation);
   }

   public void check() {
      this.weights.check();
   }

   public String json() {
      Gson gson = new Gson();
      return gson.toJson(this);
   }
}
