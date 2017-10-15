package io.github.uwfai.neural.layer;

import io.github.uwfai.neural.Matrix;
import io.github.uwfai.neural.function.ActivationFunction;
import io.github.uwfai.neural.function.InitializationFunction;

import java.util.Random;

public class InputLayer extends Layer
{
   public InputLayer(int width, int height)
   {
      super(width, height);
   }

   public InputLayer(int height)
   {
      this(1, height);
   }

   @Override
   public Matrix feedforward(Matrix activations, ActivationFunction activation, Matrix zs, Matrix as)
   {
      as.append(activations);
      zs.append(activations);
      return activations;
   }

   @Override
   public void initialize(InitializationFunction init, Layer previous, Random gen, int n)
   {
      this.weights = new Matrix();
      this.biases = new Matrix();
      for (int neuron = 0; neuron < this.size(); ++neuron)
      {
         this.weights.append(new Matrix());
         for (int i = 0; i < this.size(); ++i)
         {
            if (neuron == i)
            {
               this.weights.getm(neuron).append(1.0d);
            }
            else
            {
               this.weights.getm(neuron).append(0.0d);
            }
         }
         this.biases.append(0.0d);
      }
   }
}
