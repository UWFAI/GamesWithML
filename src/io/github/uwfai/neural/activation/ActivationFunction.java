package io.github.uwfai.neural.activation;

public interface ActivationFunction
{
   double activate(double z);

   double derivative(double z);
}
