<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
<head>
   <title>wmmusic</title>
   <link rel=stylesheet href="wmmusic.css" type="text/css">	
   <html:base/>
</head>
  <body bgcolor="white">
  <jsp:include page="menu.jsp" flush="true"/>
  EEK!!!
  <logic:messagesPresent>
  <logic:present name="error">
<UL>
 <html:messages id="error"> 
 <LI><bean:write name="error"/></LI>
 </html:messages> 
</UL> 
</logic:present>
</logic:messagesPresent>
<br>
<jsp:include page="statusline.jsp" flush="true"/> 
</body>
</html:html>