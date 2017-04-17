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

   public double Eta(double etamin, double etamax, double accuracy, boolean update) {
      if (lambda > 0 && batchsize > 0) {
         NeuralNetwork NN = new NeuralNetwork().Load(current);

         double[] costs = new double[10];
         int h = 9;
         int l = 0;
         double etadif = etamax-etamin;

         for (int ex = 0; ex < 10; ++ex) {
            NN = new NeuralNetwork().Load(current);
            NN.setEta(etamin+(ex*((etamax-etamin)/10.0d)));
            NN.train(this.data, this.answers, 25, this.batchsize);
            costs[ex] = NN.evaluate(this.data, this.answers);
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
            //System.out.println("Eta opti: shifting...");

            if (l == 0 && h == 9) {
               System.out.println("Eta opti: shifting left...");
               etamax = etamin+(etadif/4.0d);
               etamin = etamax-etadif;
            } else {
               System.out.println("Eta opti: shifting right...");
               etamin = etamax-(etadif/4.0d);
               etamax = etamin+etadif;
            }

            costs = new double[10];

            for (int ex = 0; ex < 10; ++ex) {
               NN = new NeuralNetwork().Load(current);
               NN.setEta(etamin+(ex*((etamax-etamin)/10.0d)));
               NN.train(this.data, this.answers, 25, this.batchsize);
               costs[ex] = NN.evaluate(this.data, this.answers);
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
         while (etamax-etamin > Math.pow(10.0d,-accuracy)) {
            System.out.println("Eta opti: zooming...");

            etadif = (etamax-etamin)/10.0d;
            etamin = etamin+(etadif*(l-1));
            etamax = etamin+(etadif*(l+1));

            for (int ex = 0; ex < 10; ++ex) {
               NN = new NeuralNetwork().Load(current);
               NN.setEta(etamin+(ex*((etamax-etamin)/10.0d)));
               NN.train(this.data, this.answers, 25, this.batchsize);
               costs[ex] = NN.evaluate(this.data, this.answers);
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

         for (int ex = 0; ex < 10; ++ex) {
            NN = new NeuralNetwork().Load(current);
            NN.setLambda(lambdamin+(ex*((lambdamax-lambdamin)/10.0d)));
            NN.train(this.data, this.answers, 25, this.batchsize);
            costs[ex] = NN.evaluate(this.data, this.answers);
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
               NN.train(this.data, this.answers, 25, this.batchsize);
               costs[ex] = NN.evaluate(this.data, this.answers);
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
               NN.train(this.data, this.answers, 25, this.batchsize);
               costs[ex] = NN.evaluate(this.data, this.answers);
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
