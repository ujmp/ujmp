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

package org.ujmp.core.datematrix;

import java.util.Date;

import org.ujmp.core.exceptions.MatrixException;
import org.ujmp.core.genericmatrix.GenericMatrix;

public interface DateMatrix extends GenericMatrix<Date> {

	/**
	 * Returns a Date representation of an entry in the matrix. The stored value
	 * will be converted to a Date object as good as possible.
	 * 
	 * @param coordinates
	 *            location of the entry
	 * @return a String representation of the entry
	 * @throws MatrixException
	 */
	public Date getDate(long... coordinates) throws MatrixException;

	/**
	 * Sets an entry in the matrix to a Date value. If the matrix cannot store
	 * Date objects, the value will be represented as good as possible.
	 * 
	 * @param value
	 *            Date object to store
	 * @param coordinates
	 *            location of the entry
	 * @throws MatrixException
	 */
	public void setDate(Date date, long... coordinates) throws MatrixException;

}