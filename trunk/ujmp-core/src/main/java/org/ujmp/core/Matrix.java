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

package org.ujmp.core;

import org.ujmp.core.annotation.HasAnnotation;
import org.ujmp.core.calculation.CanPerformCalculations;
import org.ujmp.core.calculation.DivideMatrix;
import org.ujmp.core.calculation.DivideScalar;
import org.ujmp.core.calculation.MinusMatrix;
import org.ujmp.core.calculation.MinusScalar;
import org.ujmp.core.calculation.Mtimes;
import org.ujmp.core.calculation.PlusMatrix;
import org.ujmp.core.calculation.PlusScalar;
import org.ujmp.core.calculation.TimesMatrix;
import org.ujmp.core.calculation.TimesScalar;
import org.ujmp.core.calculation.Transpose;
import org.ujmp.core.calculation.Calculation.Ret;
import org.ujmp.core.doublematrix.calculation.general.decomposition.Chol;
import org.ujmp.core.doublematrix.calculation.general.decomposition.Eig;
import org.ujmp.core.doublematrix.calculation.general.decomposition.Inv;
import org.ujmp.core.doublematrix.calculation.general.decomposition.LU;
import org.ujmp.core.doublematrix.calculation.general.decomposition.QR;
import org.ujmp.core.doublematrix.calculation.general.decomposition.SVD;
import org.ujmp.core.doublematrix.calculation.general.decomposition.Solve;
import org.ujmp.core.interfaces.BasicMatrixProperties;
import org.ujmp.core.interfaces.Conversions;
import org.ujmp.core.interfaces.CoordinateFunctions;
import org.ujmp.core.interfaces.CoreObject;
import org.ujmp.core.interfaces.DistanceMeasures;
import org.ujmp.core.interfaces.GettersAndSetters;
import org.ujmp.core.io.ExportMatrixInterface;
import org.ujmp.core.matrix.MatrixFactoryRoot;

/**
 * <code>Matrix</code> is the main class for storing any type of data. You have
 * to choose the suitable implementation for your needs, e.g.
 * <code>DefaultDenseDoubleMatrix2D</code> to store double values or
 * DefaultGenericMatrix if you want to specify the object type.
 * 
 * 
 * @author Holger Arndt
 * @version $Revision$
 * @date $Date$
 * 
 * @log $Log$
 * 
 */
public interface Matrix extends CoreObject, ExportMatrixInterface, CoordinateFunctions,
		GettersAndSetters, BasicMatrixProperties, CanPerformCalculations, DistanceMeasures,
		Comparable<Matrix>, HasAnnotation, Conversions {

	public enum StorageType {
		DENSE, SPARSE, LIST, SET, MAP, TREE, GRAPH
	};

	/**
	 * A factory for creating matrices.
	 */
	public static final MatrixFactoryRoot factory = new MatrixFactoryTemp();

	public static final Ret LINK = Ret.LINK;

	public static final Ret ORIG = Ret.ORIG;

	public static final Ret NEW = Ret.NEW;

	public static final int Y = 0;

	public static final int X = 1;

	public static final int Z = 2;

	public static final int ROW = 0;

	public static final int COLUMN = 1;

	public static final int ALL = 0x7fffffff;

	public static final int NONE = -1;

	public static Transpose<Matrix> transpose = Transpose.INSTANCE;

	public static PlusMatrix<Matrix> plusMatrix = PlusMatrix.INSTANCE;

	public static MinusMatrix<Matrix> minusMatrix = MinusMatrix.INSTANCE;

	public static TimesMatrix<Matrix> timesMatrix = TimesMatrix.INSTANCE;

	public static DivideMatrix<Matrix> divideMatrix = DivideMatrix.INSTANCE;

	public static PlusScalar<Matrix> plusScalar = PlusScalar.INSTANCE;

	public static MinusScalar<Matrix> minusScalar = MinusScalar.INSTANCE;

	public static TimesScalar<Matrix> timesScalar = TimesScalar.INSTANCE;

	public static DivideScalar<Matrix> divideScalar = DivideScalar.INSTANCE;

	public static Mtimes<Matrix, Matrix, Matrix> mtimes = Mtimes.INSTANCE;

	public static SVD<Matrix> svd = org.ujmp.core.doublematrix.calculation.general.decomposition.SVD.INSTANCE;

	public static LU<Matrix> lu = org.ujmp.core.doublematrix.calculation.general.decomposition.LU.INSTANCE;

	public static QR<Matrix> qr = org.ujmp.core.doublematrix.calculation.general.decomposition.QR.INSTANCE;

	public static Inv<Matrix> inv = Inv.INSTANCE;

	public static Solve<Matrix> solve = Solve.INSTANCE;

	public static Chol<Matrix> chol = Chol.INSTANCE;

	public static Eig<Matrix> eig = Eig.INSTANCE;

}