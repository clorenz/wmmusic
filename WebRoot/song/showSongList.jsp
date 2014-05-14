<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
<head>
   <link rel=stylesheet href="../wmmusic.css" type="text/css">	
   <title>wmmusic - Songs</title>
   <html:base/>
</head>
  <body bgcolor="white">
  <!-- song/showSongList.jsp -->
  <jsp:include page="../menu.jsp" flush="true"/>
  <h2>Select a song</h2>
  <table border="0" cellpadding="4" cellspacing="1" class="form">
  <tr class="form">
  	<th>Artist </th>
  	<th>Song </th>
  	<th>Rel. </th>
  	<th>Year </th>
  	<th>Authors </th>
  	<th>Dance </th>
  	<th>Remarks</th>
  </tr>
  <logic:iterate id="song" name="songList">
  <logic:equal name="song" property="bestQuality" value="true">
  	<bean:define id="formclass" value="formok"/>
  </logic:equal>
  <logic:notEqual name="song" property="bestQuality" value="true">
  	<bean:define id="formclass" value="formbad"/>
  </logic:notEqual>
  <tr class="<bean:write name="formclass"/>">
  	<td><bean:write name="song" property="artist"/></td>
  	<logic:present name="mediumid">
  		<td><a href="/wmmusic/do/selectSong?id=<bean:write name="song" property="id"/>&mediumId=<bean:write name="mediumid"/>"><bean:write name="song" property="title"/></a></td>
  	</logic:present>
  	<logic:notPresent name="mediumid">	
  		<td><a href="/wmmusic/do/selectSong?id=<bean:write name="song" property="id"/>"><bean:write name="song" property="title"/></a></td>
  	</logic:notPresent>
  	<td><bean:write name="song" property="release"/></td>
  	<td><logic:greaterThan name="song" property="year" value="0"><bean:write name="song" property="year"/></logic:greaterThan></td>
  	<td><bean:write name="song" property="authors"/></td>
  	<td><bean:write name="song" property="dance"/></td>
  	<td><bean:write name="song" property="remarks"/></td>
  </tr>
  </logic:iterate>
</table>
<jsp:include page="../statusline.jsp" flush="true"/> 
</body>
</html:html>