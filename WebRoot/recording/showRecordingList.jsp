<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
<head>
   <link rel=stylesheet href="../wmmusic.css" type="text/css">	
   <title>wmmusic - Recordings</title>
   <html:base/>
</head>
  <body bgcolor="white" onLoad="document.forms.addnewsong.addnewsong.focus()">
  <!-- recording/showRecordingList.jsp -->
  <jsp:include page="../menu.jsp" flush="true"/>
  
  <table border="0" cellpadding="4" cellspacing="1" class="form">
  <tr class="form">
  	<th>Medium</th>
  	<th>Pos.</th>
  	<th>Artist</th>
  	<th>Song</th>
  	<th>Authors</th>
  	<th>Time</th>
  </tr>
  
  <logic:iterate id="song" name="recordingList">
  <tr class="form">
  	<td><a href="/wmmusic/do/selectRecording?id=<bean:write name="song" property="id"/>"> <bean:write name="song" property="mediumTypeAbbrev"/> <bean:write name="song" property="mediumCode"/></a>
  	<td align="right">	
  		<logic:greaterEqual name="medium" property="type" value="5">
  			<logic:lessThan name="medium" property="type" value="7">
  				<bean:write name="song" property="side"/>
  			</logic:lessThan>
  		</logic:greaterEqual>
  		<logic:equal name="medium" property="type" value="5">
  			<bean:write name="song" property="track"/>
  		</logic:equal>
  		<logic:equal name="medium" property="type" value="7">
  			<bean:write name="song" property="track"/>
  		</logic:equal>
  		<logic:lessThan name="medium" property="type" value="2">
  			<bean:write name="song" property="counter"/>
  		</logic:lessThan>
  	</td>
  	<td><bean:write name="song" property="artist"/> </td>
  	<td><bean:write name="song" property="title"/> </td>
  	<td><small><bean:write name="song" property="authors"/> </td>
  	<td><bean:write name="song" property="rawTime"/> </td>
  </tr>
  </logic:iterate>
  <form name="addnewsong">
  <tr class="form"><td colspan="6" align="right">
	<a href="/wmmusic/do/addRecordingToMedium?path=ARTM&artistId=<bean:write name="medium" property="artistId"/>&mediumId=<bean:write name="medium" property="id"/>"><html:button value="Add new song" property="" styleId="addnewsong"/></a>
  </td></tr>
  </form>
</table>
<a href="/wmmusic/do/selectMedium?id=<bean:write name="medium" property="id"/>">Back to Medium</a>
<jsp:include page="../statusline.jsp" flush="true"/> 
</body>
</html:html>