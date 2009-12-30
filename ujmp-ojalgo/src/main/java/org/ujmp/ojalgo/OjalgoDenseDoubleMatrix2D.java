/*
 * Copyright (C) 2008-2009 by Holger Arndt
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

package org.ujmp.ojalgo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Future;

import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.PrimitiveMatrix;
import org.ojalgo.matrix.decomposition.Cholesky;
import org.ojalgo.matrix.decomposition.CholeskyDecomposition;
import org.ojalgo.matrix.decomposition.DecomposeAndSolve;
import org.ojalgo.matrix.decomposition.Eigenvalue;
import org.ojalgo.matrix.decomposition.EigenvalueDecomposition;
import org.ojalgo.matrix.decomposition.LU;
import org.ojalgo.matrix.decomposition.LUDecomposition;
import org.ojalgo.matrix.decomposition.QR;
import org.ojalgo.matrix.decomposition.QRDecomposition;
import org.ojalgo.matrix.decomposition.SingularValue;
import org.ojalgo.matrix.decomposition.SingularValueDecomposition;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;
import org.ujmp.core.Matrix;
import org.ujmp.core.doublematrix.stub.AbstractDenseDoubleMatrix2D;
import org.ujmp.core.exceptions.MatrixException;
import org.ujmp.core.interfaces.Wrapper;

public class OjalgoDenseDoubleMatrix2D extends AbstractDenseDoubleMatrix2D
		implements Wrapper<MatrixStore<Double>> {

	private static final long serialVersionUID = 6628172130438716653L;

	private transient PrimitiveDenseStore matrix = null;

	public OjalgoDenseDoubleMatrix2D(final long... size) {
		matrix = (PrimitiveDenseStore) PrimitiveDenseStore.FACTORY.makeZero(
				(int) size[ROW], (int) size[COLUMN]);
	}

	public OjalgoDenseDoubleMatrix2D(final Matrix m) {
		this(m.getSize());
		for (final long[] c : m.allCoordinates()) {
			this.setAsDouble(m.getAsDouble(c), c);
		}
	}

	public OjalgoDenseDoubleMatrix2D(final MatrixStore<Double> m) {
		this.setWrappedObject(m);
	}

	@Override
	public Matrix chol() {
		final Cholesky<Double> chol = CholeskyDecomposition.makePrimitive();
		chol.compute(matrix);
		final Matrix r = new OjalgoDenseDoubleMatrix2D(chol.getR());
		return r;
	}

	@Override
	public Matrix[] eig() {
		final Eigenvalue<Double> evd = EigenvalueDecomposition.makeJama();
		evd.compute(matrix);
		final Matrix v = new OjalgoDenseDoubleMatrix2D(evd.getV());
		final Matrix d = new OjalgoDenseDoubleMatrix2D(evd.getD());
		return new Matrix[] { v, d };
	}

	public final BasicMatrix getBasicMatrix() {
		return new PrimitiveMatrix(matrix);
	}

	public double getDouble(final int row, final int column) {
		return matrix.doubleValue(row, column);
	}

	public double getDouble(final long row, final long column) {
		return matrix.doubleValue((int) row, (int) column);
	}

	public long[] getSize() {
		return new long[] { matrix.getRowDim(), matrix.getColDim() };
	}

	public PrimitiveDenseStore getWrappedObject() {
		return matrix;
	}

	@Override
	public Matrix inv() {
		return new OjalgoDenseDoubleMatrix2D(LUDecomposition.makePrimitive()
				.invert(matrix));
	}

	@Override
	public Matrix[] lu() {
		final LU<Double> lu = LUDecomposition.makePrimitive();
		lu.compute(matrix);
		final Matrix l = new OjalgoDenseDoubleMatrix2D(lu.getL());
		final Matrix u = new OjalgoDenseDoubleMatrix2D(lu.getRowEchelonForm());
		final int m = (int) this.getRowCount();
		final int[] piv = lu.getPivotOrder();
		final Matrix p = new OjalgoDenseDoubleMatrix2D(m, m);
		for (int i = 0; i < m; i++) {
			p.setAsDouble(1, i, piv[i]);
		}
		return new Matrix[] { l, u, p };
	}

	@Override
	public Matrix mtimes(final Matrix m) {
		if (m instanceof OjalgoDenseDoubleMatrix2D) {
			final PrimitiveDenseStore mo = ((OjalgoDenseDoubleMatrix2D) m)
					.getWrappedObject();
			final PrimitiveDenseStore result = (PrimitiveDenseStore) matrix
					.multiplyRight(mo);
			return new OjalgoDenseDoubleMatrix2D(result);
		} else {
			return super.mtimes(m);
		}
	}

	@Override
	public Matrix[] qr() {
		final QR<Double> qr = QRDecomposition.makePrimitive();
		qr.compute(matrix);
		final Matrix q = new OjalgoDenseDoubleMatrix2D(qr.getQ());
		final Matrix r = new OjalgoDenseDoubleMatrix2D(qr.getR());
		return new Matrix[] { q, r };
	}

	public void setDouble(final double value, final int row, final int column) {
		matrix.set(row, column, value);
	}

	public void setDouble(final double value, final long row, final long column) {
		matrix.set((int) row, (int) column, value);
	}

	public void setWrappedObject(final MatrixStore<Double> object) {
		if (object instanceof PrimitiveDenseStore) {
			matrix = (PrimitiveDenseStore) object;
		} else {
			matrix = (PrimitiveDenseStore) PrimitiveDenseStore.FACTORY
					.copyAnything2D(object);
		}
	}

	@Override
	public Matrix[] svd() {
		final SingularValue<Double> svd = SingularValueDecomposition
				.makePrimitive();
		svd.compute(matrix);
		final Matrix u = new OjalgoDenseDoubleMatrix2D(svd.getQ1());
		final Matrix s = new OjalgoDenseDoubleMatrix2D(svd.getD());
		final Matrix v = new OjalgoDenseDoubleMatrix2D(svd.getQ2());
		return new Matrix[] { u, s, v };
	}

	@Override
	public double[][] toDoubleArray() throws MatrixException {
		return matrix.toRawCopy();
	}

	@Override
	public Matrix transpose() {
		return new OjalgoDenseDoubleMatrix2D(matrix.transpose());
	}

	private void readObject(final ObjectInputStream s) throws IOException,
			ClassNotFoundException {
		s.defaultReadObject();
		final double[][] data = (double[][]) s.readObject();
		matrix = (PrimitiveDenseStore) PrimitiveDenseStore.FACTORY
				.copyRaw(data);
	}

	private void writeObject(final ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		s.writeObject(this.toDoubleArray());
	}

	public Matrix solve(Matrix b) {
		try {
			if (b instanceof OjalgoDenseDoubleMatrix2D) {
				OjalgoDenseDoubleMatrix2D b2 = (OjalgoDenseDoubleMatrix2D) b;
				if (isSquare()) {
					final LU<Double> lu = LUDecomposition.makePrimitive();
					final Future<DecomposeAndSolve<Double>> ret = lu.solve(
							matrix, b2.matrix);
					return new OjalgoDenseDoubleMatrix2D(ret.get()
							.getSolution());
				} else {
					final QR<Double> qr = QRDecomposition.makePrimitive();
					final Future<DecomposeAndSolve<Double>> ret = qr.solve(
							matrix, b2.matrix);
					return new OjalgoDenseDoubleMatrix2D(ret.get()
							.getSolution());
				}
			} else {
				return super.solve(b);
			}
		} catch (Exception e) {
			throw new MatrixException(e);
		}
	}

}