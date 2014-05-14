/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/song/EditSongAndRecordingAction.java,v $
$Author: clorenz $
$Date: 2008-07-20 11:13:23 $
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
import de.christophlorenz.wmmusic.recording.Recording;
import de.christophlorenz.wmmusic.song.bean.EditSongAndRecordingBean;

public class EditSongAndRecordingAction extends CommonAction {
	
	static final Logger log;
	static {
		log = Logger.getLogger(EditSongAndRecordingAction.class);
	}

	public ActionForward commonExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String forward="success";
		String statusLine="";
		
		if ( request.getParameter("path") != null) {
			forward=request.getParameter("path");
			request.setAttribute("path", request.getParameter("path"));
		}
		
		EditSongAndRecordingBean sr = (EditSongAndRecordingBean) form;
		
		HttpSession session = request.getSession();
		IWmmusicDatabaseFactory f = (IWmmusicDatabaseFactory) session.getAttribute("wmmusicdbfactory");
		IWmmusicDatabase db = f.createWmmusicDatabase();
		
		log.debug(sr);
		
		try {
			Song s = new Song();
			s.setArtist(sr.getArtist());
			s.setArtist_id(sr.getArtistId());
			s.setAuthors(sr.getAuthors());
			s.setDance(sr.getDance());
			s.setId3Genre(sr.getId3Genre());
			s.setRelease(sr.getRelease());
			s.setRemarks(sr.getRemarks());
			s.setTitle(sr.getTitle());
			s.setYear(sr.getYear());
			
			Recording r = new Recording();
			r.setMediumId(sr.getMediumId());
			r.setSide(sr.getSide());
			r.setTrack(sr.getTrack());
			r.setCounter(sr.getCounter());
			r.setTime(sr.getTime());
			r.setYear(sr.getRecordingYear());
			r.setLongplay(sr.getLongplay());
			r.setQuality(sr.getQuality());
			r.setRemarks(sr.getRecordingRemarks());
			r.setSpecial(sr.getSpecial());
			r.setDigital(sr.getDigital());
			
			db.insertSongAndRecording(s, r);
			statusLine="Property inserted song and recording";
			
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
		request.setAttribute("mediumid", sr.getMediumId()+"");
		return mapping.findForward(forward);
	}

}
