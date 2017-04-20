package io.github.uwfai.neural;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.lang.Exception;
import java.util.concurrent.Callable;
import java.util.function.DoubleConsumer;
import java.util.function.Function;

public class Matrix {
	private ArrayList matrix;
	
	public int size() {
		return this.matrix.size();
	}
	
	public void set(int index, double value) {
		this.matrix.set(index, value);
	}
	
	public void set(int index, Matrix value) {
		this.matrix.set(index, value);
	}
	
	public Matrix shape() {
		Matrix newmatrix = new Matrix();
		try {
			for (Object element : this.matrix) {
				if (element instanceof Double) {
					newmatrix.append(0.0d);
				} else if (element instanceof Matrix) {
					newmatrix.append(((Matrix)element).shape());
				} else {
					throw new Exception("unknown type");
				}
			}
		} catch (Exception e) {
			System.err.println(String.format("Error getting matrix shape: %s", e.getMessage()));
			e.printStackTrace();
		}
		return newmatrix;
	}

	public String print() {
		String ret = "Matrix(";
		for (int p = 0; p < this.size(); ++p) {
			if (this.get(p) instanceof Matrix) {
            ret += this.getm(p).print();
         } else if (this.get(p) instanceof Double) {
            ret += this.getd(p);
         } else {
            ret += this.get(p).getClass().getName();
         }
			if (p < this.size()-1) {
				ret += ",";
			}
		}
		return ret+")";
	}

	public String json() {
		String ret = "[";
		for (int i = 0; i < this.size(); ++i) {
			if (this.get(i) instanceof Matrix) {
				ret += this.getm(i).json();
			} else {
				ret += Double.toString(this.getd(i));
			}
			if (i < this.size()-1) {
				ret += ",";
			}
		}
		return ret+"]";
	}
	
	public void remove(int index) {
		this.matrix.remove(index);
	}
	
	public Matrix copy() {
		Matrix newmatrix = new Matrix();
		try {
			for (Object element : this.matrix) {
				if (element instanceof Double) {
					newmatrix.append((double)element);
				} else if (element instanceof Matrix) {
					newmatrix.append(((Matrix)element).copy());
				} else {
					throw new Exception("unknown type");
				}
			}
		} catch (Exception e) {
			System.err.println(String.format("Error copying matrix: %s", e.getMessage()));
			e.printStackTrace();
		}
		return newmatrix;
	}
	
	public ArrayList clone() {
		ArrayList newmatrix = new ArrayList();
		try {
			for (Object element : this.matrix) {
				if (element instanceof Double) {
					newmatrix.add((double)element);
				} else if (element instanceof Matrix) {
					newmatrix.add(((Matrix)element).copy());
				} else {
					throw new Exception("unknown type ("+element.getClass().getName()+")");
				}
			}
		} catch (Exception e) {
			System.err.println(String.format("Error cloning matrix: %s", e.getMessage()));
			e.printStackTrace();
		}
		return newmatrix;
	}
	
	public boolean similar(Matrix matrix) {
		boolean sim = true;
		for (int index = 0; index < this.size(); ++index) {
			if (this.get(index) instanceof Double) {
				if (!(matrix.get(index) instanceof Double)) {
					return false;
				}
			} else {
				if (matrix.get(index) instanceof Matrix) {
					sim &= ((Matrix)matrix.get(index)).similar((Matrix)matrix.get(index));
				} else {
					return false;
				}
			}
		}
		return sim;
	}
	
	public Matrix column(int col) {
		Matrix newmatrix = new Matrix();
		try {
			for (int index = 0; index < this.size(); ++index) {
				if (this.get(index) instanceof Matrix && ((Matrix)this.get(0)).similar((Matrix)this.get(index))) {
					if (((Matrix)this.get(index)).get(col) instanceof Matrix) {
						newmatrix.append((Matrix)((Matrix)this.get(index)).get(col));
					} else if (((Matrix)this.get(index)).get(col) instanceof Double) {
						newmatrix.append((double)((Matrix)this.get(index)).get(col));
					} else {
						throw new Exception("unknown type");
					}
				} else {
					throw new Exception("not all elements are matrices and similar");
				}
			}
		} catch (Exception e) {
			System.err.println(String.format("Error getting matrix column: %s", e.getMessage()));
			e.printStackTrace();
		}
		return newmatrix;
	}
	
