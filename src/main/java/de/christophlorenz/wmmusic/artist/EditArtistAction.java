/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/artist/EditArtistAction.java,v $
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
package de.christophlorenz.wmmusic.artist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.christophlorenz.wmmusic.CommonAction;
import de.christophlorenz.wmmusic.artist.bean.EditArtistBean;
import de.christophlorenz.wmmusic.db.DAOException;
import de.christophlorenz.wmmusic.db.IWmmusicDatabase;
import de.christophlorenz.wmmusic.db.IWmmusicDatabaseFactory;

public class EditArtistAction extends CommonAction {
	
	static final Logger log;
	static {
		log = Logger.getLogger(EditArtistAction.class);
	}

	public ActionForward commonExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String statusLine="";
		
		EditArtistBean artistInput = (EditArtistBean) form;
		
		HttpSession session = request.getSession();
		IWmmusicDatabaseFactory f = (IWmmusicDatabaseFactory) session.getAttribute("wmmusicdbfactory");
		IWmmusicDatabase db = f.createWmmusicDatabase();
		
		log.debug(artistInput);
		
		try {
			long id = artistInput.getId();
			if ( db.artistExistsById(id)) {
				Artist artist = new Artist(id);
				artist.setBirthday(artistInput.getBirthday());
				artist.setCountry(artistInput.getCountry());
				artist.setLocation(artistInput.getLocation());
				artist.setName(artistInput.getName());
				artist.setPrint(artistInput.getPrint());
				artist.setRemarks(artistInput.getRemarks());
				artist.setUrl(artistInput.getUrl());
				if (db.updateArtist(artist))
					statusLine="Updated artist "+artist.getName()+" with ID "+id;
				else
					statusLine="Could not update artist "+artist.getName()+" with ID "+id;
			} else if ( db.artistExistsByName(artistInput.getName())) {
				statusLine="Artist with name "+artistInput.getName()+" but different ID exists. Cannot update!";
			} else {
				Artist artist = new Artist();
				artist.setBirthday(artistInput.getBirthday());
				artist.setCountry(artistInput.getCountry());
				artist.setLocation(artistInput.getLocation());
				artist.setName(artistInput.getName());
				artist.setPrint(artistInput.getPrint());
				artist.setRemarks(artistInput.getRemarks());
				artist.setUrl(artistInput.getUrl());
				long retId = db.createArtist(artist);
				if ( retId > -1)
					statusLine="Created artist "+artist.getName()+" with ID "+retId;
				else
					statusLine="Could not create artist "+artist.getName();
			}
		} catch ( DAOException e) {
			log.error(e,e);
			statusLine = "Could not update artist "+artistInput.getName()+": "+e.getMessage();
		} catch ( Exception e) {
			log.error(e,e);
			statusLine = "Could not update artist "+artistInput.getName()+": "+e.getMessage();
		}
		
		request.setAttribute("statusline", statusLine);
		return mapping.findForward("index");
	}

}
