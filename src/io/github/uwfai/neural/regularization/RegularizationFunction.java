package io.github.uwfai.neural.regularization;

import io.github.uwfai.neural.Matrix;

public interface RegularizationFunction
{
      public double reg(Matrix weights);

      public Matrix dv(Matrix weights);
}
