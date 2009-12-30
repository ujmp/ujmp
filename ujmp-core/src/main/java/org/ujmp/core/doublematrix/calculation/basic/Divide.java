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

package org.ujmp.core.doublematrix.calculation.basic;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.coordinates.Coordinates;
import org.ujmp.core.doublematrix.calculation.AbstractDoubleCalculation;
import org.ujmp.core.exceptions.MatrixException;
import org.ujmp.core.util.MathUtil;

public class Divide extends AbstractDoubleCalculation {
	private static final long serialVersionUID = 7228531369984022350L;

	private boolean ignoreNaN = false;

	public Divide(Matrix m1, Matrix m2) {
		this(true, m1, m2);
	}

	public Divide(boolean ignoreNaN, Matrix m1, Matrix m2) {
		super(m1, m2);
		this.ignoreNaN = ignoreNaN;
		if (m2.isScalar() && !Coordinates.equals(m1.getSize(), m2.getSize())) {
			getSources()[1] = MatrixFactory.fill(m2.getAsDouble(0, 0), m1.getSize());
		} else if (m1.isScalar() && !Coordinates.equals(m1.getSize(), m2.getSize())) {
			getSources()[0] = MatrixFactory.fill(m1.getAsDouble(0, 0), m2.getSize());
		}
	}

	public Divide(Matrix m1, double v2) throws MatrixException {
		this(m1, MatrixFactory.fill(v2, m1.getSize()));
	}

	public Divide(boolean ignoreNaN, Matrix m1, double v2) throws MatrixException {
		this(ignoreNaN, m1, MatrixFactory.fill(v2, m1.getSize()));
	}

	public Divide() {
		super();
	}

	public double getDouble(long... coordinates) throws MatrixException {
		return ignoreNaN ? MathUtil.ignoreNaN(getSources()[0].getAsDouble(coordinates))
				/ MathUtil.ignoreNaN(getSources()[1].getAsDouble(coordinates)) : getSources()[0]
				.getAsDouble(coordinates)
				/ getSources()[1].getAsDouble(coordinates);
	}

	public static Matrix calc(Matrix m1, Matrix m2) throws MatrixException {
		return calc(false, m1, m2);
	}

	public static Matrix calc(Matrix m1, double v2) throws MatrixException {
		return calc(false, m1, v2);
	}

	public static Matrix calc(boolean ignoreNaN, Matrix m1, Matrix m2) throws MatrixException {
		if (m2.isScalar()) {
			return calc(ignoreNaN, m1, m2.getAsDouble(0, 0));
		}
		if (m1.isScalar()) {
			return calc(ignoreNaN, m1.getAsDouble(0, 0), m2);
		}
		Matrix ret = MatrixFactory.zeros(m1.getSize());
		if (m1.getAnnotation() != null) {
			ret.setAnnotation(m1.getAnnotation().clone());
		}
		if (ignoreNaN) {
			for (long[] c : m2.availableCoordinates()) {
				ret.setAsDouble(MathUtil.ignoreNaN(m1.getAsDouble(c))
						/ MathUtil.ignoreNaN(m2.getAsDouble(c)), c);
			}
		} else {
			for (long[] c : m2.availableCoordinates()) {
				ret.setAsDouble(m1.getAsDouble(c) / m2.getAsDouble(c), c);
			}
		}
		return ret;
	}

	public static Matrix calc(boolean ignoreNaN, Matrix m1, double v2) throws MatrixException {
		Matrix ret = MatrixFactory.zeros(m1.getSize());
		if (m1.getAnnotation() != null) {
			ret.setAnnotation(m1.getAnnotation().clone());
		}
		v2 = ignoreNaN ? MathUtil.ignoreNaN(v2) : v2;
		if (ignoreNaN) {
			for (long[] c : m1.allCoordinates()) {
				ret.setAsDouble(MathUtil.ignoreNaN(m1.getAsDouble(c)) / v2, c);
			}
		} else {
			for (long[] c : m1.allCoordinates()) {
				ret.setAsDouble(m1.getAsDouble(c) / v2, c);
			}
		}
		return ret;
	}

	public static Matrix calc(boolean ignoreNaN, double v1, Matrix m2) throws MatrixException {
		Matrix ret = MatrixFactory.zeros(m2.getSize());
		if (m2.getAnnotation() != null) {
			ret.setAnnotation(m2.getAnnotation().clone());
		}
		v1 = ignoreNaN ? MathUtil.ignoreNaN(v1) : v1;
		if (ignoreNaN) {
			for (long[] c : m2.allCoordinates()) {
				ret.setAsDouble(MathUtil.ignoreNaN(v1 / m2.getAsDouble(c)), c);
			}
		} else {
			for (long[] c : m2.allCoordinates()) {
				ret.setAsDouble(v1 / m2.getAsDouble(c), c);
			}
		}
		return ret;
	}

}