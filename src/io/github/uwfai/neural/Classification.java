package io.github.uwfai.neural;

import io.github.uwfai.neural.activation.ActivationFunction;
import io.github.uwfai.neural.cost.CostFunction;
import io.github.uwfai.neural.initialization.InitializationFunction;
import io.github.uwfai.neural.regularization.RegularizationFunction;

import java.awt.image.BufferedImage;
import java.util.Random;
import javax.imageio.ImageIO;
import java.io.File;

public class Classification {
	public static void main(String[] args) {
		NeuralNetwork NN = new NeuralNetwork((long)1234)
			.Input(2)
			.Feedforward(3)
			.Output(1)
			.Build(200.0d,
					1.0d,
					CostFunction.CostType.QUADRATIC,
					ActivationFunction.ActivationType.SIGMOID,
					InitializationFunction.InitializationType.SMART,
					RegularizationFunction.RegularizationType.L2);
		
		Random gen = new Random(System.currentTimeMillis());
		
		Matrix data = new Matrix();
		Matrix answers = new Matrix();
		
		double flux = 0.25;
		double r = 3.0;
		double R = 5.0;
		for (int i = 0; i < 1000; ++i) {
			double theta = 2*Math.PI*gen.nextDouble();
			double rplus = flux*(gen.nextDouble()-gen.nextDouble());
			if (gen.nextDouble() > 0.5) {
				data.append(new Matrix((R+rplus)*Math.cos(theta), (R+rplus)*Math.sin(theta)));
				answers.append(new Matrix(1.0d));
			} else {
				double tr = (r+rplus)*gen.nextDouble();
				data.append(new Matrix((tr)*Math.cos(theta), (tr)*Math.sin(theta)));
				answers.append(new Matrix(0.0d));
			}
		}

		/*System.out.println("Beginning optimization");
		Optimizer op = new Optimizer(NN, data, answers);
		op.setLambda(0.1d);
		op.setBatchsize(25);
		op.Eta(200.0d, 300.0d, 2, true);
		NN.setEta(op.getEta());
		System.out.format("Optimized: eta of %.3f\n", NN.getEta());*/
		
		//Scanner scan = new Scanner(System.in);
		int width = 500;
		int height = 500;
		for (int i = 0; i < 10; ++i) {
			NN.train(data, answers, 1, 25);
			System.out.format("Evaluation: %.3f\n", NN.evaluate(data, answers));
			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			for (int xi = 0; xi < width; ++xi) {
				for (int yi = 0; yi < height; ++yi) {
					double v = NN.feedforward(new Matrix((R+r+flux)*(xi-(width/2.0))/(width/2.0), (R+r+flux)*(yi-(height/2.0))/(height/2.0))).getd(0);
					int alpha = (int)(255 << 24);
					int red = (int)(((int)Math.floor((v < 0.5 ? ((0.5-v)/0.5)*255.0 : 0))) << 16);
					int green = (int)(0 << 8);
					int blue = (int)(((int)Math.floor((v > 0.5 ? ((v-0.5)/0.5)*255.0 : 0))) << 0);
					bi.setRGB(xi, yi, alpha+red+green+blue);
				}
			}
			try {
				File output = new File("train-"+i+".png");
				if (!output.exists()) {
					output.createNewFile();
				}
				ImageIO.write(bi, "png", output);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}