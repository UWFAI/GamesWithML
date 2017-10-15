package io.github.uwfai.neural.initialization;

import java.util.Random;

public interface InitializationFunction
{
      public double weight(Random gen, int n);

      public double bias(Random gen, int n);
}
