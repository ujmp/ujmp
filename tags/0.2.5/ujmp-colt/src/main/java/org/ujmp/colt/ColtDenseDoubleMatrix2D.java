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

package org.ujmp.colt;

import org.ujmp.core.Matrix;
import org.ujmp.core.doublematrix.stub.AbstractDenseDoubleMatrix2D;
import org.ujmp.core.exceptions.MatrixException;
import org.ujmp.core.interfaces.Wrapper;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.colt.matrix.linalg.CholeskyDecomposition;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import cern.colt.matrix.linalg.LUDecomposition;
import cern.colt.matrix.linalg.QRDecomposition;
import cern.colt.matrix.linalg.SingularValueDecomposition;
import cern.colt.matrix.linalg.SmpBlas;
import cern.jet.math.Functions;

public class ColtDenseDoubleMatrix2D extends AbstractDenseDoubleMatrix2D
		implements Wrapper<DenseDoubleMatrix2D> {
	private static final long serialVersionUID = -3223474248020842822L;

	private static ColtDenseDoubleMatrix2DFactory factory = new ColtDenseDoubleMatrix2DFactory();

	private DenseDoubleMatrix2D matrix = null;

	public ColtDenseDoubleMatrix2D(long... size) {
		this.matrix = new DenseDoubleMatrix2D((int) size[ROW],
				(int) size[COLUMN]);
	}

	public ColtDenseDoubleMatrix2D(DoubleMatrix2D m) {
		if (m instanceof DenseDoubleMatrix2D) {
			this.matrix = (DenseDoubleMatrix2D) m;
		} else {
			this.matrix = new DenseDoubleMatrix2D(m.toArray());
		}
	}

	public ColtDenseDoubleMatrix2D(DenseDoubleMatrix2D m) {
		this.matrix = m;
	}

	public ColtDenseDoubleMatrix2D(Matrix source) throws MatrixException {
		this(source.getSize());
		for (long[] c : source.availableCoordinates()) {
			setDouble(source.getAsDouble(c), c);
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

	public DenseDoubleMatrix2D getWrappedObject() {
		return matrix;
	}

	public void setWrappedObject(DenseDoubleMatrix2D object) {
		this.matrix = object;
	}

	public Matrix transpose() {
		return new ColtDenseDoubleMatrix2D((DenseDoubleMatrix2D) matrix
				.viewDice().copy());
	}

	public Matrix inv() {
		return new ColtDenseDoubleMatrix2D(
				(DenseDoubleMatrix2D) Algebra.DEFAULT.inverse(matrix));
	}

	public Matrix solve(Matrix b) {
		if (b instanceof ColtDenseDoubleMatrix2D) {
			ColtDenseDoubleMatrix2D b2 = (ColtDenseDoubleMatrix2D) b;
			if (isSquare()) {
				DoubleMatrix2D ret = new LUDecomposition(matrix)
						.solve(b2.matrix);
				return new ColtDenseDoubleMatrix2D(ret);
			} else {
				DoubleMatrix2D ret = new QRDecomposition(matrix)
						.solve(b2.matrix);
				return new ColtDenseDoubleMatrix2D(ret);
			}
		} else {
			return super.solve(b);
		}
	}

	public Matrix solveSPD(Matrix b) {
		if (b instanceof ColtDenseDoubleMatrix2D) {
			ColtDenseDoubleMatrix2D b2 = (ColtDenseDoubleMatrix2D) b;
			DoubleMatrix2D ret = new CholeskyDecomposition(matrix)
					.solve(b2.matrix);
			return new ColtDenseDoubleMatrix2D(ret);
		} else {
			return super.solve(b);
		}
	}

	public Matrix invSPD() {
		DoubleMatrix2D ret = new CholeskyDecomposition(matrix)
				.solve(DoubleFactory2D.dense.identity(matrix.rows()));
		return new ColtDenseDoubleMatrix2D(ret);
	}

	public Matrix plus(double value) {
		return new ColtDenseDoubleMatrix2D((DenseDoubleMatrix2D) matrix.copy()
				.assign(Functions.plus(value)));
	}

	public Matrix minus(double value) {
		return new ColtDenseDoubleMatrix2D((DenseDoubleMatrix2D) matrix.copy()
				.assign(Functions.minus(value)));
	}

	public Matrix times(double value) {
		return new ColtDenseDoubleMatrix2D((DenseDoubleMatrix2D) matrix.copy()
				.assign(Functions.mult(value)));
	}

	public Matrix divide(double value) {
		return new ColtDenseDoubleMatrix2D((DenseDoubleMatrix2D) matrix.copy()
				.assign(Functions.div(value)));
	}

	public Matrix mtimes(Matrix m) {
		if (m instanceof ColtDenseDoubleMatrix2D) {
			DenseDoubleMatrix2D ret = new DenseDoubleMatrix2D(
					(int) getRowCount(), (int) m.getColumnCount());
			matrix.zMult(((ColtDenseDoubleMatrix2D) m).matrix, ret);
			return new ColtDenseDoubleMatrix2D(ret);
		} else {
			return super.mtimes(m);
		}
	}

	public Matrix plus(Matrix m) {
		if (m instanceof ColtDenseDoubleMatrix2D) {
			DenseDoubleMatrix2D ret = new DenseDoubleMatrix2D(
					(int) getRowCount(), (int) m.getColumnCount());
			ret.assign(matrix);
			SmpBlas.smpBlas.daxpy(1, ((ColtDenseDoubleMatrix2D) m)
					.getWrappedObject(), ret);
			return new ColtDenseDoubleMatrix2D(ret);
		} else {
			return super.plus(m);
		}
	}

	public Matrix minus(Matrix m) {
		if (m instanceof ColtDenseDoubleMatrix2D) {
			DenseDoubleMatrix2D ret = new DenseDoubleMatrix2D(
					(int) getRowCount(), (int) m.getColumnCount());
			ret.assign(matrix);
			SmpBlas.smpBlas.daxpy(-1, ((ColtDenseDoubleMatrix2D) m)
					.getWrappedObject(), ret);
			return new ColtDenseDoubleMatrix2D(ret);
		} else {
			return super.minus(m);
		}
	}

	public Matrix[] svd() {
		if (getColumnCount() > getRowCount()) {
			SingularValueDecomposition svd = new SingularValueDecomposition(
					matrix.viewDice());
			Matrix u = new ColtDenseDoubleMatrix2D(svd.getV());
			Matrix s = new ColtDenseDoubleMatrix2D(svd.getS());
			Matrix v = new ColtDenseDoubleMatrix2D(svd.getU());
			return new Matrix[] { u, s, v };
		} else {
			SingularValueDecomposition svd = new SingularValueDecomposition(
					matrix);
			Matrix u = new ColtDenseDoubleMatrix2D(svd.getU());
			Matrix s = new ColtDenseDoubleMatrix2D(svd.getS());
			Matrix v = new ColtDenseDoubleMatrix2D(svd.getV());
			return new Matrix[] { u, s, v };
		}
	}

	public Matrix[] qr() {
		if (getColumnCount() > getRowCount()) {
			throw new MatrixException("matrix size must be m>=n");
		}
		QRDecomposition qr = new QRDecomposition(matrix);
		Matrix q = new ColtDenseDoubleMatrix2D(qr.getQ());
		Matrix r = new ColtDenseDoubleMatrix2D(qr.getR());
		return new Matrix[] { q, r };
	}

	public Matrix[] eig() {
		EigenvalueDecomposition eig = new EigenvalueDecomposition(matrix);
		Matrix v = new ColtDenseDoubleMatrix2D(eig.getV());
		Matrix d = new ColtDenseDoubleMatrix2D(eig.getD());
		return new Matrix[] { v, d };
	}

	public Matrix chol() {
		CholeskyDecomposition chol = new CholeskyDecomposition(matrix);
		Matrix r = new ColtDenseDoubleMatrix2D(chol.getL());
		return r;
	}

	public Matrix[] lu() {
		if (getColumnCount() > getRowCount()) {
			throw new MatrixException("only supported for m>=n");
		}
		LUDecomposition lu = new LUDecomposition(matrix);
		Matrix l = new ColtDenseDoubleMatrix2D(lu.getL());
		Matrix u = new ColtDenseDoubleMatrix2D(lu.getU().viewPart(0, 0,
				(int) getColumnCount(), (int) getColumnCount()));
		int[] piv = lu.getPivot();
		int m = (int) getRowCount();
		Matrix p = new ColtDenseDoubleMatrix2D(m, m);
		for (int i = 0; i < m; i++) {
			p.setAsDouble(1, i, piv[i]);
		}
		return new Matrix[] { l, u, p };
	}

	public Matrix copy() {
		Matrix m = new ColtDenseDoubleMatrix2D((DenseDoubleMatrix2D) matrix
				.copy());
		if (getAnnotation() != null) {
			m.setAnnotation(getAnnotation().clone());
		}
		return m;
	}

	public ColtDenseDoubleMatrix2DFactory getFactory() {
		return factory;
	}

}