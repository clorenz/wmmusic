/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/statistics/StatisticsAction.java,v $
$Author: clorenz $
$Date: 2008-09-23 19:54:32 $
$Revision: 1.1 $

(C) 2006 Christoph Lorenz, <mail@christophlorenz.de>
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
package de.christophlorenz.wmmusic.statistics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.christophlorenz.wmmusic.CommonAction;
import de.christophlorenz.wmmusic.db.DAOException;
import de.christophlorenz.wmmusic.db.IWmmusicDatabase;
import de.christophlorenz.wmmusic.db.IWmmusicDatabaseFactory;
import de.christophlorenz.wmmusic.statistics.bean.StatisticsBean;

public class StatisticsAction extends CommonAction {
	
	static final Logger log;
	static {
		log = Logger.getLogger(StatisticsAction.class);
	}

	@Override
	public ActionForward commonExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String forward = "success";
		
		try {
			HttpSession session = request.getSession();
			IWmmusicDatabaseFactory f = (IWmmusicDatabaseFactory) session.getAttribute("wmmusicdbfactory");
			IWmmusicDatabase db = f.createWmmusicDatabase();
		
			StatisticsBean s = db.calculateStatistics();
		
			request.setAttribute("statistics", s);
			
			System.out.println(s);
		
		} catch ( DAOException e) {
			log.error(e,e);
			System.out.println(e);
		} catch ( Exception e) {
			log.error("Unknown exception: "+e,e);
			forward="error";
		}

	
		return mapping.findForward(forward);
	}

}
