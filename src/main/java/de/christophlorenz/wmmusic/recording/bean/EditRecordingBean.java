/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/recording/bean/EditRecordingBean.java,v $
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
package de.christophlorenz.wmmusic.recording.bean;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class EditRecordingBean extends ActionForm {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -4686338918159651893L;
	private String id=null;
	private String artist=null;
	private long artistId=-1;
	private String title=null;
	private long songId=-1;
	private long medium_id=-1;
	private int medium_type=-1;
	private String medium_code=null;
	private String side=null;
	private int track=-1;
	private String counter=null;
	private int time;
	private int year=-1;
	private String digital=null;
	private String remarks=null;
	private int quality=7;
	private int special=0;
	private String longplay=null;

	
	/**
	 * @return Returns the artist.
	 */
	public String getArtist() {
		return artist;
	}
	/**
	 * @param artist The artist to set.
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	/**
	 * @return Returns the artistId.
	 */
	public long getArtistId() {
		return artistId;
	}
	/**
	 * @param artistId The artistId to set.
	 */
	public void setArtistId(long artistId) {
		this.artistId = artistId;
	}
	/**
	 * @return Returns the counter.
	 */
	public String getCounter() {
		return counter;
	}
	/**
	 * @param counter The counter to set.
	 */
	public void setCounter(String counter) {
		this.counter = counter;
	}
	/**
	 * @return Returns the medium_code.
	 */
	public String getMediumCode() {
		return medium_code;
	}
	/**
	 * @param medium_code The medium_code to set.
	 */
	public void setMediumCode(String medium_code) {
		this.medium_code = medium_code;
	}
	/**
	 * @return Returns the medium_id.
	 */
	public long getMediumId() {
		return medium_id;
	}
	/**
	 * @param medium_id The medium_id to set.
	 */
	public void setMediumId(long medium_id) {
		this.medium_id = medium_id;
	}
	/**
	 * @return Returns the medium_type.
	 */
	public int getMediumType() {
		return medium_type;
	}
	/**
	 * @param medium_type The medium_type to set.
	 */
	public void setMediumType(int medium_type) {
		this.medium_type = medium_type;
	}
	/**
	 * @return Returns the mediumTypeAbbrev.
	 */
	public String getMediumTypeAbbrev() {
		switch (medium_type) {
		case 0: return "C";
		case 1: return "V";
		case 2: return "D";
		case 3: return "M";
		case 4: return "F";
		case 5: return "L";
		case 6: return "S";
		case 7: return "D";
		default: return " ";
		}
	}
	
	/**
	 * @return Returns the side.
	 */
	public String getSide() {
		return side;
	}
	/**
	 * @param side The side to set.
	 */
	public void setSide(String side) {
		this.side = side;
	}
	/**
	 * @return Returns the songId.
	 */
	public long getSongId() {
		return songId;
	}
	/**
	 * @param songId The songId to set.
	 */
	public void setSongId(long songId) {
		this.songId = songId;
	}
	/**
	 * @return Returns the time.
	 */
	public int getTime() {
		return time;
	}
	/**
	 * @param time The time to set.
	 */
	public void setTime(int time) {
		this.time = time;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return Returns the track.
	 */
	public int getTrack() {
		return track;
	}
	/**
	 * @param track The track to set.
	 */
	public void setTrack(int track) {
		this.track = track;
	}
	
	
	
	/**
	 * @return Returns the remarks.
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks The remarks to set.
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getRawTime() {
		if ( time > 0) {
			DecimalFormat df = new DecimalFormat("00");
			return df.format(Math.floor(time/60))+":"+df.format(time%60);
		} else {
			return "";
		}
	}
	
	public void setRawTime(String rawTime) {
		if ( rawTime.indexOf(":") > -1) {
			String[] parts = rawTime.split(":");
			time = (Integer.parseInt(parts[0])*60) + (Integer.parseInt(parts[1]));
		} 
	}
	
	
	
	/**
	 * @return Returns the digital.
	 */
	public String getDigital() {
		return digital;
	}
	/**
	 * @param digital The digital to set.
	 */
	public void setDigital(String digital) {
		this.digital = digital;
	}
	/**
	 * @return Returns the year.
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year The year to set.
	 */
	public void setYear(String year) {
		if ( year != null)
			try {
				this.year = Integer.parseInt(year);
			} catch ( Exception ignore) {
				this.year=-1;
			}
		else
			this.year = -1;
	}
	public String toString() {
		return "Artist='"+artist+"', Title='"+title+"', songId="+songId;
	}
	
	public boolean getStereo() {
		return ( (quality & 1) == 1);
	}
	
	public void setStereo(boolean value) {
		quality &= ~(1<<0);					// Bit löschen
		quality |= (value?1:0)<<0;		// ggf. Bit setzen
	}

	public boolean getNoisefree() {
		return ( (quality & 2) == 2);
	}
	
	public void setNoisefree(boolean value) {
		quality &= ~(1<<1);					// Bit löschen
		quality |= (value?1:0)<<1;		// ggf. Bit setzen
	}
	
	public boolean getComplete() {
		return ( (quality & 4) == 4);
	}
	
	public void setComplete(boolean value) {
		quality &= ~(1<<2);					// Bit löschen
		quality |= (value?1:0)<<2;		// ggf. Bit setzen
	}
	
	public boolean getMaxi() {
		return ( (special & 1) == 1);
	}
	
	public void setMaxi(boolean value) {
		special &= ~(1<<0);					// Bit löschen
		special |= (value?1:0)<<0;		// ggf. Bit setzen
	}
	
	public boolean getLive() {
		return ( (special & 2) == 2);
	}
	
	public void setLive(boolean value) {
		special &= ~(1<<1);					// Bit löschen
		special |= (value?1:0)<<1;		// ggf. Bit setzen
	}
	
	public boolean getRemix() {
		return ( (special & 4) == 4);
	}
	
	public void setRemix(boolean value) {
		special &= ~(1<<2);					// Bit löschen
		special |= (value?1:0)<<2;		// ggf. Bit setzen
	}
	
	public boolean getVideo() {
		return ( (special & 8) == 8);
	}
	
	public void setVideo(boolean value) {
		special &= ~(1<<3);					// Bit löschen
		special |= (value?1:0)<<3;		// ggf. Bit setzen
	}
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Returns the longplay.
	 */
	public String getLongplay() {
		return longplay;
	}
	/**
	 * @param longplay The longplay to set.
	 */
	public void setLongplay(String longplay) {
		this.longplay = longplay;
	}
	/**
	 * @return Returns the quality.
	 */
	public int getQuality() {
		return quality;
	}
	/**
	 * @param quality The quality to set.
	 */
	public void setQuality(int quality) {
		this.quality = quality;
	}
	/**
	 * @return Returns the special.
	 */
	public int getSpecial() {
		return special;
	}
	/**
	 * @param special The special to set.
	 */
	public void setSpecial(int special) {
		this.special = special;
	}
	/**
	 * @param year The year to set.
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	public String getSpecialVerbose() {
		String ret="";
		if ( getMaxi())
			ret += "Maxi, ";
		if ( getLive())
			ret += "Live, ";
		if ( getRemix())
			ret += "Remix, ";
		if ( getVideo())
			ret += "Video";
		
		if ( ret.endsWith(", "))
			ret = ret.substring(0,ret.length()-2);
		
		return ret;
	}
	
	public String getQualityVerbose() {
		String ret="";
		if ( getStereo())
			ret += "Stereo, ";
		if ( getNoisefree())
			ret += "Noisefree, ";
		if ( getComplete())
			ret += "Complete";
		
		if ( ret.endsWith(", "))
			ret = ret.substring(0,ret.length()-2);
		
		return ret;
	}
	
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		super.reset(arg0, arg1);
		special=0;
		quality=0;
	}
	
	
	
}
