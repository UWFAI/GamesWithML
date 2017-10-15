package io.github.uwfai.neural.activation;

public class ReLUActivationFunction implements ActivationFunction
{
   public double activate(double z)
   {
      return (z > 0 ? z : 0);
   }

   public double derivative(double z)
   {
      return (z > 0 ? 1 : 0);
   }
}
