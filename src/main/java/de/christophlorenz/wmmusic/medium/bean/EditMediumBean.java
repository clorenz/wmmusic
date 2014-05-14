/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/medium/bean/EditMediumBean.java,v $
$Author: clorenz $
$Date: 2008-09-24 19:56:11 $
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
package de.christophlorenz.wmmusic.medium.bean;

import org.apache.struts.action.ActionForm;

public class EditMediumBean extends ActionForm {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 2085400871775028403L;
	private long id=-1;
	private int type=-1;
	private String code=null;
	private long artistId=-1;
	private String artist=null;
	private String title=null;
	private String label=null;
	private String ordercode=null;
	private int year=-1;
	private int size=-1;
	private String manufacturer=null;
	private String system=null;
	private String recBeginDate=null;
	private String recBeginB=null;
	private String recEndDate=null;
	private String burningDate=null;
	private long discid=-1;
	private String trackOffsets=null;
	private String category=null;
	private String id3Genre=null;
	private String digital=null;
	private int audio=-1;
	private int rewritable=-1;
	private String magic=null;
	private String filesType=null;
	private String buyDate=null;
	private double buyPrice=-1;
	private String remarks=null;
	
	private boolean showDigital=false;		// show field for "digital"
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
	 * @return Returns the audio.
	 */
	public int getAudio() {
		return audio;
	}
	/**
	 * @param audio The audio to set.
	 */
	public void setAudio(int audio) {
		this.audio = audio;
	}
	/**
	 * @return Returns the burningDate.
	 */
	public String getBurningDate() {
		return burningDate;
	}
	/**
	 * @param burningDate The burningDate to set.
	 */
	public void setBurningDate(String burningDate) {
		this.burningDate = burningDate;
	}
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
	public double getBuyPrice() {
		return buyPrice;
	}
	/**
	 * @param buyPrice The buyPrice to set.
	 */
	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}
	/**
	 * @return Returns the category.
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category The category to set.
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * @return Returns the discid.
	 */
	public long getDiscid() {
		return discid;
	}
	/**
	 * @param discid The discid to set.
	 */
	public void setDiscid(long discid) {
		this.discid = discid;
	}
	/**
	 * @return Returns the filesType.
	 */
	public String getFilesType() {
		return filesType;
	}
	/**
	 * @param filesType The filesType to set.
	 */
	public void setFilesType(String filesType) {
		this.filesType = filesType;
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
	 * @return Returns the label.
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return Returns the magic.
	 */
	public String getMagic() {
		return magic;
	}
	/**
	 * @param magic The magic to set.
	 */
	public void setMagic(String magic) {
		this.magic = magic;
	}
	/**
	 * @return Returns the manufacturer.
	 */
	public String getManufacturer() {
		return manufacturer;
	}
	/**
	 * @param manufacturer The manufacturer to set.
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	/**
	 * @return Returns the ordercode.
	 */
	public String getOrdercode() {
		return ordercode;
	}
	/**
	 * @param ordercode The ordercode to set.
	 */
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	/**
	 * @return Returns the pYear.
	 */
	public int getYear() {
		return year;
	}
	
	/**
	 * @param year The pYear to set.
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	
	/**
	 * @return Returns the recBeginB.
	 */
	public String getRecBeginB() {
		return recBeginB;
	}
	/**
	 * @param recBeginB The recBeginB to set.
	 */
	public void setRecBeginB(String recBeginB) {
		this.recBeginB = recBeginB;
	}
	/**
	 * @return Returns the recBeginDate.
	 */
	public String getRecBeginDate() {
		return recBeginDate;
	}
	/**
	 * @param recBeginDate The recBeginDate to set.
	 */
	public void setRecBeginDate(String recBeginDate) {
		this.recBeginDate = recBeginDate;
	}
	/**
	 * @return Returns the recEndDate.
	 */
	public String getRecEndDate() {
		return recEndDate;
	}
	/**
	 * @param recEndDate The recEndDate to set.
	 */
	public void setRecEndDate(String recEndDate) {
		this.recEndDate = recEndDate;
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
	 * @return Returns the rewritable.
	 */
	public int getRewritable() {
		return rewritable;
	}
	/**
	 * @param rewritable The rewritable to set.
	 */
	public void setRewritable(int rewritable) {
		this.rewritable = rewritable;
	}
	/**
	 * @return Returns the size.
	 */
	public int getSize() {
		return size;
	}
	/**
	 * @param size The size to set.
	 */
	public void setSize(int size) {
		this.size = size;
	}
	/**
	 * @return Returns the system.
	 */
	public String getSystem() {
		return system;
	}
	/**
	 * @param system The system to set.
	 */
	public void setSystem(String system) {
		this.system = system;
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
	 * @return Returns the trackOffsets.
	 */
	public String getTrackOffsets() {
		return trackOffsets;
	}
	/**
	 * @param trackOffsets The trackOffsets to set.
	 */
	public void setTrackOffsets(String trackOffsets) {
		this.trackOffsets = trackOffsets;
	}
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
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
	
	public String toString() {
		return "year="+year;
	}
	
	/**
	 * @return the showDigital
	 */
	public boolean getShowDigital() {
		return showDigital;
	}
	/**
	 * @param showDigital the showDigital to set
	 */
	public void setShowDigital(boolean showDigital) {
		this.showDigital = showDigital;
	}
	
	
}
