/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/song/Song.java,v $
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

public class Song {
	
	private long id=-1;
	private long artist_id=-1;
	private String artist=null;
	private String title=null;
	private String release=null;
	private int year=-1;
	private String authors=null;
	private String dance=null;
	private String id3Genre=null;
	private String remarks=null;
	private boolean bestQuality=false;
	
	/**
	 * @return Returns the bestQuality.
	 */
	public boolean isBestQuality() {
		return bestQuality;
	}

	/**
	 * @param bestQuality The bestQuality to set.
	 */
	public void setBestQuality(boolean bestQuality) {
		this.bestQuality = bestQuality;
	}

	public Song() {};
	
	public Song(long id) {
		this.id=id;
	}
	
	
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
	 * @return Returns the artist_id.
	 */
	public long getArtist_id() {
		return artist_id;
	}
	/**
	 * @param artist_id The artist_id to set.
	 */
	public void setArtist_id(long artist_id) {
		this.artist_id = artist_id;
	}
	/**
	 * @return Returns the authors.
	 */
	public String getAuthors() {
		return authors;
	}
	/**
	 * @param authors The authors to set.
	 */
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	/**
	 * @return Returns the dance.
	 */
	public String getDance() {
		return dance;
	}
	/**
	 * @param dance The dance to set.
	 */
	public void setDance(String dance) {
		this.dance = dance;
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
	 * @return Returns the id3Genre.
	 */
	public String getId3Genre() {
		return id3Genre;
	}
	/**
	 * @param id3Genre The id3Genre to set.
	 */
	public void setId3Genre(String id3Genre) {
		this.id3Genre = id3Genre;
	}
	/**
	 * @return Returns the release.
	 */
	public String getRelease() {
		return release;
	}
	/**
	 * @param release The release to set.
	 */
	public void setRelease(String release) {
		this.release = release;
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
	
	public String toString() {
		return "id="+id+"\n"+
			"artist="+artist+"\n"+
			"artistId="+artist_id+"\n"+
			"title="+title+"\n"+
			"release="+release+"\n"+
			"year="+year+"\n"+
			"authors="+authors+"\n"+
			"dance="+dance+"\n"+
			"id3Genre="+id3Genre+"\n"+
			"remarks="+remarks;
	}

}
