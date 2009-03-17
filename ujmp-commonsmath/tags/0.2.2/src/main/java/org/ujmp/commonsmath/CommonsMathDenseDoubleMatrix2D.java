/*
 * Copyright (C) 2008-2009 Holger Arndt, A. Naegele and M. Bundschus
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

package org.ujmp.commonsmath;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;
import org.ujmp.core.Matrix;
import org.ujmp.core.coordinates.Coordinates;
import org.ujmp.core.doublematrix.AbstractDenseDoubleMatrix2D;
import org.ujmp.core.exceptions.MatrixException;
import org.ujmp.core.interfaces.Wrapper;

public class CommonsMathDenseDoubleMatrix2D extends AbstractDenseDoubleMatrix2D
		implements Wrapper<RealMatrixImpl> {
	private static final long serialVersionUID = -1161807620507675926L;

	private RealMatrixImpl matrix = null;

	public CommonsMathDenseDoubleMatrix2D(long... size) {
		if (size[ROW] > 0 && size[COLUMN] > 0) {
			matrix = new RealMatrixImpl((int) size[ROW], (int) size[COLUMN]);
		}
	}

	public CommonsMathDenseDoubleMatrix2D(org.ujmp.core.Matrix source)
			throws MatrixException {
		this(source.getSize());
		for (long[] c : source.availableCoordinates()) {
			setAsDouble(source.getAsDouble(c), c);
		}
	}

	public CommonsMathDenseDoubleMatrix2D(RealMatrix matrix) {
		if (matrix instanceof RealMatrixImpl) {
			this.matrix = (RealMatrixImpl) matrix;
		} else {
			throw new MatrixException("Can oly use RealMatrixImpl");
		}
	}

	public RealMatrixImpl getWrappedObject() {
		return matrix;
	}

	public void setWrappedObject(RealMatrixImpl object) {
		this.matrix = object;
	}

	public double getDouble(long row, long column) throws MatrixException {
		return matrix.getEntry((int) row, (int) column);
	}

	public double getDouble(int row, int column) throws MatrixException {
		return matrix.getEntry(row, column);
	}

	public void setDouble(double value, long row, long column)
			throws MatrixException {
		matrix.getDataRef()[(int) row][(int) column] = value;
	}

	public void setDouble(double value, int row, int column)
			throws MatrixException {
		matrix.getDataRef()[row][column] = value;
	}

	public long[] getSize() {
		return matrix == null ? Coordinates.ZERO2D : new long[] {
				matrix.getRowDimension(), matrix.getColumnDimension() };
	}

	@Override
	public Matrix transpose() {
		return new CommonsMathDenseDoubleMatrix2D(matrix.transpose());
	}

	@Override
	public Matrix mtimes(Matrix m2) {
		if (m2 instanceof CommonsMathDenseDoubleMatrix2D) {
			return new CommonsMathDenseDoubleMatrix2D(matrix
					.multiply(((CommonsMathDenseDoubleMatrix2D) m2).matrix));
		} else {
			return super.mtimes(m2);
		}
	}
}
