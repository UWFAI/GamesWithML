package io.github.uwfai.neural.activation;

public class TanhActivationFunction implements ActivationFunction
{
   public double activate(double z)
   {
      return Math.tanh(z);
   }

   public double derivative(double z)
   {
      return 1.0d / Math.pow(Math.cosh(z), 2.0d);
   }
}
