<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
<head>
   <link rel=stylesheet href="../wmmusic.css" type="text/css">	
   <title>wmmusic - Select song</title>
   <html:base/>
</head>
  <body bgcolor="white">
  <jsp:include page="../menu.jsp" flush="true"/>
  
  <h1>Select song 
  <logic:present name="mediumName">
  	on <bean:write name="mediumName"/>
  </logic:present>
  <logic:present name="side">
  	<bean:write name="side"/>
  </logic:present>
  <logic:present name="track">
  	<bean:write name="track"/>
  </logic:present>
  </h1>
  
  <html:form action="/selectSong">
  	<logic:present name="path">
    	<bean:define id="path" name="path"/>
  		<html:hidden name="path" property="path" value="<%= path.toString() %>"/>
  	</logic:present>	
  	<logic:present name="mediumId">
  		<bean:define id="mediumid" name="mediumId"/>
  		<html:hidden name="selectSongForm" property="mediumId" value="<%= mediumid.toString() %>"/>
  	</logic:present>
  	<html:hidden name="selectSongForm" property="artistId"/>
  	
  	<table border="0" cellpadding="4" cellspacing="1" class="form">
  		<tr class="form">
  			<td>Artist: </td>
  			<td><html:text name="selectSongForm" property="artist" size="25"/></td>
  		</tr>
  		
  		<tr class="form">
  			<td>Title: </td>
  			<td><html:text name="selectSongForm" property="title" size="50"/></td>
  		</tr>
  		
  		<tr class="form">
  			<td>Exact match: </td>
  			<td><html:checkbox name="selectSongForm" property="exact"/></td>
  		</tr>
  		
  		<tr class="form">
  			<td colspan="2" align="right"><html:submit/></td>
  		</tr>
  		
  	</table>
  </html:form>

<jsp:include page="../statusline.jsp" flush="true"/>
<logic:notEqual name="selectSongForm" property="artist" value="">
	<script>
		self.focus();document.selectSongForm.title.focus();
	</script>
</logic:notEqual>
</body>
</html:html>