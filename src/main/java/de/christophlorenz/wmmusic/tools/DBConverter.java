package de.christophlorenz.wmmusic.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;

import de.christophlorenz.wmmusic.medium.IMediumTypes;


/**
 * @author clorenz
 *
 * This class converts the gmmusic database (Version 9.5) into the jmmusic database
 * format (Version 1.0). Works ONLY for PostgreSQL!!
 * <br>
 * Requires an empty jmmusic database, Version 1.0 !
 * 
 * createdb -E unicode jmmusic2
 * psql jmmusic2 < /home/clorenz/java/jmmusic/src/de/christophlorenz/jmmusic/tools/jmmusic-pg.sql
 */
public class DBConverter {
	
	private Connection gmmusic;
	private Connection jmmusic;
	private HashMap audioTapes = new HashMap();
	private HashMap videoTapes = new HashMap();
	private HashMap files = new HashMap();
	private HashMap mds = new HashMap();
	private HashMap roms = new HashMap();
	private HashMap lps = new HashMap();
	private HashMap singles = new HashMap();
	private HashMap cds = new HashMap();
	
	
	
	private void initDatabases(String username, String password) {
		try {
			if ( password == null)
				password = "";
			Class.forName("org.postgresql.Driver");
			gmmusic = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mmusic3", username, password); 
			
		} catch ( Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("Connected to Postgres Database");
		
		try {
			//Class.forName("com.mysql.jdbc.Driver"); //load the driver
			//jmmusic = DriverManager.getConnection("jdbc:mysql://localhost/jmmusic2?encoding=latin1",
            //     	username,
			//		null); //connect to the db
			Class.forName("org.postgresql.Driver");
			jmmusic = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jmmusic2", username, password); 
			

		} catch ( Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("Connected to Mysql database");
	}
	
	
	private boolean checkDatabases() {
		try {
			Statement checkGmmusic = gmmusic.createStatement();
			Statement checkJmmusic = jmmusic.createStatement();
		
			ResultSet versionGmmusicRS = checkGmmusic.executeQuery(
			"SELECT version FROM db_info");
			ResultSet versionJmmusicRS = checkJmmusic.executeQuery(
				"SELECT version FROM db_info");
		
			if ( versionGmmusicRS.next()) {
				if ( !versionGmmusicRS.getString(1).trim().equals("9.5") ) {
					System.err.println("gmmusic database has version "+
							versionGmmusicRS.getString(1)+
					" but only version 9.5 can be converted.");
					return false;
				}
			} else {
				System.err.println("Could not get version info from gmmusic database.");
				return false;
			}
			
			if ( versionJmmusicRS.next()) {			
				if ( !versionJmmusicRS.getString(1).trim().equals("1.0") ) {
					System.err.println("jmmusic database has version "+
							versionJmmusicRS.getString(1)+
							" but only version 1.0 can be converted.");
					return false;
				}	
			} else {	
				System.err.println("Could not get version info from jmmusic database.");
				return false;
			}
			
			checkGmmusic.execute("SET CLIENT_ENCODING TO 'LATIN1'");
			
			//checkJmmusic.execute("SET NAMES latin1");
			
			return true;
		} catch ( SQLException e) {
			System.err.println("Error at accessing databases: "+e);
			return false;
		}
	}
	
	
	private void convert() {
		
		try {
			// Copy artists
			initializeArtistTable();
			copyArtists();
		
			
			initializeMediumTable();
			copyAudioTapes();
			copyVideoTapes();
			copyMD();
			copyFiles();
			copyRoms();
			copyLps();
			copySingles();
			copyCDs();
		
			
			// Copy songs
			initializeSongTable();
			copySongs();
		
			// Copy recordings
			initializeRecordingTable();
			copyRecordings();
		} catch ( Exception e) {
			System.err.println(e);
			System.exit(4);
		}
	}
	
	
	private void initializeMediumTable() {
		try {
			ResultSet rs = jmmusic.createStatement().executeQuery("SELECT max(id) as max FROM medium");
			if ( rs.next())
				if ( rs.getLong("max") != 0)
					throw new RuntimeException("Table 'medium' is already filled. Refusing to update!");
			jmmusic.createStatement().executeUpdate("DELETE FROM medium");
		} catch ( SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(2);
		}
	}
	
	
	private void initializeArtistTable() {
		try {
			jmmusic.createStatement().executeUpdate("DELETE FROM artist");
		} catch ( SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(2);
		}
	}
	
	
	private void initializeSongTable() {
		try {
			ResultSet rs = jmmusic.createStatement().executeQuery("SELECT max(id) as max FROM song");
			if ( rs.next())
				if ( rs.getLong("max") != 0)
					throw new RuntimeException("Table 'song' is already filled. Refusing to update!");
			jmmusic.createStatement().executeUpdate("DELETE FROM song");
		} catch ( SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(2);
		}
	}
	
	
	private void initializeRecordingTable() {
		try {
			ResultSet rs = jmmusic.createStatement().executeQuery("SELECT max(id) as max FROM recording");
			if ( rs.next())
				if ( rs.getLong("max") != 0)
					throw new RuntimeException("Table 'recording' is already filled. Refusing to update!");
			jmmusic.createStatement().executeUpdate("DELETE FROM recording");
		} catch ( SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(2);
		}
	}

	/**
	 * 
	 */
	private void copyAudioTapes() {
		String code="";
		
		try {
			System.out.println("Converting all audio_tape records");
			
			Statement oldStmt = gmmusic.createStatement();
			
			
			PreparedStatement newStmt = jmmusic.prepareStatement("INSERT INTO medium (type,code,"+
					"artist_id,title,size,manufacturer,rec_begin_date,rec_begin_b,rec_end_date,"+
					"remarks,timestamp)" +
					" values("+IMediumTypes.AUDIO_TAPE+",?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement retrieve = jmmusic.prepareStatement("SELECT id FROM medium WHERE type="
					+IMediumTypes.AUDIO_TAPE+" and code=?");	
			
			ResultSet rs = oldStmt.executeQuery("SELECT * FROM audio_tape ORDER BY medium_id");
			
			while ( rs.next()) {
				String id = rs.getString("id").trim();
				code = getValidString(rs,"medium_id").trim();
				int artistId = getArtistIdFromArtist(getValidString(rs,"interpret"));
				String title = getValidString(rs,"titel");
				int size = rs.getInt("size");
				String manufacturer = getValidString(rs,"firm");
				Date rec_begin_date = rs.getDate("begin_date");
				Date rec_begin_b = rs.getDate("begin_b");
				Date rec_end_Date = rs.getDate("end_date");
				String remarks = getValidString(rs,"remarks");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				
				newStmt.setString(1, code);
				newStmt.setInt(2, artistId);
				newStmt.setString(3, title);
				newStmt.setInt(4, size);
				newStmt.setString(5, manufacturer);
				newStmt.setDate(6, rec_begin_date);
				newStmt.setDate(7, rec_begin_b);
				newStmt.setDate(8, rec_end_Date);
				newStmt.setString(9, remarks);
				newStmt.setTimestamp(10, timestamp);
				
				newStmt.executeUpdate();
				
				retrieve.setString(1, code);
				ResultSet rs2 = retrieve.executeQuery();
				if ( rs2.next())
					audioTapes.put(id, rs2.getString("id"));
				else
					throw new RuntimeException("Cannot retrieve last inserted dataset.");
			
				rs2.close();
				
				System.out.print(".");
			}
		} catch ( SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(2);
		} catch ( UnsupportedEncodingException e) {
			System.err.println(code+":"+e);
			e.printStackTrace();
			System.exit(3);
		}
		
		System.out.println("Successfully converted all audio_tape records.");
	}

	
	private int getArtistIdFromArtist(String artist) throws SQLException {
		PreparedStatement stmt=null;
		ResultSet rs=null;
		
		try {
			stmt = jmmusic.prepareStatement("SELECT id from artist where name=?");
			stmt.setString(1, artist);
			rs = stmt.executeQuery();
			if ( rs.next()) {
				return rs.getInt(1);
			} else {
				return 1;
			}
		} catch ( SQLException e) {
			System.err.println("Invalid artist '"+artist+"'");
			throw e;
		} finally {
			if ( rs != null)
				rs.close();
			if ( stmt != null)
				stmt.close();
		}
	}


	private void copyVideoTapes() {
		String code="";
		
		try {
			System.out.println("Converting all video_tape records");
			
			Statement oldStmt = gmmusic.createStatement();
		
			PreparedStatement newStmt = jmmusic.prepareStatement("INSERT INTO medium (type,code,"+
					"title,size,manufacturer,system,rec_begin_date,rec_end_date,"+
					"remarks,timestamp)" +
					" values("+IMediumTypes.VIDEO_TAPE+",?,?,?,?,?,?,?,?,?)");
			PreparedStatement retrieve = jmmusic.prepareStatement("SELECT id FROM medium WHERE type="
					+IMediumTypes.VIDEO_TAPE+" and code=?");	
			
			ResultSet rs = oldStmt.executeQuery("SELECT * FROM video_tape ORDER BY medium_id");
			
			while ( rs.next()) {	
				String id = rs.getString("id").trim();
				code = getValidString(rs,"medium_id").trim();
				String title = getValidString(rs,"titel");
				int size = rs.getInt("size");
				String system = getValidString(rs,"system");
				String manufacturer = getValidString(rs,"firm");
				Date rec_begin_date = rs.getDate("begin_date");
				Date rec_end_Date = rs.getDate("end_date");
				String remarks = getValidString(rs,"remarks");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				
				newStmt.setString(1, code);
				newStmt.setString(2, title);
				newStmt.setInt(3, size);
				newStmt.setString(4, manufacturer);
				newStmt.setString(5, system);
				newStmt.setDate(6, rec_begin_date);
				newStmt.setDate(7, rec_end_Date);
				newStmt.setString(8, remarks);
				newStmt.setTimestamp(9, timestamp);
				
				newStmt.executeUpdate();
				
				retrieve.setString(1, code);
				ResultSet rs2 = retrieve.executeQuery();
				if ( rs2.next())
					videoTapes.put(id, rs2.getString("id"));
				else
					throw new RuntimeException("Cannot retrieve last inserted dataset.");
			
				rs2.close();
				
				System.out.print(".");
			}
		} catch ( SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(2);
		} catch ( UnsupportedEncodingException e) {
			System.err.println(code+":"+e);
			e.printStackTrace();
			System.exit(3);
		}
		
		System.out.println("Successfully converted all video_tape records.");
		
	}
	
	
	private void copyFiles() {
		String code="";
		
		try {
			System.out.println("Converting all files records");
			
			Statement oldStmt = gmmusic.createStatement();
			
			PreparedStatement newStmt = jmmusic.prepareStatement("INSERT INTO medium (type,code,"+
					"magic,files_type,"+
					"remarks,timestamp)" +
					" values("+IMediumTypes.FILES+",?,?,?,?,?)");
			PreparedStatement retrieve = jmmusic.prepareStatement("SELECT id FROM medium WHERE type="
					+IMediumTypes.FILES+" and code=?");	
			
			ResultSet rs = oldStmt.executeQuery("SELECT * FROM files ORDER BY medium_id");
			
			while ( rs.next()) {	
				String id = rs.getString("id").trim();
				code = getValidString(rs,"medium_id").trim();
				String magic = getValidString(rs,"magic");
				String type = getValidString(rs,"type");
				String remarks = getValidString(rs,"remarks");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				
				newStmt.setString(1, code);
				newStmt.setString(2, magic);
				newStmt.setString(3, type);
				newStmt.setString(4, remarks);
				newStmt.setTimestamp(5, timestamp);
				
				newStmt.executeUpdate();
				
				retrieve.setString(1, code);
				ResultSet rs2 = retrieve.executeQuery();
				if ( rs2.next())
					files.put(id, rs2.getString("id"));
				else
					throw new RuntimeException("Cannot retrieve last inserted dataset.");
			
				rs2.close();
				
				System.out.print(".");
			}
		} catch ( SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(2);
		} catch ( UnsupportedEncodingException e) {
			System.err.println(code+":"+e);
			e.printStackTrace();
			System.exit(3);
		}
		
		System.out.println("Successfully converted all files records.");
		
	}
	
	
	private void copyMD() {
		String code="";
		
		try {
			System.out.println("Converting all md records");
			
			Statement oldStmt = gmmusic.createStatement();
			
			PreparedStatement newStmt = jmmusic.prepareStatement("INSERT INTO medium (type,code,"+
					"artist_id,title,size,manufacturer,rec_begin_date,rec_end_date,"+
					"remarks,timestamp)" +
					" values("+IMediumTypes.MD+",?,?,?,?,?,?,?,?,?)");
			PreparedStatement retrieve = jmmusic.prepareStatement("SELECT id FROM medium WHERE type="
					+IMediumTypes.MD+" and code=?");	
						
			ResultSet rs = oldStmt.executeQuery("SELECT * FROM md ORDER BY medium_id");
			
			while ( rs.next()) {	
				String id = rs.getString("id").trim();
				code = getValidString(rs,"medium_id").trim();
				int artistId = getArtistIdFromArtist(getValidString(rs,"interpret"));
				String title = getValidString(rs,"titel");
				int size = rs.getInt("size");
				String manufacturer = getValidString(rs,"firm");
				Date rec_begin_date = rs.getDate("begin_date");
				Date rec_end_Date = rs.getDate("end_date");
				String remarks = getValidString(rs,"remarks");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				
				newStmt.setString(1, code);
				newStmt.setInt(2, artistId);
				newStmt.setString(3, title);
				newStmt.setInt(4, size);
				newStmt.setString(5, manufacturer);
				newStmt.setDate(6, rec_begin_date);
				newStmt.setDate(7, rec_end_Date);
				newStmt.setString(8, remarks);
				newStmt.setTimestamp(9, timestamp);
				
				newStmt.executeUpdate();
				
				retrieve.setString(1, code);
				ResultSet rs2 = retrieve.executeQuery();
				if ( rs2.next())
					mds.put(id, rs2.getString("id"));
				else
					throw new RuntimeException("Cannot retrieve last inserted dataset.");
			
				rs2.close();
				
				System.out.print(".");
			}
		} catch ( SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(2);
		} catch ( UnsupportedEncodingException e) {
			System.err.println(code+":"+e);
			e.printStackTrace();
			System.exit(3);
		}
		
		System.out.println("Successfully converted all md records.");
		
	}
	
	
	private void copyRoms() {
		String code="";
		
		try {
			System.out.println("Converting all rom records");
			
			Statement oldStmt = gmmusic.createStatement();
			
			PreparedStatement newStmt = jmmusic.prepareStatement("INSERT INTO medium (type,code,"+
					"discid,artist_id,title,track_offsets,size,audio,rewritable,manufacturer,"+
					"rec_begin_date,rec_end_date,burning_date,buy_price,"+
					"remarks,timestamp)" +
					" values("+IMediumTypes.ROM+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement retrieve = jmmusic.prepareStatement("SELECT id FROM medium WHERE type="
					+IMediumTypes.ROM+" and code=?");	
						
			ResultSet rs = oldStmt.executeQuery("SELECT * FROM rom ORDER BY medium_id");
			
			while ( rs.next()) {	
				String id = rs.getString("id").trim();
				code = getValidString(rs,"medium_id").trim();
				long discid = rs.getLong("cddb_id");
				int artistId = getArtistIdFromArtist(getValidString(rs,"interpret"));
				String title = getValidString(rs,"titel");
				String track_offsets = rs.getString("track_offsets");
				int size = rs.getInt("size");
				boolean audio = rs.getBoolean("audio");
				boolean rewritable = rs.getBoolean("rewritable");				
				String manufacturer = getValidString(rs,"firm");
				Date rec_begin_date = rs.getDate("begin_date");
				Date rec_end_Date = rs.getDate("end_date");
				Date burning_date = rs.getDate("burning_date");
				double buy_price = rs.getDouble("buy_price");
				String remarks = getValidString(rs,"remarks");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				
				newStmt.setString(1, code);
				newStmt.setLong(2, discid);
				newStmt.setInt(3, artistId);
				newStmt.setString(4, title);
				if ( track_offsets != null)
					newStmt.setString(5, array2String(track_offsets));	
				else
					newStmt.setNull(5, Types.VARCHAR);
				newStmt.setInt(6, size);
				newStmt.setBoolean(7, audio);
				newStmt.setBoolean(8, rewritable);
				newStmt.setString(9, manufacturer);
				newStmt.setDate(10, rec_begin_date);
				newStmt.setDate(11, rec_end_Date);
				newStmt.setDate(12, burning_date);
				newStmt.setDouble(13, buy_price);
				newStmt.setString(14, remarks);
				newStmt.setTimestamp(15, timestamp);
				
				newStmt.executeUpdate();
				
				retrieve.setString(1, code);
				ResultSet rs2 = retrieve.executeQuery();
				if ( rs2.next())
					roms.put(id, rs2.getString("id"));
				else
					throw new RuntimeException("Cannot retrieve last inserted dataset.");
			
				rs2.close();
				
				System.out.print(".");
			}
		} catch ( SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(2);
		} catch ( UnsupportedEncodingException e) {
			System.err.println(code+":"+e);
			e.printStackTrace();
			System.exit(3);
		}
		
		System.out.println("Successfully converted all rom records.");
		
	}
	
	
	private String array2String(String track_offsets) {
		track_offsets = track_offsets.replaceAll("\\{","");
		track_offsets = track_offsets.replaceAll("\\}","");
		return track_offsets;
	}


	private void copyLps() {
		String code="";
		
		try {
			System.out.println("Converting all lp records");
			
			Statement oldStmt = gmmusic.createStatement();
			
			PreparedStatement newStmt = jmmusic.prepareStatement("INSERT INTO medium (type,code,"+
					"artist_id,title,label,ordercode,p_year,buy_date,buy_price," +
					"remarks,timestamp)" +
					" values("+IMediumTypes.LP+",?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement retrieve = jmmusic.prepareStatement("SELECT id FROM medium WHERE type="
					+IMediumTypes.LP+" and code=?");	
						
			ResultSet rs = oldStmt.executeQuery("SELECT * FROM lp ORDER BY medium_id");
			
			while ( rs.next()) {
				String id = rs.getString("id").trim();
				code = getValidString(rs,"medium_id").trim();
				int artistId = getArtistIdFromArtist(getValidString(rs,"interpret"));
				String title = getValidString(rs,"titel");
				String label = getValidString(rs,"label");
				String ordercode = getValidString(rs,"order_no");
				int p_year = rs.getInt("year");
				Date buy_date = rs.getDate("buy_date");
				double buy_price = rs.getDouble("buy_price");
				String remarks = getValidString(rs,"remarks");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				
				newStmt.setString(1, code);
				newStmt.setInt(2, artistId);
				newStmt.setString(3, title);
				newStmt.setString(4, label);
				newStmt.setString(5, ordercode);
				newStmt.setInt(6, p_year);
				newStmt.setDate(7, buy_date);
				newStmt.setDouble(8, buy_price);
				newStmt.setString(9, remarks);
				newStmt.setTimestamp(10, timestamp);
				
				newStmt.executeUpdate();
				
				retrieve.setString(1, code);
				ResultSet rs2 = retrieve.executeQuery();
				if ( rs2.next())
					lps.put(id, rs2.getString("id"));
				else
					throw new RuntimeException("Cannot retrieve last inserted dataset.");
			
				rs2.close();
				
				System.out.print(".");
			}
		} catch ( SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(2);
		} catch ( UnsupportedEncodingException e) {
			System.err.println(code+":"+e);
			e.printStackTrace();
			System.exit(3);
		}
		
		System.out.println("Successfully converted all lp records.");
		
	}
	
	
	private void copySingles() {
		String code="";
		try {
			System.out.println("Converting all single records");
			
			Statement oldStmt = gmmusic.createStatement();
			
			PreparedStatement newStmt = jmmusic.prepareStatement("INSERT INTO medium (type,code,"+
					"artist_id,title,label,ordercode,p_year,buy_date,buy_price," +
					"remarks,timestamp)" +
					" values("+IMediumTypes.SINGLE+",?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement retrieve = jmmusic.prepareStatement("SELECT id FROM medium WHERE type="
					+IMediumTypes.SINGLE+" and code=?");	
						
			ResultSet rs = oldStmt.executeQuery("SELECT * FROM single ORDER BY medium_id");
			
			
			
			while ( rs.next()) {
				String id = rs.getString("id").trim();
				code = getValidString(rs,"medium_id").trim();
				int artistId = getArtistIdFromArtist(getValidString(rs,"interpret"));
				String title = getValidString(rs, "titel");
				String label = getValidString(rs,"label");
				String ordercode = getValidString(rs,"order_no");
				int p_year = rs.getInt("year");
				Date buy_date = rs.getDate("buy_date");
				double buy_price = rs.getDouble("buy_price");
				String remarks = getValidString(rs,"remarks");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				
				newStmt.setString(1, code);
				newStmt.setInt(2, artistId);
				newStmt.setString(3, title);
				newStmt.setString(4, label);
				newStmt.setString(5, ordercode);
				newStmt.setInt(6, p_year);
				newStmt.setDate(7, buy_date);
				newStmt.setDouble(8, buy_price);
				newStmt.setString(9, remarks);
				newStmt.setTimestamp(10, timestamp);
				
				newStmt.executeUpdate();
				
				retrieve.setString(1, code);
				ResultSet rs2 = retrieve.executeQuery();
				if ( rs2.next())
					singles.put(id, rs2.getString("id"));
				else
					throw new RuntimeException("Cannot retrieve last inserted dataset.");
			
				rs2.close();
				
				System.out.print(".");
			}
		} catch ( SQLException e) {
			System.err.println(code+":"+e);
			e.printStackTrace();
			System.exit(2);
		} catch ( UnsupportedEncodingException e) {
			System.err.println(code+":"+e);
			e.printStackTrace();
			System.exit(3);
		}
		
		System.out.println("Successfully converted all single records.");
		
	}
	
	

	private void copyCDs() {
		String code="";
		
		try {
			System.out.println("Converting all cd records");
			
			Statement oldStmt = gmmusic.createStatement();

			PreparedStatement newStmt = jmmusic.prepareStatement("INSERT INTO medium (type,code,"+
					"discid,artist_id,title,category,track_offsets,id3_genre,label,"+
					"ordercode,digital,p_year,buy_date,buy_price," +
					"remarks,timestamp)" +
					" values("+IMediumTypes.CD+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement retrieve = jmmusic.prepareStatement("SELECT id FROM medium WHERE type="
					+IMediumTypes.CD+" and code=?");	
						
			ResultSet rs = oldStmt.executeQuery("SELECT * FROM cd ORDER BY medium_id");
			
			while ( rs.next()) {	
				String id = rs.getString("id").trim();
				code = getValidString(rs,"medium_id").trim();
				long discid = rs.getLong("cddb_id");
				int artistId = getArtistIdFromArtist(getValidString(rs,"interpret"));
				String title = getValidString(rs,"titel");
				String category = getValidString(rs,"category");
				String track_offsets = rs.getString("track_offsets");
				
				String id3_genre = getValidString(rs,"id3_genre");
				if (( id3_genre != null ) && ( id3_genre.length()>16)) 
					id3_genre = id3_genre.substring(0,16);
				
				String label = getValidString(rs,"label");
				String ordercode = getValidString(rs,"order_no");
				String digital = getValidString(rs,"digital");
				int p_year = rs.getInt("year");
				Date buy_date = rs.getDate("buy_date");
				double buy_price = rs.getDouble("buy_price");
				String remarks = getValidString(rs,"remarks");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				
				newStmt.setString(1, code);
				newStmt.setLong(2, discid);
				newStmt.setInt(3, artistId);
				newStmt.setString(4, title);
				newStmt.setString(5, category);
				
				if ( track_offsets != null)				
					newStmt.setString(6, array2String(track_offsets));
				else
					newStmt.setNull(6, Types.VARCHAR);
				
				newStmt.setString(7, id3_genre);				
				newStmt.setString(8, label);
				newStmt.setString(9, ordercode);
				newStmt.setString(10, digital);
				newStmt.setInt(11, p_year);
				newStmt.setDate(12, buy_date);
				newStmt.setDouble(13, buy_price);
				newStmt.setString(14, remarks);
				newStmt.setTimestamp(15, timestamp);
				
				newStmt.executeUpdate();
				
				retrieve.setString(1, code);
				ResultSet rs2 = retrieve.executeQuery();
				if ( rs2.next())
					cds.put(id, rs2.getString("id"));
				else
					throw new RuntimeException("Cannot retrieve last inserted dataset.");
			
				rs2.close();
				
				System.out.print(".");
			}
		} catch ( SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(2);
		} catch ( UnsupportedEncodingException e) {
			System.err.println(code+":"+e);
			e.printStackTrace();
			System.exit(3);
		}
		
		System.out.println("Successfully converted all cd records.");
		
	}
	
	
	
	private void copyArtists() {
		String name="";
		
		try {
			System.out.println("Converting all artist records");
			
			Statement oldStmt = gmmusic.createStatement();

			PreparedStatement newStmt = jmmusic.prepareStatement("INSERT INTO artist " +
					"(name,print,birthday,country,location,url,remarks,timestamp) "+
					" values(?,?,?,?,?,?,?,?)");
						
			ResultSet rs = oldStmt.executeQuery("SELECT * FROM artist ORDER BY name");
			
			while ( rs.next()) {
				name = getValidString(rs,"name");
				String print = getValidString(rs,"print");
				Date birthday = rs.getDate("birthday");				
				String country = getValidString(rs,"country");
				String location = getValidString(rs,"location");
				String url = getValidString(rs,"url");
				String remarks = getValidString(rs,"remarks");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				
				
				
				newStmt.setString(1, name);
				newStmt.setString(2, print);
				newStmt.setDate(3, birthday);
				newStmt.setString(4, country);
				newStmt.setString(5, location);
				newStmt.setString(6, url);
				newStmt.setString(7, remarks);
				newStmt.setTimestamp(8, timestamp);
				
				newStmt.executeUpdate();
				
				System.out.print(".");
			}
		} catch ( SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(2);
		} catch ( UnsupportedEncodingException e) {
			System.err.println(name+":"+e);
			e.printStackTrace();
			System.exit(3);
		}
		
		System.out.println("Successfully converted all artist records.");
		
	}
	
	
	
	private void copySongs() {
		long id=-1;
		
		try {
			System.out.println("Converting all song records");
			
			Statement oldStmt = gmmusic.createStatement();

			PreparedStatement newStmt = jmmusic.prepareStatement("INSERT INTO song " +
					"(id,artist_id,title,release,year,authors,dance,id3genre,remarks,timestamp) "+
					" values(?,?,?,?,?,?,?,?,?,?)");
						
			ResultSet rs = oldStmt.executeQuery("SELECT * FROM song ORDER BY song_id");
			
			while ( rs.next()) {	
				id = rs.getLong("song_id");
				int artistId = getArtistIdFromArtist(getValidString(rs,"interpret"));
				String title = getValidString(rs,"titel");
				String release = getValidString(rs, "release");
				int year = rs.getInt("year");
				String authors = getValidString(rs,"authors");
				String remarks = getValidString(rs,"remarks");
				String dance = getValidString(rs,"dance");
				String id3genre = getValidString(rs,"id3_genre");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				
				if ( title.indexOf("Wenn die Blumen") > -1)
					System.out.println(title);
				
				newStmt.setLong(1, id);
				newStmt.setInt(2, artistId);
				newStmt.setString(3, title);
				newStmt.setString(4, release);
				newStmt.setInt(5, year);
				newStmt.setString(6, authors);
				newStmt.setString(7, dance);
				newStmt.setString(8, id3genre);
				newStmt.setString(9, remarks);
				newStmt.setTimestamp(10, timestamp);
				
				newStmt.executeUpdate();
				
				System.out.print(".");
			}
		} catch ( SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(2);
		} catch ( UnsupportedEncodingException e) {
			System.err.println(id+":"+e);
			e.printStackTrace();
			System.exit(3);
		}
		
		System.out.println("Successfully converted all song records.");
		
	}
	
	
	
	private void copyRecordings() {
		long recording_id=-1;
		
		try {
			System.out.println("Converting all recording records");
			
			Statement oldStmt = gmmusic.createStatement();

			PreparedStatement newStmt = jmmusic.prepareStatement("INSERT INTO recording " +
					"(song_id,medium_id,side,track,counter,time,recording_year," +
					"longplay,quality,remarks,special,digital,timestamp) "+
					" values(?,?,?,?,?,?,?,?,?,?,?,?,?)");   //13
						
			ResultSet rs = oldStmt.executeQuery("SELECT * FROM recording ORDER BY recording_id");
			
			while ( rs.next()) {	
				recording_id = rs.getLong("recording_id");
				long song_id = rs.getLong("song_id");
				int medium_type = rs.getInt("medium_type");
				String mediumId = rs.getString("medium");
				String side = getValidString(rs,"side");
				int track = rs.getInt("track");
				String counter = getValidString(rs,"counter");
				int time = rs.getInt("time");
				int recording_year = rs.getInt("recording_year");
				String longplay = getValidString(rs,"longplay");
				int quality = rs.getInt("quality");
				String remarks = getValidString(rs,"remarks");
				int special = rs.getInt("special");
				String digital = rs.getString("digital");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				
				long medium=-1;
				if ( (mediumId==null) || mediumId.equals("") )
					throw new RuntimeException("Found empty medium for recording id "+recording_id);
				
				String newMedium=null;
				switch(medium_type) {
					case IMediumTypes.AUDIO_TAPE: newMedium = (String)audioTapes.get(mediumId); break;
					case IMediumTypes.CD: newMedium = (String)cds.get(mediumId); break;
					case IMediumTypes.FILES: newMedium = (String)files.get(mediumId); break;
					case IMediumTypes.LP: newMedium = (String)lps.get(mediumId); break;
					case IMediumTypes.MD: newMedium = (String)mds.get(mediumId); break;
					case IMediumTypes.ROM: newMedium = (String)roms.get(mediumId); break;
					case IMediumTypes.SINGLE: newMedium = (String)singles.get(mediumId); break;
					case IMediumTypes.VIDEO_TAPE: newMedium = (String)videoTapes.get(mediumId); break;
					default: throw new RuntimeException("Unknown medium type: "+medium_type);
				}
				
				if ((newMedium==null) || (newMedium.equals(""))) {
					System.out.println(cds);
					throw new RuntimeException("Could not map medium "+IMediumTypes.table[medium_type]+" "
							+mediumId);
				}
				
				medium = Long.parseLong(newMedium);
				
				newStmt.setLong(1, song_id);
				newStmt.setLong(2, medium);
				newStmt.setString(3, side);
				newStmt.setInt(4, track);
				newStmt.setString(5, counter);
				newStmt.setInt(6, time);
				newStmt.setInt(7, recording_year);
				newStmt.setString(8, longplay);
				newStmt.setInt(9, quality);
				newStmt.setString(10, remarks);
				newStmt.setInt(11, special);
				newStmt.setString(12, digital);
				newStmt.setTimestamp(13, timestamp);
				
				newStmt.executeUpdate();
				
				System.out.print(".");
			}
		} catch ( SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(2);
		} catch ( UnsupportedEncodingException e) {
			System.err.println(recording_id+":"+e);
			e.printStackTrace();
			System.exit(3);
		} catch ( Exception e) {
			System.err.println(recording_id+":"+e);
			e.printStackTrace();
			System.exit(4);
		}
		
		System.out.println("Successfully converted all recording records.");
		
	}
	
	
	private String getValidString(ResultSet rs, String fieldname) throws UnsupportedEncodingException {
		String retval="";
		
		try {
			if ( rs.getString(fieldname) != null) {
				retval = new String( rs.getBytes(fieldname),"ISO-8859-1" );
				if ( (retval != null) && (retval.indexOf("Ã") > -1)) {
					String oldval = retval;
					retval = new String (rs.getString(fieldname));
					System.out.println("\nCorrected: '"+oldval+"' to '"+retval+"'\n");
				}
			} else {
				retval=null;
			}
		} catch ( Exception e) {
			try {
				if ( rs.getBytes(fieldname) != null) {
					retval = new String( rs.getBytes(fieldname),"ISO-8859-1" );
					if ( (retval != null) && (retval.indexOf("Ã") > -1)) {
						String oldval = retval;
						retval = new String (rs.getString(fieldname));
						System.out.println("\nCorrected: '"+oldval+"' to '"+retval+"' on 2nd try\n");				
					}
				}
			} catch ( SQLException e2) {
				System.err.println("For "+fieldname+": "+e2);
				e2.printStackTrace();
				System.exit(2);
			}
		}

		return retval;
			
	}
	
	
	/**
	 * @param rs
	 * @param title
	 * @return
	 */
	private String getValidStringOld(ResultSet rs, String fieldname) throws UnsupportedEncodingException{
		String retval="";
		try {
			retval= rs.getString(fieldname);
		} catch ( Exception e) {
			try { 
				BufferedReader in = new BufferedReader(
						new InputStreamReader(rs.getBinaryStream(fieldname), "8859_1"));
				retval = in.readLine();
			} catch ( Exception e2) {
				throw new UnsupportedEncodingException("Weird charset detected. Cannot decode data!" +e2.getMessage());
			}
			System.out.println("\nCorrected: '"+retval+"'\n");
		}
		return retval;
	}
	
	public static void main(String[] args) {
		if ( args.length < 1 ) {
			System.err.println("Usage: DBConverter username {password}");
			System.exit(1);
		} else {
			String userName = args[0];
			String password = "";
			if ( args.length>1 )
				password = args[1];
			
			DBConverter conv = new DBConverter();
			conv.initDatabases(userName,password);
			if ( conv.checkDatabases()) {
				conv.convert();
			} else {
				System.err.println("Invalid database format. Cannot convert.");
				System.exit(2);
			}
		}
	}

}
