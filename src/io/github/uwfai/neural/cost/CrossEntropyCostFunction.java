package io.github.uwfai.neural.cost;

import io.github.uwfai.neural.Matrix;

public class CrossEntropyCostFunction implements CostFunction
{
   public double cost(Matrix y, Matrix a)
   {
      return -y.product(a.apply(Math::log)).add(y.shape().fill(1.0d).subtract(y).product(a.shape().fill(1.0d).subtract(a).apply(Math::log))).sum();
   }

   public Matrix derivative(Matrix y, Matrix a)
   {
      return y.subtract(a).division(a.product(a.subtract(a.shape().fill(1.0d))));
   }
}
