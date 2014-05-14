/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/statistics/bean/StatisticsBean.java,v $
$Author: clorenz $
$Date: 2010-09-04 19:31:02 $
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
package de.christophlorenz.wmmusic.statistics.bean;

import java.util.HashMap;
import java.util.Map;

import de.christophlorenz.wmmusic.medium.IMediumTypes;

public class StatisticsBean {
	
	private long songs=0;
	private long artists=0;
	private long perfectSongs=0;
	private long originalSongs=0;
	
	private Map<Integer,MediaStatisticsBean>media = new HashMap<Integer,MediaStatisticsBean>();
	private Map<Integer,Long>qualities = new HashMap<Integer,Long>();
	/**
	 * @return the artists
	 */
	public long getArtists() {
		return artists;
	}
	/**
	 * @param artists the artists to set
	 */
	public void setArtists(long artists) {
		this.artists = artists;
	}
	/**
	 * @return the qualities
	 */
	public Map<Integer, Long> getQualities() {
		return qualities;
	}
	/**
	 * @param qualities the qualities to set
	 */
	public void setQualities(Map<Integer, Long> qualities) {
		this.qualities = qualities;
	}
	
	public void setQuality(Integer type, long amount) {
		qualities.put(type, amount);
	}
	
	/**
	 * @return the songs
	 */
	public long getSongs() {
		return songs;
	}
	/**
	 * @param songs the songs to set
	 */
	public void setSongs(long songs) {
		this.songs = songs;
	}
	public void setMediumAmount(Integer type, long amount) {
		MediaStatisticsBean m = media.get(type);
		if ( m == null) {
			m = new MediaStatisticsBean();
			m.setName(IMediumTypes.table[type]);
		}
		
		m.setAmount(amount);
		
		media.put(type,m);		
	}
	
	
	public void setMediumPrices(Integer type, double sumPrice, double avgPrice) {
		MediaStatisticsBean m = media.get(type);
		if ( m == null) {
			m = new MediaStatisticsBean();
			m.setName(IMediumTypes.table[type]);
		}
		
		m.setAvgPrice(avgPrice);
		m.setSumPrice(sumPrice);
		
		media.put(type, m);	
	}
	
	
	/**
	 * @return the media
	 */
	public Map<Integer, MediaStatisticsBean> getMedia() {
		return media;
	}
	
	public void setPerfectSongs(long perfectSongs) {
		this.perfectSongs = perfectSongs;
	}
	/**
	 * @return the perfectSongs
	 */
	public long getPerfectSongs() {
		return perfectSongs;
	}
	
	public double getPerfectSongsPercent() {
		if ( songs>0)
			return 100*((double)perfectSongs / (double)songs);
		else
			return 0;
	}
	/**
	 * @return the originalSongs
	 */
	public long getOriginalSongs() {
		return originalSongs;
	}
	/**
	 * @param originalSongs the originalSongs to set
	 */
	public void setOriginalSongs(long originalSongs) {
		this.originalSongs = originalSongs;
	}
	
	public double getOriginalSongsPercent() {
		if ( songs>0)
			return 100*((double)originalSongs / (double)songs);
		else
			return 0;
	}
	
	

}
