<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<html:html locale="true">
<head>
	<link rel=stylesheet href="../wmmusic.css" type="text/css">	
   <title>wmmusic - Select artist</title>
   <html:base/>
</head>
  <body bgcolor="white">
  <!-- artist/selectArtist.jsp -->
  <jsp:include page="../menu.jsp" flush="true"/>
  <h1>Select Artist</h1>
  <html:form action="/selectArtist">
  	<table border="0" cellpadding="4" cellspacing="1" class="form">
  		<tr class="form">
			<td>Name: </td>
			<td><html:text name="selectArtistForm" property="artistName" size="25"/></td>
		</tr>
		<tr class="form">
  			<td>Exact match: </td>
  			<td><html:checkbox name="selectArtistForm" property="exact"/></td>
  		</tr>
  		<tr class="form">
  			<td colspan="2" align="right"><html:submit/></td>
  		</tr>
  	</table>
  </html:form>
  <jsp:include page="../statusline.jsp" flush="true"/>
</body>
</html:html>