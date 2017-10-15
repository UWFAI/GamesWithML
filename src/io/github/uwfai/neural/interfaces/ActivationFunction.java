package io.github.uwfai.neural.interfaces;

public interface ActivationFunction
{
   double activate(double z);

   double derivative(double z);
}
