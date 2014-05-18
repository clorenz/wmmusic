/* ------------------------------------------------------------------------- *
$Source: /usr/local/cvs/wmmusic/src/de/christophlorenz/wmmusic/db/WmmusicDatabase.java,v $
$Author: clorenz $
$Date: 2010-09-04 19:31:02 $
$Revision: 1.4 $

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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import de.christophlorenz.wmmusic.artist.Artist;
import de.christophlorenz.wmmusic.medium.Medium;
import de.christophlorenz.wmmusic.recording.Recording;
import de.christophlorenz.wmmusic.song.Song;
import de.christophlorenz.wmmusic.statistics.bean.StatisticsBean;

public class WmmusicDatabase implements IWmmusicDatabase {
	
	private static final Logger log;
	private DataSource ds;
	
	private static final String SELECT_ARTIST_NAME_EXACT="SELECT * FROM artist WHERE name = ?";
	private static final String SELECT_ARTIST_NAME_FUZZY="SELECT * FROM artist WHERE name ~* ?";
	private static final String SELECT_ARTIST_ID="SELECT * FROM artist WHERE id = ?";
	private static final String CHECK_ARTIST_ID="SELECT id FROM artist WHERE id = ?";
	private static final String CHECK_ARTIST_NAME="SELECT name FROM artist WHERE name = ?";
	private static final String UPDATE_ARTIST="UPDATE artist SET name=?,print=?,birthday=?,country=?,location=?,url=?,remarks=? where id=?";
 	private static final String INSERT_ARTIST="INSERT INTO artist (name,print,birthday,country,location,url,remarks,timestamp) VALUES(?,?,?,?,?,?,?,now())";
	private static final String COUNT_SONG_ARTIST_REFERENCES="SELECT count(*) FROM song WHERE artist_id=?";
	private static final String COUNT_SONG_MEDIUM_REFERENCES="SELECT count(*) FROM medium WHERE artist_id=?";
 	private static final String DELETE_ARTIST = "DELETE FROM artist WHERE id=?";
 	
 	private static final String SELECT_MEDIUM_BY_CODE="SELECT * FROM medium WHERE type = ? AND code = ?";
 	private static final String SELECT_MEDIUM_BY_ARTIST_TITLE_EXACT="SELECT m.*,a.name as artist_name FROM medium m, artist a WHERE m.type = ? AND a.name = ? AND m.title = ? AND a.id=m.artist_id";
 	private static final String SELECT_ALL_MEDIUM_OF_TYPE="SELECT m.*,a.name as artist_name FROM medium m, artist a WHERE m.type = ? AND a.id=m.artist_id";
 	private static final String SELECT_MEDIUM_BY_ARTIST_TITLE_FUZZY="SELECT m.*,a.name as artist_name FROM medium m, artist a WHERE m.type = ? AND m.artist_id in (SELECT id FROM artist WHERE name ~* ? ) AND m.title ~* ? AND a.id=m.artist_id order by regexp_replace(m.code,'[^0-9]','','g')::int";
	private static final String SELECT_MEDIUM_ID="SELECT m.*,a.name as artist_name FROM medium m, artist a WHERE m.id = ? AND a.id=m.artist_id";
	private static final String CHECK_MEDIUM_ID="SELECT id FROM medium WHERE id = ?";
	private static final String UPDATE_MEDIUM="UPDATE medium SET type=?, code=?,artist_id=?,title=?,label=?,ordercode=?,p_year=?,size=?,manufacturer=?,system=?,rec_begin_date=?,rec_begin_b=?,rec_end_date=?,burning_date=?,discid=?,track_offsets=?,category=?,id3_genre=?,digital=?,audio=?,rewritable=?,magic=?,files_type=?,buy_date=?,buy_price=?,remarks=? WHERE id = ?";
	private static final String INSERT_MEDIUM="INSERT INTO medium (type,code,artist_id,title,label,ordercode,p_year,size,manufacturer,system,rec_begin_date,rec_begin_b,rec_end_date,burning_date,discid,track_offsets,category,id3_genre,digital,audio,rewritable,magic,files_type,buy_date,buy_price,remarks,timestamp) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
	
	private static final String SELECT_SONGS_FROM_MEDIUM = "SELECT r.id as recording_id, s.*,a.name as artist_name from recording r, song s, artist a where r.medium_id=? and r.song_id=s.id and s.artist_id=a.id ORDER BY a.name,s.title";
	private static final String SELECT_SONGS_EXACT = "SELECT s.*,a.name as artist_name FROM song s, artist a WHERE a.name=? AND s.title=? AND s.artist_id=a.id ORDER BY a.name,s.title";	
	private static final String SELECT_SONGS_FUZZY = "SELECT s.*,a.name as artist_name FROM song s, artist a WHERE a.name ~* ? AND s.title ~* ? and s.artist_id=a.id ORDER BY a.name,s.title";
	private static final String SELECT_SONG_BY_ID = "SELECT s.*,a.name as artist_name FROM song s, artist a WHERE s.id=? AND s.artist_id=a.id";
	private static final String INSERT_SONG="INSERT INTO song (artist_id,title,release,year,authors,dance,id3genre,remarks,timestamp) VALUES (?,?,?,?,?,?,?,?,now())";
	private static final String UPDATE_SONG="UPDATE song SET artist_id=?,title=?,release=?,year=?,authors=?,dance=?,id3genre=?,remarks=?,timestamp=now() WHERE id=?";
	
	private static final String SELECT_RECORDINGS_FROM_MEDIUM="SELECT a.id as artist_id, a.name as artist_name, s.id as song_id, s.title as song_title, s.authors as song_authors, m.type as medium_type, m.code as medium_code, r.* from recording r, song s, artist a, medium m where r.medium_id=? and r.song_id=s.id and s.artist_id=a.id and r.medium_id=m.id ORDER BY r.side,r.track,r.counter";
	private static final String SELECT_RECORDINGS_FOR_SONG="SELECT a.id as artist_id, a.name as artist_name, s.id as song_id, s.title as song_title, m.type as medium_type, m.code as medium_code, r.* from recording r, song s, artist a, medium m where s.id=? and r.song_id=s.id and s.artist_id=a.id and r.medium_id=m.id ORDER BY r.side,r.track,r.counter";	
	private static final String SELECT_MAX_SIDE_FROM_MEDIUM="SELECT max(side) FROM recording WHERE medium_id=?";
	private static final String SELECT_MAX_TRACK_FROM_MEDIUM="SELECT max(track) FROM recording WHERE medium_id=?";
	private static final String SELECT_MAX_TRACK_FROM_MEDIUM_AND_SIDE="SELECT max(track) FROM recording WHERE medium_id=? and side=?";
	private static final String CHECK_RECORDING_ID="SELECT id FROM recording WHERE id = ?";	
	private static final String UPDATE_RECORDING="UPDATE recording SET song_id=?,medium_id=?,side=?,track=?,counter=?,time=?,recording_year=?,longplay=?,quality=?,remarks=?,special=?,digital=? WHERE id=?";
	private static final String INSERT_RECORDING="INSERT INTO recording (song_id,medium_id,side,track,counter,time,recording_year,longplay,quality,remarks,special,digital,timestamp) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,now())";
	private static final String SELECT_RECORDING_BY_MEDIUM_ID_SIDE_TRACK_COUNTER_SONG_ID="SELECT * FROM recording WHERE medium_id=? AND side=? AND track=? AND counter=? AND song_id=?";
	private static final String SELECT_CODES_FROM_MEDIUM_BY_TYPE_ARTIST="SELECT code FROM medium WHERE type=? AND artist_id=(SELECT id FROM artist WHERE name=?)";
	private static final String SELECT_MIN_CODE_FROM_MEDIUM_BY_ARTIST="SELECT min(code) FROM medium WHERE artist_id=(SELECT id FROM artist WHERE name=?)";
	private static final String SELECT_RECORDING_BY_ID = "SELECT a.id as artist_id, a.name as artist_name, s.id as song_id, s.title as song_title, m.type as medium_type, m.code as medium_code, r.* FROM recording r, song s, artist a, medium m WHERE r.id=? AND r.song_id=s.id and s.artist_id=a.id and r.medium_id=m.id";
	private static final String SELECT_BEST_RECORDING_FOR_SONG="select r.*,m.type as medium_type, m.code as medium_code,a.id as artist_id, a.name as artist_name, s.title as song_title from recording r, medium m, artist a, song s where song_id=? and s.id=song_id and a.id=s.artist_id and m.id=r.medium_id order by r.quality desc, m.type desc limit 1";
	
	private static final String CHECK_TAG_MEDIUM="SELECT * FROM tag_medium WHERE username=? and mediumid=? and action=?";
	private static final String INSERT_TAG_MEDIUM="INSERT INTO tag_medium (username,mediumid,action) VALUES(?,?,?)";
	private static final String SELECT_TAGGED_MEDIA="SELECT mediumid FROM tag_medium WHERE username=? AND action=?";
	private static final String DELETE_TAGGED_MEDIA="DELETE FROM tag_medium WHERE username=? AND action=?";
	
	private static final String STAT_SONGS="SELECT count(*) FROM song";
	private static final String STAT_ARTISTS="SELECT count(*) FROM artist";
	private static final String STAT_MEDIUM="SELECT type,count(*) FROM medium GROUP BY type ORDER BY type";
	private static final String STAT_QUALITY="SELECT quality,count(*) FROM recording GROUP BY quality ORDER BY quality";
	private static final String STAT_MEDIUM_PRICES="SELECT type,sum(buy_price),avg(buy_price) FROM medium WHERE buy_price>0 GROUP BY type ORDER BY type";
	private static final String STAT_PERFECT_SONGS="SELECT COUNT(DISTINCT(song_id)) FROM recording WHERE quality=7";
	private static final String STAT_SONGS_ON_ORIGINAL_MEDIA="SELECT COUNT(DISTINCT(song_id)) FROM recording WHERE medium_id IN (SELECT id FROM medium WHERE type>=5)";
	
	static {
		log = Logger.getLogger(WmmusicDatabase.class);
	}
	
	public List selectArtist(String name, boolean exact) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList ret = new ArrayList();
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_ARTIST_NAME_EXACT);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			while ( rs.next()) {
				Artist a = new Artist(rs.getLong("id"));
				a.setName(rs.getString("name"));
				a.setPrint(rs.getString("print"));
				a.setBirthday(rs.getString("birthday"));
				a.setCountry(rs.getString("country"));
				a.setLocation(rs.getString("location"));
				a.setUrl(rs.getString("url"));
				a.setRemarks(rs.getString("remarks"));
				ret.add(a);
			}
			
			if ( ret.isEmpty() && !exact ) {
				rs.close();
				stmt.close();
				stmt = con.prepareStatement(SELECT_ARTIST_NAME_FUZZY);
				stmt.setString(1, name);
				rs = stmt.executeQuery();
				while ( rs.next()) {
					Artist a = new Artist(rs.getLong("id"));
					a.setName(rs.getString("name"));
					a.setPrint(rs.getString("print"));
					a.setBirthday(rs.getString("birthday"));
					a.setCountry(rs.getString("country"));
					a.setLocation(rs.getString("location"));
					a.setUrl(rs.getString("url"));
					a.setRemarks(rs.getString("remarks"));
					ret.add(a);
				}
			}
			
			return ret;
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
	}
	
	
	public Artist getArtistByName(String name) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Artist a = null;
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_ARTIST_NAME_EXACT);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			if ( rs.next()) {
				a = new Artist(rs.getLong("id"));
				a.setName(rs.getString("name"));
				a.setPrint(rs.getString("print"));
				a.setBirthday(rs.getString("birthday"));
				a.setCountry(rs.getString("country"));
				a.setLocation(rs.getString("location"));
				a.setUrl(rs.getString("url"));
				a.setRemarks(rs.getString("remarks"));
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return a;
	}

	public void setDataSource(DataSource dataSource) {
		this.ds = dataSource;		
	}

	public List selectArtist(long id) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList ret = new ArrayList();
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_ARTIST_ID);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();
			while ( rs.next()) {
				Artist a = new Artist(rs.getLong("id"));
				a.setName(rs.getString("name"));
				a.setPrint(rs.getString("print"));
				a.setBirthday(rs.getString("birthday"));
				a.setCountry(rs.getString("country"));
				a.setLocation(rs.getString("location"));
				a.setUrl(rs.getString("url"));
				a.setRemarks(rs.getString("remarks"));
				ret.add(a);
			}
			return ret;
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
	}

	public boolean artistExistsById(long id) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(CHECK_ARTIST_ID);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();
			if ( rs.next()) {
				long aid = rs.getLong(1);
				return ( aid == id);
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return false;
	}

	public boolean artistExistsByName(String name) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(CHECK_ARTIST_NAME);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			if ( rs.next()) {
				String aname = rs.getString(1);
				return ( name!=null && name.equals(aname));
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return false;
	}

	public boolean updateArtist(Artist artist) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(UPDATE_ARTIST);
			stmt.setString(1, artist.getName());
			stmt.setString(2, artist.getPrint());
			if ( artist.getBirthday()!=null && artist.getBirthday().length()>0 )
				stmt.setString(3, artist.getBirthday());
			else
				stmt.setNull(3, Types.DATE);
			stmt.setString(4, artist.getCountry());
			stmt.setString(5, artist.getLocation());
			stmt.setString(6, artist.getUrl());
			stmt.setString(7, artist.getRemarks());
			stmt.setLong(8, artist.getId());
		
			int rowCount = stmt.executeUpdate();
			
			return ( rowCount == 1);
		} catch (SQLException e) {
			logError(e, UPDATE_ARTIST);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, null);
		}
	}
	
	
	
	public long createArtist(Artist artist) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(INSERT_ARTIST);
			stmt.setString(1, artist.getName());
			stmt.setString(2, artist.getPrint());
			stmt.setString(3, artist.getBirthday().length()>0?artist.getBirthday():null);
			stmt.setString(4, artist.getCountry());
			stmt.setString(5, artist.getLocation());
			stmt.setString(6, artist.getUrl());
			stmt.setString(7, artist.getRemarks());
		
			int rowCount = stmt.executeUpdate();
			
			if ( rowCount == 1 ) {
				// Insert was successful - retrieve ID
				stmt = con.prepareStatement(SELECT_ARTIST_NAME_EXACT);
				stmt.setString(1, artist.getName());
				rs = stmt.executeQuery();
				if ( rs.next()) {
					return rs.getLong(1);
				} else {
					return -1;
				}
			} else {
				return -1;
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
	}

	public long countSongArtistReferences(long id) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(COUNT_SONG_ARTIST_REFERENCES);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();
			if ( rs.next()) {
				long count = rs.getLong(1);
				return (count);
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return 0;
	}

	public long countMediumArtistReferences(long id) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(COUNT_SONG_MEDIUM_REFERENCES);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();
			if ( rs.next()) {
				long count = rs.getLong(1);
				return (count);
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return 0;
	}

	public boolean removeArtist(long id) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(DELETE_ARTIST);
			stmt.setLong(1, id);
		
			int rowCount = stmt.executeUpdate();
			
			return ( rowCount == 1);
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, null);
		}
	}

	public List selectMediumByCode(int type, String code) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList ret = new ArrayList();
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_MEDIUM_BY_CODE);
			stmt.setLong(1, type);
			stmt.setString(2, code);
			rs = stmt.executeQuery();
			while ( rs.next()) {
				Medium m = new Medium(rs.getLong("id"));
				m.setArtistId(rs.getLong("artist_id"));
				m.setAudio(rs.getBoolean("audio")?1:0);
				m.setBurningDate(rs.getString("burning_date"));
				try {
					m.setBuyDate(rs.getString("buy_date"));
				} catch ( Exception e) {
					m.setBuyDate(null);
				}
				m.setBuyPrice(rs.getDouble("buy_price"));
				m.setCategory(rs.getString("category"));
				m.setCode(rs.getString("code"));
				m.setDigital(rs.getString("digital"));
				m.setDiscid(rs.getLong("discid"));
				m.setFilesType(rs.getString("files_type"));
				m.setId3Genre(rs.getString("id3_genre"));
				m.setLabel(rs.getString("label"));
				m.setMagic(rs.getString("magic"));
				m.setManufacturer(rs.getString("manufacturer"));
				m.setOrdercode(rs.getString("ordercode"));
				m.setYear(rs.getInt("p_year"));
				m.setRecBeginB(rs.getString("rec_begin_b"));
				m.setRecEndDate(rs.getString("rec_end_date"));
				m.setRemarks(rs.getString("remarks"));
				m.setRewritable(rs.getInt("rewritable"));
				m.setSize(rs.getInt("size"));
				m.setSystem(rs.getString("system"));
				m.setTitle(rs.getString("title"));
				m.setTrackOffsets(rs.getString("track_offsets"));
				m.setType(rs.getInt("type"));
				
				ret.add(m);
			}
			return ret;
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
	}

	public List<Medium> selectMedium(int type, String artist, String title) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList<Medium> ret = new ArrayList<Medium>();
		
		try {
			con = ds.getConnection();
			
			if ( artist==null && title==null) { 
				stmt = con.prepareStatement(SELECT_ALL_MEDIUM_OF_TYPE);
				stmt.setLong(1, type);
			} else {
				stmt = con.prepareStatement(SELECT_MEDIUM_BY_ARTIST_TITLE_EXACT);
				stmt.setLong(1, type);
				stmt.setString(2, artist);
				stmt.setString(3, title);
			}
			rs = stmt.executeQuery();
			while ( rs.next()) {
				Medium m = new Medium(rs.getLong("id"));
				m.setArtistId(rs.getLong("artist_id"));
				m.setArtist(rs.getString("artist_name"));
				m.setAudio(rs.getInt("audio"));
				m.setBurningDate(rs.getString("burning_date"));
				m.setBuyDate(rs.getString("buy_date"));
				m.setBuyPrice(rs.getDouble("buy_price"));
				m.setCategory(rs.getString("category"));
				m.setCode(rs.getString("code"));
				m.setDigital(rs.getString("digital"));
				m.setDiscid(rs.getLong("discid"));
				m.setFilesType(rs.getString("files_type"));
				m.setId3Genre(rs.getString("id3_genre"));
				m.setLabel(rs.getString("label"));
				m.setMagic(rs.getString("magic"));
				m.setManufacturer(rs.getString("manufacturer"));
				m.setOrdercode(rs.getString("ordercode"));
				m.setYear(rs.getInt("p_year"));
				m.setRecBeginB(rs.getString("rec_begin_b"));
				m.setRecEndDate(rs.getString("rec_end_date"));
				m.setRemarks(rs.getString("remarks"));
				m.setRewritable(rs.getInt("rewritable"));
				m.setSize(rs.getInt("size"));
				m.setSystem(rs.getString("system"));
				m.setTitle(rs.getString("title"));
				m.setTrackOffsets(rs.getString("track_offsets"));
				m.setType(rs.getInt("type"));
				
				ret.add(m);
			}
			
			if ( ret.isEmpty()) {
				rs.close();
				stmt.close();
				stmt = con.prepareStatement(SELECT_MEDIUM_BY_ARTIST_TITLE_FUZZY);
				stmt.setLong(1, type);
				stmt.setString(2, artist);
				stmt.setString(3, title);
				rs = stmt.executeQuery();
				while ( rs.next()) {
					Medium m = new Medium(rs.getLong("id"));
					m.setArtistId(rs.getLong("artist_id"));
					m.setArtist(rs.getString("artist_name"));
					m.setAudio(rs.getBoolean("audio")?1:0);
					m.setBurningDate(rs.getString("burning_date"));
					try {
						m.setBuyDate(rs.getString("buy_date"));
					} catch ( Exception e) {
						m.setBuyDate(null);
					}
					m.setBuyPrice(rs.getDouble("buy_price"));
					m.setCategory(rs.getString("category"));
					m.setCode(rs.getString("code"));
					m.setDigital(rs.getString("digital"));
					m.setDiscid(rs.getLong("discid"));
					m.setFilesType(rs.getString("files_type"));
					m.setId3Genre(rs.getString("id3_genre"));
					m.setLabel(rs.getString("label"));
					m.setMagic(rs.getString("magic"));
					m.setManufacturer(rs.getString("manufacturer"));
					m.setOrdercode(rs.getString("ordercode"));
					m.setYear(rs.getInt("p_year"));
					m.setRecBeginB(rs.getString("rec_begin_b"));
					m.setRecEndDate(rs.getString("rec_end_date"));
					m.setRemarks(rs.getString("remarks"));
					m.setRewritable(rs.getInt("rewritable"));
					m.setSize(rs.getInt("size"));
					m.setSystem(rs.getString("system"));
					m.setTitle(rs.getString("title"));
					m.setTrackOffsets(rs.getString("track_offsets"));
					m.setType(rs.getInt("type"));
					
					ret.add(m);
				}
			}
			return ret;
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
	}

	public List selectMedium(long id) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList ret = new ArrayList();
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_MEDIUM_ID);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();
			while ( rs.next()) {
				Medium m = new Medium(rs.getLong("id"));
				m.setArtistId(rs.getLong("artist_id"));
				if (rs.wasNull())
					m.setArtistId(-1);				
				m.setArtist(rs.getString("artist_name"));
				m.setAudio(rs.getBoolean("audio")?1:0);
				if ( rs.wasNull())
					m.setAudio(-1);
				m.setBurningDate(rs.getString("burning_date"));
				try {
					m.setBuyDate(rs.getString("buy_date"));
				} catch (Exception e) {
					m.setBuyDate(null);
				}
				m.setBuyPrice(rs.getDouble("buy_price"));
				if ( rs.wasNull())
					m.setBuyPrice(-1);
				m.setCategory(rs.getString("category"));
				m.setCode(rs.getString("code"));
				m.setDigital(rs.getString("digital"));
				m.setDiscid(rs.getLong("discid"));
				if ( rs.wasNull())
					m.setDiscid(-1);
				m.setFilesType(rs.getString("files_type"));
				m.setId3Genre(rs.getString("id3_genre"));
				m.setLabel(rs.getString("label"));
				m.setMagic(rs.getString("magic"));
				m.setManufacturer(rs.getString("manufacturer"));
				m.setOrdercode(rs.getString("ordercode"));
				m.setYear(rs.getInt("p_year"));
				if ( rs.wasNull())
					m.setYear(-1);
				m.setRecBeginB(rs.getString("rec_begin_b"));
				m.setRecEndDate(rs.getString("rec_end_date"));
				m.setRemarks(rs.getString("remarks"));
				m.setRewritable(rs.getInt("rewritable"));
				if ( rs.wasNull())
					m.setRewritable(-1);
				m.setSize(rs.getInt("size"));
				if ( rs.wasNull())
					m.setSize(-1);
				m.setSystem(rs.getString("system"));
				m.setTitle(rs.getString("title"));
				m.setTrackOffsets(rs.getString("track_offsets"));
				m.setType(rs.getInt("type"));
				
				ret.add(m);
			}
			return ret;
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
	}

	public boolean mediumExistsById(long id) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(CHECK_MEDIUM_ID);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();
			if ( rs.next()) {
				long aid = rs.getLong(1);
				return ( aid == id);
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return false;
	}


	public boolean updateMedium(Medium m) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(UPDATE_MEDIUM);
			stmt.setInt(1, m.getType());
			stmt.setString(2, m.getCode());
			DBHelper.stmtNullOrLong(stmt,3, m.getArtistId());
			stmt.setString(4, m.getTitle());
			stmt.setString(5, m.getLabel());
			stmt.setString(6, m.getOrdercode());
			DBHelper.stmtNullOrInt(stmt,7, m.getYear());
			DBHelper.stmtNullOrInt(stmt,8, m.getSize());
			stmt.setString(9, m.getManufacturer());
			stmt.setString(10, m.getSystem());
			stmt.setString(11, m.getRecBeginDate());
			stmt.setString(12, m.getRecBeginB());
			stmt.setString(13, m.getRecEndDate());
			stmt.setString(14, m.getBurningDate());
			DBHelper.stmtNullOrLong(stmt,15, m.getDiscid());
			stmt.setString(16, m.getTrackOffsets());
			stmt.setString(17, m.getCategory());
			stmt.setString(18, m.getId3Genre());
			stmt.setString(19, m.getDigital());
			DBHelper.stmtNullOrInt(stmt,20,m.getAudio());
			DBHelper.stmtNullOrInt(stmt,21,m.getRewritable());
			stmt.setString(22, m.getMagic());
			stmt.setString(23, m.getFilesType());
			stmt.setString(24, m.getBuyDate());
			DBHelper.stmtNullOrDouble(stmt,25, m.getBuyPrice());
			stmt.setString(26, m.getRemarks());
			stmt.setLong(27, m.getId());
			
			int rowCount = stmt.executeUpdate();
			
			return ( rowCount == 1);
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, null);
		}
	}


	public long createMedium(Medium m) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(INSERT_MEDIUM);
			stmt.setInt(1, m.getType());
			stmt.setString(2, m.getCode());
			DBHelper.stmtNullOrLong( stmt, 3, m.getArtistId());
			stmt.setString(4, m.getTitle());
			stmt.setString(5, m.getLabel());
			stmt.setString(6, m.getOrdercode());
			DBHelper.stmtNullOrInt(stmt, 7, m.getYear());
			DBHelper.stmtNullOrInt(stmt, 8, m.getSize());
			stmt.setString(9, m.getManufacturer());
			stmt.setString(10, m.getSystem());
			DBHelper.stmtNullOrDate(stmt,11, m.getRecBeginDate());
			DBHelper.stmtNullOrDate(stmt,12, m.getRecBeginB());
			DBHelper.stmtNullOrDate(stmt,13, m.getRecEndDate());
			DBHelper.stmtNullOrDate(stmt,14, m.getBurningDate());
			DBHelper.stmtNullOrLong(stmt, 15, m.getDiscid());
			stmt.setString(16, m.getTrackOffsets());
			stmt.setString(17, m.getCategory());
			stmt.setString(18, m.getId3Genre());
			stmt.setString(19, m.getDigital());
			stmt.setBoolean(20,m.getAudio()>0?true:false);
			DBHelper.stmtNullOrBoolean(stmt,21,m.getRewritable());
			stmt.setString(22, m.getMagic());
			stmt.setString(23, m.getFilesType());
			DBHelper.stmtNullOrDate(stmt,24, m.getBuyDate());
			DBHelper.stmtNullOrDouble(stmt,25, m.getBuyPrice());
			stmt.setString(26, m.getRemarks());
		
			int rowCount = stmt.executeUpdate();
			
			if ( rowCount == 1 ) {
				// Insert was successful - retrieve ID
				ArrayList ret = (ArrayList) selectMediumByCode(m.getType(), m.getCode());
				if (( ret != null) && (ret.size() == 1))
					return ((Medium)ret.get(0)).getId();
				else
					return -1;
			} else {
				return -1;
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
	}


	public List selectSongsFromMedium(long mediumId) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList ret = new ArrayList();
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_SONGS_FROM_MEDIUM);
			stmt.setLong(1, mediumId);
			rs = stmt.executeQuery();
			while ( rs.next()) {
				Song s = new Song(rs.getLong("id"));
				s.setArtist(rs.getString("artist_name"));
				s.setArtist_id(rs.getLong("artist_id"));
				s.setAuthors(rs.getString("authors"));
				s.setDance(rs.getString("dance"));
				s.setId3Genre(rs.getString("id3genre"));
				s.setRelease(rs.getString("release"));
				s.setRemarks(rs.getString("remarks"));
				s.setTitle(rs.getString("title"));
				s.setYear(rs.getInt("year"));
				
				ret.add(s);
			}
			
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return ret;
	}


	public List selectRecordingsFromMedium(long mediumId) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList ret = new ArrayList();
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_RECORDINGS_FROM_MEDIUM);
			stmt.setLong(1, mediumId);
			rs = stmt.executeQuery();
			while ( rs.next()) {
				Recording r = new Recording(rs.getLong("id"));
				r.setArtist(rs.getString("artist_name"));
				r.setArtistId(rs.getLong("artist_id"));
				r.setAuthors(rs.getString("song_authors"));
				r.setCounter(rs.getString("counter"));;
				r.setDigital(rs.getString("digital"));
				r.setLongplay(rs.getString("longplay"));
				r.setMediumCode(rs.getString("medium_code"));
				r.setMediumId(rs.getLong("medium_id"));
				r.setMediumType(rs.getInt("medium_type"));
				r.setQuality(rs.getInt("quality"));
				r.setRemarks(rs.getString("remarks"));
				r.setSide(rs.getString("side"));
				r.setSongId(rs.getLong("song_id"));
				r.setSpecial(rs.getInt("special"));
				r.setTime(rs.getInt("time"));
				r.setTitle(rs.getString("song_title"));
				r.setTrack(rs.getInt("track"));
				r.setYear(rs.getInt("recording_year"));
				
				ret.add(r);
			}
			
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return ret;
	}


	public List selectSongs(String artist, String title, boolean exact) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList ret = new ArrayList();
			
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_SONGS_EXACT);
			stmt.setString(1, artist);
			stmt.setString(2, title);
			
			System.out.println(stmt);
			
			rs = stmt.executeQuery();
			while ( rs.next()) {
				Song s = new Song(rs.getLong("id"));
				s.setArtist(rs.getString("artist_name"));
				s.setArtist_id(rs.getLong("artist_id"));
				s.setAuthors(rs.getString("authors"));
				s.setDance(rs.getString("dance"));
				s.setId3Genre(rs.getString("id3genre"));
				s.setRelease(rs.getString("release"));
				s.setRemarks(rs.getString("remarks"));
				s.setTitle(rs.getString("title"));
				s.setYear(rs.getInt("year"));
				
				ret.add(s);
			}
				
			if ( ret.isEmpty() && !exact ) {
				rs.close();
				stmt.close();
				stmt = con.prepareStatement(SELECT_SONGS_FUZZY);
				stmt.setString(1, artist);
				stmt.setString(2, title);
				rs = stmt.executeQuery();
				while ( rs.next()) {
					Song s = new Song(rs.getLong("id"));
					s.setArtist(rs.getString("artist_name"));
					s.setArtist_id(rs.getLong("artist_id"));
					s.setAuthors(rs.getString("authors"));
					s.setDance(rs.getString("dance"));
					s.setId3Genre(rs.getString("id3genre"));
					s.setRelease(rs.getString("release"));
					s.setRemarks(rs.getString("remarks"));
					s.setTitle(rs.getString("title"));
					s.setYear(rs.getInt("year"));
					ret.add(s);
				}
			}
			return ret;
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
	}


	public String getMaxSide(long mediumId) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_MAX_SIDE_FROM_MEDIUM);
			stmt.setLong(1, mediumId);
			rs = stmt.executeQuery();
			if ( rs.next()) {
				String side = rs.getString(1);
				return (side);
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return "";
	}


	public int getMaxTrack(long mediumId) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_MAX_TRACK_FROM_MEDIUM);
			stmt.setLong(1, mediumId);
			rs = stmt.executeQuery();
			if ( rs.next()) {
				int track = rs.getInt(1);
				return (track);
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return 0;
	}
	
	public int getMaxTrackOfSide(long mediumId, String side) throws DAOException{
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_MAX_TRACK_FROM_MEDIUM_AND_SIDE);
			stmt.setLong(1, mediumId);
			stmt.setString(2, side);
			rs = stmt.executeQuery();
			if ( rs.next()) {
				int track = rs.getInt(1);
				return (track);
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return 0;
	}
	
	
	
	public boolean recordingExistsById(long id) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(CHECK_RECORDING_ID);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();
			if ( rs.next()) {
				long aid = rs.getLong(1);
				return ( aid == id);
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return false;
	}


	public boolean updateRecording(Recording rec) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(UPDATE_RECORDING);
			int field=1;
			stmt.setLong(field++, rec.getSongId());
			stmt.setLong(field++, rec.getMediumId());
			stmt.setString(field++, rec.getSide());
			DBHelper.stmtNullOrInt(stmt, field++, rec.getTrack());
			stmt.setString(field++, rec.getCounter());
			stmt.setInt(field++, rec.getTime());
			DBHelper.stmtNullOrInt(stmt,field++, rec.getYear());
			stmt.setString(field++, rec.getLongplay());
			stmt.setInt(field++, rec.getQuality());
			stmt.setString(field++, rec.getRemarks());
			stmt.setInt(field++, rec.getSpecial());
			stmt.setString(field++, rec.getDigital());			
			stmt.setLong(field++, rec.getId());
			
			int rowCount = stmt.executeUpdate();

			return ( rowCount == 1);
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, null);
		}
	}


	public long createRecording(Recording rec) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(INSERT_RECORDING);
			int field=1;
			stmt.setLong(field++, rec.getSongId());
			stmt.setLong(field++, rec.getMediumId());
			stmt.setString(field++, rec.getSide());
			DBHelper.stmtNullOrInt(stmt, field++, rec.getTrack());
			stmt.setString(field++, rec.getCounter());
			DBHelper.stmtNullOrInt(stmt, field++, rec.getTime());
			DBHelper.stmtNullOrInt(stmt, field++, rec.getYear());
			stmt.setString(field++, rec.getLongplay());
			stmt.setInt(field++, rec.getQuality());
			stmt.setString(field++, rec.getRemarks());
			stmt.setInt(field++, rec.getSpecial());
			stmt.setString(field++, rec.getDigital());
			
			int rowCount = stmt.executeUpdate();
			
			if ( rowCount == 1 ) {
				
				/*
				rs = stmt.getGeneratedKeys();
				if ( rs.next()) {
					return rs.getLong(1);
				} else {
					return -1;
				}
				*/
				
				stmt.close();
				stmt = con.prepareStatement("select last_value from recording_id_seq");
				rs = stmt.executeQuery();
				if ( rs.next()) {
					return rs.getLong(1);
				} else {
					return -1;
				}
				
			} else {
				return -1;
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
	}


	public long createSong(Song s) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(INSERT_SONG);
			int field=1;
			stmt.setLong(field++, s.getArtist_id());
			stmt.setString(field++, s.getTitle());
			stmt.setString(field++, s.getRelease());
			DBHelper.stmtNullOrInt(stmt, field++, s.getYear());
			stmt.setString(field++, s.getAuthors());
			stmt.setString(field++, s.getDance());
			stmt.setString(field++, s.getId3Genre());
			stmt.setString(field++, s.getRemarks());
			
			int rowCount = stmt.executeUpdate();
			
			if ( rowCount == 1 ) {
				// Insert was successful - retrieve ID
				rs = stmt.getGeneratedKeys();
				if ( rs.next()) {
					return rs.getLong(1);
				} else {
					return -1;
				}
			} else {
				return -1;
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
	}


	public void tagMedium(String username, long mediumId, String action) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(CHECK_TAG_MEDIUM);
			int field=1;
			stmt.setString(field++, username);
			stmt.setLong(field++, mediumId);
			stmt.setString(field++, action);
			rs = stmt.executeQuery();
			
			if ( !rs.next()) {
				// No entry exists - insert a new one
				stmt.close();
				stmt = con.prepareStatement(INSERT_TAG_MEDIUM);
				field=1;
				stmt.setString(field++, username);
				stmt.setLong(field++, mediumId);
				stmt.setString(field++, action);
				int rowCount = stmt.executeUpdate();
				
				if ( rowCount != 1 ) {
					log.error("Could not tag ("+username+","+mediumId+","+action+")");
					throw new DAOException("Could not tag medium "+mediumId);
				}
			} else {
				log.info("Tag ("+username+","+mediumId+","+action+") already exists");
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}	
	}


	public List getTaggedMediaIds(String username, String action) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		ArrayList result=new ArrayList();
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_TAGGED_MEDIA);
			int field=1;
			stmt.setString(field++, username);
			stmt.setString(field++, action);
			rs = stmt.executeQuery();
			
			while ( rs.next()) {
				long mediumId = rs.getLong(1);
				result.add(new Long(mediumId));
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}	
		
		return result;
	}


	public void deleteMediumTags(String username, String action) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;	
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(DELETE_TAGGED_MEDIA);
			int field=1;
			stmt.setString(field++, username);
			stmt.setString(field++, action);
						
			int rowCount = stmt.executeUpdate();
			
			if ( rowCount != 1)
				throw new DAOException("Could not delete tagged media for username="+username+", action="+action);
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, null);
		}
	}


	public String getMaxMediumCode(int mediumType, String artist) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;	
		int codeNumber=-1;
		String maxCode="";
			
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_CODES_FROM_MEDIUM_BY_TYPE_ARTIST);
			stmt.setLong(1, mediumType);
			stmt.setString(2, artist);
			rs = stmt.executeQuery();
			while ( rs.next()) {
				String code = rs.getString(1);
				int number = Integer.parseInt(code.replaceAll("\\D", ""));
				if ( number > codeNumber) {
					codeNumber=number;
					maxCode = code;
					log.info("Code "+code+" has got number "+number+". MaxCode="+maxCode);
				}
			}
			if ( codeNumber > -1) {
				return maxCode;
			} else {
				return null;
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}		
	}


	public String getMinMediumCode(String artist) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
			
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_MIN_CODE_FROM_MEDIUM_BY_ARTIST);
			stmt.setString(1, artist);
			rs = stmt.executeQuery();
			if ( rs.next()) {
				return (rs.getString(1));
			} else {
				return null;
			}
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}		
	}


	public void insertSongAndRecording(Song s, Recording r) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			
			stmt = con.prepareStatement(INSERT_SONG);
			int field=1;
			stmt.setLong(field++, s.getArtist_id());
			stmt.setString(field++, s.getTitle());
			stmt.setString(field++, s.getRelease());
			DBHelper.stmtNullOrInt(stmt, field++, s.getYear());
			stmt.setString(field++, s.getAuthors());
			stmt.setString(field++, s.getDance());
			stmt.setString(field++, s.getId3Genre());
			stmt.setString(field++, s.getRemarks());
			
			int rowCount = stmt.executeUpdate();
			
			if ( rowCount == 1 ) {
				// Insert was successful - retrieve ID
				//rs = stmt.getGeneratedKeys();
				/*
				rs = stmt.getGeneratedKeys();
				if ( rs.next()) {
					return rs.getLong(1);
				} else {
					return -1;
				}
				*/
				
				stmt.close();
				stmt = con.prepareStatement("select last_value from song_id_seq");				
				rs = stmt.executeQuery();
				if ( rs.next()) {
					long songId = rs.getLong(1);
					stmt = con.prepareStatement(INSERT_RECORDING);
					field=1;
					stmt.setLong(field++, songId);
					stmt.setLong(field++, r.getMediumId());
					stmt.setString(field++, r.getSide());
					DBHelper.stmtNullOrInt(stmt, field++, r.getTrack());
					stmt.setString(field++, r.getCounter());
					DBHelper.stmtNullOrInt(stmt, field++, r.getTime());
					DBHelper.stmtNullOrInt(stmt, field++, r.getYear());
					stmt.setString(field++, r.getLongplay());
					stmt.setInt(field++, r.getQuality());
					stmt.setString(field++, r.getRemarks());
					stmt.setInt(field++, r.getSpecial());
					stmt.setString(field++, r.getDigital());
					
					rowCount = stmt.executeUpdate();
					
					if ( rowCount == 1 ) {
						//rs = stmt.getGeneratedKeys();
						/*
						rs = stmt.getGeneratedKeys();
						if ( rs.next()) {
							return rs.getLong(1);
						} else {
							return -1;
						}
						*/
						
						stmt.close();
						stmt = con.prepareStatement("select last_value from recording_id_seq");				
						rs = stmt.executeQuery();
						if ( rs.next()) {
							log.debug("Inserted recording with ID "+rs.getLong(1));
						} else {
							con.rollback();
							throw new DAOException("Could not properly insert recording");
						}
					} else {
						con.rollback();
						throw new DAOException("Could not properly insert recording");
					}					
				} else {
					con.rollback();
					throw new DAOException("Could not properly insert song");					
				}
			} else {
				con.rollback();
				throw new DAOException("Could not properly insert song");				
			}
		} catch (SQLException e) {
			log.error(e,e);
			try {
				if ( con != null)
					con.rollback();
			} catch (SQLException e1) {
				log.error(e1,e1);
			}
			throw new DAOException(e);
		} finally {
			try {
				if ( con != null)
					con.setAutoCommit(true);
			} catch (SQLException e) {
				log.error(e,e);
			}
			DBHelper.close(con, stmt, rs);
		}		
		
	}


	public Recording getRecording(long recordingId) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_RECORDING_BY_ID);
			stmt.setLong(1, recordingId);
			rs = stmt.executeQuery();
			if ( rs.next()) {
				Recording r = new Recording(rs.getLong("id"));
				r.setArtist(rs.getString("artist_name"));
				r.setArtistId(rs.getLong("artist_id"));
				r.setCounter(rs.getString("counter"));
				r.setDigital(rs.getString("digital"));
				r.setLongplay(rs.getString("longplay"));
				r.setMediumCode(rs.getString("medium_code"));
				r.setMediumId(rs.getLong("medium_id"));
				r.setMediumType(rs.getInt("medium_type"));
				r.setQuality(rs.getInt("quality"));
				r.setRemarks(rs.getString("remarks"));
				r.setSide(rs.getString("side"));
				r.setSongId(rs.getLong("song_id"));
				r.setSpecial(rs.getInt("special"));
				r.setTime(rs.getInt("time"));
				r.setTitle(rs.getString("song_title"));
				r.setTrack(rs.getInt("track"));
				r.setYear(rs.getInt("recording_year"));
				
				return r;
			}
			
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return null;
	}


	public Song getSong(long songId) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_SONG_BY_ID);
			stmt.setLong(1, songId);
			rs = stmt.executeQuery();
			while ( rs.next()) {
				Song s = new Song(rs.getLong("id"));
				s.setArtist(rs.getString("artist_name"));
				s.setArtist_id(rs.getLong("artist_id"));
				s.setAuthors(rs.getString("authors"));
				s.setDance(rs.getString("dance"));
				s.setId3Genre(rs.getString("id3genre"));
				s.setRelease(rs.getString("release"));
				s.setRemarks(rs.getString("remarks"));
				s.setTitle(rs.getString("title"));
				s.setYear(rs.getInt("year"));
				
				return s;
			}
			
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return null;
	}


	public boolean updateSong(Song s) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;		
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(UPDATE_SONG);
			int field=1;
			stmt.setLong(field++, s.getArtist_id());
			stmt.setString(field++, s.getTitle());
			stmt.setString(field++, s.getRelease());
			DBHelper.stmtNullOrInt(stmt,field++, s.getYear());
			stmt.setString(field++, s.getAuthors());
			stmt.setString(field++, s.getDance());
			stmt.setString(field++, s.getId3Genre());
			stmt.setString(field++, s.getRemarks());
			
			stmt.setLong(field, s.getId());
			
			int rowCount = stmt.executeUpdate();

			return ( rowCount == 1);
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, null);
		}
	}


	public List selectRecordingsForSong(long songId) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList ret = new ArrayList();
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_RECORDINGS_FOR_SONG);
			stmt.setLong(1, songId);
			rs = stmt.executeQuery();
			while ( rs.next()) {
				Recording r = new Recording(rs.getLong("id"));
				r.setArtist(rs.getString("artist_name"));
				r.setArtistId(rs.getLong("artist_id"));
				r.setCounter(rs.getString("counter"));
				r.setDigital(rs.getString("digital"));
				r.setLongplay(rs.getString("longplay"));
				r.setMediumCode(rs.getString("medium_code"));
				r.setMediumId(rs.getLong("medium_id"));
				r.setMediumType(rs.getInt("medium_type"));
				r.setQuality(rs.getInt("quality"));
				r.setRemarks(rs.getString("remarks"));
				r.setSide(rs.getString("side"));
				r.setSongId(rs.getLong("song_id"));
				r.setSpecial(rs.getInt("special"));
				r.setTime(rs.getInt("time"));
				r.setTitle(rs.getString("song_title"));
				r.setTrack(rs.getInt("track"));
				r.setYear(rs.getInt("recording_year"));
				
				ret.add(r);
			}
			
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return ret;
	}


	public Recording getBestRecordingForSong(long songId) throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
			
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(SELECT_BEST_RECORDING_FOR_SONG);
			stmt.setLong(1, songId);
			rs = stmt.executeQuery();
			if ( rs.next()) {
				Recording r = new Recording(rs.getLong("id"));
				r.setArtist(rs.getString("artist_name"));
				r.setArtistId(rs.getLong("artist_id"));
				r.setCounter(rs.getString("counter"));
				r.setDigital(rs.getString("digital"));
				r.setLongplay(rs.getString("longplay"));
				r.setMediumCode(rs.getString("medium_code"));
				r.setMediumId(rs.getLong("medium_id"));
				r.setMediumType(rs.getInt("medium_type"));
				r.setQuality(rs.getInt("quality"));
				r.setRemarks(rs.getString("remarks"));
				r.setSide(rs.getString("side"));
				r.setSongId(rs.getLong("song_id"));
				r.setSpecial(rs.getInt("special"));
				r.setTime(rs.getInt("time"));
				r.setTitle(rs.getString("song_title"));
				r.setTrack(rs.getInt("track"));
				r.setYear(rs.getInt("recording_year"));
					
				return r;
			}
				
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
		
		return null;
			
	}
	
	
	public StatisticsBean calculateStatistics() throws DAOException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		StatisticsBean s = new StatisticsBean();
		
		try {
			con = ds.getConnection();
			stmt = con.prepareStatement(STAT_ARTISTS);
			rs = stmt.executeQuery();
			if ( rs.next())
				s.setArtists(rs.getLong(1));
			rs.close();
			stmt.close();
			
			stmt = con.prepareStatement(STAT_SONGS);
			rs = stmt.executeQuery();
			if ( rs.next())
				s.setSongs(rs.getLong(1));
			rs.close();
			stmt.close();
			
			stmt = con.prepareStatement(STAT_PERFECT_SONGS);
			rs = stmt.executeQuery();
			if ( rs.next())
				s.setPerfectSongs(rs.getLong(1));
			rs.close();
			stmt.close();
			
			stmt = con.prepareStatement(STAT_SONGS_ON_ORIGINAL_MEDIA);
			rs = stmt.executeQuery();
			if ( rs.next())
				s.setOriginalSongs(rs.getLong(1));
			rs.close();
			stmt.close();
			
			stmt = con.prepareStatement(STAT_MEDIUM);
			rs = stmt.executeQuery();
			while ( rs.next()) {
				int type = rs.getInt(1);
				long amount = rs.getLong(2);
				s.setMediumAmount(type, amount);
			}
			rs.close();
			stmt.close();
			
			stmt = con.prepareStatement(STAT_MEDIUM_PRICES);
			rs = stmt.executeQuery();
			while ( rs.next()) {
				int type = rs.getInt(1);
				double sumPrice = rs.getDouble(2);
				double avgPrice = rs.getDouble(3);
				s.setMediumPrices(type, sumPrice, avgPrice);
			}
			rs.close();
			stmt.close();
			
			stmt = con.prepareStatement(STAT_QUALITY);
			rs = stmt.executeQuery();
			while ( rs.next()) {
				int type = rs.getInt(1);
				long amount = rs.getLong(2);
				s.setQuality(type, amount);
			}
			rs.close();
			stmt.close();
			
			return s;
		} catch (SQLException e) {
			log.error(e,e);
			throw new DAOException(e);
		} finally {
			DBHelper.close(con, stmt, rs);
		}
	}
	
	
	
	private void logError(SQLException e, String statement) {
		log.error(e.getMessage()+" on "+statement+": ",e);
	}



}
