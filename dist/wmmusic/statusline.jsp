<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-logic.tld" prefix="logic" %>
<hr>
<logic:present name="statusline">
  Status: <bean:write name="statusline"/>
  <bean:define id="statusline" value=""/>
</logic:present>