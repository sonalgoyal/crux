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
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="org.apache.struts2.json.JSONUtil"%>
<jsp:include page="header.jsp" />
<jsp:include page="topNavigation.jsp" />
<jsp:include page="progress.jsp" />
<jsp:include page="tableInclude.jsp" />
<h1 style="position: absolute; top: 10px; left: 48%;">Connection</h1>
<br>
A connection defines the location of your HBase server. Please define the server and port. Please give a unique name to the connection so that you can use it throughout.
<br>
<br>
<script>
dojo.require("dijit.form.Form");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.Select");
dojo.require("dojox.grid.DataGrid");
dojo.require("dojo.data.ItemFileWriteStore");
dojo.require("dijit.Dialog");

function showHelp(){
	 window.open ('help/connectionHelp.html', '', 'width=600,scrollbars=1');
}

var storeData = {
        identifier: 'id',
        label: 'name',
        items: <%out.println(JSONUtil.serialize(request.getAttribute("connectionViewList")));%>
    };    
var jsonStore = new dojo.data.ItemFileWriteStore({ data: storeData });	
var grid = null;  

function updateConnectionViewList(data){
	jsonStore.fetch({
		query: { id: '*' },
		onComplete: function(items, result){
    		dojo.forEach(items, function(item){
        		jsonStore.deleteItem(item);
    		});
		}
	});
	jsonStore.save();
	
	if(data.connectionViewList.length==0){
		 responseMessage("No connection defined. Please click Add to create a new connection.");
		document.getElementById("gridNode").style.visibility = "hidden";
		document.getElementById("linksForConnection").style.visibility = "hidden";
    	showConnectionForm();
	}
        else{
		document.getElementById("gridNode").style.visibility = "visible";
		document.getElementById("linksForConnection").style.visibility = "visible";
    	        dijit.byId("gridNode").selection.clear();  
	}
	//alert("deleted");
	for(var i=0;i<data.connectionViewList.length;i++){
    	jsonStore.newItem(data.connectionViewList[i]);
	}
}

dojo.addOnLoad(function(e) {
    var layoutConnections = [[{
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
       							 name: "Name",
       							 width:  "auto"
    						},
      						 {
      							 field: "datastoreName",
       						 	 name: "Datastore Name",
      							 width: "auto"
  							 },
       						 {
       							 field: "hbaseRestServerPropertyValue",
        						 name: "HBase Rest Server",
       							 width: "auto"
   							 }]];
	
	grid = new dojox.grid.DataGrid({
		query: { id: '*' },
		store: jsonStore,
		structure: layoutConnections,
		selectionMode: 'single',
		autoHeight:5
	}, 'gridNode');
	grid.layout.setColumnVisibility(1,false);
	grid.startup();
	
	if(grid.getItem(0)==null){
	 responseMessage("No connection defined. Please click Add to create a new connection.");
	 	document.getElementById("gridNode").style.visibility = "hidden";
		document.getElementById("linksForConnection").style.visibility = "hidden";
    	showConnectionForm();
	}
});

dojo.addOnLoad(function(e){
	dojo.connect(grid, "onRowClick", function(e) {
		var isAddValue = document.getElementById("isAdd").getAttribute("value");
        var isEditValue = document.getElementById("isEdit").getAttribute("value");
        clearResponse();
        if(isEditValue == "true" && isAddValue == "false"){
        	var items = grid.selection.getSelected();
			if(items.length==1){
            dojo.forEach(items, function(selectedItem) {
                        var idValue = grid.store.getValues(selectedItem, 'id');
                        document.getElementById("connectionId").value = idValue;  
                        
                        var ConnectionNameValue = grid.store.getValues(selectedItem, 'name');
                        //document.getElementById('connectionName').value = ConnectionNameValue;
                        dijit.byId('connectionName').set('value',ConnectionNameValue);
                        
                        /*var hostPropertyValue = grid.store.getValues(selectedItem, 'hostPropertyValue');
                        //document.getElementById('hostPropertyValue').value = hostPropertyValue;
                        dijit.byId('hostPropertyValue').set('value',hostPropertyValue);*/
                        
                        var portPropertyValue = grid.store.getValues(selectedItem, 'hbaseRestServerPropertyValue');
                        //document.getElementById('portPropertyValue').value = portPropertyValue;
                        dijit.byId('hbaseRestServerPropertyValue').set('value',portPropertyValue);
                    }); // end forEach
            }        	
        }
	});
});


