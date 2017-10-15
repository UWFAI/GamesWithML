package io.github.uwfai.neural;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.function.Function;

public class Matrix {
	private int rows;
	private int columns;

	private double[][] matrix;

	public Matrix(double[][] values) {
		this.rows = values.length;
		this.columns = values[0].length;
		matrix = values;
	}

	public Matrix(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		matrix = new double[rows][columns];
	}

	public Matrix(int[] dimensions, int start) {
		this.rows = dimensions[start];
		this.columns = dimensions[start+1];
		matrix = new double[rows][columns];
	}

	public Matrix(int[] dimensions) {
		this(dimensions, 0);
	}

	public Matrix (Matrix matrixToCopy) {
		rows = matrixToCopy.getRows();
		columns = matrixToCopy.getColumns();
		matrix = new double[rows][columns];

		for (int row = 0; row < rows; ++row) {
			for (int column = 0; column < columns; ++column) {
				set(row, column, matrixToCopy.get(row, column));
			}
		}
	}

	public final int getRows() {
		return rows;
	}

	public final int getColumns() {
		return columns;
	}

	public final int size() {
		return rows * columns;
	}
	
	public final void set(int row, int column, double value) {
		this.matrix[row][column] = value;
	}

	public final double get(int row, int column) {
		return matrix[row][column];
	}
	
	public Matrix shape() {
		return new Matrix(rows, columns);
	}

	public final String toString() {
		String ret = "[";
		for (int row = 0; row < rows; ++row)
		{
			for (int column = 0; column < columns; ++column)
			{
				ret = ret.concat(" ".concat(Double.toString(get(row, column))).concat(","));
			}

			if (row < rows - 1)
			{
				ret = ret.concat(",\n ");
			}
		}
		return ret.concat("]");
	}
	
	public Matrix copy() {
		Matrix result = new Matrix(rows, columns);

		for (int row = 0; row < rows; ++row) {
			for (int column = 0; column < columns; ++column) {
				result.set(row, column, get(row, column));
			}
		}

		return result;
	}
	
	public final boolean similar(Matrix matrix) {
		return getRows() == matrix.getRows() && getColumns() == matrix.getColumns();
	}
	
	public final Matrix dot(Matrix other) throws RuntimeException {
		if (getRows() != other.getRows() || getColumns() != other.getColumns()) {
			throw new RuntimeException(String.format("Cannot produce cross product of matrices due to conflicting sizes: %dx%d vs %dx%d", getRows(), getColumns(), other.getRows(), other.getColumns()));
		}

		Matrix result = new Matrix(rows, other.getColumns());

		for (int row = 0; row < rows; ++row) {
			for (int column = 0; column < columns; ++column) {
				double sum = 0.0d;

				for (int multiply = 0; multiply < columns; ++multiply) {
					sum += get(row, multiply) * other.get(column, multiply);
				}

				result.set(row, column, sum);
			}
		}

		return result;
	}

	public final Matrix add(Matrix other) {
		checkIfSimilar(other);

		Matrix result = new Matrix(rows, columns);

		for (int row = 0; row < rows; ++row) {
			for (int column = 0; column < columns; ++column) {
				result.set(row, column, get(row, column) + other.get(row, column));
			}
		}

		return result;
	}

	public final Matrix subtract(Matrix other) {
		checkIfSimilar(other);

		Matrix result = new Matrix(rows, columns);

		for (int row = 0; row < rows; ++row) {
			for (int column = 0; column < columns; ++column) {
				result.set(row, column, get(row, column) - other.get(row, column));
			}
		}

		return result;
	}

	public final Matrix divide(Matrix other) {
		checkIfSimilar(other);

		Matrix result = new Matrix(rows, columns);

		for (int row = 0; row < rows; ++row) {
			for (int column = 0; column < columns; ++column) {
				result.set(row, column, get(row, column) / other.get(row, column));
			}
		}

      return result;
   }

	public final Matrix multiply(Matrix other) {
		checkIfSimilar(other);

		Matrix result = new Matrix(rows, columns);

      for (int row = 0; row < rows; ++row) {
      	for (int column = 0; column < columns; ++column) {
      		result.set(row, column, get(row, column) * other.get(row, column));
	      }
      }

      return result;
   }

	public final Matrix apply(Function<Double, Double> function) {
      Matrix result = new Matrix(rows, columns);

      for (int row = 0; row < rows; ++row) {
      	for (int column = 0; column < columns; ++column) {
      		set(row, column, function.apply(get(row, column)));
	      }
      }

      return result;
   }

	public Matrix fill(double value) {
		for (int row = 0; row < rows; ++row) {
			for (int column = 0; column < columns; ++column) {
				set(row, column, value);
			}
		}

      return this;
   }

   public double sum() {
		double result = 0.0d;

		for (int row = 0; row < rows; ++row) {
			for (int column = 0; column < columns; ++column) {
				result += get(row, column);
			}
		}

		return result;
   }

   public Matrix sum(int axis) {
		Matrix result;

		switch (axis) {
		case 0:
		{
			result = new Matrix(1, columns);
			for (int row = 0; row < rows; ++row)
			{
				for (int column = 0; column < columns; ++column)
				{
					result.set(0, column, result.get(0, column) + get(row, column));
				}
			}
			break;
		}
		case 1:
		{
			result = new Matrix(rows, 1);
			for (int column = 0; column < columns; ++column)
			{
				for (int row = 0; row < rows; ++row)
				{
					result.set(row, 0, result.get(row, 0) + get(row, column));
				}
			}
			break;
		}
		default:
		{
			result = shape();
			break;
		}
		}

		return result;
   }

	private void checkIfSimilar(Matrix other) throws RuntimeException {
		if (!similar(other)) {
			throw new RuntimeException(String.format("Matrices are not same size: %dx%d vs %dx%d", getRows(), getColumns(), other.getRows(), other.getColumns()));
		}
	}
}