/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/medium/EditMediumAction.java,v $
$Author: clorenz $
$Date: 2008-07-20 11:13:22 $
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
package de.christophlorenz.wmmusic.medium;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.christophlorenz.wmmusic.CommonAction;
import de.christophlorenz.wmmusic.artist.Artist;
import de.christophlorenz.wmmusic.db.DAOException;
import de.christophlorenz.wmmusic.db.IWmmusicDatabase;
import de.christophlorenz.wmmusic.db.IWmmusicDatabaseFactory;
import de.christophlorenz.wmmusic.medium.bean.EditMediumBean;

public class EditMediumAction extends CommonAction {
	
	static final Logger log;
	static {
		log = Logger.getLogger(EditMediumAction.class);
	}

	public ActionForward commonExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String statusLine="";
		
		EditMediumBean mediumInput = (EditMediumBean) form;
		
		HttpSession session = request.getSession();
		IWmmusicDatabaseFactory f = (IWmmusicDatabaseFactory) session.getAttribute("wmmusicdbfactory");
		IWmmusicDatabase db = f.createWmmusicDatabase();
		
		try {
			long id = mediumInput.getId();
			if ( db.mediumExistsById(id)) {
				Medium m = new Medium(id);
				Artist a = db.getArtistByName(mediumInput.getArtist());
				m.setArtistId(a.getId());
				m.setAudio(mediumInput.getAudio());
				m.setBurningDate(mediumInput.getBurningDate());
				m.setBuyDate(mediumInput.getBuyDate());
				m.setBuyPrice(mediumInput.getBuyPrice());
				m.setCategory(mediumInput.getCategory());
				m.setCode(mediumInput.getCode());
				m.setDigital(mediumInput.getDigital());
				m.setDiscid(mediumInput.getDiscid());
				m.setFilesType(mediumInput.getFilesType());
				m.setId3Genre(mediumInput.getId3Genre());
				m.setLabel(mediumInput.getLabel());
				m.setMagic(mediumInput.getMagic());
				m.setManufacturer(mediumInput.getManufacturer());
				m.setOrdercode(mediumInput.getOrdercode());
				m.setYear(mediumInput.getYear());
				m.setRecBeginB(mediumInput.getRecBeginB());
				m.setRecBeginDate(mediumInput.getRecBeginDate());
				m.setRecEndDate(mediumInput.getRecEndDate());
				m.setRemarks(mediumInput.getRemarks());
				m.setRewritable(mediumInput.getRewritable());
				m.setSize(mediumInput.getSize());
				m.setSystem(mediumInput.getSystem());
				m.setTitle(mediumInput.getTitle());
				m.setTrackOffsets(mediumInput.getTrackOffsets());
				m.setType(mediumInput.getType());
				if (db.updateMedium(m))
					statusLine="Updated medium "+m.getCode()+" with ID "+id;
				else
					statusLine="Could not update medium "+m.getCode()+" with ID "+id;
			} else {
				Medium m = new Medium();
				Artist a = db.getArtistByName(mediumInput.getArtist());
				m.setArtistId(a.getId());
				m.setAudio(mediumInput.getAudio());
				m.setBurningDate(mediumInput.getBurningDate());
				m.setBuyDate(mediumInput.getBuyDate());
				m.setBuyPrice(mediumInput.getBuyPrice());
				m.setCategory(mediumInput.getCategory());
				m.setCode(mediumInput.getCode());
				m.setDigital(mediumInput.getDigital());
				m.setDiscid(mediumInput.getDiscid());
				m.setFilesType(mediumInput.getFilesType());
				m.setId3Genre(mediumInput.getId3Genre());
				m.setLabel(mediumInput.getLabel());
				m.setMagic(mediumInput.getMagic());
				m.setManufacturer(mediumInput.getManufacturer());
				m.setOrdercode(mediumInput.getOrdercode());
				m.setYear(mediumInput.getYear());
				m.setRecBeginB(mediumInput.getRecBeginB());
				m.setRecBeginDate(mediumInput.getRecBeginDate());
				m.setRecEndDate(mediumInput.getRecEndDate());
				m.setRemarks(mediumInput.getRemarks());
				m.setRewritable(mediumInput.getRewritable());
				m.setSize(mediumInput.getSize());
				m.setSystem(mediumInput.getSystem());
				m.setTitle(mediumInput.getTitle());
				m.setTrackOffsets(mediumInput.getTrackOffsets());
				m.setType(mediumInput.getType());

				long retId = db.createMedium(m);
				if ( retId > -1)
					statusLine="Created medium "+m.getCode()+" with ID "+retId;
				else
					statusLine="Could not create medium "+m.getCode();
			}
		} catch ( DAOException e) {
			log.error(e,e);
			statusLine = "Could not update medium "+mediumInput.getCode()+": "+e.getMessage();
		} catch ( Exception e) {
			log.error(e,e);
			statusLine = "Could not update artist "+mediumInput.getCode()+": "+e.getMessage();
		}
		
		request.setAttribute("statusline", statusLine);

		return mapping.findForward("success");
	}

}
