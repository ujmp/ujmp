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

package org.ujmp.core.export.destination;

import org.ujmp.core.export.format.MatrixDenseCSVStringExportFormat;
import org.ujmp.core.export.format.MatrixHtmlStringExportFormat;
import org.ujmp.core.export.format.MatrixJsonStringExportFormat;
import org.ujmp.core.export.format.MatrixLatexStringExportFormat;
import org.ujmp.core.export.format.MatrixMatlabScriptStringExportFormat;
import org.ujmp.core.export.format.MatrixRScriptStringExportFormat;
import org.ujmp.core.export.format.MatrixSQLStringExportFormat;

public interface MatrixStringExportDestination extends MatrixDenseCSVStringExportFormat, MatrixSQLStringExportFormat,
		MatrixMatlabScriptStringExportFormat, MatrixRScriptStringExportFormat, MatrixLatexStringExportFormat,
		MatrixHtmlStringExportFormat, MatrixJsonStringExportFormat, MatrixExportDestination {

}