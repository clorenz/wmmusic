/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/song/SelectSongFormAction.java,v $
$Author: clorenz $
$Date: 2008-09-23 19:23:18 $
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
import org.apache.struts.action.DynaActionForm;

import de.christophlorenz.wmmusic.CommonAction;
import de.christophlorenz.wmmusic.artist.Artist;
import de.christophlorenz.wmmusic.db.DAOException;
import de.christophlorenz.wmmusic.db.IWmmusicDatabase;
import de.christophlorenz.wmmusic.db.IWmmusicDatabaseFactory;
import de.christophlorenz.wmmusic.medium.IMediumTypes;
import de.christophlorenz.wmmusic.medium.Medium;

public class SelectSongFormAction extends CommonAction {
	
	static final Logger log;
	static {
		log = Logger.getLogger("SelectSongFormAction.class");
	}

	public ActionForward commonExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String forward="success";
		String mediumId=null;
		
		if ( request.getParameter("path") != null) {
			forward=request.getParameter("path");
			request.setAttribute("path", request.getParameter("path"));
		}
		
		if ( request.getParameter("mediumId") != null) {
			request.setAttribute("mediumId", request.getParameter("mediumId"));
			mediumId = request.getParameter("mediumId");
		}
		
		if ( request.getParameter("artistId") != null) {

			// fetch artist name and put it into the form bean
			HttpSession session = request.getSession();
			IWmmusicDatabaseFactory f = (IWmmusicDatabaseFactory) session.getAttribute("wmmusicdbfactory");
			IWmmusicDatabase db = f.createWmmusicDatabase();
			
			try {
				Artist a = (Artist) db.selectArtist(Long.parseLong(request.getParameter("artistId"))).get(0);
				DynaActionForm selectSongForm = (DynaActionForm) form;
				selectSongForm.set("artist", a.getName());
				selectSongForm.set("artistId", a.getId()+"");
				
				if ( mediumId != null) {
					try {
						Medium m = (Medium) db.selectMedium(Long.parseLong(mediumId)).get(0);
						
						String mediumName = m.getMediumTypeAbbrev()+" "+m.getCode();
						request.setAttribute("mediumName", mediumName);
						
						String maxSide="";
						int maxTrack=0;
						String nextSide="";
						int nextTrack=0;
						
						if (( m.getType() == 5) || ( m.getType() == 6))
							maxSide = db.getMaxSide(m.getId());
						if (( m.getType() == 2) || (m.getType() == 7))
							maxTrack = db.getMaxTrack(m.getId());
						if ( m.getType() == 5 ) 
							maxTrack = db.getMaxTrackOfSide(m.getId(), maxSide);
					
						switch ( m.getType()) {
						case 0: break;
						case 1: break;
						case 2: nextTrack = maxTrack+1; break;
						case 3: break;
						case 4: break;
						case 5: nextSide=(maxSide!=null)?maxSide:"A"; nextTrack=maxTrack+1; break;
						case IMediumTypes.SINGLE: char side='A';
								char locnextSide='A';
								if ( maxSide!=null) {
									side = maxSide.charAt(0);
									locnextSide = (char) (((int) side)+1);
								}
								nextSide=new String(new Character(locnextSide).toString());  // sigh 
								if ( "A".equals(nextSide))
									selectSongForm.set("title", m.getTitle());
								break;
						case 7: nextTrack=maxTrack+1; break;
						}
						
						request.setAttribute("side", nextSide);
						if (nextTrack > 0)
							request.setAttribute("track", nextTrack+"");
					} catch ( Exception ignore) {
						log.error(ignore);
					}
				}
				
			} catch (NumberFormatException e) {
				log.error("artistId "+request.getParameter("artistId")+" is not a number: "+e);
				forward="error";
			} catch (DAOException e) {
				log.error("Could not retrieve artist with artistId "+request.getParameter("artistId")+": "+e);
				forward="error";
			} catch (Exception e) {
				log.error("Unknown exception: "+e,e);
				forward="error";
			}
			
		}
		
		return mapping.findForward(forward);
	}

}
