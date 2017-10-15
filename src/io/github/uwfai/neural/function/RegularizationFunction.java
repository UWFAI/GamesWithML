package io.github.uwfai.neural.function;

import io.github.uwfai.neural.Matrix;

public enum RegularizationFunction
{
   NONE
      {
         @Override
         final public double regularize(Matrix weights)
         {
            return 0.0d;
         }

         @Override
         final public Matrix derivative(Matrix weights)
         {
            return weights.shape();
         }
      },
   L2
      {
         @Override
         final public double regularize(Matrix weights)
         {
            return 0.5*weights.apply((j) -> Math.pow(j,2.0d)).sum();
         }

         @Override
         final public Matrix derivative(Matrix weights)
         {
            return weights;
         }
      };

   public abstract double regularize(Matrix weights);

   public abstract Matrix derivative(Matrix weights);
}
