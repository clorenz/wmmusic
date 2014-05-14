/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/song/EditSongAction.java,v $
$Author: clorenz $
$Date: 2008-09-24 19:55:39 $
$Revision: 1.2 $

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
package de.christophlorenz.wmmusic.song;

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
import de.christophlorenz.wmmusic.song.bean.EditSongBean;

public class EditSongAction extends CommonAction {
	
	static final Logger log;
	static {
		log = Logger.getLogger(EditSongAction.class);
	}

	public ActionForward commonExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String forward="success";
		String statusLine="";
		
		// Passing-through of parameters
		if ( request.getParameter("path") != null) {
			forward=request.getParameter("path");
			//request.setAttribute("path", request.getParameter("path"));
		}
		
		if ( request.getParameter("mediumId") != null) {
			request.setAttribute("mediumid", request.getParameter("mediumId"));
		}
		
		EditSongBean sf = (EditSongBean) form;
		
		HttpSession session = request.getSession();
		IWmmusicDatabaseFactory f = (IWmmusicDatabaseFactory) session.getAttribute("wmmusicdbfactory");
		IWmmusicDatabase db = f.createWmmusicDatabase();
		
		log.debug(sf);
		
		try {
			Song s = new Song(sf.getId());
			s.setArtist(sf.getArtist());
			s.setArtist_id(sf.getArtistId());
			s.setAuthors(sf.getAuthors());
			s.setDance(sf.getDance());
			s.setId3Genre(sf.getId3Genre());
			s.setRelease(sf.getRelease());
			s.setRemarks(sf.getRemarks());
			s.setTitle(sf.getTitle());
			s.setYear(sf.getYear());
			
			System.out.println("YEAR="+sf.getYear());
			System.out.println("RELEASE="+sf.getRelease());
			
			if (db.updateSong(s))			
				statusLine="Property updated song";
			else
				statusLine="Could not update song!";
			
		} catch ( DAOException e) {
			log.error(e,e);
			statusLine = "Error : "+e.getMessage();
			forward="error";
		} catch ( Exception e) {
			log.error(e,e);
			statusLine = "Error: "+e.getMessage();
			forward="error";
		}
		
		request.setAttribute("statusline", statusLine);
		
		log.debug("Forwarding to "+forward);
		return mapping.findForward(forward);
	}

}
