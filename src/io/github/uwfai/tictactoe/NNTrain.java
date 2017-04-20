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
   private static double eta = 2.5d;
   private static double lambda = 0.01d;
   private static int inner = 3;
   private static int batchsize = 15;
   private static NeuralNetwork.CostType ct = NeuralNetwork.CostType.QUADRATIC;
   private static NeuralNetwork.ActivationType at = NeuralNetwork.ActivationType.TANH;
   private static NeuralNetwork.InitializeType it = NeuralNetwork.InitializeType.SMART;
   private static NeuralNetwork.RegularizationType rt = NeuralNetwork.RegularizationType.L2;

   private static NeuralNetwork[] NNs = {
         new NeuralNetwork(),
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
            Data.answ8,
            Data.answ9
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
      for (int i = 0; i < answ[9].size(); ++i) {
         ans[9].append(new Matrix());
         for (int n = 0; n < 9; ++n) {
            if (answ[9].getd(i) == (double)n) {
               ans[9].getm(i).append(1.0d);
            } else {
               ans[9].getm(i).append(0.0d);
            }
         }
      }

      // BELOW IS THE CODE FOR FINDING OPTIMAL ETAS - DO NOT CHANGE
      double[] optietas = new double[9];
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
                     it,
                     rt);

         Optimizer op = new Optimizer(NNs[n], Data.data, ans[n]);
         System.out.format("Optimizing eta for %d...\n", n, answ[n].size());
         op.setLambda(lambda);
         op.setBatchsize(batchsize);
         op.Eta(0.1, 2.0, 1, true);
         NNs[n].setEta(op.getEta());
         System.out.format("Optimal eta: %.3f\n", op.getEta());
         optietas[n] = op.getEta();

         //System.out.format("Optimizing lambda for %d...\n", n, answ[n].size());
         //op.Lambda(0.001, 1.0, 3, true);
         //NNs[n] = new NeuralNetwork().Load(op.getCurrent());
         //System.out.format("Optimal lambda: %.3f\n", op.getLambda());

         //NNs[n].save("NN"+n+".json", false);
      }
      for (int n = 0; n < 9; ++n) {
         NNs[n] = new NeuralNetwork()
               .Input(9)
               .Feedforward(inner)
               .Output(1)
               .Build(optietas[n],
                     lambda,
                     ct,
                     at,
                     it,
                     rt);
         System.out.format("Before training: %.5f\n", NNs[n].evaluate(Data.data, ans[n]));
         double score = NNs[n].evaluate(Data.data, ans[n]);
         String best = NNs[n].json();
         int attempt = 0;
         while (attempt < 5) {
            NNs[n].train(Data.data, ans[n], 1, batchsize);
            while (NNs[n].evaluate(Data.data, ans[n]) < score)
            {
               best = NNs[n].json();
               score = NNs[n].evaluate(Data.data, ans[n]);
               NNs[n].train(Data.data, ans[n], 1, batchsize);
               attempt = 0;
            }
            ++attempt;
         }
         NNs[n].Load(best);
         System.out.format("After training: %.5f\n", NNs[n].evaluate(Data.data, ans[n]));
         NNs[n].save("NN"+n+".json", false);
      }

      NNs[9] = new NeuralNetwork()
            .Input(9)
            .Feedforward(9)
            .Feedforward(9)
            .Output(9)
            .Build(eta,
                  lambda,
                  ct,
                  at,
                  it,
                  rt);

      Matrix fdata = new Matrix();
      for (int i = 0; i < 852; ++i) {
         fdata.append(new Matrix());
         for (int n = 0; n < 9; ++n) {
            fdata.getm(i).append(NNs[n].feedforward(Data.data.getm(i)).getd(0));
         }
      }

      System.out.format("BEFORE TRAINING: %.3f\n", NNs[9].evaluate(fdata, ans[9]));
      Optimizer op = new Optimizer(NNs[9], fdata, ans[9]);
      op.setLambda(0.1d);
      op.setBatchsize(batchsize);
      op.Eta(0.01, 5.0, 1, true);
      NNs[9].setEta(op.getEta());
      int score = 0;
      for (int i = 0; i < 852; ++i) {
         Matrix fa = NNs[9].feedforward(fdata.getm(i));
         int max = 0;
         for (int n = 1; n < 9; ++n) {
            if (fa.getd(n) > fa.getd(max)) {
               max = n;
            }
         }
         if ((double)max == answ[9].getd(i)) {
            ++score;
         }
      }
      int nscore = score;
      String best = NNs[9].json();
      int attempt = 0;
      while (attempt < 25) {
         NNs[9].train(Data.data, ans[9], 1, batchsize);
         while (nscore >= score)
         {
            best = NNs[9].json();
            System.out.format("Current: %.3f (%d of %d correct)\n", NNs[9].evaluate(Data.data, ans[9]), nscore, 852);
            NNs[9].train(Data.data, ans[9], 1, batchsize);
            attempt = 0;
            score = nscore;

            nscore = 0;
            for (int i = 0; i < 852; ++i) {
               Matrix fa = NNs[9].feedforward(fdata.getm(i));
               int max = 0;
               for (int n = 1; n < 9; ++n) {
                  if (fa.getd(n) > fa.getd(max)) {
                     max = n;
                  }
               }
               if ((double)max == answ[9].getd(i)) {
                  ++nscore;
               }
            }
         }
         ++attempt;
      }
      NNs[9].Load(best);

      System.out.format("AFTER TRAINING: %.3f\n", NNs[9].evaluate(fdata, ans[9]));
   }
}
