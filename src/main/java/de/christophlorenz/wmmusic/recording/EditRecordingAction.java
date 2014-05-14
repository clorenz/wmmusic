/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/recording/EditRecordingAction.java,v $
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

public class EditRecordingAction extends CommonAction {
	
	static final Logger log;
	static {
		log = Logger.getLogger(EditRecordingAction.class);
	}

	public ActionForward commonExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String forward="success";
		String statusLine="";
		
		if ( request.getParameter("path") != null) {
			forward=request.getParameter("path");
			request.setAttribute("path", request.getParameter("path"));
		} else {
			request.setAttribute("path", "success");
		}
		
		EditRecordingBean recordingInput = (EditRecordingBean) form;
		
		HttpSession session = request.getSession();
		IWmmusicDatabaseFactory f = (IWmmusicDatabaseFactory) session.getAttribute("wmmusicdbfactory");
		IWmmusicDatabase db = f.createWmmusicDatabase();
		
		log.debug(recordingInput);
		
		try {
			long id = Long.parseLong(recordingInput.getId()!=null?recordingInput.getId():"-1");
			if ( (id >=0) && (db.recordingExistsById(id))) {
				Recording rec = new Recording(id);
				rec.setArtist(recordingInput.getArtist());
				rec.setArtistId(recordingInput.getArtistId());
				rec.setCounter(recordingInput.getCounter());
				rec.setDigital(recordingInput.getDigital());
				rec.setLongplay(recordingInput.getLongplay());
				rec.setMediumCode(recordingInput.getMediumCode());
				rec.setMediumId(recordingInput.getMediumId());
				rec.setMediumType(recordingInput.getMediumType());
				rec.setQuality(recordingInput.getQuality());
				rec.setRemarks(recordingInput.getRemarks());
				rec.setSide(recordingInput.getSide());
				rec.setSongId(recordingInput.getSongId());
				rec.setSpecial(recordingInput.getSpecial());
				rec.setTime(recordingInput.getTime());
				rec.setTitle(recordingInput.getTitle());
				rec.setTrack(recordingInput.getTrack());
				rec.setYear(recordingInput.getYear());
				
				request.setAttribute("mediumid", rec.getMediumId()+"");
				
				if (db.updateRecording(rec))
					statusLine="Updated recording on "+recordingInput.getMediumTypeAbbrev()+" "+
						recordingInput.getMediumCode()+" "+recordingInput.getSide()+
						recordingInput.getTrack()+recordingInput.getCounter()+" with ID "+id;
				else
					statusLine="Could not update recording on "+recordingInput.getMediumTypeAbbrev()+" "+
					recordingInput.getMediumCode()+" "+recordingInput.getSide()+
					recordingInput.getTrack()+recordingInput.getCounter()+"with ID "+id;
			
				request.setAttribute("mediumid", ""+recordingInput.getMediumId());
			} else {
				Recording rec = new Recording();
				rec.setArtist(recordingInput.getArtist());
				rec.setArtistId(recordingInput.getArtistId());
				rec.setCounter(recordingInput.getCounter());
				rec.setDigital(recordingInput.getDigital());
				rec.setLongplay(recordingInput.getLongplay());
				rec.setMediumCode(recordingInput.getMediumCode());
				rec.setMediumId(recordingInput.getMediumId());
				rec.setMediumType(recordingInput.getMediumType());
				rec.setQuality(recordingInput.getQuality());
				rec.setRemarks(recordingInput.getRemarks());
				rec.setSide(recordingInput.getSide());
				rec.setSongId(recordingInput.getSongId());
				rec.setSpecial(recordingInput.getSpecial());
				rec.setTime(recordingInput.getTime());
				rec.setTitle(recordingInput.getTitle());
				rec.setTrack(recordingInput.getTrack());
				rec.setYear(recordingInput.getYear());
				long retId = db.createRecording(rec);
				
				Recording r = db.getRecording(retId);
				if ( retId > -1)
					statusLine="Created recording on "+r.getMediumTypeAbbrev()+" "+
					r.getMediumCode()+" "+(r.getSide()!=null?r.getSide():"")+
					(r.getTrack()>0?r.getTrack()+"":"")+
					(r.getCounter()!=null?r.getCounter():"")+" with ID "+retId;
				else
					statusLine="Could not create recording on "+recordingInput.getMediumTypeAbbrev()+" "+
					recordingInput.getMediumCode()+" "+recordingInput.getSide()+
					recordingInput.getTrack()+recordingInput.getCounter();
				
				request.setAttribute("mediumid", rec.getMediumId()+"");
			}
		} catch ( DAOException e) {
			log.error(e,e);
			statusLine = "Could not update recording on "+recordingInput.getMediumTypeAbbrev()+" "+
				recordingInput.getMediumCode()+" "+recordingInput.getSide()+
				recordingInput.getTrack()+recordingInput.getCounter()+": "+e.getMessage();
		} catch ( Exception e) {
			log.error(e,e);
			statusLine = "Could not update artist "+recordingInput.getMediumTypeAbbrev()+" "+
				recordingInput.getMediumCode()+" "+recordingInput.getSide()+
				recordingInput.getTrack()+recordingInput.getCounter()+": "+e.getMessage();
		}
		
		request.setAttribute("statusline", statusLine);
		
		log.debug("Forwarding to "+forward);
		return mapping.findForward(forward);
	}

}
