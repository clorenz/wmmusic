/* ------------------------------------------------------------------------- *
 $Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/Log4jInitServlet.java,v $
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

import javax.servlet.http.HttpServlet;

import org.apache.log4j.xml.DOMConfigurator;

public class Log4jInitServlet extends HttpServlet {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -8792941466896968440L;
	private static final String DEFAULT_LOG4J_FILENAME = "WEB-INF/log4j.xml";

	public void init() {
		try {
			String prefix = getServletContext().getRealPath("/");
			String file = getInitParameter("log4j-init-file");

			if (null == file || "".equals(file.trim())) {
				file = DEFAULT_LOG4J_FILENAME;
			}

			DOMConfigurator.configureAndWatch(prefix + file);
		
			System.out.println("Using log config from "+prefix + file);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}