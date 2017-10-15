package io.github.uwfai.neural.regularization;

import io.github.uwfai.neural.Matrix;

public class NoRegularizationFunction implements RegularizationFunction
{
   public double reg(Matrix weights)
   {
      return 0.0d;
   }

   public Matrix dv(Matrix weights)
   {
      return weights.shape();
   }
}
