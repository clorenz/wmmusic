/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/tools/GetInventoryAction.java,v $
$Author: clorenz $
$Date: 2010-09-08 20:36:55 $
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
package de.christophlorenz.wmmusic.tools;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.christophlorenz.wmmusic.CommonAction;
import de.christophlorenz.wmmusic.db.DAOException;
import de.christophlorenz.wmmusic.db.IWmmusicDatabase;
import de.christophlorenz.wmmusic.db.IWmmusicDatabaseFactory;
import de.christophlorenz.wmmusic.medium.IMediumTypes;
import de.christophlorenz.wmmusic.medium.Medium;
import de.christophlorenz.wmmusic.tools.bean.InventoryBean;

public class GetInventoryAction extends CommonAction {
	
	static final Logger log;
	static {
		log = Logger.getLogger(GetInventoryAction.class);
	}

	@Override
	public ActionForward commonExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String forward = "error";
		JsonConfig jsonConfig = new JsonConfig();
		
		try {
			InventoryBean inventoryBean = new InventoryBean();
			HttpSession session = request.getSession();
			IWmmusicDatabaseFactory f = (IWmmusicDatabaseFactory) session.getAttribute("wmmusicdbfactory");
			IWmmusicDatabase db = f.createWmmusicDatabase();
			
			
			List<Medium> singleList = db.selectMedium(IMediumTypes.SINGLE,null,null);
			
			JSONArray jsonArraySingles = JSONArray.fromObject(singleList, jsonConfig);	
			inventoryBean.setSingles(jsonArraySingles.toString());
			
			List<Medium> lpList = db.selectMedium(IMediumTypes.LP, null, null);
			JSONArray jsonArrayLps = JSONArray.fromObject(lpList, jsonConfig);
			inventoryBean.setLps(jsonArrayLps.toString());
			
			List<Medium> cdList = db.selectMedium(IMediumTypes.CD, null, null);
			JSONArray jsonArrayCds = JSONArray.fromObject(cdList, jsonConfig);
			inventoryBean.setCds(jsonArrayCds.toString());
			
			
			JSONObject inventory = JSONObject.fromObject(inventoryBean);
			//JSONArray inventory = JSONArray.fromObject(inventoryBean);
			
			//JSONArray inventory = new JSONArray();
			//inventory.add("singles"); inventory.add(jsonArraySingles);
			//inventory.add("lps:"); inventory.add(jsonArrayLps);
			//inventory.add("cds:"); inventory.add(jsonArrayCds);
			
			request.setAttribute("inventory", inventory);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			response.getWriter().print(inventory.toString());
			response.getWriter().flush();
			
			forward = "success";
		} catch ( DAOException e) {
			log.error(e,e);
		} catch ( Exception e) {
			log.error(e,e);
			request.setAttribute("error", e.getLocalizedMessage());
		}
		
		//return mapping.findForward(forward);
		return null;
	}

}
