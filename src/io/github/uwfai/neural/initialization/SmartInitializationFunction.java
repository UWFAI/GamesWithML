package io.github.uwfai.neural.initialization;

import java.util.Random;

public class SmartInitializationFunction implements InitializationFunction
{
   public double weight(Random gen, int n)
   {
      return (gen.nextDouble() - gen.nextDouble()) / Math.sqrt(n);
   }

   public double bias(Random gen, int n)
   {
      return gen.nextDouble() - gen.nextDouble();
   }
}
