package io.github.uwfai.tictactoe;

import io.github.uwfai.neural.Matrix;
import io.github.uwfai.neural.NeuralNetwork;
import io.github.uwfai.neural.function.ActivationFunction;
import io.github.uwfai.neural.function.CostFunction;
import io.github.uwfai.neural.function.InitializationFunction;
import io.github.uwfai.neural.function.RegularizationFunction;
import io.github.uwfai.neural.layer.Layer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by carso on 4/13/2017.
 */
public final class NNTrain
{
   private static double eta = 5.0d;
   private static double lambda = 0.1d;
   private static int inner = 5;
   private static int batchsize = 33;
   private static CostFunction ct = CostFunction.CROSSENTROPY;
   private static ActivationFunction at = ActivationFunction.SIGMOID;
   private static InitializationFunction it = InitializationFunction.SMART;
   private static RegularizationFunction rt = RegularizationFunction.L2;

   private static Random gen = new Random();
   private static double mutation = 0.01d;

   private static NeuralNetwork[] NNs = new NeuralNetwork[9];

   public static double mutate(double value) {
      return value+(value*mutation*(gen.nextDouble()-gen.nextDouble()));
   }

   public static void main(String[] args)
   {
      ArrayList<Matrix> alData = new ArrayList<>();

      alData.addAll(Arrays.stream(Data.data).map((d) -> new Matrix(d, true)).collect(Collectors.toList()));

      ArrayList[] ans = {
            new ArrayList<Matrix>(),
            new ArrayList<Matrix>(),
            new ArrayList<Matrix>(),
            new ArrayList<Matrix>(),
            new ArrayList<Matrix>(),
            new ArrayList<Matrix>(),
            new ArrayList<Matrix>(),
            new ArrayList<Matrix>(),
            new ArrayList<Matrix>(),
            new ArrayList<Matrix>()
      };

      int[][] answ = {Data.answ0, Data.answ1, Data.answ2, Data.answ3, Data.answ4, Data.answ5, Data.answ6, Data.answ7, Data.answ8, Data.answ9};

      int[] ansi = {0, 0, 0, 0, 0, 0, 0, 0, 0};

      // GENERATE DATA FOR VERTICAL+HORIZONTAL NETWORK STRUCTURE
      for (int i = 0; i < 852; ++i)
      {
         if (answ[0][ansi[0]] == i)
         {
            ans[0].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ans[3].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ansi[0] = (ansi[0] + 1 < answ[0].length ? ansi[0] + 1 : ansi[0]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 0 && x != 3)
               {
                  ans[x].add(new Matrix(new double[][]{new double[]{0.0d}}));
               }
            }
         }
         else if (answ[1][ansi[1]] == i)
         {
            ans[0].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ans[4].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ansi[1] = (ansi[1] + 1 < answ[1].length ? ansi[1] + 1 : ansi[1]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 0 && x != 4)
               {
                  ans[x].add(new Matrix(new double[][]{new double[]{0.0d}}));
               }
            }
         }
         else if (answ[2][ansi[2]] == i)
         {
            ans[0].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ans[5].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ansi[2] = (ansi[2] + 1 < answ[2].length ? ansi[2] + 1 : ansi[2]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 0 && x != 5)
               {
                  ans[x].add(new Matrix(new double[][]{new double[]{0.0d}}));
               }
            }
         }
         else if (answ[3][ansi[3]] == i)
         {
            ans[1].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ans[3].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ansi[3] = (ansi[3] + 1 < answ[3].length ? ansi[3] + 1 : ansi[3]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 1 && x != 3)
               {
                  ans[x].add(new Matrix(new double[][]{new double[]{0.0d}}));
               }
            }
         }
         else if (answ[4][ansi[4]] == i)
         {
            ans[1].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ans[4].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ansi[4] = (ansi[4] + 1 < answ[4].length ? ansi[4] + 1 : ansi[4]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 1 && x != 4)
               {
                  ans[x].add(new Matrix(new double[][]{new double[]{0.0d}}));
               }
            }
         }
         else if (answ[5][ansi[5]] == i)
         {
            ans[1].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ans[5].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ansi[5] = (ansi[5] + 1 < answ[5].length ? ansi[5] + 1 : ansi[5]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 1 && x != 5)
               {
                  ans[x].add(new Matrix(new double[][]{new double[]{0.0d}}));
               }
            }
         }
         else if (answ[6][ansi[6]] == (double) i)
         {
            ans[2].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ans[3].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ansi[6] = (ansi[6] + 1 < answ[6].length ? ansi[6] + 1 : ansi[6]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 2 && x != 3)
               {
                  ans[x].add(new Matrix(new double[][]{new double[]{0.0d}}));
               }
            }
         }
         else if (answ[7][ansi[7]] == (double) i)
         {
            ans[2].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ans[4].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ansi[7] = (ansi[7] + 1 < answ[7].length ? ansi[7] + 1 : ansi[7]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 2 && x != 4)
               {
                  ans[x].add(new Matrix(new double[][]{new double[]{0.0d}}));
               }
            }
         }
         else if (answ[8][ansi[8]] == (double) i)
         {
            ans[2].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ans[5].add(new Matrix(new double[][]{new double[]{1.0d}}));
            ansi[8] = (ansi[8] + 1 < answ[8].length ? ansi[8] + 1 : ansi[8]);
            for (int x = 0; x < 6; ++x)
            {
               if (x != 2 && x != 5)
               {
                  ans[x].add(new Matrix(new double[][]{new double[]{0.0d}}));
               }
            }
         }
      }
      for (int i = 0; i < 852; ++i)
      {
         ans[7].add(new Matrix(9, 1));

         for (int n = 0; n < 9; ++n)
         {
            if (answ[9][i] == n)
            {
               ((Matrix) ans[7].get(i)).set(n, 0, 1.0d);
            }
         }
      }

      // GENERATE DATA FOR INDIVIDUAL SPACE NETWORK STRUCTURE
      /*for (int a = 0; a < 9; ++a) {
         int l = 0;
         for (int i = 0; i < answ[a].size(); ++i) {
            for (; l < (int)Math.floor(answ[a].getd(i)); ++l) {
               ans[a].add(new Matrix(0.0d));
            }
            ans[a].add(new Matrix(1.0d));
            l = ans[a].size();
         }
         while (l < Data.data.size()) {
            ans[a].add(new Matrix(0.0d));
            ++l;
         }
      }
      for (int i = 0; i < answ[9].size(); ++i) {
         ans[9].add(new Matrix());
         for (int n = 0; n < 9; ++n) {
            if (answ[9].getd(i) == (double)n) {
               ans[9].getm(i).add(1.0d);
            } else {
               ans[9].getm(i).add(0.0d);
            }
         }
      }*/

      for (int n = 0; n < 6; ++n)
      {
         NNs[n] = new NeuralNetwork(9, it, rt, ct, new Layer[]{new Layer(inner, at), new Layer(1, at)});
         //NNs[n] = NeuralNetwork.LoadFromFile("NN"+n+".json");
      }

      NNs[7] = new NeuralNetwork(6, it, rt, ct, new Layer[]{new Layer(inner, at), new Layer(9, at)});

      int bestscore = 0;
      while (bestscore < 500)
      {
         for (int n = 0; n < 6; ++n)
         {
            System.out.format("Before training: %.5f\n", NNs[n].evaluate(alData, ans[n]));
            double score = NNs[n].evaluate(alData, ans[n]);
            String best = NNs[n].json();
            int attempt = 0;
            while (attempt < 50)
            {
               NNs[n].train(alData, ans[n], 1, batchsize, eta, lambda);
               while (NNs[n].evaluate(alData, ans[n]) < score)
               {
                  best = NNs[n].json();
                  score = NNs[n].evaluate(alData, ans[n]);
                  NNs[n].train(alData, ans[n], 1, batchsize, eta, lambda);
                  attempt = 0;
               }
               ++attempt;
            }
            NNs[n] = NeuralNetwork.Load(best);
            System.out.format("After training: %.5f\n", NNs[n].evaluate(alData, ans[n]));
            NNs[n].save("NN" + n + ".json", false);
         }

         ArrayList<Matrix> fdata = new ArrayList<>();
         for (int i = 0; i < 852; ++i)
         {
            fdata.add(new Matrix(6, 1));
            for (int n = 0; n < 6; ++n)
            {
               fdata.get(i).set(n, 0, NNs[n].feedforward(alData.get(i)).get(0, 0));
            }
         }

         int score = 0;
         for (int i = 0; i < 852; ++i)
         {
            Matrix fa = NNs[7].feedforward(fdata.get(i));
            int max = 0;
            for (int n = 1; n < 6; ++n)
            {
               if (fa.get(n, 0) > fa.get(max, 0))
               {
                  max = n;
               }
            }
            if (max == answ[9][i])
            {
               ++score;
            }
         }

         int nscore = score;
         String best = NNs[7].json();
         int attempt = 0;
         while (attempt < 100)
         {
            NNs[7].train(fdata, ans[7], 1, batchsize, eta, lambda);
            while (nscore >= score)
            {
               best = NNs[7].json();
               System.out.format("Current: %.3f (%d of %d correct)\n", NNs[7].evaluate(fdata, ans[7]), nscore, 852);
               NNs[7].train(fdata, ans[7], 1, batchsize, eta, lambda);
               attempt = 0;
               score = nscore;

               nscore = 0;
               for (int i = 0; i < 852; ++i)
               {
                  Matrix fa = NNs[7].feedforward(fdata.get(i));
                  int max = 0;
                  for (int n = 1; n < 6; ++n)
                  {
                     if (fa.get(n, 0) > fa.get(max, 0))
                     {
                        max = n;
                     }
                  }
                  if (max == answ[9][i])
                  {
                     ++nscore;
                  }
               }
            }
            ++attempt;
         }
         NNs[7] = NeuralNetwork.Load(best);
         System.out.format("AFTER TRAINING: %.3f\n", NNs[7].evaluate(fdata, ans[7]));
         NNs[7].save("NN7.json", false);

         bestscore = score;
         eta = Math.max(0.01d,mutate(eta));
         lambda = Math.max(0.001d,mutate(lambda));
         System.out.format("New eta: %.3f; new lambda: %.3f\n", eta, lambda);
      }
   }
}