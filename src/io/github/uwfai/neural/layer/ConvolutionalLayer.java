package io.github.uwfai.neural.layer;

import io.github.uwfai.neural.Matrix;
import io.github.uwfai.neural.function.ActivationFunction;
import io.github.uwfai.neural.function.InitializationFunction;
import io.github.uwfai.neural.function.RegularizationFunction;

import java.util.Random;

public class ConvolutionalLayer extends Layer {
   @Override
   public Matrix feedforward(Matrix activations, Matrix as, Matrix zs) {
      return new Matrix();
   }

   @Override
   public void initialize(InitializationFunction init, int[] previousSize, Random gen, int n) {
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

   public ConvolutionalLayer(int width, int height, ActivationFunction activation) {
      super(width, height, activation);
   }
}
