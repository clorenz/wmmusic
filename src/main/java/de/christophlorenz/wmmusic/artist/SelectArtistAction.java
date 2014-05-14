/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/artist/SelectArtistAction.java,v $
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
package de.christophlorenz.wmmusic.artist;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import de.christophlorenz.wmmusic.CommonAction;
import de.christophlorenz.wmmusic.artist.bean.EditArtistBean;
import de.christophlorenz.wmmusic.db.DAOException;
import de.christophlorenz.wmmusic.db.IWmmusicDatabase;
import de.christophlorenz.wmmusic.db.IWmmusicDatabaseFactory;

public class SelectArtistAction extends CommonAction {
	
	private static Logger log = 
	    Logger.getLogger(SelectArtistAction.class.getName());
	
	public ActionForward commonExecute(ActionMapping mapping, 
             ActionForm form, 
             HttpServletRequest req, 
             HttpServletResponse res) {
	
		DynaActionForm selectArtistForm = (DynaActionForm) form;
		String artistName = (String) selectArtistForm.get("artistName");
		String exact = (String) selectArtistForm.get("exact");
		HttpSession session = req.getSession();
		IWmmusicDatabaseFactory f = (IWmmusicDatabaseFactory) session.getAttribute("wmmusicdbfactory");
		IWmmusicDatabase db = f.createWmmusicDatabase();
		
		try {
			List artistList = new ArrayList();
			if (( artistName != null) && ( artistName.length()>0))
				artistList = db.selectArtist(artistName, "on".equals(exact));
			else {
				String id=null;
				if ( (id=req.getParameter("id")) != null ) {
					artistList = db.selectArtist(Long.parseLong(id));
				}
			}

			if ( artistList.size() == 1) {
				Artist artist = (Artist) artistList.get(0);
				System.out.println("Retrieved artist: "+artist);
				EditArtistBean a = new EditArtistBean();
				a.setBirthday(artist.getBirthday());
				a.setCountry(artist.getCountry());
				a.setId(artist.getId());
				a.setLocation(artist.getLocation());
				a.setName(artist.getName());
				a.setPrint(artist.getPrint());
				a.setRemarks(artist.getRemarks());
				a.setUrl(artist.getUrl());
				
				System.out.println("Setting EditArtistBean: "+a);
				
				req.setAttribute("artist", a);
				
				return mapping.findForward("single");
			} else if ( artistList.size() == 0) {
				EditArtistBean a = new EditArtistBean();
				a.setName(artistName);
				
				req.setAttribute("artist", a);
				
				return mapping.findForward("single");
			} else {
				req.setAttribute("artistList", artistList);
				
				System.out.println("Retrieved list: "+artistList);
				return mapping.findForward("list");
			}
		} catch ( DAOException e) {
			log.error(e,e);
		}
		
		return mapping.findForward("error");
	}

}
