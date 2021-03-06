/*******************************************************************************
 * This file is part of BOINC.
 * http://boinc.berkeley.edu
 * Copyright (C) 2012 University of California
 * 
 * BOINC is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * 
 * BOINC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with BOINC.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.berkeley.boinc.utils;

public class Logging {
	static public String TAG = "BOINC_GUI";
	
	/**
	 * Loglevel for debugging, set to 0 for release builds
	 */
	static public int LOGLEVEL = 4;
	static public Boolean ERROR = LOGLEVEL > 0;
	static public Boolean WARNING = LOGLEVEL > 1;
	static public Boolean INFO = LOGLEVEL > 2;
	static public Boolean DEBUG = LOGLEVEL > 3;
	static public Boolean VERBOSE = LOGLEVEL > 4;
	
	/**
	 * Set to false for release builds
	 */
	static public Boolean RPC_PERFORMANCE = false;
	
	/**
	 * Set to false for release builds
	 */
	static public Boolean RPC_DATA = false; 
	
	/**
	 * Sets log level
	 * @param logLevel
	 * 	<ul>
	 * 	<li>0 - No logging</li>
	 * 	<li>1 - Errors</li>
	 * 	<li>2 - Warnings</li>
	 * 	<li>3 - Info</li>
	 * 	<li>4 - Debug</li>
	 * 	<li>5 - Verbose</li>
	 * 	</ul>
	 */
	static public void setLogLevel(Integer logLevel) {
		LOGLEVEL = logLevel;
		ERROR = LOGLEVEL > 0;
		WARNING = LOGLEVEL > 1;
		INFO = LOGLEVEL > 2;
		DEBUG = LOGLEVEL > 3;
		VERBOSE = LOGLEVEL > 4;
	}
}