dojo.addOnLoad(function(e){
	var editConnectionLink = dijit.byId("editConnection");
		
		dojo.connect(editConnectionLink, "onClick", function(e) {
			clearResponse();
			clearValues();
			document.getElementById("updateConnectionForm").style.visibility = "hidden";  
			dijit.byId("updateConnectionForm").reset();     
			var items = grid.selection.getSelected();
			if(items.length==1){
            dojo.forEach(items, function(selectedItem) {
                        var idValue = grid.store.getValues(selectedItem, 'id');
                        document.getElementById("connectionId").value = idValue;  
                        
                        var ConnectionNameValue = grid.store.getValues(selectedItem, 'name');
                        //document.getElementById('connectionName').value = ConnectionNameValue;
                        dijit.byId('connectionName').set('value',ConnectionNameValue);
                        
                        /*var hostPropertyValue = grid.store.getValues(selectedItem, 'hostPropertyValue');
                        //document.getElementById('hostPropertyValue').value = hostPropertyValue;
                        dijit.byId('hostPropertyValue').set('value',hostPropertyValue);*/
                        
                        var portPropertyValue = grid.store.getValues(selectedItem, 'hbaseRestServerPropertyValue');
                        //document.getElementById('portPropertyValue').value = portPropertyValue;
                        dijit.byId('hbaseRestServerPropertyValue').set('value',portPropertyValue);
                    }); // end forEach
                    
            document.getElementById("updateConnectionForm").style.visibility = "visible";  
            document.getElementById("isAdd").value = "false";  
            document.getElementById("isEdit").value = "true";     
			} else{
				responseMessage("Please select a connection.");
			}
		});
});
			

function showConnectionForm(){
		clearResponse();
		clearValues();
		dijit.byId("updateConnectionForm").reset();      
		document.getElementById("updateConnectionForm").style.visibility = "visible";  
		document.getElementById("isAdd").value = "true";  
		document.getElementById("isEdit"). value = "false"; 
}

function clearValues(){
	document.getElementById('connectionId').value = 0;
	document.getElementById('isAdd').value = '';
	document.getElementById('isEdit').value = '';
	document.getElementById('connectionName').value = '';
	//document.getElementById('hostPropertyValue').value = '';
	document.getElementById('hbaseRestServerPropertyValue').value = '';
}

