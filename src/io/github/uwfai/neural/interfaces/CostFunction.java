package io.github.uwfai.neural.interfaces;

import io.github.uwfai.neural.Matrix;

public interface CostFunction
{
   public double fn(Matrix y, Matrix a);

   public Matrix delta(Matrix y, Matrix a);
}
