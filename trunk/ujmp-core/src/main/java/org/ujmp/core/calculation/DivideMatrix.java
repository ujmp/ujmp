/*
 * Copyright (C) 2008-2010 by Holger Arndt
 *
 * This file is part of the Universal Java Matrix Package (UJMP).
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership and licensing.
 *
 * UJMP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * UJMP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with UJMP; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.ujmp.core.calculation;

import java.math.BigDecimal;

import org.ujmp.core.Matrix;
import org.ujmp.core.doublematrix.DenseDoubleMatrix2D;
import org.ujmp.core.interfaces.HasDoubleArray;
import org.ujmp.core.interfaces.HasDoubleArray2D;
import org.ujmp.core.matrix.DenseMatrix;
import org.ujmp.core.matrix.DenseMatrix2D;
import org.ujmp.core.matrix.SparseMatrix;
import org.ujmp.core.util.MathUtil;
import org.ujmp.core.util.UJMPSettings;
import org.ujmp.core.util.concurrent.PForEquidistant;

public interface DivideMatrix<T> {

	public static DivideMatrix<Matrix> INSTANCE = new DivideMatrix<Matrix>() {

		public void calc(final Matrix source1, final Matrix source2, final Matrix target) {
			if (source1 instanceof DenseMatrix && source2 instanceof DenseMatrix
					&& target instanceof DenseMatrix) {
				DivideMatrix.DENSEMATRIX.calc((DenseMatrix) source1, (DenseMatrix) source2,
						(DenseMatrix) target);
			} else if (source1 instanceof SparseMatrix && source2 instanceof SparseMatrix
					&& target instanceof SparseMatrix) {
				DivideMatrix.SPARSEMATRIX.calc((SparseMatrix) source1, (SparseMatrix) source2,
						(SparseMatrix) target);
			} else {
				for (long[] c : source1.allCoordinates()) {
					BigDecimal v1 = source1.getAsBigDecimal(c);
					BigDecimal v2 = source2.getAsBigDecimal(c);
					BigDecimal result = MathUtil.divide(v1, v2);
					target.setAsBigDecimal(result, c);
				}
			}
		}
	};

	public static DivideMatrix<DenseMatrix> DENSEMATRIX = new DivideMatrix<DenseMatrix>() {

		public void calc(final DenseMatrix source1, final DenseMatrix source2,
				final DenseMatrix target) {
			if (source1 instanceof DenseMatrix2D && source2 instanceof DenseMatrix2D
					&& target instanceof DenseMatrix2D) {
				DivideMatrix.DENSEMATRIX2D.calc((DenseMatrix2D) source1, (DenseMatrix2D) source2,
						(DenseMatrix2D) target);
			} else {
				for (long[] c : source1.allCoordinates()) {
					BigDecimal v1 = source1.getAsBigDecimal(c);
					BigDecimal v2 = source2.getAsBigDecimal(c);
					BigDecimal result = MathUtil.divide(v1, v2);
					target.setAsBigDecimal(result, c);
				}
			}
		}
	};

	public static DivideMatrix<SparseMatrix> SPARSEMATRIX = new DivideMatrix<SparseMatrix>() {

		public void calc(final SparseMatrix source1, final SparseMatrix source2,
				final SparseMatrix target) {
			// copy all elements in source1 to target
			for (long[] c : source1.availableCoordinates()) {
				BigDecimal svalue = source1.getAsBigDecimal(c);
				target.setAsBigDecimal(svalue, c);
			}
			// calculate difference with source2
			for (long[] c : source2.availableCoordinates()) {
				BigDecimal v1 = target.getAsBigDecimal(c);
				BigDecimal v2 = source2.getAsBigDecimal(c);
				BigDecimal result = MathUtil.divide(v1, v2);
				target.setAsBigDecimal(result, c);
			}
		}

	};

	public static DivideMatrix<DenseMatrix2D> DENSEMATRIX2D = new DivideMatrix<DenseMatrix2D>() {

		public void calc(final DenseMatrix2D source1, final DenseMatrix2D source2,
				final DenseMatrix2D target) {
			if (source1 instanceof DenseDoubleMatrix2D && source2 instanceof DenseDoubleMatrix2D
					&& target instanceof DenseDoubleMatrix2D) {
				DivideMatrix.DENSEDOUBLEMATRIX2D.calc((DenseDoubleMatrix2D) source1,
						(DenseDoubleMatrix2D) source2, (DenseDoubleMatrix2D) target);
			} else {
				for (int r = (int) source1.getRowCount(); --r != -1;) {
					for (int c = (int) source1.getColumnCount(); --c != -1;) {
						BigDecimal v1 = source1.getAsBigDecimal(r, c);
						BigDecimal v2 = source2.getAsBigDecimal(r, c);
						BigDecimal result = MathUtil.divide(v1, v2);
						target.setAsBigDecimal(result, r, c);
					}
				}
			}
		}
	};

	public static DivideMatrix<DenseDoubleMatrix2D> DENSEDOUBLEMATRIX2D = new DivideMatrix<DenseDoubleMatrix2D>() {

		public void calc(final DenseDoubleMatrix2D source1, final DenseDoubleMatrix2D source2,
				final DenseDoubleMatrix2D target) {
			if (source1 instanceof HasDoubleArray2D && source2 instanceof HasDoubleArray2D
					&& target instanceof HasDoubleArray2D) {
				calc(((HasDoubleArray2D) source1).getDoubleArray2D(), ((HasDoubleArray2D) source2)
						.getDoubleArray2D(), ((HasDoubleArray2D) target).getDoubleArray2D());
			} else if (source1 instanceof HasDoubleArray && source2 instanceof HasDoubleArray
					&& target instanceof HasDoubleArray) {
				calc(((HasDoubleArray) source1).getDoubleArray(), ((HasDoubleArray) source2)
						.getDoubleArray(), ((HasDoubleArray) target).getDoubleArray());
			} else {
				for (int r = (int) source1.getRowCount(); --r != -1;) {
					for (int c = (int) source1.getColumnCount(); --c != -1;) {
						target.setDouble(source1.getDouble(r, c) / source2.getDouble(r, c), r, c);
					}
				}
			}
		}

		private void calc(final double[][] source1, final double[][] source2,
				final double[][] target) {
			if (UJMPSettings.getNumberOfThreads() > 1 && source1.length >= 100
					&& source1[0].length >= 100) {
				new PForEquidistant(0, source1.length - 1) {
					public void step(int i) {
						double[] v1 = source1[i];
						double[] v2 = source2[i];
						double[] t = target[i];
						for (int c = source1[0].length; --c != -1;) {
							t[c] = v1[c] / v2[c];
						}
					}
				};
			} else {
				double[] v1 = null;
				double[] v2 = null;
				double[] t = null;
				for (int r = source1.length; --r != -1;) {
					v1 = source1[r];
					v2 = source2[r];
					t = target[r];
					for (int c = source1[0].length; --c != -1;) {
						t[c] = v1[c] / v2[c];
					}
				}
			}
		}

		private void calc(final double[] source1, final double[] source2, final double[] target) {
			final int length = source1.length;
			for (int i = 0; i < length; i++) {
				target[i] = source1[i] / source2[i];
			}
		}

	};

	public void calc(T source1, T source2, T target);

}