dojo.addOnLoad(function(e) {
	var updateConnectionForm = dijit.byId("updateConnectionForm");    
    dojo.connect(updateConnectionForm, "onSubmit", function(e) {
    	
    	clearResponse();
    	e.preventDefault();
        
        var isAddValue = document.getElementById("isAdd").getAttribute("value");  
        //alert("isAddValue: " + isAddValue);
        var isEditValue = document.getElementById("isEdit").getAttribute("value");
        //alert("isEditValue: " + isEditValue);
        
        //alert(updateConnectionForm.isValid());
        if (updateConnectionForm.validate() && isEditValue == "true" && isAddValue == "false") { 
        	showProgressIndicator();
        		var xhrArgs = {
                    form: dojo.byId("updateConnectionForm"),
                    url: "<s:url action='updateConnection'/>",
                    handleAs: "json",                    
                    load: function(data) {
                    	 hideProgressIndicator(); 
                    	console.log("Data is  " + data);
                    	//alert(data.error);
                    	if(data.error.error){
                    		 responseMessage("Error: "+data.error.message);
                        }else{
                    		//alert ("Returned successfully");
                    		updateConnectionViewList(data); 
                    		clearValues();
                    		updateConnectionForm.reset();                			
                    		document.getElementById("updateConnectionForm").style.visibility = "hidden"; 
                    	}                    	
                    },
                    error: function(error) {
                    	 hideProgressIndicator(); 
                        //We'll 404 in the demo, but that's okay.  We don't have a 'postIt' service on the
                         //docs server.
                         console.log("Error " + error);
                    }
                };         	
                //Call the asynchronous xhrPost
              var deferred = dojo.xhrPost(xhrArgs);
         } else if (updateConnectionForm.validate() && isEditValue == "false" && isAddValue == "true") { 
        	 showProgressIndicator();
        	 	var xhrArgs = {
                        form: dojo.byId("updateConnectionForm"),
                        url: "<s:url action='saveConnection'/>",
                        handleAs: "json",
                        load: function(data) {
                        	 hideProgressIndicator(); 
                        	console.log("Data is  " + data);
                        	if(data.error.error){
                        		 responseMessage("Error: "+data.error.message);
                            }else{
                        	//alert ("Returned successfully");
                        	updateConnectionViewList(data); 
                        	clearValues();
                        	updateConnectionForm.reset();           			
                    		document.getElementById("updateConnectionForm").style.visibility = "hidden";
                        	}                           	
                        },
                        error: function(error) {
                        	 hideProgressIndicator(); 
                            //We'll 404 in the demo, but that's okay.  We don't have a 'postIt' service on the
                             //docs server.
                             console.log("Error " + error);
                        }
                    };       	
                    //Call the asynchronous xhrPost
                  var deferred = dojo.xhrPost(xhrArgs);
           } else {
        	   responseMessage("Please fill up the form with required and correct values.");
           }
       
    });  
});


dojo.addOnLoad(function(e){
	var deleteConnectionLink = dijit.byId("deleteConnection");		
		dojo.connect(deleteConnectionLink, "onClick", function(e) {
			clearResponse();
			var items = grid.selection.getSelected(); 
		    if(items.length==1){
				document.getElementById("updateConnectionForm").style.visibility = "hidden";  
				dijit.byId("updateConnectionForm").reset();     
				dijit.byId("deleteConnectionDialog").show();
		    }else{
		    	responseMessage("Please select a connection.");
		    }
		});	
});


function deleteTrue() {		
	var deleteConnectionLink = dijit.byId("deleteConnection");	
	var items = grid.selection.getSelected(); 
    if(items.length==1){
    dojo.forEach(items, function(selectedItem) {
                var value = grid.store.getValues(selectedItem, 'id');
                var id = document.getElementById("connectionIdToDelete").value = value;
            }); // end forEach
            
    	if (deleteConnectionLink.validate()) {
    			showProgressIndicator();
				var xhrArgs = {
                	form: dojo.byId("deleteConnection"),
                	url: "<s:url action='deleteConnection'/>",
                	handleAs: "json",
               		load: function(data) {
               			hideProgressIndicator();   		
                		console.log("Data is  " + data);
                		if(data.error.error){
                		 responseMessage("Error: "+data.error.message);
                		 //dijit.byId("deleteErrorDialog").show();
                    	}else{
                		//alert ("Returned successfully");
                		updateConnectionViewList(data);
                		//document.getElementById("updateConnectionForm").style.visibility = "hidden";
                		} 
                	},
                	error: function(error) {
                		hideProgressIndicator();   		
                		//alert("error:"+error);
               	 	}
				};
			var deferred = dojo.xhrPost(xhrArgs);
			
		}  
    }else{
    	responseMessage("Please select a connection.");
    }
    dijit.byId("deleteConnectionDialog").hide();	
}

function deleteFalse(){	
	dijit.byId("deleteConnectionDialog").hide();
}

dojo.addOnLoad(function(e) {
	dijit.byId("hbaseRestServerPropertyValue").validator = function (value, constraints) {
		return testPattern(value, constraints);		
	}
});


