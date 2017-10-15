package io.github.uwfai.neural.regularization;

import io.github.uwfai.neural.Matrix;

public class L2RegularizationFunction implements RegularizationFunction
{
   public double reg(Matrix weights)
   {
      return 0.5 * weights.apply((j) -> Math.pow(j, 2.0d)).sum();
   }

   public Matrix dv(Matrix weights)
   {
      return weights;
   }
}
