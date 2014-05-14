/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/medium/SelectMediumAction.java,v $
$Author: clorenz $
$Date: 2008-10-13 19:34:09 $
$Revision: 1.5 $

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
import de.christophlorenz.wmmusic.artist.Artist;
import de.christophlorenz.wmmusic.db.DAOException;
import de.christophlorenz.wmmusic.db.IWmmusicDatabase;
import de.christophlorenz.wmmusic.db.IWmmusicDatabaseFactory;
import de.christophlorenz.wmmusic.medium.bean.EditMediumBean;

public class SelectMediumAction extends CommonAction {
	
	private static Logger log = 
	    Logger.getLogger(SelectMediumAction.class.getName());
	
	public ActionForward commonExecute(ActionMapping mapping, 
             ActionForm form, 
             HttpServletRequest req, 
             HttpServletResponse res) {
		
		List mediumList = new ArrayList();
	
		DynaActionForm selectMediumForm = (DynaActionForm) form;
		
		HttpSession session = req.getSession();
		IWmmusicDatabaseFactory f = (IWmmusicDatabaseFactory) session.getAttribute("wmmusicdbfactory");
		IWmmusicDatabase db = f.createWmmusicDatabase();
		
		String code = (String) selectMediumForm.get("code");
		String type = (String) selectMediumForm.get("type");
		String artist = (String) selectMediumForm.get("artist");
		String title = (String) selectMediumForm.get("title");
		String id = (String) req.getParameter("id");
		
		try {
			if (( id != null ) && ( id.length()>0))
				mediumList = db.selectMedium(Long.parseLong(id));
			else if (( code!=null) && (code.length()>0))
				mediumList = db.selectMediumByCode(Integer.parseInt(type), code);
			else
				mediumList = db.selectMedium(Integer.parseInt(type), artist, title);
			
			log.debug("CODE="+code+"="+mediumList);
			
			
			if ( mediumList.size() == 1) {
				Medium medium = (Medium) mediumList.get(0);
				EditMediumBean m = new EditMediumBean();
				m.setArtistId(medium.getArtistId());
				m.setArtist(((Artist) db.selectArtist(medium.getArtistId()).get(0)).getName());
				m.setAudio(medium.getAudio());
				m.setBurningDate(medium.getBurningDate());
				m.setBuyDate(medium.getBuyDate());
				m.setBuyPrice(medium.getBuyPrice());
				m.setCategory(medium.getCategory());
				m.setCode(medium.getCode());
				m.setDigital(medium.getDigital());
				m.setDiscid(medium.getDiscid());
				m.setFilesType(medium.getFilesType());
				m.setId(medium.getId());
				m.setId3Genre(medium.getId3Genre());
				m.setLabel(medium.getLabel());
				m.setMagic(medium.getMagic());
				m.setManufacturer(medium.getManufacturer());
				m.setOrdercode(medium.getOrdercode());
				m.setYear(medium.getYear());
				m.setRecBeginB(medium.getRecBeginB());
				m.setRecBeginDate(medium.getRecBeginDate());
				m.setRecEndDate(medium.getRecEndDate());
				m.setRemarks(medium.getRemarks());
				m.setRewritable(medium.getRewritable());
				m.setSize(medium.getSize());
				m.setSystem(medium.getSystem());
				m.setTitle(medium.getTitle());
				m.setTrackOffsets(medium.getTrackOffsets());
				m.setType(medium.getType());
				
				setFormFields(m);
				
				req.setAttribute("medium", m);
				
				return mapping.findForward("single");
			} else if ( mediumList.size() == 0) {
				if ( (artist!=null) && (artist.length()>0)) {
					if (db.artistExistsByName(artist)) {
						Artist a = (Artist) db.selectArtist(artist, true).get(0);   // Exact artist match required!
				
						EditMediumBean m = new EditMediumBean();
						m.setType(Integer.parseInt(type));
						m.setArtistId(a.getId());
						m.setTitle(title);
						m.setArtist(a.getName());
						
						setFormFields(m);
						
						try {
							m.setCode(calculateCode(db, m.getType(), a.getName()));
						} catch ( Exception e) {
							log.error("Error on code calculation for ("+m.getType()+","+a.getName()+"): "+e,e);
						}
							
						req.setAttribute("medium", m);
				
						return mapping.findForward("single");
					} else {
						log.error("Unknown artist: "+artist+" FIXME!!!!");
						return mapping.findForward("error");
					}
				} else {
					log.error("HIER SOLLTE DIE ABFRAGE REIN, OB DAS MEDIUM NEU ANGELEGT WERDEN SOLL!!!");
					return mapping.findForward("index");
				}
			} else {
				req.setAttribute("mediumList", mediumList);
				
				System.out.println("Retrieved list: "+mediumList);
				return mapping.findForward("list");
			}
		} catch ( DAOException e) {
			log.error(e,e);
		} catch ( Exception e) {
			log.error(e,e);
			req.setAttribute("error", e.getLocalizedMessage());
		}
		
		return mapping.findForward("index");
	}


