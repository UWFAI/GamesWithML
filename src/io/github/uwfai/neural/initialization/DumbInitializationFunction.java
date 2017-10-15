package io.github.uwfai.neural.initialization;

import java.util.Random;

public class DumbInitializationFunction implements InitializationFunction
{
   public double weight(Random gen, int n)
   {
      return gen.nextDouble() - gen.nextDouble();
   }

   public double bias(Random gen, int n)
   {
      return gen.nextDouble() - gen.nextDouble();
   }
}
