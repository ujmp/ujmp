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

package org.ujmp.core.objectmatrix.calculation;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.annotation.DefaultAnnotation;
import org.ujmp.core.coordinates.Coordinates;
import org.ujmp.core.exceptions.MatrixException;

public class ExtractAnnotation extends AbstractObjectCalculation {
	private static final long serialVersionUID = 1461447576658284276L;

	private long[] size = null;

	public ExtractAnnotation(Matrix m, int dim) {
		super(dim, m);
		size = Coordinates.copyOf(m.getSize());
		setAnnotation(new DefaultAnnotation());
		getAnnotation().setMatrixAnnotation(m.getMatrixAnnotation());

		size[dim]--;

		if (dim == Matrix.ROW) {
			for (long c = m.getColumnCount() - 1; c != -1; c--) {
				getAnnotation().setAxisAnnotation(Matrix.COLUMN, c, m.getAsObject(0, c));
			}
			for (long r = m.getRowCount() - 2; r != -1; r--) {
				getAnnotation().setAxisAnnotation(Matrix.ROW, r,
						m.getAxisAnnotation(Matrix.ROW, r + 1));
			}
		} else if (dim == Matrix.COLUMN) {
			for (long r = m.getRowCount() - 1; r != -1; r--) {
				getAnnotation().setAxisAnnotation(Matrix.ROW, r, m.getAsObject(r, 0));
			}
			for (long c = m.getColumnCount() - 2; c != -1; c--) {
				getAnnotation().setAxisAnnotation(Matrix.COLUMN, c,
						m.getAxisAnnotation(Matrix.COLUMN, c + 1));
			}
		}

	}

	public Object getObject(long... coordinates) throws MatrixException {
		coordinates = Coordinates.copyOf(coordinates);
		coordinates[getDimension()]++;
		return getSource().getAsObject(coordinates);
	}

	public long[] getSize() {
		return size;
	}

	public static void main(String[] args) throws Exception {
		Matrix m = MatrixFactory.randn(5, 5);
		m.setLabel("test");
		m.setColumnLabel(3, "col3");
		m.setRowLabel(2, "row2");
		m.setAsDouble(Double.NaN, 2, 2);
		m.setAsDouble(Double.NEGATIVE_INFINITY, 3, 2);
		System.out.println(m);
		System.out.println(m.extractAnnotation(Ret.NEW, Matrix.COLUMN).times(1));
	}

}
