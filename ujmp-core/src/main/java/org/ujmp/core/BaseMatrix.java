/*
 * Copyright (C) 2008-2016 by Holger Arndt
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

import org.ujmp.core.calculation.Calculation.Ret;
import org.ujmp.core.interfaces.BaseMatrixProperties;
import org.ujmp.core.matrix.factory.BaseMatrixFactory;

public interface BaseMatrix extends BaseMatrixProperties {

	Ret LINK = Ret.LINK;
	Ret ORIG = Ret.ORIG;
	Ret NEW = Ret.NEW;

	int Y = 0;
	int X = 1;
	int Z = 2;

	int ROW = 0;
	int COLUMN = 1;
	int ALL = 0x7fffffff;
	int NONE = -1;

	BaseMatrixFactory<? extends BaseMatrix> getFactory();

}