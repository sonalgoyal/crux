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
<script >
 dojo.require("dojox.json.schema");
 dojo.require("dojox.json.ref");
 dojo.require("dijit.form.Form");
 dojo.require("dijit.form.Button");
 dojo.require("dijit.form.ValidationTextBox");
 dojo.require("dijit.form.SimpleTextarea");
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

	function updateAnalysisViewList(data){
		jsonStore.fetch({
			query: { id: '*' },
			onComplete: function(items, result){
	    		dojo.forEach(items, function(item){
	        		jsonStore.deleteItem(item);
	    		});
			}
		});
		jsonStore.save(); 
 
		if(data.analysisViewList.length==0){
			responseMessage("No analysis defined. Please click Add to create a new analysis.");
			document.getElementById("gridNode").style.visibility = "hidden";
			document.getElementById("linksForAnalysis").style.visibility = "hidden";
	    	showAnalysisForm();
		}else{
			document.getElementById("gridNode").style.visibility = "visible";
			document.getElementById("linksForAnalysis").style.visibility = "visible";
	    	        dijit.byId("gridNode").selection.clear();  
		}
		//alert("deleted");
		for(var i=0;i<data.analysisViewList.length;i++){
	    	jsonStore.newItem(data.analysisViewList[i]);
		}
	} 

	dojo.addOnLoad(function(e) {
	    var layoutAnalysis = [[{
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
	      							 field: "text",
	       						 	 name: "Text",
	      							 width: "auto"
	  							 }
	   							 ]];
		
		grid = new dojox.grid.DataGrid({
			query: { id: '*' },
			store: jsonStore,
			structure: layoutAnalysis,
			selectionMode: 'single',
			autoHeight:5
		}, 'gridNode');
		grid.layout.setColumnVisibility(1,false);
		grid.startup();
		
		if(grid.getItem(0)==null){
		 responseMessage("No analysis defined. Please click Add to create a new analysis.");
		 	document.getElementById("gridNode").style.visibility = "hidden";
			document.getElementById("linksForAnalysis").style.visibility = "hidden";
	    	showAnalysisForm();
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
	                        document.getElementById("analysisId").value = idValue;  
	                        
	                        var AnalysisNameValue = grid.store.getValues(selectedItem, 'name');
	                        //document.getElementById('analysisName').value = AnalysisNameValue;
	                        dijit.byId('analysisName').set('value',AnalysisNameValue);
	                        
	                                                
	                        var jsonTypeValue = grid.store.getValues(selectedItem, 'text');
	                        dijit.byId('jsonType').set('value',jsonTypeValue);
	                    }); // end forEach
	            }        	
	        }
		});
	});

	dojo.addOnLoad(function(e){
		var editAnalysisLink = dijit.byId("editAnalysis");
			
			dojo.connect(editAnalysisLink, "onClick", function(e) {
				clearResponse();
				clearValues();
				document.getElementById("updateAnalysisForm").style.visibility = "hidden";  
				dijit.byId("updateAnalysisForm").reset();     
				var items = grid.selection.getSelected();
				if(items.length==1){
	            dojo.forEach(items, function(selectedItem) {
	                        var idValue = grid.store.getValues(selectedItem, 'id');
	                        document.getElementById("analysisId").value = idValue;  
	                        
	                        var AnalysisNameValue = grid.store.getValues(selectedItem, 'name');
	                        //document.getElementById('analysisName').value = AnalysisNameValue;
	                        dijit.byId('analysisName').set('value',AnalysisNameValue);
	                        
	                       	                        
	                        var jsonTypeValue = grid.store.getValues(selectedItem, 'text');
	                        document.getElementById('jsonType').value = jsonTypeValue;
	                        //dijit.byId('jsonType').set('value',jsonTypeValue);
	                    }); // end forEach
	                    
	            document.getElementById("updateAnalysisForm").style.visibility = "visible";  
	            document.getElementById("isAdd").value = "false";  
	            document.getElementById("isEdit").value = "true";     
				} else{
					responseMessage("Please select an analysis.");
				}
			});
	});	

	function showAnalysisForm(){
		clearResponse();
		clearValues();
		dijit.byId("updateAnalysisForm").reset();      
		document.getElementById("updateAnalysisForm").style.visibility = "visible";  
		document.getElementById("isAdd").value = "true";  
		document.getElementById("isEdit"). value = "false"; 
    }	
	
	function clearValues(){
		document.getElementById('analysisId').value = 0;
		document.getElementById('isAdd').value = '';
		document.getElementById('isEdit').value = '';
		document.getElementById('analysisName').value = '';
		document.getElementById('jsonType').value = '';
	}	
	
	dojo.addOnLoad(function(e) {
		var updateAnalysisForm = dijit.byId("updateAnalysisForm");    
	    dojo.connect(updateAnalysisForm, "onSubmit", function(e) {
	    	
	    	clearResponse();
	    	e.preventDefault();
	    	results = getAnalysisSchema();
	        
	        var isAddValue = document.getElementById("isAdd").getAttribute("value");  
	        //alert("isAddValue: " + isAddValue);
	        var isEditValue = document.getElementById("isEdit").getAttribute("value");
	        //alert("isEditValue: " + isEditValue);
	        
	        //alert(updateAnalysisForm.isValid());
	        if (updateAnalysisForm.validate() && results.valid && isEditValue == "true" && isAddValue == "false") { 
	        	showProgressIndicator();
	        		var xhrArgs = {
	                    form: dojo.byId("updateAnalysisForm"),
	                    url: "<s:url action='updateAnalysis'/>",
	                    handleAs: "json",                    
	                    load: function(data) {
	                    	 hideProgressIndicator(); 
	                    	console.log("Data is  " + data);
	                    	//alert(data.error);
	                    	if(data.error.error){
	                    		 responseMessage("Error: "+data.error.message);
	                        }else{
	                    		//alert ("Returned successfully");
	                    		updateAnalysisViewList(data); 
	                    		clearValues();
	                    		updateAnalysisForm.reset();                			
	                    		document.getElementById("updateAnalysisForm").style.visibility = "hidden"; 
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
	         } else if (updateAnalysisForm.validate() && results.valid && isEditValue == "false" && isAddValue == "true") { 
	        	 showProgressIndicator();
	        	 	var xhrArgs = {
	                        form: dojo.byId("updateAnalysisForm"),
	                        url: "<s:url action='saveAnalysis'/>",
	                        handleAs: "json",
	                        load: function(data) {
	                        	 hideProgressIndicator(); 
	                        	console.log("Data is  " + data);
	                        	if(data.error.error){
	                        		 responseMessage("Error: "+data.error.message);
	                            }else{
	                        	//alert ("Returned successfully");
	                        	updateAnalysisViewList(data); 
	                        	clearValues();
	                        	updateAnalysisForm.reset();           			
	                    		document.getElementById("updateAnalysisForm").style.visibility = "hidden";
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
		var deleteAnalysisLink = dijit.byId("deleteAnalysis");		
			dojo.connect(deleteAnalysisLink, "onClick", function(e) {
				clearResponse();
				var items = grid.selection.getSelected(); 
			    if(items.length==1){
					document.getElementById("updateAnalysisForm").style.visibility = "hidden";  
					dijit.byId("updateAnalysisForm").reset();     
					dijit.byId("deleteAnalysisDialog").show();
			    }else{
			    	responseMessage("Please select an analysis.");
			    }
			});	
	});
	
	function deleteTrue() {		
		var deleteAnalysisLink = dijit.byId("deleteAnalysis");	
		var items = grid.selection.getSelected(); 
	    if(items.length==1){
	    dojo.forEach(items, function(selectedItem) {
	                var value = grid.store.getValues(selectedItem, 'id');
	                var id = document.getElementById("analysisIdToDelete").value = value;
	            }); // end forEach
	            
	    	if (deleteAnalysisLink.validate()) {
	    			showProgressIndicator();
					var xhrArgs = {
	                	form: dojo.byId("deleteAnalysis"),
	                	url: "<s:url action='deleteAnalysis'/>",
	                	handleAs: "json",
	               		load: function(data) {
	               			hideProgressIndicator();   		
	                		console.log("Data is  " + data);
	                		if(data.error.error){
	                		 responseMessage("Error: "+data.error.message);
	                		 //dijit.byId("deleteErrorDialog").show();
	                    	}else{
	                		//alert ("Returned successfully");
	                		updateAnalysisViewList(data);
	                		//document.getElementById("updateAnalysisForm").style.visibility = "hidden";
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
	    	responseMessage("Please select an analysis.");
	    }
	    dijit.byId("deleteAnalysisDialog").hide();	
	}

	function deleteFalse(){	
		dijit.byId("deleteAnalysisDialog").hide();
	}
	
   function getAnalysisSchema(){
	 
	   schema = {
			    properties : {
			    	            boo : {type:"number"},
			    	            foo : {type:"string"}
	                          }
	   };
	 
	   name = document.getElementById("analysisName").value;
	   obj = dojox.json.ref.fromJson(document.getElementById("jsonType").value);
	   results = dojox.json.schema.validate(obj, schema);
	 

	 return results;
 }

</script>

<div id="deleteAnalysisDialog" dojoType="dijit.Dialog" title="Confirm Deletion">
	 Are you sure you want to delete? Deletion can not be undone.
	<br /><br />
	<button id="deleteTrue" dojoType="dijit.form.Button" onClick="deleteTrue()">Yes</button>
	<button id="deleteFalse" dojoType="dijit.form.Button" onClick="deleteFalse()">No</button>
</div> 

<div style="width: 60%; float: center;">
	<div id="gridNode" style="width: 200px; float: center;"></div>
</div>

<br />
<table id="linksForAnalysis">
<tr>
<td align="center" width="50px">
<div dojoType="dijit.form.Form" id="addNewAnalysis">
		<a href="#" onClick="showAnalysisForm()">Add</a>
</div>
</td>
<td align="center" width="50px">
<div dojoType="dijit.form.Form" id="editAnalysis" name="editAnalysis" style="float:center;">
	<a href="#">Edit</a>
</div>
</td>
<td align="center" width="50px">
<div dojoType="dijit.form.Form" id="deleteAnalysis" name="deleteAnalysis" style="float:center;">
	<input type="hidden" id="analysisIdToDelete" name="analysis.id"	dojoType="dijit.form.TextBox" /> 
	<a href="#">Delete</a>
</div>
</td>
</tr>
</table>

<br />

<div dojoType="dijit.form.Form" id="updateAnalysisForm" name="updateAnalysisForm" style="visibility:hidden">
			<input type="hidden" id="isAdd" name="isAdd" />
			<input type="hidden" id="isEdit" name="isEdit" />
			<input type="hidden" id="analysisId" name="analysis.id"/>
			<table>
            <tr><td align="right">Name:&nbsp;</td>
            <td align="left">
            <input type="text" id="analysisName" name="analysis.name" dojoType="dijit.form.ValidationTextBox" 
                                      maxlength=100 required="true" trim="true" missingMessage="Required."></td></tr>
            <tr><td align="right">Json:&nbsp;</td>
            <td align="left">
            <textarea id="jsonType" name="analysis.text" dojoType="dijit.form.SimpleTextarea"  ></textarea></td></tr>
            <tr><td></td>
            <td align="center">
            <button type="submit" dojoType="dijit.form.Button" >Save</button>
            </td></tr>
            </table>
            </div>
            <br />

<jsp:include page="footer.jsp" />
