/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/medium/SelectMediumForm.java,v $
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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.christophlorenz.wmmusic.CommonAction;

public class SelectMediumForm extends CommonAction {
	
	static final Logger log;
	static {
		log = Logger.getLogger(SelectMediumForm.class);
	}

	public ActionForward commonExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		try {
			int type = Integer.parseInt(request.getParameter("type"));
		
			switch (type) {
			case IMediumTypes.AUDIO_TAPE: setAudioTapeAttributes(request); break;
			case IMediumTypes.VIDEO_TAPE: setVideoTapeAttributes(request); break;
			case IMediumTypes.MD: setMDAttributes(request); break;
			case IMediumTypes.ROM: setROMAttributes(request); break;
			case IMediumTypes.FILES: setFilesAttributes(request); break;
			case IMediumTypes.LP: setLPAttributes(request); break;
			case IMediumTypes.SINGLE: setSingleAttributes(request); break;
			case IMediumTypes.CD: setCDAttributes(request); break;
			default:
				throw new UnknownMediumTypeException("Medium "+request.getParameter("type")+" unknown!");
			}
			
			request.setAttribute("type", ""+type);
			return mapping.findForward("success");
		} catch ( Exception e) {
			log.error(e,e);
			return mapping.findForward("error");
		}
	}

	private void setCDAttributes(HttpServletRequest request) {
		request.setAttribute("name", "CD");
		request.setAttribute("id", "true");
		request.setAttribute("artist", "true");
		request.setAttribute("title", "true");
	}

	private void setLPAttributes(HttpServletRequest request) {
		request.setAttribute("name", "LP");
		request.setAttribute("id", "true");
		request.setAttribute("artist", "true");
		request.setAttribute("title", "true");
	}

	private void setFilesAttributes(HttpServletRequest request) {
		request.setAttribute("name", "Files");
	}

	private void setROMAttributes(HttpServletRequest request) {
		request.setAttribute("name", "CD-ROM");
		request.setAttribute("id", "true");
	}

	private void setMDAttributes(HttpServletRequest request) {
		request.setAttribute("name", "MiniDisc");
		request.setAttribute("id", "true");
	}

	private void setVideoTapeAttributes(HttpServletRequest request) {
		request.setAttribute("name", "Video Tape");
		request.setAttribute("id", "true");
	}

	private void setAudioTapeAttributes(HttpServletRequest request) {
		request.setAttribute("name", "Audio Tape");
		request.setAttribute("id", "true");
	}

	/**
	 * @param request
	 */
	private void setSingleAttributes(HttpServletRequest request) {
		request.setAttribute("name", "Single");
		request.setAttribute("id", "true");
		request.setAttribute("artist", "true");
		request.setAttribute("title", "true");
	}

}
