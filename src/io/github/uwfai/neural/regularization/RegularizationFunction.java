package io.github.uwfai.neural.regularization;

import io.github.uwfai.neural.Matrix;

public interface RegularizationFunction
{
      enum RegularizationType { NONE, L2 };

      double reg(Matrix weights);

      Matrix dv(Matrix weights);
}
