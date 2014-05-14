<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
<head>
   <link rel=stylesheet href="../wmmusic.css" type="text/css">	
   <title>wmmusic - Artists</title>
   <html:base/>
</head>
  <body bgcolor="white">
  <jsp:include page="../menu.jsp" flush="true"/>
  <!-- artist/showArtistList.jsp -->
  <h2>Select an artist</h2>
  <table border="0" cellpadding="4" cellspacing="1" class="form">
  	<tr class="form">
  		<th>Artist </th>
  		<th>Print </th>
  	</tr>  
  	<logic:iterate id="artist" name="artistList">
  		<tr class="form">
  			<td><a href="/wmmusic/do/selectArtist?id=<bean:write name="artist" property="id"/>"><bean:write name="artist" property="name"/></a></td>
  			<td><bean:write name="artist" property="print"/></td>
  		</tr>
	</logic:iterate>
  </table>
<jsp:include page="../statusline.jsp" flush="true"/> 
</body>
</html:html>