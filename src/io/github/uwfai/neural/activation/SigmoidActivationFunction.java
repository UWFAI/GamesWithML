package io.github.uwfai.neural.activation;

public class SigmoidActivationFunction implements ActivationFunction
{
   public double activate(double z)
   {
      return 1.0d / (1.0d + Math.exp(-z));
   }

   public double derivative(double z)
   {
      return this.activate(z) * (1.0 - this.activate(z));
   }
}
