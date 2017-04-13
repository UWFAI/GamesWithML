package io.github.uwfai.tictactoe;

import io.github.uwfai.neural.Matrix;
import io.github.uwfai.neural.NeuralNetwork;

import java.util.ArrayList;

/**
 * Created by carso on 4/13/2017.
 */
public final class NNMove
{
   private static double eta = 1.0;
   private static double lmbda = 0.01;
   private static int inner = 3;
   private static NeuralNetwork.CostType ct = NeuralNetwork.CostType.CROSSENTROPY;
   private static NeuralNetwork.ActivationType at = NeuralNetwork.ActivationType.SIGMOID;
   private static NeuralNetwork.InitializeType it = NeuralNetwork.InitializeType.SMART;

   private static NeuralNetwork NN0 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta,
               lmbda,
               ct,
               at,
               it);
   private static NeuralNetwork NN1 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta,
               lmbda,
               ct,
               at,
               it);
   private static NeuralNetwork NN2 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta,
               lmbda,
               ct,
               at,
               it);
   private static NeuralNetwork NN3 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta,
               lmbda,
               ct,
               at,
               it);
   private static NeuralNetwork NN4 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta,
               lmbda,
               ct,
               at,
               it);
   private static NeuralNetwork NN5 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta,
               lmbda,
               ct,
               at,
               it);
   private static NeuralNetwork NN6 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta,
               lmbda,
               ct,
               at,
               it);
   private static NeuralNetwork NN7 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta,
               lmbda,
               ct,
               at,
               it);
   private static NeuralNetwork NN8 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta,
               lmbda,
               ct,
               at,
               it);

   private static ArrayList<Matrix> data0;
   private static ArrayList<Matrix> data1;
   private static ArrayList<Matrix> data2;
   private static ArrayList<Matrix> data3;
   private static ArrayList<Matrix> data4;
   private static ArrayList<Matrix> data5;
   private static ArrayList<Matrix> data6;
   private static ArrayList<Matrix> data7;
   private static ArrayList<Matrix> data8;

   private static ArrayList<Matrix> answ0;
   private static ArrayList<Matrix> answ1;
   private static ArrayList<Matrix> answ2;
   private static ArrayList<Matrix> answ3;
   private static ArrayList<Matrix> answ4;
   private static ArrayList<Matrix> answ5;
   private static ArrayList<Matrix> answ6;
   private static ArrayList<Matrix> answ7;
   private static ArrayList<Matrix> answ8;
}
