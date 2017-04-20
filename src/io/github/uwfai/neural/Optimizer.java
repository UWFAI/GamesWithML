package io.github.uwfai.neural;

/**
 * Created by carso on 4/14/2017.
 */
public class Optimizer
{
   private String current;
   private Matrix data;
   private Matrix answers;
   private double eta = 0.0d;
   private double lambda = 0.0d;
   private int batchsize = 25;
   private int steps = 50;

   public void histogram(double[] data) {
      int[] bars = new int[steps];
      int l = 0;
      int h = steps-1;
      for (int i = 0; i < steps; ++i) {
         if (data[i] < data[l]) {
            l = i;
         }
         if (data[i] > data[h]) {
            h = i;
         }
      }
      for (int i = 0; i < steps; ++i) {
         bars[i] = (int)Math.round(10*(data[i]-data[l])/(data[h]-data[l]));
      }
      for (int i = 10; i > 0; --i) {
         for (int b = 0; b < steps; ++b) {
            if (bars[b] >= i) {
               System.out.format("*");
            } else {
               System.out.format(" ");
            }
         }
         if (i == 10) {
            System.out.format(" %.5f", data[h]);
         }
         if (i == 1) {
            System.out.format(" %.5f", data[l]);
         }
         System.out.format("\n");
      }
   }

   public double Eta(double etamin, double etamax, double accuracy, boolean update) {
      if (lambda > 0 && batchsize > 0) {
         NeuralNetwork NN = new NeuralNetwork().Load(current);

         double[] costs = new double[steps];
         int h = steps-1;
         int l = 0;
         double etadif = etamax-etamin;

         for (int ex = 0; ex < steps; ++ex) {
            NN = new NeuralNetwork().Load(current);
            NN.setEta(etamin+(ex*(etadif/(double)steps)));
            NN.train(this.data, this.answers, 10, this.batchsize);
            costs[ex] = NN.evaluate(this.data, this.answers);
         }

         for (int c = 0; c < steps; ++c) {
            if (costs[c] > costs[h]) {
               h = c;
            }
            if (costs[c] < costs[l]) {
               l = c;
            }
         }

         System.out.format("Lowest at %d (%.3f); highest at %d (%.3f)\n", l, costs[l], h, costs[h]);

         // find range of minimum
         while ((l == 0 && h == steps-1) || (l == steps-1 && h == 0)) {
            //System.out.println("Eta opti: shifting...");

            histogram(costs);

            if (l == 0 && h == steps-1 && (etamin+(etadif/4.0d)-etadif) > 0) {
               System.out.println("Eta opti: shifting left...");
               etamax = etamin+(etadif/4.0d);
               etamin = etamax-etadif;
            } else if (l == steps-1 && h == 0) {
               System.out.println("Eta opti: shifting right...");
               etamin = etamax-(etadif/4.0d);
               etamax = etamin+etadif;
            } else {
               break;
            }

            costs = new double[steps];

            for (int ex = 0; ex < steps; ++ex) {
               NN = new NeuralNetwork().Load(current);
               NN.setEta(etamin+(ex*((etamax-etamin)/(double)steps)));
               NN.train(this.data, this.answers, 10, this.batchsize);
               costs[ex] = NN.evaluate(this.data,this.answers);
            }

            h = 9;
            l = 0;
            for (int c = 0; c < steps; ++c) {
               if (costs[c] > costs[h]) {
                  h = c;
               }
               if (costs[c] < costs[l]) {
                  l = c;
               }
            }
         }

         // zoom in down to accuracy on minimum
         while (etamax-etamin > Math.pow(10.0d,-accuracy)) {
            histogram(costs);

            System.out.println("Eta opti: zooming...");

            etadif = (etamax-etamin)/(double)steps;
            etamin = etamin+(etadif*(l-(l > 0 ? 1 : 0)));
            etamax = etamin+(etadif*(l+(l < 9 ? 1 : 0)));

            costs = new double[steps];

            for (int ex = 0; ex < steps; ++ex) {
               NN = new NeuralNetwork().Load(current);
               NN.setEta(etamin+(ex*((etamax-etamin)/(double)steps)));
               NN.train(this.data, this.answers, 10, this.batchsize);
               costs[ex] = NN.evaluate(this.data, this.answers);
            }

            l = 0;
            for (int c = 0; c < steps; ++c) {
               if (costs[c] < costs[l]) {
                  l = c;
               }
            }
         }

         // if update, change current NN
         if (update) {
            System.out.format("etamin: %.5f, etamax: %.3f\n", etamin, etamax);
            this.eta = (etamax+etamin)/2.0d;
            this.current = NN.json();
         }
      }
      return (etamax+etamin)/2.0d;
   }

