package io.github.uwfai.neural.initialization;

import java.util.Random;

public interface InitializationFunction
{
      enum InitializationType { DUMB, SMART };

      double weight(Random gen, int n);

      double bias(Random gen, int n);
}
