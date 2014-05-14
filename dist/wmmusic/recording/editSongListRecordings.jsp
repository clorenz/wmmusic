<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="editSongForm" scope="request" class="de.christophlorenz.wmmusic.song.bean.EditSongBean"/>
<html:html locale="true">
<head>
   <link rel=stylesheet href="../wmmusic.css" type="text/css">	
   <title>wmmusic - Edit song</title>
   <html:base/>
</head>
  <body bgcolor="white" onLoad="document.forms.editSongForm.authors.focus()">
  <!-- recording/editSongListRecordings.jsp -->
  <jsp:include page="../menu.jsp" flush="true"/>
  
  <h1>Edit song</h1>
  <html:form action="/editSong">
  	<logic:present name="path">
    	<bean:define id="path" name="path"/>
  		<html:hidden name="path" property="path" value="<%= path.toString() %>"/>
  	</logic:present>
  	<html:hidden name="editSongForm" property="id"/>
  	<html:hidden name="editSongForm" property="artistId"/>
  	
  	<logic:present name="mediumid">
  		<input type="hidden" name="mediumId" value="<bean:write name="mediumid"/>">
  	</logic:present>
  	
  	<table border="0" cellpadding="4" cellspacing="1" class="form">
  		<tr class="form">
			<td>Artist: </td>
			<td><html:text name="editSongForm" property="artist" size="25" readonly="true"/></td>
		</tr>
		
		<tr class="form">
  			<td>Title: </td>
  			<logic:greaterThan name="editSongForm" property="id" value="0">
  				<td><html:text name="editSongForm" property="title" size="50"/></td>
  			</logic:greaterThan>
  			<logic:lessEqual name="editSongForm" property="id" value="0">	
  				<td><html:text name="editSongForm" property="title" size="50" readonly="true"/></td>
  			</logic:lessEqual>
  		</tr>
  		
  		<tr class="form"> 	
  			<td>Release: </td>
  			<td><html:text name="editSongForm" property="release" size="3"/></td>
  		</tr>
  		
  		<tr class="form">
  			<td>Year: </td>
  			<td><html:text name="editSongForm" property="year" value="<%= editSongForm.getYear()>0?editSongForm.getYear()+"":"" %>" size="4"/></td>
  		</tr>
  		
  		<tr class="form">
  			<td>Authors: </td>
  			<td><html:text name="editSongForm" property="authors" size="80"/></td>
  		</tr>
  	
  		<tr class="form">
  			<td>Dance: </td>
  			<td><html:text name="editSongForm" property="dance" size="2"/></td>
  		</tr>
  	
  		<tr class="form">
  			<td>Genre: </td>
  			<td><html:text name="editSongForm" property="id3Genre" size="24"/></td>
  		</tr>
  	
  		<tr class="form">
  			<td>Remarks: </td>
  			<td><html:textarea name="editSongForm" property="remarks" cols="80" rows="4"/></td>
  		</tr>

  		<tr class="form">
  			<td colspan="2" align="right"><html:submit/></td>
  		</tr>
  		
  	</table>
  </html:form>
  
  <h2>Occurrences</h2>
  
  <table border="0" cellpadding="4" cellspacing="1" class="form">
  <tr class="form">
    <th>Position </th>
    <th>Time </th>
    <th>Year </th>
    <th>LP </th>
    <th>Quality </th>
    <th>Special </th>
    <th>Digital </th>
    <th>Remarks </th>
  </tr>
  <logic:iterate id="recording" name="recordingList">
  <tr class="form">
  	<td><a href="/wmmusic/do/selectRecording?id=<bean:write name="recording" property="id"/>"> <bean:write name="recording" property="mediumTypeAbbrev"/> <bean:write name="recording" property="mediumCode"/>
  		<logic:greaterEqual name="recording" property="mediumType" value="5">
  			<logic:lessThan name="recording" property="mediumType" value="7">
  				<bean:write name="recording" property="side"/>
  			</logic:lessThan>
  		</logic:greaterEqual>
  		<logic:equal name="recording" property="mediumType" value="2">
  			<bean:write name="recording" property="track"/>
  		</logic:equal>
  		<logic:equal name="recording" property="mediumType" value="5">
  			<bean:write name="recording" property="track"/>
  		</logic:equal>
  		<logic:equal name="recording" property="mediumType" value="7">
  			<bean:write name="recording" property="track"/>
  		</logic:equal>
  		<logic:lessThan name="recording" property="mediumType" value="2">
  			<bean:write name="recording" property="counter"/>
  		</logic:lessThan></a>
  		<logic:greaterEqual name="recording" property="mediumType" value="5"><br><small><bean:write name="recording" property="mediumArtist"/> - <bean:write name="recording" property="mediumTitle"/></small></logic:greaterEqual>
  	</td>
  	<td><bean:write name="recording" property="rawTime"/> </td>
  	<td><bean:write name="recording" property="year"/> </td>
  	<td><bean:write name="recording" property="longplay"/> </td>
  	<td><bean:write name="recording" property="qualityVerbose"/> </td>
  	<td><bean:write name="recording" property="specialVerbose"/> </td>
  	<td><bean:write name="recording" property="digital"/> </td>
  	<td><bean:write name="recording" property="remarks"/></td>
  </tr>
  </logic:iterate>
  
  <logic:greaterThan name="editSongForm" property="id" value="0">
    <tr class="form">
  		<td colspan="8" align="right"><a href="/wmmusic/do/selectSong?path=ARTM&id=<bean:write name="editSongForm" property="id"/>&mediumId=<bean:write name="mediumid"/>"><html:button value="Add new recording" property=""/></a></td>
	</tr>
  </logic:greaterThan>
</table>
  <jsp:include page="../statusline.jsp" flush="true"/>
</body>
</html:html>