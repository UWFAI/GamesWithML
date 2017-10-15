package io.github.uwfai.neural.function;

public enum ActivationFunction
{
   SIGMOID
      {
         @Override
         public double activate(double z)
         {
            return 1.0d / (1.0d + Math.exp(-z));
         }

         @Override
         public double derivative(double z)
         {
            return this.activate(z) * (1.0d - this.activate(z));
         }
      },
   RELU
      {
         @Override
         public double activate(double z)
         {
            return (z > 0.0d ? z : 0.0d);
         }

         @Override
         public double derivative(double z)
         {
            return (z > 0.0d ? 1.0d : 0.0d);
         }
      },
   TANH
      {
         @Override
         public double activate(double z)
         {
            return Math.tanh(z);
         }

         @Override
         public double derivative(double z)
         {
            return 1.0d / Math.pow(Math.cosh(z), 2.0d);
         }
      };

   abstract public double activate(double z);

   abstract public double derivative(double z);
}
