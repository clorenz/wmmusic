/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/recording/Recording.java,v $
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
package de.christophlorenz.wmmusic.recording;

import java.text.DecimalFormat;

public class Recording {
	
	private long id=-1;
	private long artist_id=-1;
	private long song_id=-1;
	private String artist=null;
	private String title=null;
	private long medium_id=-1;
	private int medium_type=-1;
	private String medium_code=null;
	private String side=null;
	private int track=-1;
	private String counter=null;
	private int time;
	private int year=-1;
	private String longplay=null;
	private int quality=7;
	private int special=0;
	private String remarks=null;
	private String digital=null;
	private String mediumArtist=null;
	private String mediumTitle=null;

	/**
	 * @return Returns the mediumArtist.
	 */
	public String getMediumArtist() {
		return mediumArtist;
	}

	/**
	 * @param mediumArtist The mediumArtist to set.
	 */
	public void setMediumArtist(String mediumArtist) {
		this.mediumArtist = mediumArtist;
	}

	/**
	 * @return Returns the mediumTitle.
	 */
	public String getMediumTitle() {
		return mediumTitle;
	}

	/**
	 * @param mediumTitle The mediumTitle to set.
	 */
	public void setMediumTitle(String mediumTitle) {
		this.mediumTitle = mediumTitle;
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

	public Recording(long id) {
		this.id=id;
	}

	public Recording() {}

	/**
	 * @return Returns the artist_id.
	 */
	public long getArtistId() {
		return artist_id;
	}

	/**
	 * @param artist_id The artist_id to set.
	 */
	public void setArtistId(long artist_id) {
		this.artist_id = artist_id;
	}

	/**
	 * @return Returns the artist_name.
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * @param artist_name The artist_name to set.
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	/**
	 * @return Returns the id.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(long id) {
		this.id = id;
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
	 * @return Returns the song_id.
	 */
	public long getSongId() {
		return song_id;
	}

	/**
	 * @param song_id The song_id to set.
	 */
	public void setSongId(long song_id) {
		this.song_id = song_id;
	}

	/**
	 * @return Returns the song_title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param song_title The song_title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the mediumTypeAbbrev.
	 */
	public String getMediumTypeAbbrev() {
		switch (medium_type) {
		case 0: return "C";
		case 1: return "V";
		case 2: return "M";
		case 3: return "D";
		case 4: return "F";
		case 5: return "L";
		case 6: return "S";
		case 7: return "D";
		default: return "";
		}
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
	public void setYear(int year) {
		this.year = year;
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
	
	public String getRawTime() {
		if ( time > 0) {
			DecimalFormat df = new DecimalFormat("00");
			return df.format(Math.floor(time/60))+":"+df.format(time%60);
		} else {
			return "";
		}
	}
	
	public boolean getMaxi() {
		return ( (special & 1) == 1);
	}
	
	public boolean getLive() {
		return ( (special & 2) == 2);
	}
	
	public boolean getRemix() {
		return ( (special & 4) == 4);
	}
	
	public boolean getVideo() {
		return ( (special & 8) == 8);
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
	
	public boolean getStereo() {
		return ( (quality & 1) == 1);
	}
	
	public boolean getNoisefree() {
		return ( (quality & 2) == 2);
	}
	
	public boolean getComplete() {
		return ( (quality & 4) == 4);
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

	
}
