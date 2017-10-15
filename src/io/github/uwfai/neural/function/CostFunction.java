package io.github.uwfai.neural.function;

import io.github.uwfai.neural.Matrix;

public enum CostFunction
{
   QUADRATIC
      {
         @Override
         final public double cost(Matrix y, Matrix a)
         {
            return 0.5 * y.subtract(a).apply((j) -> Math.pow(j, 2.0d)).sum();
         }

         @Override
         final public Matrix derivative(Matrix y, Matrix a)
         {
            return a.subtract(y);
         }
      },
   CROSSENTROPY
      {
         @Override
         final public double cost(Matrix y, Matrix a)
         {
            return -y.multiply(a.apply(Math::log)).add(y.shape().fill(1.0d).subtract(y).multiply(a.shape().fill(1.0d).subtract(a).apply(Math::log))).sum();
         }

         @Override
         final public Matrix derivative(Matrix y, Matrix a)
         {
            return y.subtract(a).divide(a.multiply(a.subtract(a.shape().fill(1.0d))));
         }
      };

   abstract public double cost(Matrix y, Matrix a);

   abstract public Matrix derivative(Matrix y, Matrix a);
}
