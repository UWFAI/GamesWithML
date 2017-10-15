package io.github.uwfai.neural.function;

import java.util.Random;

public enum InitializationFunction
{
   DUMB
      {
         @Override
         final public double weight(Random gen, int n)
         {
            return gen.nextDouble()-gen.nextDouble();
         }

         @Override
         final public double bias(Random gen, int n)
         {
            return gen.nextDouble()-gen.nextDouble();
         }
      },
   SMART
      {
         @Override
         final public double weight(Random gen, int n)
         {
            return (gen.nextDouble()-gen.nextDouble())/Math.sqrt(n);
         }

         @Override
         final public double bias(Random gen, int n)
         {
            return gen.nextDouble()-gen.nextDouble();
         }
      };

   abstract public double weight(Random gen, int n);

   abstract public double bias(Random gen, int n);
}
