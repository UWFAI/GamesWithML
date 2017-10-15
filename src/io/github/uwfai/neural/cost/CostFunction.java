package io.github.uwfai.neural.cost;

import io.github.uwfai.neural.Matrix;

public interface CostFunction
{
   double cost(Matrix y, Matrix a);

   Matrix derivative(Matrix y, Matrix a);
}
