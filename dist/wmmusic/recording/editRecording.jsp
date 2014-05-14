<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="editRecordingForm" scope="request" class="de.christophlorenz.wmmusic.recording.bean.EditRecordingBean"/>
<html:html locale="true">
<head>
   <link rel=stylesheet href="../wmmusic.css" type="text/css">	
   <title>wmmusic - Edit Recording</title>
   <html:base/>
</head>
  <body bgcolor="white" onLoad="document.forms.editRecordingForm.rawTime.focus()">
  <jsp:include page="../menu.jsp" flush="true"/>
  
  <h1>Edit Recording</h1>
  <html:form action="/editRecording">
    <logic:present name="path">
    	<bean:define id="path" name="path"/>
  		<html:hidden name="path" property="path" value="<%= path.toString() %>"/>
  	</logic:present>
  	<html:hidden property="songId"/>
  	<html:hidden property="mediumId" />
  	<html:hidden property="id" value="<%= editRecordingForm.getId()!=null?editRecordingForm.getId()+"":"-1" %>"/>
	
	<table border="0" cellpadding="4" cellspacing="1" class="form">
  		<tr class="form">
			<td>Artist: </td>
			<td><bean:write name="songBean" property="artist"/></td>
		</tr>
		
		<tr class="form">
			<td>Title: </td>
			<td><bean:write name="songBean" property="title"/></td>
		</tr>
		
		<tr class="form">
			<td>Release: </td>
			<td><bean:write name="songBean" property="release"/></td>
		</tr>
		
		<tr class="form">
			<td>Year: </td>
			<td><logic:greaterThan name="songBean" property="year" value="0"><bean:write name="songBean" property="year"/></logic:greaterThan></td>
		</tr>
		
		<tr class="form">
			<td>Authors: </td>
			<td><bean:write name="songBean" property="authors"/></td>
		</tr>
		
		<tr class="form">
			<td>Dance: </td>
			<td><bean:write name="songBean" property="dance"/></td>
		</tr>
		
		<tr class="form">
			<td>Remarks: </td>
			<td><bean:write name="songBean" property="remarks"/></td>
		</tr>
		
		<tr class="form">
		    <td colspan="2" align="right">
			  <a href="/wmmusic/do/selectSong?id=<bean:write name="songBean" property="id"/>&mediumId=<bean:write name="editRecordingForm" property="mediumId"/>&path=SONG"><html:button value="Edit song" property=""/></a>
  		</td></tr>
		
		<tr class="form">
			<td colspan="2"><hr></td>
		</tr>
  	
  		<tr class="form">
  			<td>Medium: </td>
  			<td><html:text property="medium" value="<%= editRecordingForm.getMediumTypeAbbrev()+" "+editRecordingForm.getMediumCode() %>" readonly="true"/></td>
  		</tr>
  	
  		<!-- SINGLE -->
  		<logic:equal name="editRecordingForm" property="mediumType" value="6">
  			<tr class="form">
  				<td>Side: </td>
  				<td><html:text property="side" size="1"/></td>
  			</tr>
  		</logic:equal>	
  		
  		<!-- LP -->
  		<logic:equal name="editRecordingForm" property="mediumType" value="5">
  			<tr class="form">
  				<td>Side: </td>
  				<td><html:text property="side" size="1"/></td>
  			</tr>
  		</logic:equal>
  		
  		<!-- AUDIO TAPE -->
  		<logic:equal name="editRecordingForm" property="mediumType" value="0">
  			<tr class="form">
  				<td>Side: </td>
  				<td><html:text property="side" size="1"/></td>
  			</tr>
  		</logic:equal>
  		
  		<!-- MD -->
  		<logic:equal name="editRecordingForm" property="mediumType" value="2">
  			<tr class="form">
  				<td>Track: </td>
  				<td><html:text property="track" size="1"/></td>
  			</tr>
  		</logic:equal>
  		
  		<!-- ROM -->
  		<logic:equal name="editRecordingForm" property="mediumType" value="3">
  			<tr class="form">
  				<td>Track: </td>
  				<td><html:text property="track" size="1"/></td>
  			</tr>
  		</logic:equal>
  		
  		<!-- LP -->
  		<logic:equal name="editRecordingForm" property="mediumType" value="5">
  			<tr class="form">
  				<td>Track: </td>
  				<td><html:text property="track" size="1"/></td>
  			</tr>
  		</logic:equal>
  		
  		<!-- CD -->
  		<logic:equal name="editRecordingForm" property="mediumType" value="7">
  			<tr class="form">
  				<td>Track: </td>
  				<td><html:text property="track" size="1"/></td>
  			</tr>
  		</logic:equal>
  		
  		<!-- AUDIO TAPE -->
  		<logic:equal name="editRecordingForm" property="mediumType" value="0">
  			<tr class="form">
  				<td>Counter: </td>
  				<td><html:text property="counter" size="1"/></td>
  			</tr>
  		</logic:equal>
  		
  		<!-- VIDEO TAPE -->
  		<logic:equal name="editRecordingForm" property="mediumType" value="1">
  			<tr class="form">
  				<td>Counter: </td>
  				<td><html:text property="counter" size="1"/></td>
  			</tr>
  		</logic:equal>
  		
  		<!-- FILES -->
  		<logic:equal name="editRecordingForm" property="mediumType" value="3">
  			<tr class="form">
  				<td>Counter: </td>
  				<td><html:text property="counter" size="1"/></td>
  			</tr>
  		</logic:equal>
  		
  		<!-- COMMON STUFF -->	
  		<tr class="form">
  			<td>Time: </td>
  			<td><html:text property="rawTime" size="6"/></td>
  		</tr>
  		
  		<tr class="form">
  			<td>Recording Year: </td>
  			<td><html:text property="year" value="<%= editRecordingForm.getYear()>0?editRecordingForm.getYear()+"":"" %>" size="4"/></td>
  		</tr>
  		
  		<tr class="form">
  			<td>Quality: </td>
  			<td>Stereo: <html:checkbox property="stereo" />
  				Noisefree: <html:checkbox property="noisefree" />
  				Complete: <html:checkbox property="complete" /></td>
  		</tr>
  		
  		<tr class="form">
  			<td>Special bits: </td>
  			<td>Maxi: <html:checkbox property="maxi" />
  				Live: <html:checkbox property="live" />
  				Remix: <html:checkbox property="remix" />
  				Video: <html:checkbox property="video" /></td>
  		</tr>
  	
  		<tr class="form">
  			<td>Digital: </td>
  			<td><html:text property="digital" size="3"/></td>
  		</tr>
  	
  		<tr class="form">
  			<td>Remarks: </td>
  			<td><html:textarea property="remarks" cols="80" rows="4"/></td>
  		</tr>
  	
  		<tr class="form">
  			<td colspan="2" align="right">
  			<logic:greaterEqual name="editRecordingForm" property="id" value="0">
  			<html:submit value="update"/>
  			</logic:greaterEqual>
  			<logic:lessThan name="editRecordingForm" property="id" value="0">
  			<html:submit/>
  			</logic:lessThan>
  			</td>
  		</tr>
  	</table>
  </html:form>
  
<jsp:include page="../statusline.jsp" flush="true"/> 
</body>
</html:html>