	public Matrix flip() {
		Matrix newmatrix = new Matrix();
		try {
			for (int col = 0; col < ((Matrix)this.get(0)).size(); ++col) {
				newmatrix.append(this.column(col));
			}
		} catch (Exception e) {
			System.err.println(String.format("Error flipping matrix: %s", e.getMessage()));
			e.printStackTrace();
		}
		return newmatrix;
	}
	
	public Object get(int index) {
		Object obj = new Object();
		try {
			if (index < this.matrix.size() && index >= 0) {
				obj = this.matrix.get(index);
			} else {
				throw new Exception(String.format("index %d out of matrix bounds", index));
			}
		} catch (Exception e) {
			System.err.println(String.format("Error getting element: %s", e.getMessage()));
			e.printStackTrace();
		}
		return obj;
	}
	
	public Matrix product(Matrix m) {
		Matrix newmatrix = new Matrix();
		try {
			if (this.similar(m)) {
				for (int index = 0; index < this.size(); ++index) {
					if (this.get(index) instanceof Matrix) {
						newmatrix.append(((Matrix)this.get(index)).product((Matrix)m.get(index)));
					} else if (this.get(index) instanceof Double){
						newmatrix.append((double)this.get(index)*(double)m.get(index));
					} else {
                  throw new Exception("unknown type");
               }
				}
			} else {
				throw new Exception("matrices not the same shape for Hadamard product");
			}
		} catch (Exception e) {
			System.err.println(String.format("Error performing Hadamard product: %s", e.getMessage()));
			e.printStackTrace();
		}
		return newmatrix;
	}

	public Matrix division(Matrix m) {
      Matrix newmatrix = new Matrix();
      try {
         if (this.similar(m)) {
            for (int index = 0; index < this.size(); ++index) {
               if (this.get(index) instanceof Matrix) {
                  newmatrix.append(((Matrix)this.get(index)).product((Matrix)m.get(index)));
               } else if (this.get(index) instanceof Double) {
                  newmatrix.append((double)this.get(index)/(double)m.get(index));
               } else {
                  throw new Exception("unknown type");
               }
            }
         } else {
            throw new Exception("matrices not the same shape for Hadamard division");
         }
      } catch (Exception e) {
         System.err.println(String.format("Error performing Hadamard division: %s", e.getMessage()));
         e.printStackTrace();
      }
      return newmatrix;
   }
	
	public double sum() {
		double total = 0.0d;
		try {
			for (int index = 0; index < this.size(); ++index) {
				if (this.get(index) instanceof Matrix) {
					total += ((Matrix)this.get(index)).sum();
				} else if (this.get(index) instanceof Double) {
					total += (double)this.get(index);
				} else {
					throw new Exception("element type unknown");
				}
			}
		} catch (Exception e) {
			System.err.println(String.format("Error performing matrix sum: %s", e.getMessage()));
			e.printStackTrace();
		}
		return total;
	}
	
	public Matrix append(double value) {
		this.matrix.add(value);
		return this;
	}
	
	public Matrix append(Matrix value) {
		this.matrix.add(value);
		return this;
	}
	
	public Matrix prepend(double value) {
		this.matrix.add(0,value);
		return this;
	}
	
	public Matrix prepend(Matrix value) {
		this.matrix.add(0,value);
		return this;
	}

	public Matrix mapply(Function<Matrix,Matrix> f) {
		Matrix newmatrix = new Matrix();
		try {
			for (int index = 0; index < this.size(); ++index) {
            if (this.get(index) instanceof Matrix)
            {
               newmatrix.append(f.apply(this.getm(index)));
            } else {
               throw new Exception("can't apply matrix-only function to non-matrix");
            }
			}
		}
		catch (Exception e)
		{
         System.err.println("Failed mapplying to Matrix: %s".format(e.getMessage()));
         e.printStackTrace();
		}
		return newmatrix;
	}

	public Matrix apply(Function<Double,Double> f) {
      Matrix newmatrix = new Matrix();
      try {
         for (int index = 0; index < this.size(); ++index) {
            if (this.get(index) instanceof Matrix) {
               newmatrix.append(this.getm(index).apply(f));
            } else if (this.get(index) instanceof Double) {
               newmatrix.append(f.apply(this.getd(index)));
            } else {
               throw new Exception("unknown type");
            }
         }
      }
      catch (Exception e)
      {
         System.err.println("cannot apply function to matrix: %s".format(e.getMessage()));
         e.printStackTrace();
      }
      return newmatrix;
   }

