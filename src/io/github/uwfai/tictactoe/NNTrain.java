package io.github.uwfai.tictactoe;

import io.github.uwfai.neural.Matrix;
import io.github.uwfai.neural.NeuralNetwork;
import io.github.uwfai.neural.activation.ActivationFunction;
import io.github.uwfai.neural.cost.CostFunction;

import java.util.Random;

/**
 * Created by carso on 4/13/2017.
 */
public final class NNTrain
{
   private static double eta = 5.0d;
   private static double lambda = 0.1d;
   private static int inner = 5;
   private static int batchsize = 33;
   private static CostFunction.CostType ct = CostFunction.CostType.CROSSENTROPY;
   private static ActivationFunction.ActivationType at = ActivationFunction.ActivationType.SIGMOID;
   private static NeuralNetwork.InitializeType it = NeuralNetwork.InitializeType.SMART;
   private static NeuralNetwork.RegularizationType rt = NeuralNetwork.RegularizationType.L2;

   private static Random gen = new Random();
   private static double mutation = 0.01d;

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

   public static double mutate(double value) {
      return value+(value*mutation*(gen.nextDouble()-gen.nextDouble()));
   }

   public static void main(String[] args)
   {

      Matrix[] ans = {new Matrix(), new Matrix(), new Matrix(), new Matrix(), new Matrix(), new Matrix(), new Matrix(), new Matrix(), new Matrix(),
            new Matrix()};

      Matrix[] answ = {Data.answ0, Data.answ1, Data.answ2, Data.answ3, Data.answ4, Data.answ5, Data.answ6, Data.answ7, Data.answ8, Data.answ9};

      int[] ansi = {0, 0, 0, 0, 0, 0, 0, 0, 0};

      // GENERATE DATA FOR VERTICAL+HORIZONTAL NETWORK STRUCTURE
      for (int i = 0; i < 852; ++i)
      {
         if (answ[0].getd(ansi[0]) == (double) i)
         {
            ans[0].append(new Matrix(1.0d));
            ans[3].append(new Matrix(1.0d));
            ansi[0] = (ansi[0] + 1 < answ[0].size() ? ansi[0] + 1 : ansi[0]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 0 && x != 3)
               {
                  ans[x].append(new Matrix(0.0d));
               }
            }
         }
         else if (answ[1].getd(ansi[1]) == (double) i)
         {
            ans[0].append(new Matrix(1.0d));
            ans[4].append(new Matrix(1.0d));
            ansi[1] = (ansi[1] + 1 < answ[1].size() ? ansi[1] + 1 : ansi[1]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 0 && x != 4)
               {
                  ans[x].append(new Matrix(0.0d));
               }
            }
         }
         else if (answ[2].getd(ansi[2]) == (double) i)
         {
            ans[0].append(new Matrix(1.0d));
            ans[5].append(new Matrix(1.0d));
            ansi[2] = (ansi[2] + 1 < answ[2].size() ? ansi[2] + 1 : ansi[2]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 0 && x != 5)
               {
                  ans[x].append(new Matrix(0.0d));
               }
            }
         }
         else if (answ[3].getd(ansi[3]) == (double) i)
         {
            ans[1].append(new Matrix(1.0d));
            ans[3].append(new Matrix(1.0d));
            ansi[3] = (ansi[3] + 1 < answ[3].size() ? ansi[3] + 1 : ansi[3]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 1 && x != 3)
               {
                  ans[x].append(new Matrix(0.0d));
               }
            }
         }
         else if (answ[4].getd(ansi[4]) == (double) i)
         {
            ans[1].append(new Matrix(1.0d));
            ans[4].append(new Matrix(1.0d));
            ansi[4] = (ansi[4] + 1 < answ[4].size() ? ansi[4] + 1 : ansi[4]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 1 && x != 4)
               {
                  ans[x].append(new Matrix(0.0d));
               }
            }
         }
         else if (answ[5].getd(ansi[5]) == (double) i)
         {
            ans[1].append(new Matrix(1.0d));
            ans[5].append(new Matrix(1.0d));
            ansi[5] = (ansi[5] + 1 < answ[5].size() ? ansi[5] + 1 : ansi[5]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 1 && x != 5)
               {
                  ans[x].append(new Matrix(0.0d));
               }
            }
         }
         else if (answ[6].getd(ansi[6]) == (double) i)
         {
            ans[2].append(new Matrix(1.0d));
            ans[3].append(new Matrix(1.0d));
            ansi[6] = (ansi[6] + 1 < answ[6].size() ? ansi[6] + 1 : ansi[6]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 2 && x != 3)
               {
                  ans[x].append(new Matrix(0.0d));
               }
            }
         }
         else if (answ[7].getd(ansi[7]) == (double) i)
         {
            ans[2].append(new Matrix(1.0d));
            ans[4].append(new Matrix(1.0d));
            ansi[7] = (ansi[7] + 1 < answ[7].size() ? ansi[7] + 1 : ansi[7]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 2 && x != 4)
               {
                  ans[x].append(new Matrix(0.0d));
               }
            }
         }
         else if (answ[8].getd(ansi[8]) == (double) i)
         {
            ans[2].append(new Matrix(1.0d));
            ans[5].append(new Matrix(1.0d));
            ansi[8] = (ansi[8] + 1 < answ[8].size() ? ansi[8] + 1 : ansi[8]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 2 && x != 5)
               {
                  ans[x].append(new Matrix(0.0d));
               }
            }
         }
      }
      for (int i = 0; i < 852; ++i)
      {
         ans[7].append(new Matrix());
         for (int n = 0; n < 9; ++n)
         {
            if (answ[9].getd(i) == (double) n)
            {
               ans[7].getm(i).append(1.0d);
            }
            else
            {
               ans[7].getm(i).append(0.0d);
            }
         }
      }

      // GENERATE DATA FOR INDIVIDUAL SPACE NETWORK STRUCTURE
      /*for (int a = 0; a < 9; ++a) {
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
      }*/

      // BELOW IS THE CODE FOR FINDING OPTIMAL ETAS - DO NOT CHANGE
      /*double[] optietas = new double[9];
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
      }*/

      double[] optietas = {4.35, 3.40, 5.00, 4.30, 3.35, 5.10};
      for (int n = 0; n < 6; ++n)
      {
         NNs[n] = new NeuralNetwork().LoadFromFile("NN"+n+".json");
      }

      NNs[7] = new NeuralNetwork().Input(6).Feedforward(inner).Output(9).Build(eta, lambda, ct, at, it, rt);

      int bestscore = 0;
      while (bestscore < 500)
      {
         for (int n = 0; n < 6; ++n)
         {
            System.out.format("Before training: %.5f\n", NNs[n].evaluate(Data.data, ans[n]));
            double score = NNs[n].evaluate(Data.data, ans[n]);
            String best = NNs[n].json();
            int attempt = 0;
            while (attempt < 50)
            {
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
            NNs[n].save("NN" + n + ".json", false);
         }

         Matrix fdata = new Matrix();
         for (int i = 0; i < 852; ++i)
         {
            fdata.append(new Matrix());
            for (int n = 0; n < 6; ++n)
            {
               fdata.getm(i).append(NNs[n].feedforward(Data.data.getm(i)).getd(0));
            }
         }

         /*System.out.format("BEFORE TRAINING: %.3f\n", NNs[7].evaluate(fdata, ans[9]));
         Optimizer op = new Optimizer(NNs[7], fdata, ans[9]);
         op.setLambda(0.1d);
         op.setBatchsize(batchsize);
         op.Eta(0.01, 5.0, 1, true);
         NNs[7].setEta(op.getEta());*/
         int score = 0;
         for (int i = 0; i < 852; ++i)
         {
            Matrix fa = NNs[7].feedforward(fdata.getm(i));
            int max = 0;
            for (int n = 1; n < 6; ++n)
            {
               if (fa.getd(n) > fa.getd(max))
               {
                  max = n;
               }
            }
            if ((double) max == answ[9].getd(i))
            {
               ++score;
            }
         }

         int nscore = score;
         String best = NNs[7].json();
         int attempt = 0;
         while (attempt < 100)
         {
            NNs[7].train(fdata, ans[7], 1, batchsize);
            while (nscore >= score)
            {
               best = NNs[7].json();
               System.out.format("Current: %.3f (%d of %d correct)\n", NNs[7].evaluate(fdata, ans[7]), nscore, 852);
               NNs[7].train(fdata, ans[7], 1, batchsize);
               attempt = 0;
               score = nscore;

               nscore = 0;
               for (int i = 0; i < 852; ++i)
               {
                  Matrix fa = NNs[7].feedforward(fdata.getm(i));
                  int max = 0;
                  for (int n = 1; n < 6; ++n)
                  {
                     if (fa.getd(n) > fa.getd(max))
                     {
                        max = n;
                     }
                  }
                  if ((double) max == answ[9].getd(i))
                  {
                     ++nscore;
                  }
               }
            }
            ++attempt;
         }
         NNs[7].Load(best);
         System.out.format("AFTER TRAINING: %.3f\n", NNs[7].evaluate(fdata, ans[7]));
         NNs[7].save("NN7.json", false);

         bestscore = score;
         eta = Math.max(0.01d,mutate(eta));
         lambda = Math.max(0.001d,mutate(lambda));
         System.out.format("New eta: %.3f; new lambda: %.3f\n", eta, lambda);
      }
   }
}