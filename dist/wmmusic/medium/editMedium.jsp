<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="medium" scope="request" class="de.christophlorenz.wmmusic.medium.bean.EditMediumBean"/>
<html:html locale="true">
<head>
   <link rel=stylesheet href="../wmmusic.css" type="text/css">	
   <logic:equal name="medium" property="type" value="6"><title>wmmusic - Edit Single</title></logic:equal>
   <html:base/>
</head>
  <body bgcolor="white" onLoad="document.forms.editMediumForm.label.focus()">
  <jsp:include page="../menu.jsp" flush="true"/>
  
  <logic:equal name="medium" property="type" value="6">
  	<h1>Edit Single</h1>
  </logic:equal>	
  	
  <html:form action="/editMedium">
  	<table border="0" cellpadding="4" cellspacing="1" class="form">
  		
  		<tr class="form">
  			<td>ID: </td>
  			<td><html:text property="id" size="8" value="<%=medium.getId()>=0?medium.getId()+"":""%>" readonly="true"/></td>
  		</tr>
  		
  		<tr class="form">
  			<td>Code: </td>
  			<td><html:text property="code" size="8" value="<%=medium.getCode()%>"/></td>
  		</tr>
  		
  		
  		<!-- Stuff for Singles, LP and CD -->
  		<logic:greaterEqual name="medium" property="type" value="5">
  			<tr class="form">
  				<td>Artist: </td>
  				<td><html:text property="artist" size="50" value="<%=medium.getArtist()%>"/></td>
  			</tr>	
 
  			<tr class="form">
  				<td>Title: </td>
  				<td><html:text property="title" size="50" value="<%=medium.getTitle()%>"/></td>
  			</tr>
  		
  			<tr class="form">
  				<td>Record label: </td>
  				<td><html:text property="label" size="25" value="<%=medium.getLabel()%>"/></td>
  			</tr>
  			
  			<tr class="form">
  				<td>Ordercode: </td>
  				<td><html:text property="ordercode" size="25" value="<%=medium.getOrdercode()%>"/></td>
  			</tr>
  			
  			<tr class="form">
  				<td>Publish year: </td>
  				<td><html:text property="year" size="4" value="<%=medium.getYear()>0?medium.getYear()+"":""%>"/></td>
  			</tr>
  			
  			<logic:equal name="medium" property="showDigital" value="true">
  				<tr class="form">
  					<td>Digital: </td>
  					<td><html:text property="digital" size="3" value="<%=medium.getDigital()%>"/></td>
  				</tr>
  			</logic:equal>
  		
  			<tr class="form">
  				<td>Buy date: </td>
  				<td><html:text property="buyDate" size="10" value="<%=medium.getBuyDate()%>"/></td>
  			</tr>
  			
  			<tr class="form">
  				<td>Buy price: </td>
  				<td><html:text property="buyPrice" size="8" value="<%=medium.getBuyPrice()>0?medium.getBuyPrice()+"":""%>"/></td>
  			</tr>
  		</logic:greaterEqual>
  		
  		<tr class="form">
  			<td>Remarks: </td>
  			<td><html:textarea property="remarks" cols="50" rows="4" value="<%=medium.getRemarks()%>"/></td>
  		</tr>
  		
  		<html:hidden property="type" value="<%=medium.getType()+""%>"/>
  		<html:hidden property="artistId" value="<%=medium.getArtistId()+""%>"/>
  		
  		<tr class="form">
  			<td colspan="2" align="right">
  				<logic:greaterEqual name="medium" property="id" value="0">
  					<html:submit value="Delete" onclick="this.form.action='/wmmusic/do/deleteMedium'"/>
  					or <html:submit value="Edit Songs" onclick="this.form.action='/wmmusic/do/selectRecordingsOnMedium'"/>
  					or <html:submit value="Generate Sticker" onclick="this.form.action='/wmmusic/do/generateSticker'"/>
  				or <html:submit value="Update"/>
  				</logic:greaterEqual>
  				<logic:lessThan name="medium" property="id" value="0">
  					<html:submit value="Create"/>
  				</logic:lessThan> 
  			</td>
  		</tr>
  	</table>
</html:form>

<jsp:include page="../statusline.jsp" flush="true"/>
</body>
</html:html>