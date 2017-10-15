package io.github.uwfai.neural.cost;

import io.github.uwfai.neural.Matrix;

public interface CostFunction
{
   enum CostType { QUADRATIC, CROSSENTROPY };

   double cost(Matrix y, Matrix a);

   Matrix derivative(Matrix y, Matrix a);
}
