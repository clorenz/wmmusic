<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<html:html locale="true">
<head>
   <title>wmmusic - Select medium</title>
   <link rel=stylesheet href="../wmmusic.css" type="text/css">
   <html:base/>
</head>
  <body bgcolor="white" onLoad="document.forms.selectMediumForm.artist.focus()">
  <bean:define id="mediumtype" name="type"/>
  <jsp:include page="../menu.jsp" flush="true"/>
  <h2>Select <bean:write name="name"/></h1>
  <html:form action="/selectMedium">
  	<html:hidden name="selectMediumForm" property="type" value="<%=mediumtype.toString() %>"/>
  	<table border="0" cellpadding="4" cellspacing="1" class="form">
  		<logic:present name="id">
  		<tr class="form">
  			<td>Code: </td>
  		    <td><html:text name="selectMediumForm" property="code" size="8"/></td>
  		</tr>
  		</logic:present>
  		<logic:present name="artist">
  		<tr class="form">
  			<td>Artist: </td>
  			<td><html:text name="selectMediumForm" property="artist" size="25"/></td>
  		</tr>
  		</logic:present>
  		<logic:present name="title">
  		<tr class="form"> 
  			<td>Title: </td>
  			<td><html:text name="selectMediumForm" property="title" size="25"/></td>
  		</tr>
  		</logic:present>
  		<tr class="form">
  			<td></td>
  			<td><div align="right"><html:submit/></div></td>
  		</tr>
  	</table>
  </html:form>
  <jsp:include page="../statusline.jsp" flush="true" />
</body>
</html:html>