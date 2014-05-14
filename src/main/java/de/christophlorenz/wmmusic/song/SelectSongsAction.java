/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/song/SelectSongsAction.java,v $
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
package de.christophlorenz.wmmusic.song;

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
import de.christophlorenz.wmmusic.medium.Medium;
import de.christophlorenz.wmmusic.recording.Recording;
import de.christophlorenz.wmmusic.recording.bean.EditRecordingBean;
import de.christophlorenz.wmmusic.song.bean.EditSongAndRecordingBean;
import de.christophlorenz.wmmusic.song.bean.EditSongBean;

public class SelectSongsAction extends CommonAction {
	
	static final Logger log;
	static {
		log = Logger.getLogger(SelectSongsAction.class);
	}

	public ActionForward commonExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse response) {
		
		String forward="success";
		String path=null;
		
		if ( req.getParameter("path") != null) {
			forward=req.getParameter("path");
			req.setAttribute("path", req.getParameter("path"));
			path = req.getParameter("path");
		}

		List songsList = new ArrayList();
		
		DynaActionForm selectSongForm = (DynaActionForm) form;
		String artist = (String) selectSongForm.get("artist");
		String title = (String) selectSongForm.get("title");
		String mediumId = (String) selectSongForm.get("mediumId");
		String artistId = (String) selectSongForm.get("artistId");
		String songId = (String) req.getParameter("id");
		String exact = (String) req.getParameter("exact");
		
		HttpSession session = req.getSession();
		IWmmusicDatabaseFactory f = (IWmmusicDatabaseFactory) session.getAttribute("wmmusicdbfactory");
		IWmmusicDatabase db = f.createWmmusicDatabase();

		// Schritt 1: artistId paßt zu artist? Wenn nicht, dann Ausstieg
		if ( artistId.length()>0) {
			try {
				Artist a = (Artist) db.selectArtist(Long.parseLong(artistId)).get(0);
				if ( ! a.getName().equals(artist)) {
					log.info("Provided artist '"+artist+"' does not match with provided artistId "+artistId);
					//return mapping.findForward("unknownArtist");
					a = (Artist) db.selectArtist(artist, false).get(0);		// No exact search required here
					
					artist = a.getName();
					artistId = ""+a.getId();
				}
			} catch (NumberFormatException e) {
				log.error("Invalid artistID: "+artistId+": "+e);
				forward="error";
			} catch (DAOException e) {
				log.error("Cannot retrieve artist with ID "+artistId+": "+e);
			}
		}
			
		// Schritt 2: Existiert der Song in der DB?
		
		try {
			System.out.println("ARTIST='"+artist+"', TITLE='"+title+"', SONGID="+songId+", EXACT="+exact+", MEDIUMID="+mediumId);
			if ( artist.length()<=0 && title.length()<=0 && songId!=null && songId.length()>0) {
				System.out.println("Retrieving song "+songId);
				Song s = db.getSong(Long.parseLong(songId));
				System.out.println("S="+s);
				if ( s != null) {
					EditSongBean e = new EditSongBean();
					e.setArtist(s.getArtist());
					e.setArtistId(s.getArtist_id());
					e.setAuthors(s.getAuthors());
					e.setDance(s.getDance());
					e.setId(s.getId());
					e.setId3Genre(s.getId3Genre());
					e.setRelease(s.getRelease());
					e.setRemarks(s.getRemarks());
					e.setTitle(s.getTitle());
					e.setYear(""+s.getYear());
					req.setAttribute("editSongForm", e);
					
					//forward="editSong";
					
					List recordingsList = new ArrayList();
					recordingsList = db.selectRecordingsForSong(s.getId());	
					for ( int i=0; i<recordingsList.size(); i++) {
						Recording r = (Recording) recordingsList.get(i);
						if ( r.getMediumType()>=5) {
							Medium m = (Medium) db.selectMedium(r.getMediumId()).get(0);
							r.setMediumArtist(m.getArtist());
							r.setMediumTitle(m.getTitle());
						}
						recordingsList.set(i, r);
					}
					req.setAttribute("recordingList", recordingsList);
					forward="editSongAndListRecordings";
					
					if ( (mediumId!=null) && "ARTM".equals(path)) {
						req.setAttribute("mediumid", mediumId);
						List sl = new ArrayList();
						sl.add(s);
						return mapping.findForward(prepareNewRecordingForSong(req, sl, mediumId, db));
					} else { 
						if ( mediumId != null)
							req.setAttribute("mediumid", mediumId);
						return mapping.findForward(forward);
					}
				}
			} else
				songsList = db.selectSongs(artist, title, "on".equals(exact));
			for ( int i=0; i<songsList.size(); i++) {
				Song s = (Song) songsList.get(i);
				Recording r = db.getBestRecordingForSong(s.getId());
				if ( ( r.getQuality() == 7) && ( r.getMediumType() >= 5) )
					s.setBestQuality(true);
				songsList.set(i, s);
			}
			
			System.out.println("songsList.size()="+songsList.size());
			
			// Wenn mehr als ein Song gefunden, muß noch ein Zwischenschritt eingebaut
			// werden, der dann den passenden Song heraussucht
		
			req.setAttribute("songList", songsList);
			
			System.out.println("mediumId="+mediumId);
		
			if ( mediumId != null && mediumId.length()>0) {
				System.out.println("MediumID is set!");
				req.setAttribute("mediumid", mediumId);
				
				//req.setAttribute("mediumId", mediumId);
				if ( songsList.size() == 0) {
					EditSongAndRecordingBean editBean = new EditSongAndRecordingBean();
					if ( mediumId.length()>0) editBean.setMediumId(Long.parseLong(mediumId));
					editBean.setArtist(artist);
					if ( artistId.length()>0) editBean.setArtistId(Long.parseLong(artistId));
					editBean.setTitle(title);
					
					if ( mediumId.length()>0) {
						Medium m = (Medium) db.selectMedium(editBean.getMediumId()).get(0);
						editBean.setYear(m.getYear()+"");
						editBean.setRecordingYear(m.getYear()+"");
						editBean.setMediumCode(m.getCode());
						editBean.setMediumType(m.getType());
					
						// nächste Aufnahmeposition ermitteln
						String maxSide=null;
						int maxTrack=-1;
					
						if (( m.getType() == 5) || ( m.getType() == 6))
							maxSide = db.getMaxSide(m.getId());
						if (( m.getType() == 2) || (m.getType() == 7))
							maxTrack = db.getMaxTrack(m.getId());
						if ( m.getType() == 5 ) 
							maxTrack = db.getMaxTrackOfSide(m.getId(), maxSide);
					
						System.out.println("maxSide="+maxSide);
					
					
						switch ( m.getType()) {
						case 0: editBean.setDigital((m.getDigital()!=null)?m.getDigital():"AAA"); break;
						case 1: editBean.setDigital((m.getDigital()!=null)?m.getDigital():"AAA"); break;
						case 2: editBean.setDigital((m.getDigital()!=null)?m.getDigital():"AAD"); 
								editBean.setTrack(maxTrack+1); break;
						case 3: editBean.setDigital((m.getDigital()!=null)?m.getDigital():"AAD");
						case 4: editBean.setDigital((m.getDigital()!=null)?m.getDigital():"AAD");
						case 5: editBean.setSide((maxSide!=null)?maxSide:"A"); editBean.setTrack(maxTrack+1); 
								editBean.setDigital((m.getDigital()!=null)?m.getDigital():"AAA"); 
								editBean.setRelease("LP"); break;
						case 6: char side='A';
								char nextSide='A';
								if ( maxSide!=null) {
									side = maxSide.charAt(0);
									nextSide = (char) (((int) side)+1);
								}
								editBean.setSide(new String(new Character(nextSide).toString()));  // sigh 
								editBean.setRelease("S/"+nextSide);
								editBean.setDigital((m.getDigital()!=null)?m.getDigital():"AAA"); break;   // TODO: FIXME!
						case 7: editBean.setTrack(maxTrack+1); editBean.setRelease("LP");
								editBean.setDigital((m.getDigital()!=null)?m.getDigital():"DDD");break;
						}
					}
					
					req.setAttribute("editSongAndRecordingForm", editBean);
					
					System.out.println("editSongAndRecordingForm="+editBean);
					
					forward="insertSongAndRecording";
					
				} else if ( songsList.size() == 1) {
					forward = prepareNewRecordingForSong(req, songsList, mediumId, db);
				} else {
					forward="songList";
				}
				
			} else {
				if ( songsList.size() == 0) {
					// Fill the bean!
					Song s = (Song) songsList.get(0);
					EditSongBean e = new EditSongBean();
					e.setArtist(s.getArtist());
					e.setArtistId(s.getArtist_id());
					e.setAuthors(s.getAuthors());
					e.setDance(s.getDance());
					e.setId(s.getId());
					e.setId3Genre(s.getId3Genre());
					e.setRelease(s.getRelease());
					e.setRemarks(s.getRemarks());
					e.setTitle(s.getTitle());
					e.setYear(""+s.getYear());
					req.setAttribute("editSongForm", e);
				
					List recordingsList = new ArrayList();
					recordingsList = db.selectRecordingsForSong(s.getId());	
					req.setAttribute("recordingList", recordingsList);
					forward="editSongAndListRecordings";
				} else {
					req.setAttribute("songList", songsList);
					forward="songList";
				}
			}
			
		} catch ( DAOException e) {
			log.error(e,e);
			System.out.println(e);
		} catch ( Exception e) {
			log.error("Unknown exception: "+e,e);
			forward="error";
		}
		
		System.out.println("forwarding to "+forward);
		
		return mapping.findForward(forward);
	}

	/**
	 * @param req
	 * @param songsList
	 * @param mediumId
	 * @param db
	 * @return
	 * @throws DAOException
	 */
	private String prepareNewRecordingForSong(HttpServletRequest req, List songsList, String mediumId, IWmmusicDatabase db) throws DAOException {
		String forward;
		EditRecordingBean editBean = new EditRecordingBean();
		
		Song s = (Song) songsList.get(0);
		
		editBean.setArtist(s.getArtist());   // For display purposes only
		editBean.setTitle(s.getTitle());	 // For display purposes only
		editBean.setSongId(s.getId());
		if ( mediumId!=null && mediumId.length()>0) editBean.setMediumId(Long.parseLong(mediumId));
		editBean.setYear(s.getYear()+"");
		
		if ( mediumId != null && mediumId.length()>0) {
			Medium m = (Medium) db.selectMedium(editBean.getMediumId()).get(0);
			
			req.setAttribute("mediumid", ""+editBean.getMediumId());
			
			editBean.setMediumCode(m.getCode());
			editBean.setMediumType(m.getType());
		
			// nächste Aufnahmeposition ermitteln
			String maxSide=null;
			int maxTrack=-1;
		
			if (( m.getType() == 5) || ( m.getType() == 6))
				maxSide = db.getMaxSide(m.getId());
			if (( m.getType() == 2) || (m.getType() == 7))
				maxTrack = db.getMaxTrack(m.getId());
			if ( m.getType() == 5)
				maxTrack = db.getMaxTrackOfSide(m.getId(), maxSide);
		
			System.out.println("maxSide="+maxSide);
		
		
			switch ( m.getType()) {
				case 0: editBean.setDigital((m.getDigital()!=null)?m.getDigital():"AAA"); break;
				case 1: editBean.setDigital((m.getDigital()!=null)?m.getDigital():"AAA"); break;
				case 2: editBean.setDigital((m.getDigital()!=null)?m.getDigital():"AAD"); 
						editBean.setTrack(maxTrack+1); break;
				case 3: editBean.setDigital((m.getDigital()!=null)?m.getDigital():"AAD");
				case 4: editBean.setDigital((m.getDigital()!=null)?m.getDigital():"AAD");
				case 5: editBean.setSide((maxSide!=null)?maxSide:"A"); editBean.setTrack(maxTrack+1); 
						editBean.setDigital((m.getDigital()!=null)?m.getDigital():"AAA"); break;
				case 6: char side='A';
						char nextSide='A';
						if ( maxSide!=null) {
							side = maxSide.charAt(0);
							nextSide = (char) (((int) side)+1);
						}
						editBean.setSide((maxSide!=null)?new String(new Character(nextSide).toString()):"A");  // sigh 
						editBean.setDigital((m.getDigital()!=null)?m.getDigital():"AAA"); break;   // TODO: FIXME!
				case 7: editBean.setTrack(maxTrack+1); 
						editBean.setDigital((m.getDigital()!=null)?m.getDigital():"DDD");break;
			}
		}
		
		EditSongBean songBean = new EditSongBean();
		songBean.setArtist(s.getArtist());
		songBean.setArtistId(s.getArtist_id());
		songBean.setAuthors(s.getAuthors());
		songBean.setDance(s.getDance());
		songBean.setId(s.getId());
		songBean.setId3Genre(s.getId3Genre());
		songBean.setRelease(s.getRelease());
		songBean.setRemarks(s.getRemarks());
		songBean.setTitle(s.getTitle());
		songBean.setYear(s.getYear()+"");
		
		
		req.setAttribute("editRecordingForm", editBean);
		req.setAttribute("songBean", songBean);
		
		System.out.println("Forwarding to insertRecording");
		forward="insertRecording";
		return forward;
	}

}
