/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/medium/EditMediumForm.java,v $
$Author: clorenz $
$Date: 2008-09-24 19:56:28 $
$Revision: 1.2 $

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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.christophlorenz.wmmusic.CommonAction;

public class EditMediumForm extends CommonAction {
	
	static final Logger log;
	static {
		log = Logger.getLogger(EditMediumForm.class);
	}

	public ActionForward commonExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		try {
			int type = Integer.parseInt(request.getParameter("type"));
		
			request.setAttribute("type", ""+request.getParameter("type"));
			
			log.info("Medium type="+type);
			
			switch (type ) {
				case IMediumTypes.SINGLE:
					setSingleAttributes(request); break;
				default:
					throw new UnknownMediumTypeException("Medium type "+request.getParameter("type")+"("+type+") unknown!");
			}
		} catch ( Exception e) {
			log.error(e,e);
			return mapping.findForward("error");
		}
		
		return mapping.findForward("success");
	}

	/**
	 * @param request
	 */
	private void setSingleAttributes(HttpServletRequest request) {
		request.setAttribute("artist", "true");
		request.setAttribute("title", "true");
		request.setAttribute("label", "true");
		request.setAttribute("ordercode", "true");
		request.setAttribute("p_year", "true");
		request.setAttribute("size", "false");
		request.setAttribute("manufacturer", "false");
		request.setAttribute("system", "false");
		request.setAttribute("recbegindate", "false");
		request.setAttribute("recbeginb", "false");
		request.setAttribute("recenddate", "false");
		request.setAttribute("burning_date", "false");
		request.setAttribute("discid", "false");
		request.setAttribute("trackoffsets", "false");
		request.setAttribute("category", "false");
		request.setAttribute("id3genre", "false");
		request.setAttribute("digital", "false");
		request.setAttribute("audio", "false");
		request.setAttribute("rewriteable", "false");
		request.setAttribute("magic", "false");
		request.setAttribute("filestype", "false");
		request.setAttribute("buy_price", "true");
		request.setAttribute("buy_date", "true");
		request.setAttribute("remarks", "true");
	}

}
