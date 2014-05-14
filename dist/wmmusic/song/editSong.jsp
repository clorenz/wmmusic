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
  			<td><html:text name="editSongForm" property="year" value="<%= editSongForm.getYear()>-1?editSongForm.getYear()+"":"" %>" size="4"/></td>
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
  <jsp:include page="../statusline.jsp" flush="true"/>
</body>
</html:html>