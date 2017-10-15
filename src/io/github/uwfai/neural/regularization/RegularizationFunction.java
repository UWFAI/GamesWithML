package io.github.uwfai.neural.regularization;

import io.github.uwfai.neural.Matrix;

public interface RegularizationFunction
{
      enum RegularizationType { NONE, L2 };

      public double reg(Matrix weights);

      public Matrix dv(Matrix weights);
}
