<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="org.apache.struts2.json.JSONUtil"%>
<jsp:include page="header.jsp" />
<jsp:include page="topNavigation.jsp" />
<jsp:include page="progress.jsp" />
<jsp:include page="tableInclude.jsp" />
<h1 style="position: absolute; top: 10px; left: 48%;">Analysis</h1><br><br><br>
<br><br>
<script src="http://ajax.googleapis.com/ajax/libs/dojo/1.6.1/dojo/dojo.xd.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/dojo/1.6.1/dojox/json/schema.js"></script>
<script >
 dojo.require("dojox.json.schema");
 dojo.require("dojox.json.ref");
 dojo.require("dijit.form.Form");
 dojo.require("dijit.form.Button");
 dojo.require("dijit.form.ValidationTextBox");
 dojo.require("dijit.form.Select");
 dojo.require("dojox.grid.DataGrid");
 dojo.require("dojo.data.ItemFileWriteStore");
 dojo.require("dijit.Dialog");

   function showHelp(){
	 window.open ('help/analysisHelp.html', '', 'width=600,scrollbars=1');
   }

   var storeData = {
	        identifier: 'id',
	        label: 'name',
	        items: <%out.println(JSONUtil.serialize(request.getAttribute("analysisViewList")));%>
	    };    
	var jsonStore = new dojo.data.ItemFileWriteStore({ data: storeData });	
	var grid = null;
	
   function validateAnalysis(){
	 
	 schema = {
			    properties : {
			    	            boo : {type:"number"},
			    	            foo : {type:"string"}
	                          }
	 };
	 
	 obj1 = {foo:"aa"};
	 
	 name = document.getElementById("analysisName").value;
	 obj = dojox.json.ref.fromJson(document.getElementById("jsonType").value);
	 
	 alert(name);
	 alert(obj);
	 results = dojox.json.schema.validate(obj, schema);
	 alert(results);
	 
	 var updateAnalysisForm = dijit.byId("updateAnalysisForm");
	 dojo.connect(updateAnalysisForm, "onSubmit", function(e) {
		 
		 if (updateAnalysisForm.validate() && results.valid) {
			 
			 alert("hello");
			 document.updateAnalysisForm.action = "saveAnalysis";
			 
		 }
		 
	 });
	 if(results.valid){
		 alert("True");
	 }
	 else {
		 alert("False");
	 }
 }

</script>


<div dojoType="dijit.form.Form" id="updateAnalysisForm" name="updateAnalysisForm" >
			
			<table>
            <tr><td align="right">Name:&nbsp;</td>
            <td align="left">
            <input type="text" id="analysisName" name="analysis.name" dojoType="dijit.form.ValidationTextBox" 
                                      maxlength=100 required="true" trim="true" missingMessage="Required."></td></tr>
            <tr><td align="right">Json:&nbsp;</td>
            <td align="left">
            <textarea id="jsonType" name="analysis.text" ></textarea></td></tr>
            <tr><td></td>
            <td align="center">
            <button type="submit" dojoType="dijit.form.Button" onclick="validateAnalysis()" >Save</button>
            </td></tr>
            </table>
            </div>
            <br />

<jsp:include page="footer.jsp" />
