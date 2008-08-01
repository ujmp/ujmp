/*
 * Copyright (C) 2008 Holger Arndt, Andreas Naegele and Markus Bundschus
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

package org.ujmp.jmatrices;

import org.jmatrices.dbl.MatrixFactory;
import org.ujmp.core.exceptions.MatrixException;
import org.ujmp.core.interfaces.Wrapper;
import org.ujmp.core.matrices.stubs.AbstractDenseDoubleMatrix2D;

public class JMatricesDenseDoubleMatrix2D extends AbstractDenseDoubleMatrix2D implements Wrapper<org.jmatrices.dbl.Matrix> {
	private static final long serialVersionUID = 513251881654621L;

	private org.jmatrices.dbl.Matrix matrix = null;

	public JMatricesDenseDoubleMatrix2D(org.jmatrices.dbl.Matrix matrix) {
		this.matrix = matrix;
	}

	public JMatricesDenseDoubleMatrix2D(long... size) {
		this.matrix = MatrixFactory.getMatrix((int) size[ROW], (int) size[COLUMN], null);
	}

	public JMatricesDenseDoubleMatrix2D(org.ujmp.core.Matrix source) throws MatrixException {
		this(source.getSize());
		for (long[] c : source.availableCoordinates()) {
			setAsDouble(source.getAsDouble(c), c);
		}
	}

	public double getDouble(long row, long column) {
		return matrix.get((int) row + 1, (int) column + 1);
	}

	public long[] getSize() {
		return new long[] { matrix.rows(), matrix.cols() };
	}

	public void setDouble(double value, long row, long column) {
		matrix.set((int) row + 1, (int) column + 1, value);
	}

	public org.jmatrices.dbl.Matrix getWrappedObject() {
		return matrix;
	}

	public void setWrappedObject(org.jmatrices.dbl.Matrix object) {
		this.matrix = object;
	}

}