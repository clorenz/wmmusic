<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
<head>
   <title>wmmusic - Medium</title>
   <link rel=stylesheet href="../wmmusic.css" type="text/css">
   <html:base/>
</head>
  <body bgcolor="white">
  <jsp:include page="../menu.jsp" flush="true"/>
  
  <table border="0" cellpadding="4" cellspacing="1" class="form">
  <logic:iterate id="medium" name="mediumList">
  	<tr class="form">
  		<td><a href="/wmmusic/do/selectMedium?id=<bean:write name="medium" property="id"/>"><bean:write name="medium" property="code"/> </td>
  		<td><bean:write name="medium" property="artist"/> </td>
  		<td><bean:write name="medium" property="title"/></td>
  	</tr>
  </logic:iterate>
  </table>

<jsp:include page="../statusline.jsp" flush="true" />
</body>
</html:html>