/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/artist/bean/EditArtistBean.java,v $
$Author: clorenz $
$Date: 2008-07-20 11:13:23 $
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
package de.christophlorenz.wmmusic.artist.bean;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

public class EditArtistBean extends ActionForm {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3314547100708969763L;
	private long id=-1;
	private String name=null;
	private String print=null;
	private String birthday=null;
	private String country=null;
	private String location=null;
	private String url=null;
	private String remarks=null;
	private static final Logger log;
	
	static {
		log = Logger.getLogger(EditArtistBean.class);
	}
	
	/**
	 * @return Returns the aname.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param aname The aname to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the birthday.
	 */
	public String getBirthday() {
		return birthday;
	}
	/**
	 * @param birthday The birthday to set.
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	
	/**
	 * @return Returns the country.
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country The country to set.
	 */
	public void setCountry(String country) {
		this.country = country;
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
	 * @return Returns the location.
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location The location to set.
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return Returns the print.
	 */
	public String getPrint() {
		return print;
	}
	/**
	 * @param print The print to set.
	 */
	public void setPrint(String print) {
		this.print = print;
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
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String toString() {
		return "ID="+id+", name="+name+", print="+print+
			", birthday="+birthday+", country="+country+
			", location="+location+", url="+url+
			", remarks="+remarks;
	}
	

}
