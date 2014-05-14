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
  
  <h1>Statistics</h1>
  
  <h2>Song metadata</h2>
  
  <table border="0" cellpadding="4" cellspacing="1" class="form">
  	<tr class="form">
  		<td>Number of artists: </td>
  		<td><bean:write name="statistics" property="artists"/></td>
  	</tr>
  	<tr class="form">
  		<td>Number of songs: </td>
  		<td><bean:write name="statistics" property="songs"/></td>
  	</tr>
  	<tr class="form">
  		<td>Number of songs on original media: </td>
  		<td><bean:write name="statistics" property="originalSongs"/>  (<bean:write name="statistics" property="originalSongsPercent" format="0.0"/>%)</td>
  	</tr>
  </table>
  
  <br/>
  
  <h2>Media</h2>
  
  <table border="0" cellpadding="4" cellspacing="1" class="form">
  	<tr class="form">
  		<th>Medium type</td>
  		<th>Amount</td>
  		<th>Sum Value</td>
  		<th>Avg. Value</td>
  	</tr>
  	<logic:iterate id="medium" name="statistics" property="media">
  		<tr class="form">
  			<td>Number of <bean:write name="medium" property="value.name"/>: </td>
  			<td class="right"><bean:write name="medium" property="value.amount"/></td>
  			<td class="right"><bean:write name="medium" property="value.sumPrice" format="###0.00"/></td>
  			<td class="right"><bean:write name="medium" property="value.avgPrice" format="###0.00"/></td>
  		</tr>
  	</logic:iterate>
  </table>
  
  <br/>
  
  <h2>Recording qualities</h2>
  
  <table border="0" cellpadding="4" cellspacing="1" class="form">
  	<logic:iterate id="quality" name="statistics" property="qualities">
  		<tr class="form">
  			<td>Number of <bean:write name="quality" property="key"/> recordings: </td>
  			<td><bean:write name="quality" property="value"/></td>
  		</tr>
  	</logic:iterate>
  	
  	<tr class="form">
  		<td>Number of songs in perfect quality: </td>
  		<td><bean:write name="statistics" property="perfectSongs"/> (<bean:write name="statistics" property="perfectSongsPercent" format="0.0"/>%)</td>
  	</tr>
  
  </table>
  <jsp:include page="../statusline.jsp" flush="true"/>
</body>
</html:html>