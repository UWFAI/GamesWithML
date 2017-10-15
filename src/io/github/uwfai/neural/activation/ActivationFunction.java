package io.github.uwfai.neural.activation;

public interface ActivationFunction
{
   enum ActivationType { SIGMOID, RELU, TANH };

   double activate(double z);

   double derivative(double z);
}
