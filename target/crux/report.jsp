<!--
 * Copyright 2011 Nube Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="org.apache.struts2.json.JSONUtil"%>
<jsp:include page="header.jsp" />
<jsp:include page="topNavigation.jsp" />
<jsp:include page="progress.jsp" />
<jsp:include page="tableInclude.jsp" />
<h1 style="position: absolute; top: 10px; left: 48%;">Report</h1>
<br>
<br>
<div style="color:#FF0000"><s:property value='%{error.message}'/></div>
<script type="text/javascript">
dojo.require("dijit.form.Form");
dojo.require("dojox.grid.DataGrid");
dojo.require("dojo.data.ItemFileWriteStore");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.Dialog");

function hideLinks(){
	document.getElementById("links").style.visibility = "hidden";
}

function showHelp(){
	 window.open ('help/reportHelp.html', '', 'width=600,scrollbars=1');
}
    
dojo.addOnLoad(function(){
	document.getElementById("addText").style.visibility = "hidden";
	var storeData = {
	        identifier: 'id',
	        label: 'name',
	        items: <%out.println(JSONUtil.serialize(request.getAttribute("reportList")));%>
	    };
	    
	var jsonStore = new dojo.data.ItemFileWriteStore({ data: storeData });
	var grid = null;
	
    var layoutReports = [[{
   					   	   field: "index",
  					   	   name: "Serial No.",
 					       width: "5"
   							 },
                          {
                           field: "id",
                           name: "Id",
                           width: "auto"
                       },
                       {
                           field: "name",
                           name: "Report Name",
                           width:  "auto"
                       },
                       {
                           field: "reportType",
                           name: "Report Type",
                           width:  "auto"
                       }]];

	grid = new dojox.grid.DataGrid({
		query: { id: '*' },
		store: jsonStore,
		structure: layoutReports,
		selectionMode: 'single',
		autoHeight:10
	}, 'gridNode');
	grid.layout.setColumnVisibility(1,false);
	grid.startup();
	if(grid.getItem(0)==null){
		 document.getElementById("addText").style.visibility = "visible";
		 document.getElementById("gridNode").style.visibility = "hidden";
		 hideLinks();
	}
	
	dojo.connect(grid, "onRowClick", function(e) {
		clearResponse();	
	});
	var deleteReportForm = dijit.byId("deleteReport");
	
	dojo.connect(deleteReportForm, "onClick", function(e) {
		if (setSelectedValue("reportId")) {
			clearResponse();			
			dijit.byId("deleteDialog").show();	 
		}
	});	
	
	var deleteTrue = dijit.byId("deleteTrue");
	dojo.connect(deleteTrue, "onClick", function(e) {
		showProgressIndicator();
		var xhrArgs = {
	                form: dojo.byId("deleteReport"),
	                url: "<s:url action='deleteReport'/>",
	                handleAs: "json",
	                load: function(data) {
	                	if(data.error.error){ 
	                		responseMessage("Error: "+data.error.message);
	      	                    	} else {
	                	 jsonStore.fetch({
	                	        query: { id: '*' },
	                	        onComplete: function(items, result){
	                	            dojo.forEach(items, function(item){
	                	                jsonStore.deleteItem(item);
	                	            });
	                	        }
	                	    });
	                	 jsonStore.save();
	                	 
	                	 if(data.reportList.length==0){
	                		 hideLinks();
	                		// responseMessage("No Report defined. Please click Add to create a new Report.");
	                		  document.getElementById("addText").style.visibility = "visible";
	                		 document.getElementById("gridNode").style.visibility = "hidden";
	                		
	                	 }
	                
	                	for(var i=0;i<data.reportList.length;i++){
	                	jsonStore.newItem(data.reportList[i]);
	            	}
	                    	}
	                	grid.selection.clear();
	                	hideProgressIndicator();
	                },
	                error: function(error) {
	                	responseMessage("Error:"+error);
	                	hideProgressIndicator();
	                }
			};
		var deferred = dojo.xhrPost(xhrArgs);
		dijit.byId("deleteDialog").hide();
	});	
	
});

function setSelectedValue(id){
	 var grid = dijit.byId('gridNode');
	 var items = grid.selection.getSelected();
	 if(items.length==1){
     dojo.forEach(items, function(selectedItem) {
                 var value = grid.store.getValues(selectedItem, 'id');
                 var element = document.getElementById(id);
                 element.setAttribute("value", value);
                
             });
     return true;
	 } else {
		 responseMessage("Please Select A Report");
		 return false;
	 }
}

function submitReport(){
		clearResponse();
	   if( setSelectedValue("repId")){
 		var form=document.getElementById("viewReportForm");
		form.submit();
	   }
	}
	
function submitEditReport(){
	clearResponse();
	if(setSelectedValue("repoId")){
	var form=document.getElementById("editReportForm");
		form.submit();
	}
	
}

function deleteFalse(){	
	dijit.byId("deleteDialog").hide();
}
</script>

<div style="width: 60%; float: center;">
<div id="gridNode" style="width: 200px; float: center;"></div>
</div>
<br />
<br />
<div id="addText" >
No report designed. Please <a style="background-color: white; 	padding:0px 0px 0px 0px;" href="<s:url action='getDesignPage'/>">add</a> a new report.
</div>
<div id="links" style="height: 80%;">
<table>
  <tr>
  <td width="50px"><a id="addReport" href="<s:url action='getDesignPage'/>">Add</a></td>
   
  <td width="50px">
  <div id="editLink">
 <s:a href="#" onClick='submitEditReport()'>Edit</s:a>
 </div>
</td>
    <td width="50px">
    <div dojoType="dijit.form.Form" id="deleteReport" name="deleteReport" style="float:center;">
			<input type="hidden" id="reportId" name="report.id"
				dojoType="dijit.form.TextBox" /> 
			<a href="#">Delete</a>	
</div ></td >
<td width="50px">
<div id='viewLink'>
<s:a href="#" onClick='submitReport()'>View</s:a>
</div>
</td>

</tr>
</table>
</div>

<s:form action="viewReport" id="viewReportForm">
	<s:hidden name="report.id" id="repId">
	</s:hidden>
</s:form>
<s:form action="editReport" id="editReportForm">
	<s:hidden name="report.id" id="repoId">
	</s:hidden>
</s:form>

<div id="deleteDialog" dojoType="dijit.Dialog" title="Confirm Deletion">
	Are you sure you want to delete? Deletion can not be undone.
	<br /><br />
	<button id="deleteTrue" dojoType="dijit.form.Button">Yes</button>
	<button id="deleteFalse" dojoType="dijit.form.Button" onClick="deleteFalse()">No</button>
</div> 

<jsp:include page="footer.jsp" />