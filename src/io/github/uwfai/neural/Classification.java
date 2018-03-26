package io.github.uwfai.neural;

import io.github.uwfai.neural.function.ActivationFunction;
import io.github.uwfai.neural.function.CostFunction;
import io.github.uwfai.neural.function.InitializationFunction;
import io.github.uwfai.neural.function.RegularizationFunction;
import io.github.uwfai.neural.layer.Layer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Classification {
	public static void main(String[] args) {
		NeuralNetwork NN = new NeuralNetwork(
				1234L,
				2,
				InitializationFunction.SMART,
				RegularizationFunction.L2,
				CostFunction.QUADRATIC,
				new Layer[]{
					new Layer(27, ActivationFunction.SIGMOID),
					new Layer(1, ActivationFunction.SIGMOID)
				});
		
		Random gen = new Random(System.currentTimeMillis());
		
		List<Matrix> data = new ArrayList<>();
		List<Matrix> answers = new ArrayList<>();
		
		double flux = 0.25;
		double r = 3.0;
		double R = 5.0;
		for (int i = 0; i < 1000; ++i) {
			double theta = 2*Math.PI*gen.nextDouble();
			double rplus = flux*(gen.nextDouble()-gen.nextDouble());
			if (gen.nextDouble() > 0.5) {
				data.add(new Matrix(new double[][]{ new double[]{ Math.pow((R+rplus)*Math.cos(theta),2.0d) }, new double[] { Math.pow((R+rplus)*Math.sin(theta),2.0d) } }));
				answers.add(new Matrix(new double[][]{ new double[] { 1.0d } }));
			} else {
				double tr = (r+rplus)*gen.nextDouble();
				data.add(new Matrix(new double[][]{ new double[]{ Math.pow((tr)*Math.cos(theta),2.0d) }, new double[] { Math.pow((tr)*Math.sin(theta),2.0d) } }));
				answers.add(new Matrix(new double[][]{ new double[] { 0.0d } }));
			}
		}

		int width = 500;
		int height = 500;
		for (int i = 0; i < 100; ++i) {
			NN.train(data, answers, 1, 25, 5.0d, 0.1d);
			System.out.format("Evaluation: %.3f\n", NN.evaluate(data, answers));
			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			for (int xi = 0; xi < width; ++xi) {
				for (int yi = 0; yi < height; ++yi) {
					//double v = NN.feedforward(new Matrix((R+r+flux)*(xi-(width/2.0))/(width/2.0), (R+r+flux)*(yi-(height/2.0))/(height/2.0))).getd(0);
					double v = NN.feedforward(new Matrix(new double[][]{ new double[]{ Math.pow((R+r+flux)*(xi-(width/2.0))/(width/2.0),2.0d) }, new double[]{ Math.pow((R+r+flux)*(yi-(height/2.0))/(height/2.0),2.0d) } })).get(0, 0);
					int alpha = (int)(255 << 24);
					int red = (int)(((int)Math.floor((v < 0.5 ? ((0.5-v)/0.5)*255.0 : 0))) << 16);
					int green = (int)(0 << 8);
					int blue = (int)(((int)Math.floor((v > 0.5 ? ((v-0.5)/0.5)*255.0 : 0))) << 0);
					bi.setRGB(xi, yi, alpha+red+green+blue);
				}
			}
			try {
				File output = new File("train-"+i+".png");

				if (!output.exists())
				{
					output.createNewFile();
				}

				ImageIO.write(bi, "png", output);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}