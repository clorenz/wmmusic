<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
<head>
   <link rel=stylesheet href="../wmmusic.css" type="text/css">
   <title>wmmusic - Stickerlist</title>
   <html:base/>
</head>
  <body bgcolor="white">
  <table cellpadding="1" cellspacing="0" class="sticker">
  <logic:iterate id="sticker" name="stickerlist">
  	<tr class="sticker"><td class="sticker"><div id="stickercode"><bean:write name="sticker" property="code" filter="false"/></div>
  	    <logic:equal name="sticker" property="showDetails" value="true">
  	    	<table border=0 cellspacing=0 cellpadding=0 width="100%"><tr><td><div id="stickerdate"><bean:write name="sticker" property="buyDate"/>&nbsp;</div></td>
  	    	<td><div id="stickerprice"><bean:write name="sticker" property="buyPrice"/></div></td></tr></table>
  	    </logic:equal>
  	    </td>
  	<td width="100%">&nbsp;&nbsp;<bean:write name="sticker" property="artist"/> - <bean:write name="sticker" property="title"/></td>
  	</tr>
  </logic:iterate>
  </table>
  <hr>
  <a href="/wmmusic/do/deleteStickerTags">Delete Sticker</a>
</body>
</html:html>