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

package org.ujmp.parallelcolt;

import org.ujmp.core.Matrix;
import org.ujmp.core.doublematrix.DenseDoubleMatrix2D;
import org.ujmp.core.doublematrix.stub.AbstractDenseDoubleMatrix2D;
import org.ujmp.core.exceptions.MatrixException;
import org.ujmp.core.interfaces.HasColumnMajorDoubleArray1D;
import org.ujmp.core.interfaces.HasRowMajorDoubleArray2D;
import org.ujmp.core.interfaces.Wrapper;

import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra;
import cern.colt.matrix.tdouble.algo.decomposition.DenseDoubleCholeskyDecomposition;
import cern.colt.matrix.tdouble.algo.decomposition.DenseDoubleEigenvalueDecomposition;
import cern.colt.matrix.tdouble.algo.decomposition.DenseDoubleLUDecomposition;
import cern.colt.matrix.tdouble.algo.decomposition.DenseDoubleQRDecomposition;
import cern.colt.matrix.tdouble.algo.decomposition.DenseDoubleSingularValueDecomposition;
import cern.jet.math.tdouble.DoubleFunctions;

public class ParallelColtDenseDoubleMatrix2D extends
		AbstractDenseDoubleMatrix2D implements
		Wrapper<cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D> {
	private static final long serialVersionUID = -1941030601886654699L;

	private static final DenseDoubleAlgebra ALG = new DenseDoubleAlgebra();

	private cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D matrix;

	public ParallelColtDenseDoubleMatrix2D(long... size) {
		this.matrix = new cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D(
				(int) size[ROW], (int) size[COLUMN]);
	}

	public ParallelColtDenseDoubleMatrix2D(DoubleMatrix2D m) {
		if (m instanceof DenseDoubleMatrix2D) {
			this.matrix = (cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D) m;
		} else {
			this.matrix = new cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D(
					m.toArray());
		}
	}

	public ParallelColtDenseDoubleMatrix2D(
			cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D m) {
		this.matrix = m;
	}

	public ParallelColtDenseDoubleMatrix2D(Matrix source)
			throws MatrixException {
		if (source instanceof HasColumnMajorDoubleArray1D) {
			final double[] data = ((HasColumnMajorDoubleArray1D) source)
					.getColumnMajorDoubleArray1D();
			this.matrix = new cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D(
					(int) source.getRowCount(), (int) source.getColumnCount(),
					data, 0, 0, 1, (int) source.getRowCount(), false);
		} else if (source instanceof HasRowMajorDoubleArray2D) {
			final double[][] data = ((HasRowMajorDoubleArray2D) source)
					.getRowMajorDoubleArray2D();
			this.matrix = new cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D(
					data);
		} else if (source instanceof DenseDoubleMatrix2D) {
			this.matrix = new cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D(
					(int) source.getRowCount(), (int) source.getColumnCount());
			final DenseDoubleMatrix2D m2 = (DenseDoubleMatrix2D) source;
			for (int r = (int) source.getRowCount(); --r >= 0;) {
				for (int c = (int) source.getColumnCount(); --c >= 0;) {
					matrix.setQuick(r, c, m2.getDouble(r, c));
				}
			}
		} else {
			this.matrix = new cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D(
					(int) source.getRowCount(), (int) source.getColumnCount());
			for (long[] c : source.availableCoordinates()) {
				setDouble(source.getAsDouble(c), c);
			}
		}
	}

	public double getDouble(long row, long column) {
		return matrix.getQuick((int) row, (int) column);
	}

	public double getDouble(int row, int column) {
		return matrix.getQuick(row, column);
	}

	public long[] getSize() {
		return new long[] { matrix.rows(), matrix.columns() };
	}

	public void setDouble(double value, long row, long column) {
		matrix.setQuick((int) row, (int) column, value);
	}

	public void setDouble(double value, int row, int column) {
		matrix.setQuick(row, column, value);
	}

	public cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D getWrappedObject() {
		return matrix;
	}

	public void setWrappedObject(
			cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D object) {
		this.matrix = object;
	}

	public Matrix plus(double value) {
		return new ParallelColtDenseDoubleMatrix2D(
				(cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D) matrix
						.copy().assign(DoubleFunctions.plus(value)));
	}

	public Matrix inv() {
		return new ParallelColtDenseDoubleMatrix2D(
				(cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D) ALG
						.inverse(matrix));
	}

	public Matrix times(double value) {
		return new ParallelColtDenseDoubleMatrix2D(
				(cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D) matrix
						.copy().assign(DoubleFunctions.mult(value)));
	}

	public Matrix transpose() {
		return new ParallelColtDenseDoubleMatrix2D(
				(cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D) matrix
						.viewDice().copy());
	}

	public Matrix plus(Matrix m) {
		if (m instanceof ParallelColtDenseDoubleMatrix2D) {
			DoubleMatrix2D result = matrix.copy();
			result.assign(((ParallelColtDenseDoubleMatrix2D) m)
					.getWrappedObject(), DoubleFunctions.plus);
			return new ParallelColtDenseDoubleMatrix2D(result);
		} else {
			return super.plus(m);
		}
	}

	public Matrix minus(Matrix m) {
		if (m instanceof ParallelColtDenseDoubleMatrix2D) {
			DoubleMatrix2D result = matrix.copy();
			result.assign(((ParallelColtDenseDoubleMatrix2D) m)
					.getWrappedObject(), DoubleFunctions.minus);
			return new ParallelColtDenseDoubleMatrix2D(result);
		} else {
			return super.minus(m);
		}
	}

	public Matrix mtimes(Matrix m) throws MatrixException {
		if (m instanceof ParallelColtDenseDoubleMatrix2D) {
			cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D ret = new cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D(
					(int) getRowCount(), (int) m.getColumnCount());
			matrix.zMult(((ParallelColtDenseDoubleMatrix2D) m).matrix, ret);
			return new ParallelColtDenseDoubleMatrix2D(ret);
		} else {
			return super.mtimes(m);
		}
	}

	public Matrix[] svd() {
		DenseDoubleSingularValueDecomposition svd = new DenseDoubleSingularValueDecomposition(
				matrix, true, false);
		Matrix u = new ParallelColtDenseDoubleMatrix2D(svd.getU());
		Matrix s = new ParallelColtDenseDoubleMatrix2D(svd.getS());
		Matrix v = new ParallelColtDenseDoubleMatrix2D(svd.getV());
		return new Matrix[] { u, s, v };
	}

	public Matrix[] eig() {
		DenseDoubleEigenvalueDecomposition eig = new DenseDoubleEigenvalueDecomposition(
				matrix);
		Matrix v = new ParallelColtDenseDoubleMatrix2D(eig.getV());
		Matrix d = new ParallelColtDenseDoubleMatrix2D(eig.getD());
		return new Matrix[] { v, d };
	}

	public Matrix[] qr() {
		DenseDoubleQRDecomposition qr = new DenseDoubleQRDecomposition(matrix);
		Matrix q = new ParallelColtDenseDoubleMatrix2D(qr.getQ(false));
		Matrix r = new ParallelColtDenseDoubleMatrix2D(qr.getR(false));
		return new Matrix[] { q, r };
	}

	public Matrix[] lu() {
		if (getRowCount() >= getColumnCount()) {
			DenseDoubleLUDecomposition lu = new DenseDoubleLUDecomposition(
					matrix);
			Matrix l = new ParallelColtDenseDoubleMatrix2D(lu.getL());
			Matrix u = new ParallelColtDenseDoubleMatrix2D(lu.getU().viewPart(
					0, 0, (int) getColumnCount(), (int) getColumnCount()));
			int m = (int) getRowCount();
			int[] piv = lu.getPivot();
			Matrix p = new ParallelColtDenseDoubleMatrix2D(m, m);
			for (int i = 0; i < m; i++) {
				p.setAsDouble(1, i, piv[i]);
			}
			return new Matrix[] { l, u, p };
		} else {
			throw new MatrixException("only supported for matrices m>=n");
		}
	}

	public Matrix chol() {
		DenseDoubleCholeskyDecomposition chol = new DenseDoubleCholeskyDecomposition(
				matrix);
		Matrix r = new ParallelColtDenseDoubleMatrix2D(chol.getL());
		return r;
	}

	public Matrix copy() {
		Matrix m = new ParallelColtDenseDoubleMatrix2D(
				(cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D) matrix
						.copy());
		if (getAnnotation() != null) {
			m.setAnnotation(getAnnotation().clone());
		}
		return m;
	}

	public Matrix solve(Matrix b) {
		if (b instanceof ParallelColtDenseDoubleMatrix2D) {
			DoubleMatrix2D b2 = ((ParallelColtDenseDoubleMatrix2D) b).matrix;
			DoubleMatrix2D result = ALG.solve(matrix, b2);
			return new ParallelColtDenseDoubleMatrix2D(result);
		} else {
			return super.solve(b);
		}
	}

	public Matrix solveSPD(Matrix b) {
		if (b instanceof ParallelColtDenseDoubleMatrix2D) {
			ParallelColtDenseDoubleMatrix2D b2 = new ParallelColtDenseDoubleMatrix2D(
					b);
			DenseDoubleCholeskyDecomposition chol = new DenseDoubleCholeskyDecomposition(
					matrix);
			chol.solve(b2.matrix);
			return b2;
		} else {
			return super.solve(b);
		}
	}

	public Matrix invSPD() {
		DenseDoubleCholeskyDecomposition chol = new DenseDoubleCholeskyDecomposition(
				matrix);
		DoubleMatrix2D ret = DoubleFactory2D.dense.identity(matrix.rows());
		chol.solve(ret);
		return new ParallelColtDenseDoubleMatrix2D(ret);
	}
}