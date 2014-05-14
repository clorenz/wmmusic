<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<jsp:useBean id="artist" scope="request" class="de.christophlorenz.wmmusic.artist.bean.EditArtistBean"/>
  
<html:html locale="true">
<head>
	<link rel=stylesheet href="../wmmusic.css" type="text/css">	
   <title>wmmusic - Edit artist</title>
   <html:base/>
</head>
  <body bgcolor="white">
  <!-- artist/editArtist.jsp -->
  <jsp:include page="../menu.jsp" flush="true"/>
  <h1>Select Artist</h1>
  
  
  <html:form action="/editArtist">
  	<table border="0" cellpadding="4" cellspacing="1" class="form">
  		<tr class="form">
  			<td>ID: </td>
  			<td><html:text property="id" size="8" value="<%= (artist.getId()>-1)?artist.getId()+"":""%>" readonly="true"/></td>
  		</tr>
  		<tr class="form">
  			<td>Name: </td>
  			<td><html:text property="name" size="25" value="<%=artist.getName()%>"/></td>
  		</tr>
  		<tr class="form">
    		<td>Print: </td>
    		<td><html:text property="print" size="25" value="<%=artist.getPrint()%>"/></td>
    	</tr>
    	<tr class="form">
    		<td>Birthday: </td>
    		<td><html:text property="birthday" size="25" value="<%=artist.getBirthday()%>"/></td>
    	</tr>
    	<tr class="form">
    		<td>Country: </td>
    		<td><html:text property="country" size="25" value="<%=artist.getCountry()%>"/></td>
    	</tr>
    	<tr class="form">
    		<td>Location: </td>
    		<td><html:text property="location" size="25" value="<%=artist.getLocation()%>"/></td>
    	</tr>
    	<tr class="form">
    		<td>URL: </td>
    		<td><html:text property="url" size="25" value="<%=artist.getUrl()%>"/></td>
    	</tr>
    	<tr class="form">
    		<td>Remarks: </td>
    		<td><html:text property="remarks" size="25" value="<%=artist.getRemarks()%>"/></td>
    	</tr>
    	<tr class="form">
  			<td colspan="2" align="right"><html:submit value="Delete" onclick="this.form.action='/wmmusic/do/deleteArtist'"/> or <html:submit/></td>
  		</tr>
  	</table>
  </html:form>
  <jsp:include page="../statusline.jsp" flush="true"/>
</body>
</html:html>