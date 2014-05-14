/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/db/IWmmusicDatabase.java,v $
$Author: clorenz $
$Date: 2008-09-24 19:56:56 $
$Revision: 1.2 $

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
package de.christophlorenz.wmmusic.db;

import java.util.List;

import de.christophlorenz.wmmusic.artist.Artist;
import de.christophlorenz.wmmusic.medium.Medium;
import de.christophlorenz.wmmusic.recording.Recording;
import de.christophlorenz.wmmusic.song.Song;
import de.christophlorenz.wmmusic.statistics.bean.StatisticsBean;

public interface IWmmusicDatabase {
	
	public List selectArtist(String name, boolean exact) throws DAOException;

	public List selectArtist(long id) throws DAOException;

	public boolean artistExistsById(long id) throws DAOException;

	public boolean artistExistsByName(String name) throws DAOException;

	public boolean updateArtist(Artist artist) throws DAOException;
	
	public long createArtist(Artist artist) throws DAOException;

	public long countSongArtistReferences(long id) throws DAOException;

	public long countMediumArtistReferences(long id) throws DAOException;

	public boolean removeArtist(long id) throws DAOException;
	
	public Artist getArtistByName(String name) throws DAOException;

	public List selectMediumByCode(int type, String code) throws DAOException;

	public List selectMedium(int type, String artist, String title) throws DAOException;
	
	public List selectMedium(long id) throws DAOException;

	public boolean mediumExistsById(long id) throws DAOException;

	public boolean updateMedium(Medium m) throws DAOException;

	public long createMedium(Medium m) throws DAOException;

	public List selectSongsFromMedium(long mediumId) throws DAOException;

	public List selectRecordingsFromMedium(long mediumId) throws DAOException;

	public List selectSongs(String artist, String title, boolean exact) throws DAOException;

	public String getMaxSide(long mediumId) throws DAOException;

	public int getMaxTrack(long mediumId) throws DAOException;
	
	public int getMaxTrackOfSide(long mediumId, String side) throws DAOException;

	public boolean recordingExistsById(long id) throws DAOException;

	public boolean updateRecording(Recording rec) throws DAOException;

	public long createRecording(Recording rec) throws DAOException;

	public long createSong(Song s) throws DAOException;

	public void tagMedium(String username, long mediumId, String action) throws DAOException;

	public List getTaggedMediaIds(String username, String action) throws DAOException;

	public void deleteMediumTags(String username, String action) throws DAOException;

	public String getMaxMediumCode(int mediumType, String artist) throws DAOException;

	public String getMinMediumCode(String artist) throws DAOException;

	public void insertSongAndRecording(Song s, Recording r) throws DAOException;

	public Recording getRecording(long recordingId) throws DAOException;

	public Song getSong(long songId) throws DAOException;

	public boolean updateSong(Song s) throws DAOException;

	public List selectRecordingsForSong(long songId) throws DAOException;

	public Recording getBestRecordingForSong(long songId) throws DAOException;
	
	public StatisticsBean calculateStatistics() throws DAOException;
	

}
