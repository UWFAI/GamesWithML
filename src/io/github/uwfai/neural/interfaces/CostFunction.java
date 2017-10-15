package io.github.uwfai.neural.interfaces;

import io.github.uwfai.neural.Matrix;

public interface CostFunction
{
   public double cost(Matrix y, Matrix a);

   public Matrix derivative(Matrix y, Matrix a);
}
