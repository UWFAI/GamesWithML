package io.github.uwfai.tictactoe;

import io.github.uwfai.neural.Matrix;
import io.github.uwfai.neural.NeuralNetwork;
import io.github.uwfai.neural.Optimizer;

import java.util.Arrays;
import java.util.ArrayList;

/**
 * Created by carso on 4/13/2017.
 */
public final class NNTrain
{
   private static double eta = 2.1d;
   private static double lambda = 0.0d;
   private static int inner = 3;
   private static int batchsize = 25;
   private static NeuralNetwork.CostType ct = NeuralNetwork.CostType.QUADRATIC;
   private static NeuralNetwork.ActivationType at = NeuralNetwork.ActivationType.RELU;
   private static NeuralNetwork.InitializeType it = NeuralNetwork.InitializeType.SMART;

   private static NeuralNetwork[] NNs = {
         new NeuralNetwork(),
         new NeuralNetwork(),
         new NeuralNetwork(),
         new NeuralNetwork(),
         new NeuralNetwork(),
         new NeuralNetwork(),
         new NeuralNetwork(),
         new NeuralNetwork(),
         new NeuralNetwork()
   };

   public static void main(String[] args) {

      Matrix[] ans = {
            new Matrix(),
            new Matrix(),
            new Matrix(),
            new Matrix(),
            new Matrix(),
            new Matrix(),
            new Matrix(),
            new Matrix(),
            new Matrix()
      };

      Matrix[] answ = {
            Data.answ0,
            Data.answ1,
            Data.answ2,
            Data.answ3,
            Data.answ4,
            Data.answ5,
            Data.answ6,
            Data.answ7,
            Data.answ8
      };

      for (int a = 0; a < 9; ++a) {
         int l = 0;
         for (int i = 0; i < answ[a].size(); ++i) {
            for (; l < (int)Math.floor(answ[a].getd(i)); ++l) {
               ans[a].append(new Matrix(0.0d));
            }
            ans[a].append(new Matrix(1.0d));
            l = ans[a].size();
         }
         while (l < Data.data.size()) {
            ans[a].append(new Matrix(0.0d));
            ++l;
         }
      }

      // BELOW IS THE CODE FOR FINDING OPTIMAL ETAS - DO NOT CHANGE
      /*double[] etas = new double[9];
      int es = 25;
      int bs = 15;
      for (int n = 0; n < 9; ++n) {
         NNs[n] = new NeuralNetwork()
               .Input(9)
               .Feedforward(inner)
               .Output(1)
               .Build(eta,
                     lambda,
                     ct,
                     at,
                     it);

         Optimizer op = new Optimizer(NNs[n], Data.data, ans[n]);
         System.out.format("Optimizing eta for %d...\n", n, answ[n].size());
         op.Eta(0.1, 5.0, 2, true);
         NNs[n] = new NeuralNetwork().Load(op.getCurrent());
         System.out.format("Optimal eta: %.3f\n", op.getEta());
         etas[n] = op.getEta();

         //System.out.format("Optimizing lambda for %d...\n", n, answ[n].size());
         //op.Lambda(0.001, 1.0, 3, true);
         //NNs[n] = new NeuralNetwork().Load(op.getCurrent());
         //System.out.format("Optimal lambda: %.3f\n", op.getLambda());

         //NNs[n].save("NN"+n+".json", false);
      }*/

      // THIS IS THE OPTIMAL ETA FOR ALL NINE NETWORKS - DO NOT CHANGE
      double[] optietas = {5.438, 3.125, 0.979, 4.031, 3.546, 1.812, 6.049, 4.311, 1.628};
      for (int n = 0; n < 9; ++n) {
         NNs[n] = new NeuralNetwork()
               .Input(9)
               .Feedforward(inner)
               .Output(1)
               .Build(optietas[n],
                     lambda,
                     ct,
                     at,
                     it);
         System.out.format("Before training: %.5f\n", NNs[n].evaluate(Data.data, ans[n]));
         NNs[n].train(Data.data, ans[n], 10000, batchsize);
         System.out.format("After training: %.5f\n", NNs[n].evaluate(Data.data, ans[n]));
         NNs[n].save("NN"+n+".json", false);
      }
   }
}