	public Matrix fill(double value) {
      try
      {
         for (int index = 0; index < this.size(); ++index)
         {
            if (this.get(index) instanceof Matrix) {
               this.getm(index).fill(value);
            } else if (this.get(index) instanceof Double) {
               this.set(index, value);
            } else {
               throw new Exception("unknown type");
            }
         }
      }
      catch (Exception e)
      {
         System.err.println("Failed to fill matrix: %s".format(e.getMessage()));
         e.printStackTrace();
      }
      return this;
   }
	
	public Matrix add(Matrix matrix) {
		Matrix newmatrix = new Matrix();
		try {
			if (this.similar(matrix)) {
				for (int index = 0; index < this.size(); ++index) {
					if (this.get(index) instanceof Matrix) {
						newmatrix.append(((Matrix)this.get(index)).add((Matrix)matrix.get(index)));
					} else {
						newmatrix.append((double)this.get(index)+(double)matrix.get(index));
					}
				}
			} else {
				throw new Exception("matrices are not the same shape");
			}
		} catch (Exception e) {
			System.err.println(String.format("Error adding matrices: %s", e.getMessage()));
			e.printStackTrace();
		}
		return newmatrix;
	}
	
	public Matrix subtract(Matrix matrix) {
		Matrix newmatrix = new Matrix();
		try {
			if (this.similar(matrix)) {
				for (int index = 0; index < this.size(); ++index) {
					if (this.get(index) instanceof Matrix) {
						newmatrix.append(((Matrix)this.get(index)).subtract((Matrix)matrix.get(index)));
					} else {
						newmatrix.append((double)this.get(index)-(double)matrix.get(index));
					}
				}
			} else {
				throw new Exception("matrices are not the same shape");
			}
		} catch (Exception e) {
			System.err.println(String.format("Error subracting matrices: %s", e.getMessage()));
			e.printStackTrace();
		}
		return newmatrix;
	}
	
	public Matrix getm(int index) {
		Matrix result = new Matrix();
		try {
			if (index < this.size() && index >= 0) {
				if (this.get(index) instanceof Matrix) {
					result = (Matrix)this.get(index);
				} else {
					throw new Exception("item at index is not matrix - it's ("+this.get(index).getClass().getName()+")");
				}
			} else {
				throw new Exception("index out of matrix bounds");
			}
		} catch (Exception e) {
			System.err.println(String.format("Error strict-Matrix getting from matrix: %s", e.getMessage()));
			e.printStackTrace();
		}
		return result;
	}

	public double getd(int index) {
		double result = 0.0d;
		try {
			if (index < this.size() && index >= 0) {
				if (this.get(index) instanceof Double) {
					result = (double)this.get(index);
				} else {
					throw new Exception("item at index is not double - it's ("+this.get(index).getClass().getName()+")");
				}
			} else {
				throw new Exception("index out of matrix bounds");
			}
		} catch (Exception e) {
			System.err.println(String.format("Error strict-Double getting from matrix: %s", e.getMessage()));
			e.printStackTrace();
		}
		return result;
	}

	public void insert(int index, Double db) {
      this.matrix.add(index, db);
   }

   public void insert(int index, Matrix mx) {
      this.matrix.add(index, mx);
   }

	public void check() {
      for (int index = 0; index < this.size(); ++index) {
         if (this.get(index) instanceof LinkedTreeMap) {
            this.insert(index, GsonConvert.convert((LinkedTreeMap)this.get(index)));
            this.remove(index+1);
         }
      }
	}
	
	public Matrix(double... values) {
		this.matrix = new ArrayList();
		for (double value : values) {
			matrix.add(value);
		}
	}

	public Matrix (Matrix matrix1, Matrix... matrices) {
		this.matrix = new ArrayList();
		this.matrix.add(matrix1);
		for (Matrix nmatrix : matrices) {
			this.matrix.add(nmatrix);
		}
	}
	
	public Matrix(Matrix matrix) {
		this.matrix = matrix.clone();
	}
	
	public Matrix() {
		this.matrix = new ArrayList();
	}
}