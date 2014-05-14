<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="editSongAndRecordingForm" scope="request" class="de.christophlorenz.wmmusic.song.bean.EditSongAndRecordingBean"/>
<html:html locale="true">
<head>
   <link rel=stylesheet href="../wmmusic.css" type="text/css">	
   <title>wmmusic - Edit song and recording</title>
   <html:base/>
   <script type="text/javascript">
		function checkYear () {
			if (document.editSongAndRecordingForm.recordingYear.value == "" ) {
				if (document.editSongAndRecordingForm.year.value != "" ) {
					document.editSongAndRecordingForm.recordingYear.value = document.editSongAndRecordingForm.year.value;
				}
			}
			return true;
		}
	</script>
</head>
  <body bgcolor="white" onload="self.focus();document.editSongAndRecordingForm.authors.focus();">
  <jsp:include page="../menu.jsp" flush="true"/>
  
  <h1>Edit song and recording</h1>
  <html:form action="/editSongAndRecording">
  	<logic:present name="path">
    	<bean:define id="path" name="path"/>
  		<html:hidden name="path" property="path" value="<%= path.toString() %>"/>
  	</logic:present>
  	<html:hidden name="editSongAndRecordingForm" property="artistId"/>
  	
  	<table border="0" cellpadding="4" cellspacing="1" class="form">
  		<tr class="form">
			<td>Artist: </td>
			<td><html:text name="editSongAndRecordingForm" property="artist" size="25" readonly="true"/></td>
		</tr>
		
		<tr class="form">
  			<td>Title: </td>
  			<td><html:text name="editSongAndRecordingForm" property="title" size="50" readonly="true"/></td>
  		</tr>
  		
  		<tr class="form"> 	
  			<td>Release: </td>
  			<td><html:text name="editSongAndRecordingForm" property="release" size="3"/></td>
  		</tr>
  		
  		<tr class="form">
  			<td>Year: </td>
  			<td><html:text name="editSongAndRecordingForm" property="year" value="<%= editSongAndRecordingForm.getYear()>-1?editSongAndRecordingForm.getYear()+"":"" %>" size="4" onchange="return checkYear()"/></td>
  		</tr>
  		
  		<tr class="form">
  			<td>Authors: </td>
  			<td><html:text name="editSongAndRecordingForm" property="authors" size="80"/></td>
  		</tr>
  	
  		<tr class="form">
  			<td>Dance: </td>
  			<td><html:text name="editSongAndRecordingForm" property="dance" size="2"/></td>
  		</tr>
  	
  		<tr class="form">
  			<td>Genre: </td>
  			<td><html:text name="editSongAndRecordingForm" property="id3Genre" size="24"/></td>
  		</tr>
  	
  		<tr class="form">
  			<td>Remarks: </td>
  			<td><html:textarea name="editSongAndRecordingForm" property="remarks" cols="80" rows="4"/></td>
  		</tr>
  		
  		<tr class="form">
  			<td colspan="2"><hr></td>
  		</tr>
  	
  		<tr class="form">
  			<td>Medium: </td>
  			<td><html:text name="editSongAndRecordingForm" property="medium" value="<%= editSongAndRecordingForm.getMediumTypeAbbrev()+" "+editSongAndRecordingForm.getMediumCode() %>" readonly="true"/></td>
  		</tr>
  	
  		<html:hidden name="editSongAndRecordingForm" property="mediumId" />
  	
  		<!-- SINGLE -->
  		<logic:equal name="editSongAndRecordingForm" property="mediumType" value="6">
  			<tr class="form">
  				<td>Side: </td>
  				<td><html:text name="editSongAndRecordingForm" property="side" size="1"/></td>
  			</tr>
  		</logic:equal>	
  		
  		<!-- LP -->
  		<logic:equal name="editSongAndRecordingForm" property="mediumType" value="5">
  			<tr class="form">
  				<td>Side: </td>
  				<td><html:text name="editSongAndRecordingForm" property="side" size="1"/></td>
  			</tr>
  		</logic:equal>	
  		
  		<!-- AUDIO TAPE -->
  		<logic:equal name="editSongAndRecordingForm" property="mediumType" value="0">
  			<tr class="form">
  				<td>Side: </td>
  				<td><html:text name="editSongAndRecordingForm" property="side" size="1"/></td>
  			</tr>
  		</logic:equal>	
  		
  		<!-- MD -->
  		<logic:equal name="editSongAndRecordingForm" property="mediumType" value="2">
  			<tr class="form">
  				<td>Track: </td>
  				<td><html:text name="editSongAndRecordingForm" property="track" size="1"/></td>
  			</tr>
  		</logic:equal>	
  		
  		<!-- ROM -->
  		<logic:equal name="editSongAndRecordingForm" property="mediumType" value="3">
  			<tr class="form">
  				<td>Track: </td>
  				<td><html:text name="editSongAndRecordingForm" property="track" size="1"/></td>
  			</tr>
  		</logic:equal>
  		
  		<!-- LP -->
  		<logic:equal name="editSongAndRecordingForm" property="mediumType" value="5">
  			<tr class="form">
  				<td>Track: </td>
  				<td><html:text name="editSongAndRecordingForm" property="track" size="1"/></td>
  			</tr>
  		</logic:equal>
  		
  		<!-- CD -->
  		<logic:equal name="editSongAndRecordingForm" property="mediumType" value="7">
  			<tr class="form">
  				<td>Track: </td>
  				<td><html:text name="editSongAndRecordingForm" property="track" size="1"/></td>
  			</tr>
  		</logic:equal>
  		
  		<!-- AUDIO TAPE -->
  		<logic:equal name="editSongAndRecordingForm" property="mediumType" value="0">
  			<tr class="form">
  				<td>Counter: </td>
  				<td><html:text name="editSongAndRecordingForm" property="counter" size="1"/></td>
  			</tr>
  		</logic:equal>
  		
  		<!-- VIDEO TAPE -->
  		<logic:equal name="editSongAndRecordingForm" property="mediumType" value="1">
  			<tr class="form">
  				<td>Counter: </td>
  				<td><html:text name="editSongAndRecordingForm" property="counter" size="1"/></td>
  			</tr>
  		</logic:equal>
  		
  		<!-- FILES -->
  		<logic:equal name="editSongAndRecordingForm" property="mediumType" value="3">
  			<tr class="form">
  				<td>Counter: </td>
  				<td><html:text name="editSongAndRecordingForm" property="counter" size="1"/></td>
  			</tr>
  		</logic:equal>
  		
  		<!-- COMMON STUFF -->	
  		<tr class="form">
  			<td>Time: </td>
  			<td><html:text name="editSongAndRecordingForm" property="rawTime" size="6"/></td>
  		</tr>
  	
  		<tr class="form">
  			<td>Recording Year: </td>
  			<td><html:text name="editSongAndRecordingForm" property="recordingYear" value="<%= editSongAndRecordingForm.getRecordingYear()>-1?editSongAndRecordingForm.getRecordingYear()+"":"" %>" size="4"/></td>
  		</tr>
  	
  		<tr class="form">
  			<td>Quality: </td>
  			<td>Stereo: <html:checkbox name="editSongAndRecordingForm" property="stereo" value="<%= editSongAndRecordingForm.getStereo()?"on":"off" %>"/>
  			Noisefree: <html:checkbox name="editSongAndRecordingForm" property="noisefree" value="<%= editSongAndRecordingForm.getNoisefree()?"on":"off" %>"/>
  			Complete: <html:checkbox name="editSongAndRecordingForm" property="complete" value="<%= editSongAndRecordingForm.getComplete()?"on":"off" %>"/></td>
  		</tr>
  		
		<tr class="form">
  			<td>Special bits: </td>
  			<td>Maxi: <html:checkbox name="editSongAndRecordingForm" property="maxi" value="<%= editSongAndRecordingForm.getMaxi()?"on":"off" %>"/>
  				Live: <html:checkbox name="editSongAndRecordingForm" property="live" value="<%= editSongAndRecordingForm.getLive()?"on":"off" %>"/>
  				Remix: <html:checkbox name="editSongAndRecordingForm" property="remix" value="<%= editSongAndRecordingForm.getRemix()?"on":"off" %>"/>
  				Video: <html:checkbox name="editSongAndRecordingForm" property="video" value="<%= editSongAndRecordingForm.getVideo()?"on":"off" %>"/></td>
  		</tr>
  		
  		<tr class="form">
  			<td>Digital: </td>
  			<td><html:text name="editSongAndRecordingForm" property="digital" value="<%= editSongAndRecordingForm.getDigital() %>" size="3"/></td>
  		</tr>
  	
  		<tr class="form">
  			<td>Remarks: </td>
  			<td><html:textarea name="editSongAndRecordingForm" property="recordingRemarks" value="<%= editSongAndRecordingForm.getRecordingRemarks() %>" cols="80" rows="4"/></td>
  		</tr>

  		<tr class="form">
  			<td colspan="2" align="right"><html:submit/></td>
  		</tr>
  		
  	</table>
  </html:form>
  <jsp:include page="../statusline.jsp" flush="true"/>
</body>
</html:html>