	/**
	 * This method sets, which fields are to be displayed 
	 * @param m
	 */
	private void setFormFields(EditMediumBean m) {
		if ( m.getType()==7)
			m.setShowDigital(true);
	}
	
	
	private String calculateCode(IWmmusicDatabase db, int mediumType, String artist) throws Exception {
		// 1. Existiert bereits ein Medium selben Typs dieses Artists
		//	=> Code +1
		if ( db.getMaxMediumCode(mediumType, artist) != null ) {
			log.debug("maxMediumCode="+db.getMaxMediumCode(mediumType, artist));
			
			String [] codeparts = db.getMaxMediumCode(mediumType, artist).split(" ");
			
			for ( int i=0; i<codeparts.length; i++) {
				log.debug("Codeparts["+i+"]="+codeparts[i]);
			}
			
			int codenumber;
			
			if ( codeparts.length==1) {
				String artistcode = codeparts[0].substring(0,6);
				codenumber = Integer.parseInt(codeparts[0].substring(6));
				
				// fix codeparts, so that we always have got two parts at least
				codeparts = new String[2];
				codeparts[0] = artistcode;
				codeparts[1] = ""+codenumber;
				
			} else
				codenumber = Integer.parseInt(codeparts[codeparts.length-1]);
					
			String code="";
			for ( int i=0; i<(codeparts.length-1); i++) {
				code += codeparts[i];
			}
			
			code = (code+"        ").substring(0,6);	
			
			if ( codenumber<9) {			
				code += " " + (codenumber+1);
			} else {
				code += (codenumber+1);
			}
			
			return code;
		}
		
		
		// 2. Existiert bereits ein Medium anderen Typs dieses Artists
		//  => Codeprefix 1
		if ( db.getMinMediumCode(artist) != null ) {
			String [] codeparts = db.getMinMediumCode(artist).split(" ");
			String code="";
			for ( int i=0; i<(codeparts.length-1); i++) {
				code += codeparts[i];
			}
			
			code = (code+"        ").substring(0,6);
			code += " 1";
			return code;
		}
		
		// 3. Nachname/Vorname 1 als Code verwenden
		boolean nameOk=false;
		int lengthSurname=5;
		String surname="";
		String firstname="";
		String tempId="";
		
		while ( ( !nameOk ) && ( lengthSurname > 0 ) ) {
			tempId="";
			if ( artist.indexOf(",") > -1  ) {
				String [] nameparts = artist.split(",");
				surname = nameparts[0];
				firstname = nameparts[1];
				firstname=firstname.trim();
				if ( surname.length() > lengthSurname ) {
					surname = surname.substring(0,lengthSurname);
				}
				
				log.debug("Artist="+artist+", surname="+surname+", firstName="+firstname);
				
				tempId = surname + firstname + "      ";
				tempId = tempId.substring(0, 6) + " 1";
			} else {
				String[] rest = artist.split("\\s+");
				if ( rest.length < 5) {
					System.out.println("2");
					for ( int i=0; i<rest.length; i++) {
						if ( lengthSurname > 5 )
							tempId = tempId + rest[i].substring(5-lengthSurname, ( 6 / rest.length));
						else
							tempId = tempId + rest[i];
					}
					log.debug("tempId='"+tempId+"'");
				} else {
					for ( int i=0; i<5; i++) {
						log.debug("rest["+i+"]="+rest[i]+"; lengthSurname="+lengthSurname);
						tempId = tempId + rest[i].substring(5-lengthSurname, 1);
					}
				}
				tempId = tempId+"      ";
				tempId = tempId.substring(0,6) + " 1";
			}
			
			nameOk = (db.selectMediumByCode(mediumType, tempId).isEmpty());
			
			lengthSurname--;
		}
		
		if ( nameOk )
			return tempId;
		else {
			log.warn("Cannot calculate code for medium type "+mediumType+" and tempId "+tempId);
			return "--------";
		}
		
	}

}
