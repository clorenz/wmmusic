/* ------------------------------------------------------------------------- *
 $Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/CommonAction.java,v $
 $Author: clorenz $
 $Date: 2008-07-20 11:13:23 $
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
package de.christophlorenz.wmmusic;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.christophlorenz.wmmusic.db.DAOException;
import de.christophlorenz.wmmusic.db.IWmmusicDatabaseFactory;
import de.christophlorenz.wmmusic.db.MySQLWmmusicDatabaseFactory;
import de.christophlorenz.wmmusic.db.PostgreSQLWmmusicDatabaseFactory;
import de.christophlorenz.wmmusic.db.StrutsWmmusicDatabaseFactory;
import de.christophlorenz.wmmusic.db.TomcatWmmusicDatabaseFactory;

public abstract class CommonAction extends Action {
	
	// TODO: FIXME
	public String getUsername() {
		return "clorenz";
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws DAOException {

		HttpSession session = request.getSession();
		
		/*
		Enumeration e = getServlet().getServletContext().getAttributeNames();
		while (e.hasMoreElements()) {
			System.out.println(e.nextElement());
		}
		
		System.err.println("CONTEXT="+getServlet().getServletContext().getAttribute("wmmusic"));
		*/
		
		if (session.getAttribute("wmmusicdbfactory") == null) {
			// No factory factory created yet
			ServletConfig conf = servlet.getServletConfig();
			String managerName = conf.getInitParameter("dbfactory");

			IWmmusicDatabaseFactory factory = null;

			// Select the proper factory
			if (managerName.equals("Tomcat")) {
				factory = new TomcatWmmusicDatabaseFactory();
			} else if (managerName.equals("mysql")) {
				factory = new MySQLWmmusicDatabaseFactory();
			} else if (managerName.equals("postgresql")) {
				factory = new PostgreSQLWmmusicDatabaseFactory();
			} else if (managerName.equals("Struts")) {
				
				DataSource ds = (DataSource) getServlet().getServletContext().getAttribute("wmmusic");

				factory = new StrutsWmmusicDatabaseFactory(ds);
				System.out.println("Using factory "+factory.getClass().getName());
			} else 
				throw new DAOException("factory " + managerName
						+ " is not known");

			session.setAttribute("wmmusicdbfactory", factory);
		}
		
		session.setAttribute("wmmusicversion", Wmmusic.PROGRAM_VERSION);
		
		return commonExecute(mapping, form, request, response);
	}

	public abstract ActionForward commonExecute(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response);
}