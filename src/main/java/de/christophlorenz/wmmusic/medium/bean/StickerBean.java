/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/medium/bean/StickerBean.java,v $
$Author: clorenz $
$Date: 2010-09-04 19:31:02 $
$Revision: 1.3 $

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
package de.christophlorenz.wmmusic.medium.bean;

public class StickerBean {

	private String code;
	private String buyDate;
	private String buyPrice;
	private String artist;
	private String title;
	
	/**
	 * @return Returns the buyDate.
	 */
	public String getBuyDate() {
		return buyDate;
	}
	/**
	 * @param buyDate The buyDate to set.
	 */
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
	/**
	 * @return Returns the buyPrice.
	 */
	public String getBuyPrice() {
		return buyPrice;
	}
	/**
	 * @param buyPrice The buyPrice to set.
	 */
	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}
	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code.replaceAll(" ", "&nbsp;");
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
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
	
	public boolean getShowDetails() {
		return ( (buyDate!=null && buyDate.length()>0) || (buyPrice!=null && buyPrice.length()>0) );
	}
	
	
}
