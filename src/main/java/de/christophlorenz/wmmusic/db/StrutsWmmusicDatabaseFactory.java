/* ------------------------------------------------------------------------- *
 $Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/db/StrutsWmmusicDatabaseFactory.java,v $
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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class StrutsWmmusicDatabaseFactory implements IWmmusicDatabaseFactory {

	private DataSource dataSource;

	public StrutsWmmusicDatabaseFactory(DataSource ds) throws DAOException {
		
		System.out.println("Starting StrutsFactory with datasource "+ds);
		this.dataSource = ds;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public IWmmusicDatabase createWmmusicDatabase() {
		WmmusicDatabase db = new WmmusicDatabase();
		db.setDataSource(dataSource);
		return db;
	}

	
}
