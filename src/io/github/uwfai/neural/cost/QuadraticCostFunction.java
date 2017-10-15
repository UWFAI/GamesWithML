package io.github.uwfai.neural.cost;

import io.github.uwfai.neural.Matrix;

public class QuadraticCostFunction implements CostFunction
{
   public double cost(Matrix y, Matrix a)
   {
      return 0.5 * y.subtract(a).apply((j) -> Math.pow(j, 2.0d)).sum();
   }

   public Matrix derivative(Matrix y, Matrix a)
   {
      return a.subtract(y);
   }
}
