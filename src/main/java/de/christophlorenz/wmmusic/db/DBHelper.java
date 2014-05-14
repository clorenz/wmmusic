/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/db/DBHelper.java,v $
$Author: clorenz $
$Date: 2008-07-20 11:13:21 $
$Revision: 1.1 $

(C) 2005 Christoph Lorenz, <mail@christoph.lorenz.de>
All rights reserved.

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

** ------------------------------------------------------------------------- */
package de.christophlorenz.wmmusic.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

public class DBHelper {
	
	static final Logger log;
	static {
		log = Logger.getLogger(DBHelper.class);
	}

	static void close(Connection con, PreparedStatement stmt, ResultSet rs) {
		try {
			if ( con != null)
				con.close();
			if ( stmt != null)
				stmt.close();
			if ( rs != null)
				rs.close();
		} catch ( SQLException e) {
			log.error(e,e);
		}
		
	}
	
	
	static void stmtNullOrInt(PreparedStatement stmt, int position, int value) throws SQLException {
		if ( value>0)
			stmt.setInt(position, value);
		else
			stmt.setNull(position, Types.INTEGER);
	}
	
	
	static void stmtNullOrBoolean(PreparedStatement stmt, int position, int value) throws SQLException {
		if ( value>0)
			stmt.setBoolean(position, value>0?true:false);
		else
			stmt.setNull(position, Types.BOOLEAN);
	}
	
	
	static void stmtNullOrLong(PreparedStatement stmt, int position, long value) throws SQLException {
		if ( value>0)
			stmt.setLong(position, value);
		else
			stmt.setNull(position, Types.INTEGER);
	}
	
	
	static void stmtNullOrDouble(PreparedStatement stmt, int position, double value) throws SQLException {
		if ( value>0)
			stmt.setDouble(position, value);
		else
			stmt.setNull(position, Types.DOUBLE);
	}


	public static void stmtNullOrDate(PreparedStatement stmt, int position, String date) throws SQLException {
		if (( date!=null) && (date.length()>0))
			try {
				stmt.setDate(position, Date.valueOf(date));
			} catch ( Exception e) {
				log.error(date + " is not a valid sql date string in format yyyy-mm-dd");
				stmt.setNull(position, Types.DATE);
			}
		else
			stmt.setNull(position, Types.DATE);		
	}
	
}
