/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/recording/SelectRecordingAction.java,v $
$Author: clorenz $
$Date: 2008-07-20 11:13:22 $
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
package de.christophlorenz.wmmusic.recording;

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
import de.christophlorenz.wmmusic.recording.bean.EditRecordingBean;
import de.christophlorenz.wmmusic.song.Song;
import de.christophlorenz.wmmusic.song.bean.EditSongBean;

public class SelectRecordingAction extends CommonAction {
	
	static final Logger log;
	static {
		log = Logger.getLogger(SelectRecordingAction.class);
	}

	public ActionForward commonExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String forward="success";
		String statusLine="";
		
		if ( request.getParameter("path") != null) {
			forward=request.getParameter("path");
			request.setAttribute("path", request.getParameter("path"));
		}
		
		HttpSession session = request.getSession();
		IWmmusicDatabaseFactory f = (IWmmusicDatabaseFactory) session.getAttribute("wmmusicdbfactory");
		IWmmusicDatabase db = f.createWmmusicDatabase();
		
		try {
			long recordingId = Integer.parseInt(request.getParameter("id"));
			
			Recording r = db.getRecording(recordingId);
			if ( r != null) {
				Song s = db.getSong(r.getSongId());
			
				// 2 Beans füllen: Song und Recording.
				EditRecordingBean bean = new EditRecordingBean();
				bean.setArtist(s.getArtist());
				bean.setArtistId(s.getArtist_id());
				bean.setCounter(r.getCounter());
				bean.setDigital(r.getDigital());
				bean.setId(r.getId()+"");
				bean.setLongplay(r.getLongplay());
				bean.setMediumCode(r.getMediumCode());
				bean.setMediumId(r.getMediumId());
				bean.setMediumType(r.getMediumType());
				bean.setQuality(r.getQuality());
				bean.setRemarks(r.getRemarks());
				bean.setSide(r.getSide());
				bean.setSongId(s.getId());
				bean.setSpecial(r.getSpecial());
				bean.setTime(r.getTime());
				bean.setTitle(s.getTitle());
				bean.setTrack(r.getTrack());
				bean.setYear(r.getYear());
				
				EditSongBean songBean = new EditSongBean();
				songBean.setArtist(s.getArtist());
				songBean.setArtistId(s.getArtist_id());
				songBean.setAuthors(s.getAuthors());
				songBean.setDance(s.getDance());
				songBean.setId(s.getId());
				songBean.setId3Genre(s.getId3Genre());
				songBean.setRelease(s.getRelease());
				songBean.setRemarks(s.getRemarks());
				songBean.setTitle(s.getTitle());
				songBean.setYear(s.getYear()+"");
				
				request.setAttribute("editRecordingForm", bean);
				request.setAttribute("songBean", songBean);
			} else {
				return mapping.findForward("error");
			}
		} catch ( DAOException e) {
			log.error(e,e);
		} catch ( Exception e) {
			log.error(e,e);
		}
		
		request.setAttribute("statusline", statusLine);
		
		log.debug("SelectRecordingAction: Forwarding to "+forward);
		return mapping.findForward(forward);
	}

}
