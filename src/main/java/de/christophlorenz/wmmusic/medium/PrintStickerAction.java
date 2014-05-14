/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/medium/PrintStickerAction.java,v $
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
package de.christophlorenz.wmmusic.medium;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import de.christophlorenz.wmmusic.medium.bean.StickerBean;

public class PrintStickerAction extends CommonAction {
	
	static final Logger log;
	static final DecimalFormat price = new DecimalFormat("#0.00");
	static {
		log = Logger.getLogger(PrintStickerAction.class);
	}

	public ActionForward commonExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		
		ArrayList stickerList = new ArrayList();
		HttpSession session = request.getSession();
		IWmmusicDatabaseFactory f = (IWmmusicDatabaseFactory) session.getAttribute("wmmusicdbfactory");
		IWmmusicDatabase db = f.createWmmusicDatabase();
		
		try {
			List media = db.getTaggedMediaIds(getUsername(),"sticker");
			for ( Iterator it = media.iterator() ; it.hasNext() ; ) {
				long mediumId = ((Long) it.next()).longValue();
				List<Medium> mediaForMediumId = db.selectMedium(mediumId);
				if ( mediaForMediumId.size()<1) {
					log.error("Cannot retrieve a medium for ID "+mediumId);
				} else {
					Medium m = mediaForMediumId.get(0);
					StickerBean s = new StickerBean();
					s.setCode(m.getMediumTypeAbbrev()+" "+m.getCode().replaceAll("\\s","&nbsp;"));
					s.setBuyDate(m.getBuyDate());
					if ( (m.getBuyDate()!=null) && (m.getBuyDate().indexOf("-")> -1) ) {
						// Datum im amerik. Format => umformatieren!
						String[] dateparts = m.getBuyDate().split("-");
						if (dateparts.length==3)
							s.setBuyDate(dateparts[2]+"."+dateparts[1]+"."+dateparts[0]);
					}
				
					if ( m.getBuyPrice() >= 0.0d)
						s.setBuyPrice("EUR "+price.format(m.getBuyPrice()));
				
					s.setArtist(m.getArtist());
					s.setTitle(m.getTitle());
					stickerList.add(s);
				}
			}
		} catch ( DAOException e) {
			log.error(e,e);
			return mapping.findForward("error");
		} catch ( Exception e) {
			log.error(e,e);
			return mapping.findForward("error");
		}
	
		
		request.setAttribute("stickerlist", stickerList);
		
		return mapping.findForward("success");
	}

}