   public double Eta(double etamin, double etamax, int accuracy) {
      return this.Eta(etamin, etamax, accuracy, false);
   }

   public double Lambda(double lambdamin, double lambdamax, int accuracy, boolean update) {
      if (eta > 0 && batchsize > 0) {
         NeuralNetwork NN = new NeuralNetwork().Load(current);

         double[] costs = new double[10];
         int h = 9;
         int l = 0;
         double lambdadif = lambdamax-lambdamin;

         double first;
         double last;
         for (int ex = 0; ex < 10; ++ex) {
            NN = new NeuralNetwork().Load(current);
            NN.setLambda(lambdamin+(ex*((lambdamax-lambdamin)/10.0d)));
            last = NN.evaluate(this.data, this.answers);
            first = last;
            for (int e = 0; e < 10; ++e) {
               NN.train(this.data, this.answers, 1, this.batchsize);
               double temp = NN.evaluate(this.data, this.answers);
               costs[ex] += temp-last;
               last = temp;
            }
            costs[ex] += last-first;
         }

         for (int c = 0; c < 10; ++c) {
            if (costs[c] > costs[h]) {
               h = c;
            }
            if (costs[c] < costs[l]) {
               l = c;
            }
         }

         // find range of minimum
         while ((l == 0 && h == 9) || (l == 9 && h == 0)) {
            System.out.println("Lambda opti: shifting...");

            if (l == 0 && h == 9) {
               lambdamax = lambdamin;
               lambdamin = lambdamax-lambdadif;
            } else {
               lambdamin = lambdamax;
               lambdamax = lambdamin+lambdadif;
            }

            costs = new double[10];

            for (int ex = 0; ex < 10; ++ex) {
               NN = new NeuralNetwork().Load(current);
               NN.setLambda(lambdamin+(ex*((lambdamax-lambdamin)/10.0d)));
               last = NN.evaluate(this.data, this.answers);
               first = last;
               for (int e = 0; e < 5; ++e) {
                  NN.train(this.data, this.answers, 1, this.batchsize);
                  double temp = NN.evaluate(this.data, this.answers);
                  costs[ex] += temp-last;
                  last = temp;
               }
               costs[ex] += last-first;
            }

            h = 9;
            l = 0;
            for (int c = 0; c < 10; ++c) {
               if (costs[c] > costs[h]) {
                  h = c;
               }
               if (costs[c] < costs[l]) {
                  l = c;
               }
            }
         }

         // zoom in down to accuracy on minimum
         while (lambdamax-lambdamin > Math.pow(10.0d,-accuracy)) {
            System.out.println("Lambda opti: zooming...");

            lambdadif = (lambdamax-lambdamin)/10.0d;
            lambdamin = lambdamin+(lambdadif*(l-1));
            lambdamax = lambdamin+(lambdadif*(l+1));

            costs = new double[10];

            for (int ex = 0; ex < 10; ++ex) {
               NN = new NeuralNetwork().Load(current);
               NN.setLambda(lambdamin+(ex*((lambdamax-lambdamin)/10.0d)));
               last = NN.evaluate(this.data, this.answers);
               first = last;
               for (int e = 0; e < 5; ++e) {
                  NN.train(this.data, this.answers, 1, this.batchsize);
                  double temp = NN.evaluate(this.data, this.answers);
                  costs[ex] += temp-last;
                  last = temp;
               }
               costs[ex] += last-first;
            }

            l = 0;
            for (int c = 0; c < 10; ++c) {
               if (costs[c] < costs[l]) {
                  l = c;
               }
            }
         }

         // if update, change current NN
         if (update) {
            this.lambda = (lambdamax+lambdamin)/2.0d;
            this.current = NN.json();
         }
      }
      return (lambdamax+lambdamin)/2.0d;
   }

   public double Lambda(double lambdamin, double lambdamax, int accuracy) {
      return this.Lambda(lambdamin, lambdamax, accuracy, false);
   }

   public void setEta(double neta) {
      this.eta = neta;
   }

   public void setLambda(double nlambda) {
      this.lambda = nlambda;
   }

   public void setBatchsize(int nbatchsize) {
      this.batchsize = nbatchsize;
   }

   public double getEta() {
      return this.eta;
   }

   public double getLambda() {
      return this.lambda;
   }

   public int getBatchsize() {
      return this.batchsize;
   }

   public String getCurrent() {
      return this.current;
   }

   public Optimizer(NeuralNetwork NN, Matrix data, Matrix answers) {
      this.current = NN.json();
      this.eta = NN.getEta();
      this.lambda = NN.getLambda();
      this.data = data;
      this.answers = answers;
   }
}
