/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/statistics/bean/MediaStatisticsBean.java,v $
$Author: clorenz $
$Date: 2008-09-23 19:54:52 $
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
package de.christophlorenz.wmmusic.statistics.bean;

public class MediaStatisticsBean {
	
	private String name;
	private long amount;
	private double sumPrice;
	private double avgPrice;
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the amount
	 */
	public long getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(long amount) {
		this.amount = amount;
	}
	/**
	 * @return the avgPrice
	 */
	public double getAvgPrice() {
		return avgPrice;
	}
	/**
	 * @param avgPrice the avgPrice to set
	 */
	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
	}
	/**
	 * @return the sumPrice
	 */
	public double getSumPrice() {
		return sumPrice;
	}
	/**
	 * @param sumPrice the sumPrice to set
	 */
	public void setSumPrice(double sumPrice) {
		this.sumPrice = sumPrice;
	}
	
	// TODO maybe a year/amount hash?
	
	

}