function testPattern(value, constraints) {
	var returnValue = true;
	var hbaseRestServerPropertyValue = value.split(",");
	var length = hbaseRestServerPropertyValue.length;
	
	var regExp = new RegExp("[a-zA-Z0-9.-]+:[0-9]");
	var returnValue = true;
	
	for( i=0; i<length; i++){
		//hbaseRestServerPropertyValue[i].match(regExp)
		if(regExp.test(hbaseRestServerPropertyValue[i])){
			var integerValue = hbaseRestServerPropertyValue[i].split(":")[1];
			if (integerValue != parseInt(integerValue)) {
				returnValue = false;
			}
		}else{
			returnValue = false;
		}
	}
	return returnValue;
}




</script>

<div id="deleteConnectionDialog" dojoType="dijit.Dialog" title="Confirm Deletion">
	 Are you sure you want to delete? Deletion can not be undone.
	<br /><br />
	<button id="deleteTrue" dojoType="dijit.form.Button" onClick="deleteTrue()">Yes</button>
	<button id="deleteFalse" dojoType="dijit.form.Button" onClick="deleteFalse()">No</button>
</div> 

<div style="width: 60%; float: center;">
	<div id="gridNode" style="width: 200px; float: center;"></div>
</div>

<br />

<table id="linksForConnection">
<tr>
<td align="center" width="50px">
<div dojoType="dijit.form.Form" id="addNewConnection">
		<a href="#" onClick="showConnectionForm()">Add</a>
</div>
</td>
<td align="center" width="50px">
<div dojoType="dijit.form.Form" id="editConnection" name="editConnection" style="float:center;">
	<a href="#">Edit</a>
</div>
</td>
<td align="center" width="50px">
<div dojoType="dijit.form.Form" id="deleteConnection" name="deleteConnection" style="float:center;">
	<input type="hidden" id="connectionIdToDelete" name="connection.id"	dojoType="dijit.form.TextBox" /> 
	<a href="#">Delete</a>
</div>
</td>
</tr>
</table>

<br />

<div dojoType="dijit.form.Form" id="updateConnectionForm" name="updateConnectionForm" style="visibility:hidden">
			<input type="hidden" id="isAdd" name="isAdd" />
			<input type="hidden" id="isEdit" name="isEdit" />
			<input type="hidden" id="connectionId" name="connection.id"/>
			<table>
			<tr><td align="right">
			Connection Name:&nbsp; 
			</td><td align="left">
			<input type="text" id="connectionName" name="connection.name"
				  dojoType="dijit.form.ValidationTextBox" maxlength=100 required="true" trim="true" missingMessage="Required."/>
		    </td></tr>
		    
		    <!-- 
		    <tr><td align="right">
		    HBase Rest Server Host:&nbsp; 
		    </td><td align="left">		    
			<input type="text" id="hostPropertyValue" name="hostProperty.value" 
				dojoType="dijit.form.ValidationTextBox" maxlength=100 required="true" trim="true" missingdMessage="Required."/>
			</td></tr>
			 -->
			
			<tr><td align="right">
			HBase Zookeeper Location:&nbsp;
			</td><td align="left">
			<input type="text" id="hbaseRestServerPropertyValue" name="hbaseRestServerProperty.value"
				dojoType="dijit.form.ValidationTextBox" maxlength=100 required="true" trim="true"
				 missingMessage="host:port e.g. hbseZookeeperQuorum:hbaseZookeeperPropertyClientPort" 
				 invalidMessage="This value is not valid. Expected: host:port e.g. hbseZookeeperQuorum:hbaseZookeeperPropertyClientPort"/>
			</td></tr>
			<tr><td></td>
			<td align="center">
			<button type="submit"  dojoType="dijit.form.Button">Save</button>
			</td></tr>
			</table>
</div>
<br />
<jsp:include page="footer.jsp" />
