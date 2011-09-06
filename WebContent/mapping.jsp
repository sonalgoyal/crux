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
<%@ page import="co.nubetech.crux.view.ColumnAliasView"%>
<jsp:include page="header.jsp" />
<jsp:include page="topNavigation.jsp" />
<jsp:include page="progress.jsp" />
<jsp:include page="tableInclude.jsp" />
<h1 style="position: absolute; top: 10px; left: 48%;">Mapping</h1>
<br>
A mapping defines the HBase schema. Using aliases, you can provide user friendly names to identify the row and column keys. 
<br>
<br>
<script>
dojo.require("dijit.form.Form");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.Select");
dojo.require("dojox.grid.DataGrid");
dojo.require("dojo.data.ItemFileWriteStore");
dojo.require("dijit.layout.TabContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dijit.Dialog");

function showHelp(){
	 window.open ('help/mappingHelp.html', '', 'width=600,height=400,scrollbars=1');
}

var storeDataForMapping = {
        identifier: 'id',
        label: 'name',
        items: <%out.println(JSONUtil.serialize(request.getAttribute("mappingViewList")));%>
    };    
var jsonStoreForMapping = new dojo.data.ItemFileWriteStore({ data: storeDataForMapping });	
var gridForMapping = null;  

function updateMappingViewList(data){
	jsonStoreForMapping.fetch({
		query: { id: '*' },
		onComplete: function(items, result){
    		dojo.forEach(items, function(item){
        		jsonStoreForMapping.deleteItem(item);
    		});
		}
	});
	jsonStoreForMapping.save();
	
	if(data.mappingViewList.length==0){
		//responseMessage("No mapping defined. Please click Add to create a new mapping.");
		document.getElementById("gridNodeForMapping").style.visibility = "hidden";
		document.getElementById("linksForMapping").style.visibility = "hidden";
    	showMappingForm();
	}else{
		for(var i=0;i<data.mappingViewList.length;i++){
	    	jsonStoreForMapping.newItem(data.mappingViewList[i]);
		}
		
		document.getElementById("gridNodeForMapping").style.visibility = "visible";
		document.getElementById("linksForMapping").style.visibility = "visible";
    	dijit.byId("gridNodeForMapping").selection.clear();   	
	}
}

dojo.addOnLoad(function(e) {
    	var layoutMapping = [[{
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
       							 name: "Mapping Name",
       							 width:  "auto"
    						},
      						 {
      							 field: "connectionName",
       						 	 name: "Connection Name",
      							 width: "auto"
  							 },
       						 {
       							 field: "tableName",
        						 name: "Table Name",
       							 width: "auto"
   							 }]];
	
	gridForMapping = new dojox.grid.DataGrid({
		query: { id: '*' },
		store: jsonStoreForMapping,
		structure: layoutMapping,
		selectionMode: 'single',
		autoHeight:5
	}, 'gridNodeForMapping');
	gridForMapping.layout.setColumnVisibility(1,false);
	gridForMapping.startup();
	if(gridForMapping.getItem(0)==null){
		//responseMessage("No mapping defined. Please click Add to create a new mapping.");
		document.getElementById("gridNodeForMapping").style.visibility = "hidden";
		document.getElementById("linksForMapping").style.visibility = "hidden";
    	showMappingForm();
	}
});



var storeDataForColumnAlias = {
        identifier: 'alias',
        label: 'alias',
        items: <%out.println(JSONUtil.serialize(request.getAttribute("columnAliasViewList")));%>
    };    
var jsonStoreForColumnAlias = new dojo.data.ItemFileWriteStore({ data: storeDataForColumnAlias });	
var gridForColumnAlias = null;  


var storeDataForRowAlias = {
        identifier: 'alias',
        label: 'alias',
        items: <%out.println(JSONUtil.serialize(request.getAttribute("rowAliasViewList")));%>
    };    
var jsonStoreForRowAlias = new dojo.data.ItemFileWriteStore({ data: storeDataForRowAlias });	
var gridForRowAlias = null;  


function removeItemsForColumnAlias(){
	
	if(gridForColumnAlias != null){
		jsonStoreForColumnAlias.fetch({
    		query: { alias: '*' },
    		onComplete: function(items, result){
        		dojo.forEach(items, function(item){
            		jsonStoreForColumnAlias.deleteItem(item);
        		});
    		}
    	});
    	jsonStoreForColumnAlias.save();
	
	
	if(gridForColumnAlias.getItem(0)==null){
		document.getElementById("gridNodeForColumnAlias").style.visibility = "hidden";
    	document.getElementById("linksForColumnAlias").style.visibility = "hidden";
	} else{
		document.getElementById("gridNodeForColumnAlias").style.visibility = "visible";  
    	document.getElementById("linksForColumnAlias").style.visibility = "visible";
    	dijit.byId("gridNodeForColumnAlias").selection.clear();
	}
	//alert("deleted");
	}
}

function removeItemsForRowAlias(){	
	if(gridForRowAlias != null){
		jsonStoreForRowAlias.fetch({
    		query: { alias: '*' },
    		onComplete: function(items, result){
        		dojo.forEach(items, function(item){
            		jsonStoreForRowAlias.deleteItem(item);
        		});
    		}
    	});
    	jsonStoreForRowAlias.save();
	
	if(gridForRowAlias.getItem(0)==null){
		document.getElementById("gridNodeForRowAlias").style.visibility = "hidden";
    	document.getElementById("linksForRowKeyAlias").style.visibility = "hidden";
    	//showAddRowAlias();
	} else{
		document.getElementById("gridNodeForRowAlias").style.visibility = "visible"; 
    	document.getElementById("linksForRowKeyAlias").style.visibility = "visible";
    	dijit.byId("gridNodeForRowAlias").selection.clear();
	}
	//alert("deleted");
	}
}

function updateColumnAliasViewList(data){
	removeItemsForColumnAlias();
	//alert("columnAliasViewListLength: " + data.columnAliasViewList.length);
	for(var i=0;i<data.columnAliasViewList.length;i++){
    	jsonStoreForColumnAlias.newItem(data.columnAliasViewList[i]);
	}	
	
	if(data.columnAliasViewList.length == 0){
		//alert("gridForColumnAlias is empty.");
		document.getElementById("gridNodeForColumnAlias").style.visibility = "hidden";
    	document.getElementById("linksForColumnAlias").style.visibility = "hidden";
    	showAddColumnAlias();
	} else{
		//alert("gridForColumnAlias is not empty.");
		document.getElementById("gridNodeForColumnAlias").style.visibility = "visible";
    	document.getElementById("linksForColumnAlias").style.visibility = "visible";
    	dijit.byId("gridNodeForColumnAlias").selection.clear();  
	}
}

function updateRowAliasViewList(data){
	removeItemsForRowAlias();
	for(var i=0;i<data.rowAliasViewList.length;i++){
    	jsonStoreForRowAlias.newItem(data.rowAliasViewList[i]);
	}
	//alert(data.rowAliasViewList.length);
	if(data.rowAliasViewList.length == 0){
		//alert(data.rowAliasViewList.length);
		document.getElementById("gridNodeForRowAlias").style.visibility = "hidden";
    	document.getElementById("linksForRowKeyAlias").style.visibility = "hidden";
    	showAddRowAlias();
	} else{
		//alert("Items in rowAliasGrid is: " + data.rowAliasViewList.length);
		document.getElementById("gridNodeForRowAlias").style.visibility = "visible";
    	document.getElementById("linksForRowKeyAlias").style.visibility = "visible";
    	dijit.byId("gridNodeForRowAlias").selection.clear();  
	}
}

function addingItemsToColumnAliasGrid(){	
	jsonStoreForColumnAlias.newItem({
		id:dijit.byId("columnAliasId").get('value'),
		columnFamily:dijit.byId("columnFamily").get('value'),
		qualifier:dijit.byId("qualifier").get('value'), 
		alias:dijit.byId("alias").get('value'), 
		columnTypeName:dijit.byId("columnTypeName").get('value')});
	//alert("populated");
	//j++;
	//alert("Checking gridForColumnAlias.");
	if(gridForColumnAlias.getItem(0)==null){
		//alert("GridForColumnAlias is null.");
		document.getElementById("gridNodeForColumnAlias").style.visibility = "hidden";
		document.getElementById("linksForColumnAlias").style.visibility = "hidden";
    	showAddColumnAlias();
	} else{
		//alert("GridForColumnAlias is not null.");
		document.getElementById("gridNodeForColumnAlias").style.visibility = "visible";
		document.getElementById("linksForColumnAlias").style.visibility = "visible";
    	dijit.byId("gridNodeForColumnAlias").selection.clear();
	}
	
	dijit.byId("addColumnAlias").reset();
	document.getElementById("addColumnAlias").style.visibility = "hidden"; 
	clearValuesOfColumnAlias();
}


function populateGridNodeForColumnAlias(){
	//alert("populating");
	var addColumnAlias = dijit.byId("addColumnAlias");
	//alert("addColumnAlias.isValid: " + addColumnAlias.isValid());
	if(addColumnAlias.validate()){
		
		var isEditDetailValue = document.getElementById("isEditDetail").getAttribute("value");
		if(isEditDetailValue == "true"){
			//alert("deleting to edit");
			deleteColumnAlias();
		}else{
			addingItemsToColumnAliasGrid();
		}
	}else{
		responseMessage("Please fill up the form with required and correct values.");
	}
	
}

function addingItemsToRowAliasGrid(){
	//alert("Adding items");
	jsonStoreForRowAlias.newItem({	
		id:dijit.byId("rowAliasId").get('value'), 
		length:dijit.byId("length").get('value'), 
		alias:dijit.byId("rowAliasAlias").get('value'), 
		columnTypeName:dijit.byId("rowAliasColumnTypeName").get('value')});
	//alert("populated");
	//j++;
	//alert("Checking gridForRowAlias.");
	if(gridForRowAlias.getItem(0)==null){
		//alert("GridForRowAlias is null.");
		document.getElementById("gridNodeForRowAlias").style.visibility = "hidden";
		document.getElementById("linksForRowKeyAlias").style.visibility = "hidden";
    	showAddRowAlias();
	} else{
		//alert("GridForRowAlias is not null.");
		document.getElementById("gridNodeForRowAlias").style.visibility = "visible";
		document.getElementById("linksForRowKeyAlias").style.visibility = "visible";
    	dijit.byId("gridNodeForRowAlias").selection.clear();
	}
	dijit.byId("addRowAlias").reset();
	document.getElementById("addRowAlias").style.visibility = "hidden"; 
	clearValuesOfRowAlias();
}

function checkForLastRowAlias(){
	var returnValue = false;
	jsonStoreForRowAlias.fetch( { query: { alias: '*' },  
        onItem: function(item) {
           var length = jsonStoreForRowAlias.getValue( item, 'length');
           if(length == '' || length == null){
        	   returnValue = true;
           }
        }
	});
	
	var isEditRowAliasValue = document.getElementById("isEditRowAlias").getAttribute("value");
	if(isEditRowAliasValue == "true"){
		returnValue = false;
	}
	
	return returnValue;
}

function populateGridNodeForRowAlias(){
	var isLastRowAliasExists = checkForLastRowAlias();
	if(isLastRowAliasExists == true){
		responseMessage("No more alias can be saved.");
	}else{
	//alert("populating");
	var addRowAlias = dijit.byId("addRowAlias");
	//alert("addRowAlias.isValid: " + addRowAlias.isValid());
	if(addRowAlias.validate()){
	
		var isEditRowAliasValue = document.getElementById("isEditRowAlias").getAttribute("value");
		if(isEditRowAliasValue == "true"){
			//alert("deleting to edit");
			deleteRowAlias();
		}else{
			addingItemsToRowAliasGrid();
		}
	}else{
		responseMessage("Please fill up the form with required and correct values.");
	}
	}
}

function jsonConcat(o1, o2) {
	 for (var key in o2) {
	  o1[key] = o2[key];
	 }
	 return o1;
	}
	
function getDataFromTableForm(){
	var jsons = {};
	var connectionField = "connection.id";
	var connectionIdValue = dijit.byId("connection").get('value');
	var tableField = "mapping.tableName";
	var tableNameValue = dijit.byId("tableName").get('value');
	
	var json = '{' + '"' + connectionField + '"' + ':' + '"' + connectionIdValue + '"' + ',"' + tableField + '"' + ':' + '"' + tableNameValue + '"' + '}';
	obj = dojo.fromJson(json);
	jsons = jsonConcat(jsons, obj);
	return jsons;
}
	
function getDataFromGridNodeForColumnAlias(){	
	var jsons = {};
	var i = 0;
	var obj;
	//alert(gridForColumnAlias.rowCount);
	jsonStoreForColumnAlias.fetch( { query: { alias: '*' },  
        onItem: function(item) {
           var id = jsonStoreForColumnAlias.getValue( item, 'id');
           var cf = jsonStoreForColumnAlias.getValue( item, 'columnFamily');
           var qualifier = jsonStoreForColumnAlias.getValue( item, 'qualifier');
           var alias = jsonStoreForColumnAlias.getValue( item, 'alias');
           var columnTypeName = jsonStoreForColumnAlias.getValue( item, 'columnTypeName');
           //alert(cf);
           //alert(qualifier);
           //alert(alias);
           //alert(columnTypeName);
           var idField = "columnAliasViewList["+i+"].id";
           var cfField = "columnAliasViewList["+i+"].columnFamily";
           var qualifierField = "columnAliasViewList["+i+"].qualifier";
           var aliasField = "columnAliasViewList["+i+"].alias";
           var columnTypeNameField = "columnAliasViewList["+i+"].columnTypeName";
           
           //alert(cfField);
           
           var json = '{' + '"' + idField + '"' + ':' + '"' + id + '"' + ',"' + cfField + '"' + ':' + '"' + cf + '"' + ',"' + qualifierField + '"' + ':' + '"' + qualifier + '"' + ',"' + aliasField + '"' + ':' + '"' + alias + '"' + ',"' + columnTypeNameField + '"' + ':' + '"' + columnTypeName + '"' + '}'; 
           //alert(json);
		   obj = dojo.fromJson(json);
		   //alert(obj);
           jsons = jsonConcat(jsons, obj);
           i++;          
        }
	});
	return jsons;
}

function getDataFromGridNodeForRowAlias(){	
	var jsons = {};
	var i = 0;
	var obj;
	//alert(gridForColumnAlias.rowCount);
	jsonStoreForRowAlias.fetch( { query: { alias: '*' },  
        onItem: function(item) {
           var id = jsonStoreForRowAlias.getValue( item, 'id');
           var length = jsonStoreForRowAlias.getValue( item, 'length');
           //alert(length);
           if(length == null){
        	   length = '';
        	   //alert(length);
           }
           var alias = jsonStoreForRowAlias.getValue( item, 'alias');
           var columnTypeName = jsonStoreForRowAlias.getValue( item, 'columnTypeName');
           //alert(qualifier);
           //alert(alias);
           //alert(columnTypeName);
           var idField = "rowAliasViewList["+i+"].id";
           var lengthField = "rowAliasViewList["+i+"].length";
           var aliasField = "rowAliasViewList["+i+"].alias";
           var columnTypeNameField = "rowAliasViewList["+i+"].columnTypeName";
           
           //alert(cfField);
           
           var json = '{' + '"' + idField + '"' + ':' + '"' + id + '"' + ',"' + lengthField + '"' + ':' + '"' + length + '"' + ',"' + aliasField + '"' + ':' + '"' + alias + '"' + ',"' + columnTypeNameField + '"' + ':' + '"' + columnTypeName + '"' + '}'; 
           //alert(json);
		   obj = dojo.fromJson(json);
		   //alert(obj);
           jsons = jsonConcat(jsons, obj);
           i++;          
        }
	});
	return jsons;
}

dojo.addOnLoad(function(e) {
    var layoutColumnAlias = [[{
			 					field: "id",
								name: "id",
				 				width:  "auto"
							  },                              
                              {
       							 field: "columnFamily",
       							 name: "Column Family",
       							 width:  "auto"
    						 },
      						 {
      							 field: "qualifier",
       						 	 name: "Qualifier",
      							 width: "auto"
  							 },
       						 {
       							 field: "alias",
        						 name: "Alias",
       							 width: "auto"
   							 },
       						 {
       							 field: "columnTypeName",
        						 name: "Value Type",
       							 width: "auto"
   							 }]];
	
	gridForColumnAlias = new dojox.grid.DataGrid({
		query: { alias: '*' },
		store: jsonStoreForColumnAlias,
		structure: layoutColumnAlias,
		selectionMode: 'single',
		autoHeight:5
	}, 'gridNodeForColumnAlias');
	gridForColumnAlias.layout.setColumnVisibility(0,false);
	gridForColumnAlias.startup();
	
	if(gridForColumnAlias.getItem(0)==null){
		document.getElementById("gridNodeForColumnAlias").style.visibility = "hidden";
		document.getElementById("linksForColumnAlias").style.visibility = "hidden";
    	showAddColumnAlias();
	}
});


dojo.addOnLoad(function(e) {
    var layoutRowAlias =	[[{
								field: "id",
								name: "id",
								width:  "auto"
		  					},
		  					{
    							field: "alias",
		 						name: "Alias",
			 					width: "auto"
    						},
      						 {
       							 field: "length",
        						 name: "Length",
       							 width: "auto"
   							 },
       						 {
       							 field: "columnTypeName",
        						 name: "Value Type",
       							 width: "auto"
   							 }]];
	
	gridForRowAlias = new dojox.grid.DataGrid({
		query: { alias: '*' },
		store: jsonStoreForRowAlias,
		structure: layoutRowAlias,
		selectionMode: 'single',
		autoHeight:5
	}, 'gridNodeForRowAlias');
	gridForRowAlias.layout.setColumnVisibility(0,false);
	gridForRowAlias.startup();
	
	if(gridForRowAlias.getItem(0)==null){
		document.getElementById("gridNodeForRowAlias").style.visibility = "hidden";
		document.getElementById("linksForRowKeyAlias").style.visibility = "hidden";
    	showAddRowAlias();
	}
});


function showMappingForm(){
	//alert("Showing mapping form.")
		clearResponse();		
		dijit.byId("mapping").reset();
		dijit.byId("table").reset();
		
		dijit.byId("mappingIdToEdit").set('value', 0);
		onChangeConnection();
		
		clearValuesOfMapping();
		clearValuesOfColumnAlias();
		removeItemsForColumnAlias();
		removeItemsForRowAlias();
		
		document.getElementById("createMapping").style.visibility = "visible";		
		document.getElementById("addColumnAlias").style.visibility = "hidden"; 
		showAddColumnAlias();
		document.getElementById("addRowAlias").style.visibility = "hidden"; 
		showAddRowAlias();
    	
		dijit.byId("connection").setAttribute('disabled',false); 
		dijit.byId("tableName").setAttribute('disabled',false);
		
		resetTab();		
}

function resetTab(){
	var tableTab = dijit.byId("tableContentPane");
	var tabContainer = dijit.byId("tabContainer");
	tabContainer.selectChild(tableTab);
}

dojo.addOnLoad(function(e) {
	var columnFamily = dijit.byId("columnFamily");
	dojo.connect(columnFamily, "onChange", function(e){
		var qualifier = dijit.byId("qualifier");
		qualifier.set('value', qualifier.get('value'));
		
	});
});
	
function onChangeConnection(){
	var connection = dijit.byId("connection");	
	
	clearResponse();	
	//alert("Connection changed");
	var connectionIdValue = dijit.byId("connection").get('value');		
	//alert("Connection id is: " + connectionIdValue);
	var json = '{' + '"connection.id"' + ':' + '"' + connectionIdValue + '"' + '}';
	obj = dojo.fromJson(json);		
	
	var mappingIdToEdit = dijit.byId("mappingIdToEdit").get('value');
	//alert(mappingIdToEdit);
	if (connection.validate() && (mappingIdToEdit == 0 || mappingIdToEdit == '' || mappingIdToEdit == null)) {
		showProgressIndicator();
        var xhrArgs = {
                url: "<s:url action='populateHBaseTable'/>",
                handleAs: "json",
                content: obj,
                load: function(data) {
                	hideProgressIndicator();
                	console.log("Data is  " + data);
                	//alert ("Returned successfully");
                	if(data.error.error){
                		responseMessage("Error: "+data.error.message);
                    }else{
                		var select_box  = dijit.byId('tableName');                    	
                		select_box.options.length = 0;
                		select_box.addOption();
                	
                		for(var i=0; i < data.tableList.length; ++i){
                			//alert ("Key is " + data.tableList[i]);
                			var key = data.tableList[i];
                			select_box.addOption({value: key, label: key});                     		
                		};
                    }
                },
                error: function(error) {
                	hideProgressIndicator();
                    //We'll 404 in the demo, but that's okay.  We don't have a 'postIt' service on the
                     //docs server.
                     console.log("Error " + error);
                }
            }; 
          var deferred = dojo.xhrPost(xhrArgs);
        }
	
}


dojo.addOnLoad(function(e) {
	var connection = dijit.byId("connection");	
	dojo.connect(connection, "onChange", function(e){
		onChangeConnection();
	});	
});


dojo.addOnLoad(function(e) {
	
	var tableName = dijit.byId("tableName");
	dojo.connect(tableName, "onChange", function(e){
		
		//alert("table changed");
		clearResponse();
		//clearValuesOfColumnAlias();
		var mappingIdToEdit = dijit.byId("mappingIdToEdit").get('value');
		//alert("Mapping id to edit: " + mappingIdToEdit);
		if (tableName.validate() && (mappingIdToEdit == 0 || mappingIdToEdit == '' || mappingIdToEdit == null)) {   
			//alert("TableName is valid.");
			showProgressIndicator();
            var xhrArgs = {
                    form: dojo.byId("table"),
                    url: "<s:url action='populateColumnFamily'/>",
                    handleAs: "json",
                    load: function(data) {
                    	hideProgressIndicator();
                    	console.log("Data is  " + data);
                    	//alert ("Returned successfully");
                    	
                    	var select_box  = dijit.byId('columnFamily');    
                    	//alert("Select box is " + select_box);
                    	//alert("data.columnFamilyList " + data.columnFamilyList);
                    	
                    	select_box.options.length = 0;
                    	select_box.addOption();
                    	//alert("data.columnFamilyList.length" + data.columnFamilyList.length);
                    	for(var i=0; i < data.columnFamilyList.length; ++i){
                    		//alert("populating cf list");
                    		//alert ("Key is " + data.columnFamilyList[i]);
                    		var key = data.columnFamilyList[i];
                    		select_box.addOption({value: key, label: key});         
                    	};
                    	
                    	var mappingIdToEdit = document.getElementById("mappingIdToEdit").getAttribute("value");
                    	if(mappingIdToEdit == null || mappingIdToEdit == '' || mappingIdToEdit == 0){
                    		//alert("Removing items from columnAlias grid.");
                    		removeItemsForColumnAlias();
                    	}
                    },
                    error: function(error) {
                    	hideProgressIndicator();
                        //We'll 404 in the demo, but that's okay.  We don't have a 'postIt' service on the
                         //docs server.
                         console.log("Error " + error);
                    }
                }; 
              var deferred = dojo.xhrPost(xhrArgs);
            }
		
	});	
});


dojo.addOnLoad(function(e) {
	
	var mappingForm = dijit.byId("mapping"); 
	var submitMappingButton = dijit.byId("submitMapping");
	
    dojo.connect(submitMappingButton, "onClick", function(e) {
    	  	
    	clearResponse();
    	//alert("Clicked on Submit");
        //alert(mappingForm.isValid());
        if (mappingForm.validate()) {   
        	//alert("Form is valid");
        	var json1 = getDataFromGridNodeForColumnAlias();
        	var json2 = getDataFromGridNodeForRowAlias();
        	var json3 = getDataFromTableForm();
        	
        	json1 = jsonConcat(json1, json2);
        	json1 = jsonConcat(json1, json3);
        	//var returnFromTest = test();
        	//alert(dojo.toJson(json1, true));
        	showProgressIndicator();  
            var xhrArgs = {
                    form: dojo.byId("mapping"),
                    url: "<s:url action='saveMapping'/>",
                    handleAs: "json",
                    content: json1,
                    load: function(data) {
                    	 hideProgressIndicator();
                    	console.log("Data is  " + data);
                    	if(data.error.error){
                    		responseMessage("Error: "+data.error.message);
                        }else{
                    	//alert ("Returned successfully");
                    	updateMappingViewList(data);
                    	mappingForm.reset();
                    	dijit.byId("mappingIdToEdit").set('value', 0);
                    	document.getElementById("createMapping").style.visibility = "hidden"; 
                		document.getElementById("addColumnAlias").style.visibility = "hidden"; 
                		document.getElementById("addRowAlias").style.visibility = "hidden"; 
                		
                		/*document.getElementById("connection").style.visibility = "hidden"; 
                		document.getElementById("tableName").style.visibility = "hidden"; 
                		document.getElementById("connectionLabel").style.visibility = "hidden"; 
                		document.getElementById("tableNameLabel").style.visibility = "hidden"; */
                		dijit.byId("connection").setAttribute('disabled',true); 
                		dijit.byId("tableName").setAttribute('disabled',true);
                		
                		document.getElementById("gridNodeForColumnAlias").style.visibility = "hidden";
                    	document.getElementById("gridNodeForRowAlias").style.visibility = "hidden";
                    	document.getElementById("linksForRowKeyAlias").style.visibility = "hidden";
                    	document.getElementById("linksForColumnAlias").style.visibility = "hidden";
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
	var deleteMappingLink = dijit.byId("deleteMappingLink");
		
		dojo.connect(deleteMappingLink, "onClick", function(e) {
			clearResponse();
			var items = gridForMapping.selection.getSelected();
			//alert("Items length is: " + items.length);
		    if(items.length==1){
				dijit.byId("mappingIdToEdit").set('value', 0);
				document.getElementById("createMapping").style.visibility = "hidden";
				document.getElementById("addColumnAlias").style.visibility = "hidden"; 
				document.getElementById("addRowAlias").style.visibility = "hidden"; 
				document.getElementById("gridNodeForColumnAlias").style.visibility = "hidden";
	        	document.getElementById("gridNodeForRowAlias").style.visibility = "hidden";
	        	document.getElementById("linksForRowKeyAlias").style.visibility = "hidden";
	        	document.getElementById("linksForColumnAlias").style.visibility = "hidden";
			
				document.getElementById("delete").value = 'mapping';
				dijit.byId("deleteDialog").show();
		    }else{
		 		responseMessage("Please select a mapping.");
			}
		});	
});


function showAddColumnAlias(){
	clearResponse();
	document.getElementById("addColumnAlias").style.visibility = "visible"; 
	dijit.byId("addColumnAlias").reset();
	clearValuesOfColumnAlias();	
}

function showAddRowAlias(){
	clearResponse();
	document.getElementById("addRowAlias").style.visibility = "visible"; 
	dijit.byId("addRowAlias").reset();
	clearValuesOfRowAlias();	
}

function clearValuesOfMapping(){
	document.getElementById('mappingName').value = '';
}

function clearValuesOfColumnAlias(){
	document.getElementById("columnAliasId").value = 0;
	document.getElementById("isEditDetail").value = '';
	document.getElementById('qualifier').value = '';
	document.getElementById('alias').value = '';
}

function clearValuesOfRowAlias(){
	document.getElementById("rowAliasId").value = 0;
	document.getElementById("isEditRowAlias").value = '';
	document.getElementById('length').value = '';
	document.getElementById('rowAliasAlias').value = '';
}

dojo.addOnLoad(function(e){
	dojo.connect(gridForMapping, "onRowClick", function(e) {
		var mappingIdToEdit = document.getElementById("mappingIdToEdit").getAttribute("value");
		//alert(mappingIdToEdit);
		clearResponse();
		if(mappingIdToEdit == null || mappingIdToEdit == '' || mappingIdToEdit == 0){
		}else{
			//alert(mappingIdToEdit);
			editMapping();
		}
	});
});

function editMapping(){
	clearResponse();
	clearValuesOfMapping();
	clearValuesOfColumnAlias();
	clearValuesOfRowAlias();
	var items = gridForMapping.selection.getSelected(); 
    if(items.length==1){
   
    var mappingIdValue;
    dojo.forEach(items, function(selectedItem) {
                mappingIdValue = gridForMapping.store.getValues(selectedItem, 'id');
                document.getElementById("mappingIdToEdit").value = mappingIdValue;
            }); // end forEach
            showProgressIndicator();
	var xhrArgs = {
            form: dojo.byId("editMappingLink"),
            url: "<s:url action='editMapping'/>",
            handleAs: "json",
            content: {
                "mapping.id":mappingIdValue
                },
            load: function(data) {
            	hideProgressIndicator();
            	if(data.error.error){
            		responseMessage("Error: "+data.error.message);
                }else{
            		console.log("Data is  " + data);
            	
            		dijit.byId('mappingName').set('value',data.mapping.name);
            		//alert(data.mapping.connection.id);
            		dijit.byId('connection').set('value',data.mapping.connection.id);
            		//alert(data.mapping.tableName);
            		//dijit.byId('tableName').set('value',data.mapping.tableName);
            	
            		var tableName = dijit.byId('tableName');
            		tableName.options.length = 0;
            		tableName.addOption({value: data.mapping.tableName, label: data.mapping.tableName});  
            	
            		//alert("updating row and column alias");
            		updateColumnAliasViewList(data); 
            		updateRowAliasViewList(data); 
            		//alert("updated row and column alias");
            	
            		var select_box  = dijit.byId('columnFamily');    
            		//alert("Select box is " + select_box);
            		//alert("data.columnFamilyList " + data.columnFamilyList);
            	
            		select_box.options.length = 0;
            	
            		for(var i=0; i < data.columnFamilyList.length; ++i){
            			//alert ("Key is " + data.columnFamilyList[i]);
            			var key = data.columnFamilyList[i];
            			select_box.addOption({value: key, label: key});  
            			//alert("Added in select box");
            		};
            		//alert("Select box populated");
                }
            },
            error: function(error) {
            	hideProgressIndicator();
            	//alert("error:"+error);
            }
	};
	var deferred = dojo.xhrPost(xhrArgs);
	
	document.getElementById("createMapping").style.visibility = "visible"; 
	
	dijit.byId("connection").setAttribute('disabled',true); 
	dijit.byId("tableName").setAttribute('disabled',true);
	
	document.getElementById("addColumnAlias").style.visibility = "hidden"; 
	document.getElementById("addRowAlias").style.visibility = "hidden"; 
	resetTab();	
	}else{
    	responseMessage("Please select a mapping.");
    }
}

dojo.addOnLoad(function(e) {
	var editMappingLink = dijit.byId("editMappingLink");
	dojo.connect(editMappingLink, "onClick", function(e) {	
		document.getElementById("createMapping").style.visibility = "hidden";
		document.getElementById("addColumnAlias").style.visibility = "hidden"; 
		document.getElementById("addRowAlias").style.visibility = "hidden";
		document.getElementById("gridNodeForColumnAlias").style.visibility = "hidden";
    	document.getElementById("linksForRowKeyAlias").style.visibility = "hidden";
    	document.getElementById("linksForColumnAlias").style.visibility = "hidden";
    	document.getElementById("gridNodeForRowAlias").style.visibility = "hidden";
		
		editMapping();
    });		
});

dojo.addOnLoad(function(e){
	dojo.connect(gridForColumnAlias, "onRowClick", function(e){
		var isEditValue = dijit.byId("isEditDetail").get('value');
		clearResponse();
		if(isEditValue == "true"){
		var items = gridForColumnAlias.selection.getSelected();   
		if(items.length==1){
			  dojo.forEach(items, function(selectedItem) {
				  var idValue = gridForColumnAlias.store.getValues(selectedItem, 'id');
	              dijit.byId('columnAliasId').set('value',idValue);
	        	  var qualifierValue = gridForColumnAlias.store.getValues(selectedItem, 'qualifier');
	              dijit.byId('qualifier').set('value',qualifierValue);
	              var aliasValue = gridForColumnAlias.store.getValues(selectedItem, 'alias');
	              dijit.byId('alias').set('value',aliasValue);
	              var columnFamilyValue = gridForColumnAlias.store.getValues(selectedItem, 'columnFamily');
	              dijit.byId('columnFamily').set('value',columnFamilyValue);
	              var columnTypeNameValue = gridForColumnAlias.store.getValues(selectedItem, 'columnTypeName');
	              dijit.byId('columnTypeName').set('value',columnTypeNameValue);
	          }); // end forEach
		}
		}
	});
});

function editColumnAlias(){	
	clearResponse();
	document.getElementById("addColumnAlias").style.visibility = "hidden"; 
	dijit.byId("addColumnAlias").reset();
	var items = gridForColumnAlias.selection.getSelected();   
	if(items.length==1){
		  showAddColumnAlias();
          dojo.forEach(items, function(selectedItem) {
        	  dijit.byId("isEditDetail").set('value', 'true');
        	  var idValue = gridForColumnAlias.store.getValues(selectedItem, 'id');
              dijit.byId('columnAliasId').set('value',idValue);
        	  var qualifierValue = gridForColumnAlias.store.getValues(selectedItem, 'qualifier');
              dijit.byId('qualifier').set('value',qualifierValue);
              var aliasValue = gridForColumnAlias.store.getValues(selectedItem, 'alias');              
              dijit.byId('alias').set('value',aliasValue);
              var columnFamilyValue = gridForColumnAlias.store.getValues(selectedItem, 'columnFamily');
              dijit.byId('columnFamily').set('value',columnFamilyValue);
              var columnTypeNameValue = gridForColumnAlias.store.getValues(selectedItem, 'columnTypeName');
              dijit.byId('columnTypeName').set('value',columnTypeNameValue);
          }); // end forEach
	}else{
		responseMessage("Please select an alias.");
	}
}


dojo.addOnLoad(function(e){
	dojo.connect(gridForRowAlias, "onRowClick", function(e){
		var isEditValue = dijit.byId("isEditRowAlias").get('value');
		clearResponse();
		if(isEditValue == "true"){
		var items = gridForRowAlias.selection.getSelected();   
		if(items.length==1){
			  dojo.forEach(items, function(selectedItem) {
				  var idValue = gridForRowAlias.store.getValues(selectedItem, 'id');
	              dijit.byId('rowAliasId').set('value',idValue);
				  var lengthValue = gridForRowAlias.store.getValues(selectedItem, 'length');
	              dijit.byId('length').set('value',lengthValue);
	              var aliasValue = gridForRowAlias.store.getValues(selectedItem, 'alias');
	              dijit.byId('rowAliasAlias').set('value',aliasValue);
	              var columnTypeNameValue = gridForRowAlias.store.getValues(selectedItem, 'columnTypeName');
	              dijit.byId('rowAliasColumnTypeName').set('value',columnTypeNameValue);
	          }); // end forEach
		}
		}
	});
});

function editRowAlias(){	
	clearResponse();
	document.getElementById("addRowAlias").style.visibility = "hidden"; 
	dijit.byId("addRowAlias").reset();
	var items = gridForRowAlias.selection.getSelected();	
	if(items.length==1){
		  showAddRowAlias();
          dojo.forEach(items, function(selectedItem) {
        	  var idValue = gridForRowAlias.store.getValues(selectedItem, 'id');
              dijit.byId('rowAliasId').set('value',idValue);
        	  var lengthValue = gridForRowAlias.store.getValues(selectedItem, 'length');
              dijit.byId('length').set('value',lengthValue);
              var aliasValue = gridForRowAlias.store.getValues(selectedItem, 'alias');
              dijit.byId("isEditRowAlias").set('value', 'true');
              dijit.byId('rowAliasAlias').set('value',aliasValue);
              var columnTypeNameValue = gridForRowAlias.store.getValues(selectedItem, 'columnTypeName');
              dijit.byId('rowAliasColumnTypeName').set('value',columnTypeNameValue);
          }); // end forEach    
	}else{
		responseMessage("Please select an alias.");
	}
                   
}

function deleteColumn(){
	clearResponse();
	var items = gridForColumnAlias.selection.getSelected(); 
	if(items.length==1){
		document.getElementById("addColumnAlias").style.visibility = "hidden"; 
		dijit.byId("addColumnAlias").reset();
		document.getElementById("delete").value = 'columnAlias';
		dijit.byId("deleteDialog").show();
	}else{
		responseMessage("Please select an alias.");
	}
}

function deleteColumnAlias(){
	//alert("Deleting columnAlias selected");
	clearResponse();
	var items = gridForColumnAlias.selection.getSelected(); 
	if(items.length==1){
		  
          dojo.forEach(items, function(selectedItem) {
        	  var columnAliasIdValue = gridForColumnAlias.store.getValues(selectedItem, 'id'); 
        	  showProgressIndicator();
  			  var xhrArgs = {            
          		url: "<s:url action='checkColumnAliasForForeignKey'/>",
          		handleAs: "json",
          		content: dojo.fromJson('{' + '"columnAlias.id"' + ':' + '"' + columnAliasIdValue + '"' + '}'),
          		load: function(data) {
          			 hideProgressIndicator();
          			console.log("Data is  " + data);
          			if(data.error.error){
          				responseMessage("Error: "+data.error.message);
              		}else{
              			deletingColumnAlias(items);      	
          			} 
         		},
          		error: function(error) {
          			 hideProgressIndicator();
          		//alert("error:"+error);
          		}
			  };
			  var deferred = dojo.xhrPost(xhrArgs);
  			}); // end forEach
         
	}else{
		responseMessage("Please select an alias.");
	}
}


function deletingColumnAlias(items){
	clearResponse();
	dojo.forEach(items, function(selectedItem) {
                      var value = gridForColumnAlias.store.getValues(selectedItem, 'alias');
                      //alert("Value selected of alias is: " + value);
                      var json = '{' + '"alias"' + ':' + '"' + value + '"' + '}';
                      var obj = dojo.fromJson(json);
                      //alert(dojo.toJson(obj, true));
                      jsonStoreForColumnAlias.fetch({
                  		query: obj,
                  		onComplete: function(items, result){
                      		dojo.forEach(items, function(item){
                          		jsonStoreForColumnAlias.deleteItem(item);
                      		});
                  		}
                  	});
                  	jsonStoreForColumnAlias.save();
                  }); // end forEach
                  
	var isEditDetailValue = document.getElementById("isEditDetail").getAttribute("value");
	if(isEditDetailValue == "true"){
		addingItemsToColumnAliasGrid();
	}else{
		//alert("Checking gridForColumnAlias.");
	    if(gridForColumnAlias.getItem(0)==null){
	    	//alert("GridForColumnAlias is null.");
	    	document.getElementById("gridNodeForColumnAlias").style.visibility = "hidden";
	    	document.getElementById("linksForColumnAlias").style.visibility = "hidden";
	    	showAddColumnAlias();
	    } else{
	    	//alert("GridForColumnAlias is not null.");
	    	document.getElementById("gridNodeForColumnAlias").style.visibility = "visible";
	    	document.getElementById("linksForColumnAlias").style.visibility = "visible";
	       	dijit.byId("gridNodeForColumnAlias").selection.clear();
	    }
	} 
	
}



function deleteRow(){
	clearResponse();
	var items = gridForRowAlias.selection.getSelected();  
    if(items.length==1){
		document.getElementById("addRowAlias").style.visibility = "hidden"; 
		dijit.byId("addRowAlias").reset();
		document.getElementById("delete").value = 'rowAlias';
		dijit.byId("deleteDialog").show();	
    }else{
		responseMessage("Please select an alias.");
	}
}

function deleteRowAlias(){
	clearResponse();	
	var items = gridForRowAlias.selection.getSelected();  
    if(items.length==1){
   
    dojo.forEach(items, function(selectedItem) {
                var rowAliasIdValue = gridForRowAlias.store.getValues(selectedItem, 'id'); 
                showProgressIndicator();
    			var xhrArgs = {            
            		url: "<s:url action='checkRowAliasForForeignKey'/>",
            		handleAs: "json",
            		content: dojo.fromJson('{' + '"rowAlias.id"' + ':' + '"' + rowAliasIdValue + '"' + '}'),
            		load: function(data) {
            			 hideProgressIndicator();
            			console.log("Data is  " + data);
            			if(data.error.error){
            				responseMessage("Error: "+data.error.message);
            				//dijit.byId("updateErrorDialog").show();
                		}else{
                			deletingRowAlias(items);               			         	
            			} 
           			},
            		error: function(error) {
            			 hideProgressIndicator();
            		//alert("error:"+error);
            		}
				};
				var deferred = dojo.xhrPost(xhrArgs);
    			}); // end forEach 
   
	}else{
		responseMessage("Please select an alias.");
	}
	
}


function deletingRowAlias(items){
	clearResponse();
	dojo.forEach(items, function(selectedItem) {
                      var value = gridForRowAlias.store.getValues(selectedItem, 'alias');
                      //alert("Value selected of alias is: " + value);
                      var json = '{' + '"alias"' + ':' + '"' + value + '"' + '}';
                      var obj = dojo.fromJson(json);
                      //alert(dojo.toJson(obj, true));
                      jsonStoreForRowAlias.fetch({
                  		query: obj,
                  		onComplete: function(items, result){
                      		dojo.forEach(items, function(item){
                          		jsonStoreForRowAlias.deleteItem(item);
                      		});
                  		}
                  	});
                  	jsonStoreForRowAlias.save();
                  }); // end forEach	
           
                  
	var isEditRowAliasValue = document.getElementById("isEditRowAlias").getAttribute("value");
    //alert("isEditRowAliasValue: " + isEditRowAliasValue);
	if(isEditRowAliasValue == "true"){
		addingItemsToRowAliasGrid();
	}else{
		//alert("Checking gridForRowAlias."); 
	    if(gridForRowAlias.getItem(0)==null){
	    	//alert("GridForRowAlias is null.");
	    	document.getElementById("gridNodeForRowAlias").style.visibility = "hidden";
	    	document.getElementById("linksForRowKeyAlias").style.visibility = "hidden";
	        //alert("Show rowAlias form");
	    	showAddRowAlias();
	    } else{
	    	//alert("GridForRowAlias is not null.");
	    	document.getElementById("gridNodeForRowAlias").style.visibility = "visible";
	    	document.getElementById("linksForRowKeyAlias").style.visibility = "visible";
	       	dijit.byId("gridNodeForRowAlias").selection.clear();
	    }			
	}            
    
}


dojo.addOnLoad(function(e) {
	dijit.byId("alias").validator = function (value, constraints) {
		return validateColumnAlias(value, constraints);		
	}
	
	dijit.byId("rowAliasAlias").validator = function (value, constraints) {
		return validateRowAlias(value, constraints);		
	}
	
	dijit.byId("qualifier").validator = function (value, constraints) {
		return validateQualifier(value, constraints);		
	}
});

function validateRowAlias(value, constraints){
	var returnValue = true;
	
	if(value == "" || value == null){
		returnValue = false;
	}
	
	jsonStoreForColumnAlias.fetch( { query: { alias: '*' },  
        onItem: function(item) {
           var alias = jsonStoreForColumnAlias.getValue( item, 'alias');
           if(value == alias){
        	   returnValue = false;
           }
        }
	});
	
	jsonStoreForRowAlias.fetch( { query: { alias: '*' },  
        onItem: function(item) {
           var alias = jsonStoreForRowAlias.getValue( item, 'alias');
           if(value == alias){
        	   returnValue = false;
           }
        }
	});
	
	var isEditRowAliasValue = document.getElementById("isEditRowAlias").getAttribute("value");
	
	var rowAliasValue;
	
	if(isEditRowAliasValue == "true"){
	var rowItems = gridForRowAlias.selection.getSelected();   
	dojo.forEach(rowItems, function(selectedItem) {
		rowAliasValue = gridForRowAlias.store.getValues(selectedItem, 'alias');
	});	
	}
	
	if( (isEditRowAliasValue == "true" && value == rowAliasValue)){
		return true;
	} else{
	return returnValue;
	}	
}

function validateColumnAlias(value, constraints){
	var returnValue = true;
	
	if(value == "" || value == null){
		returnValue = false;
	}
	
	jsonStoreForColumnAlias.fetch( { query: { alias: '*' },  
        onItem: function(item) {
           var alias = jsonStoreForColumnAlias.getValue( item, 'alias');
           if(value == alias){
        	   returnValue = false;
           }
        }
	});
	
	jsonStoreForRowAlias.fetch( { query: { alias: '*' },  
        onItem: function(item) {
           var alias = jsonStoreForRowAlias.getValue( item, 'alias');
           if(value == alias){
        	   returnValue = false;
           }
        }
	});
	
	var isEditDetailValue = document.getElementById("isEditDetail").getAttribute("value");
	
	var columnAliasValue;
	
	if(isEditDetailValue == "true"){
	var columnItems = gridForColumnAlias.selection.getSelected();   
	dojo.forEach(columnItems, function(selectedItem) {
		columnAliasValue = gridForColumnAlias.store.getValues(selectedItem, 'alias');
	});	
	}
	
	if((isEditDetailValue == "true" && value == columnAliasValue)){
		return true;
	} else{
	return returnValue;
	}	
}


function validateQualifier(value, constraints){
	var returnValue = true;
	var cfValue = dijit.byId("columnFamily").get('value');
	
	if(value == "" || value == null){
		returnValue = false;
	}
	
	jsonStoreForColumnAlias.fetch( { query: { alias: '*' },  
        onItem: function(item) {
           var qualifier = jsonStoreForColumnAlias.getValue( item, 'qualifier');
           var columnFamily = jsonStoreForColumnAlias.getValue( item, 'columnFamily');
           if(value == qualifier && cfValue == columnFamily){
        	   returnValue = false;
           }
        }
	});
	
	var isEditColumnAliasValue = document.getElementById("isEditDetail").getAttribute("value");
	
	var qualifierValue;
	var columnFamilyValue;
	
	if(isEditColumnAliasValue == "true"){
	var columnItems = gridForColumnAlias.selection.getSelected();   
	dojo.forEach(columnItems, function(selectedItem) {
		qualifierValue = gridForColumnAlias.store.getValues(selectedItem, 'qualifier');
		columnFamilyValue = gridForColumnAlias.store.getValues(selectedItem, 'columnFamily');
	});	
	}
	
	if( (isEditColumnAliasValue == "true" && value == qualifierValue && cfValue == columnFamilyValue)){
		return true;
	} else{
	return returnValue;
	}	
}

function deleteMapping(){
	//alert("DeletingMapping");
	var items = gridForMapping.selection.getSelected();
	//alert("Items length is: " + items.length);
    if(items.length==1){
    	
    	dojo.forEach(items, function(selectedItem) {
                var value = gridForMapping.store.getValues(selectedItem, 'id');
                document.getElementById("mappingIdToDelete").value = value;
        }); // end forEach
        showProgressIndicator();
    	var xhrArgs = {
            form: dojo.byId("deleteMappingLink"),
            url: "<s:url action='deleteMapping'/>",
            handleAs: "json",
            load: function(data) {
            	hideProgressIndicator();
            	console.log("Data is  " + data);
            	if(data.error.error){
            		responseMessage("Error: "+data.error.message);
                }else{
            	//alert ("Returned successfully");
            	updateMappingViewList(data);
            	
            	} 
            },
            error: function(error) {
            	hideProgressIndicator();
            	//alert("error:"+error);
            }
		};
		var deferred = dojo.xhrPost(xhrArgs);
		
	}else{
 		responseMessage("Please select a mapping.");
	}
}



function deleteTrue(){
	var deleteValue = document.getElementById("delete").getAttribute("value");
	//alert(deleteValue);
	if(deleteValue=="mapping"){
		deleteMapping();		
	} else if(deleteValue=="rowAlias"){
		deleteRowAlias();		
	} else if(deleteValue=="columnAlias"){
		deleteColumnAlias();
	}
	dijit.byId("deleteDialog").hide();
}

function deleteFalse(){	
	dijit.byId("deleteDialog").hide();
}

dojo.addOnLoad(function(e) {	
	var rowAliasColumnTypeName = dijit.byId("rowAliasColumnTypeName"); 
	var length = document.getElementById("length");	
    dojo.connect(rowAliasColumnTypeName, "onChange", function(e) {
    	if(rowAliasColumnTypeName.get('value') == 'Short'){
    		length.value = '2';
    	} else if(rowAliasColumnTypeName.get('value') == 'Integer'){
    		length.value = '4';
    	} else if(rowAliasColumnTypeName.get('value') == 'Long'){
    		length.value = '8';
    	} else if(rowAliasColumnTypeName.get('value') == 'Float'){
    		length.value = '4';
    	} else if(rowAliasColumnTypeName.get('value') == 'Double'){
    		length.value = '8';
    	} else if(rowAliasColumnTypeName.get('value') == 'Boolean'){
    		length.value = '1';
    	} else {
    		length.value = '';
    	}
    });
});
</script>


<s:if test="%{connections.size() > 0}">
	<div style="width: 60%; float: center">
		<div id="gridNodeForMapping" style="width: 200px; float: center"></div>
	</div>
	
	<br />

	<table id="linksForMapping">
		<tr>
			<td align="center" width="50px">
				<div dojoType="dijit.form.Form" id="addMappingLink">
					<a href="#" onClick="showMappingForm()">Add</a>
				</div>
			</td>
			<td align="center" width="50px">
				<div dojoType="dijit.form.Form" id="editMappingLink">
					<a href="#">Edit</a>
				</div>
			</td>
			<td align="center" width="50px">
				<div dojoType="dijit.form.Form" id="deleteMappingLink"
					name="deleteMappingLink" style="float: center">
					<input type="hidden" id="mappingIdToDelete" name="mapping.id"
						dojoType="dijit.form.TextBox" /> <a href="#">Delete</a>
				</div>
			</td>
		</tr>
	</table>

	<br />

	<div id="createMapping" name="createMapping" style="visibility: hidden; width: 1000px;">
		<table width="1000px"  align="center">
			<tr>
				<td align="center">
					<div dojoType="dijit.form.Form" id="mapping" name="mapping">
						<input type="hidden" id="mappingIdToEdit" name="mapping.id"
							dojoType="dijit.form.TextBox" />
						<table>							
							<tr>
								<td align="right">Mapping Name:&nbsp;</td>
								<td align="left"><input id="mappingName" type="text" name="mapping.name"
									dojoType="dijit.form.ValidationTextBox" maxlength=100 required="true"
									trim="true" promptMessage="Required." />
								</td>
							</tr>
						</table>
					</div>
					<div dojoType="dijit.layout.TabContainer" id="tabContainer"
						 style="width: 100%;" doLayout="false" tabStrip="true" tabPosition="left-h">
						<div dojoType="dijit.layout.ContentPane" id="tableContentPane" title="Table" selected="true" align="center">
							<div style="float: center">
								<div dojoType="dijit.form.Form" id="table" name="table">
								<table>
									<tr>
										<td align="right">
											<div id="connectionLabel">Connection:&nbsp;</div>
										</td>
										<td align="left"><select id="connection" name="connection.id"
												dojoType="dijit.form.Select">
												<s:iterator value="connections">
													<option value="${id}">${name}</option>
												</s:iterator>
											</select>
										</td>
									</tr>
									<tr>
										<td align="right">
											<div id="tableNameLabel">HBaseTable:&nbsp;</div>
										</td>
										<td align="left"><select id="tableName" name="mapping.tableName"
											dojoType="dijit.form.Select"></select>
										</td>
									</tr>
								</table>
								</div>
							</div>
						</div>
						<div dojoType="dijit.layout.ContentPane" title="Row Key Alias" align="center">
							<div style="float: center">
								<div id="gridNodeForRowAlias"
									style="width: 300px; float: center"></div>
							</div>
							<br />
							<table id="linksForRowKeyAlias">
								<tr>
									<td align="center" width="100px">
										<div id="addRowAliasLink">
											<a href="#" onClick="showAddRowAlias()">Add</a>
										</div>
									</td>
									<td align="center" width="100px">
										<div id="editRowAliasLink">
											<a href="#" onClick="editRowAlias()">Edit</a>
										</div>
									</td>
									<td align="center" width="100px">
										<div id="deleteRowAliasLink" name="deleteRowAliasLink"
											style="float: center">
											<a href="#" onClick="deleteRow()">Delete</a>
										</div></td>
								</tr>
							</table>
							<br />
							<div dojoType="dijit.form.Form" id="addRowAlias" name="addRowAlias"
								style="visibility: hidden">
								<input type="hidden" id="isEditRowAlias"
									dojoType="dijit.form.TextBox" />
								<input type="hidden" id="rowAliasId" name="rowAlias.id" dojoType="dijit.form.TextBox" />								
								<table frame="border">
									<tr>
										<td align="right">Alias:&nbsp;</td>
										<td align="left"><input id="rowAliasAlias" type="text"
											dojoType="dijit.form.ValidationTextBox" maxlength=100 required="true"
											trim="true" promptMessage="Required." invalidMessage="This alias already exists." />
										</td>
									</tr>
									<tr>
										<td align="right">Length:&nbsp;</td>
										<td align="left"><input id="length" type="text"
											 dojoType="dijit.form.ValidationTextBox" maxlength=100 regExp="[0-9]+" required="false" trim="true"
											 promptMessage="Numeric" invalidMessage="Invalid length" />
										</td>
									</tr>
									<tr>
										<td align="right">ValueType:&nbsp;</td>
										<td align="left"><select id="rowAliasColumnTypeName" name="columnType.name"
											dojoType="dijit.form.Select">
												<s:iterator value="columnTypeList">
													<option value="${name}">${name}</option>
												</s:iterator>
										</select>
										</td>
									</tr>
									<tr>
										<td></td>
										<td>
											<button type="button"
												onClick="populateGridNodeForRowAlias()"
												dojoType="dijit.form.Button">Save</button>
										</td>
									</tr>
								</table>
							</div>
						</div>

						<div dojoType="dijit.layout.ContentPane" title="Column Alias" align="center">
							<div style="float: center">
								<div id="gridNodeForColumnAlias"
									style="width: 300px; float: center"></div>
							</div>
							<br />
							<table id="linksForColumnAlias">
								<tr>
									<td align="center" width="100px">
										<div id="addColumnAliasLink">
											<a href="#" onClick="showAddColumnAlias()">Add</a>
										</div>
									</td>
									<td align="center" width="100px">
										<div id="editColumnAliasLink">
											<a href="#" onClick="editColumnAlias()">Edit</a>
										</div>
									</td>
									<td align="center" width="100px">
										<div id="deleteColumnAliasLink" name="deleteColumnAliasLink"
											style="float: center">
											<a href="#" onClick="deleteColumn()">Delete</a>
										</div></td>
								</tr>
							</table>
							<br />
							<div dojoType="dijit.form.Form" id="addColumnAlias" name="addColumnAlias"
								style="visibility: hidden">
								<input type="hidden" id="isEditDetail"
									dojoType="dijit.form.TextBox" />
								<input type="hidden" id="columnAliasId" name="columnAlias.id" dojoType="dijit.form.TextBox" />
								<table frame="border">
									<tr>
									<tr>
										<td align="right">ColumnFamily:&nbsp;</td>
										<td align="left"><select id="columnFamily"
											name="columnAlias.columnFamily" dojoType="dijit.form.Select"></select>
										</td>
									</tr>
									<tr>
										<td align="right">Qualifier:&nbsp;</td>
										<td align="left"><input id="qualifier" type="text"
											name="columnAlias.qualifier" invalidMessage="This qualifier and columnFamily pair already exists."
											dojoType="dijit.form.ValidationTextBox" maxlength=100 required="true"
											trim="true" promptMessage="Required." />
										</td>
									</tr>
									<tr>
										<td align="right">Alias:&nbsp;</td>
										<td align="left"><input id="alias" type="text"
											name="columnAlias.alias"
											dojoType="dijit.form.ValidationTextBox" maxlength=100 required="true"
											trim="true" promptMessage="Required." invalidMessage="This alias already exists." />
										</td>
									</tr>
									<tr>
										<td align="right">ValueType:&nbsp;</td>
										<td align="left"><select id="columnTypeName" name="columnType.name"
											dojoType="dijit.form.Select">
												<s:iterator value="columnTypeList">
													<option value="${name}">${name}</option>
												</s:iterator>
										</select>
										</td>
									</tr>
									<tr>
										<td></td>
										<td>
											<button type="button"
												onClick="populateGridNodeForColumnAlias()"
												dojoType="dijit.form.Button">Save</button>
										</td>
									</tr>
								</table>
							</div>

						</div>
					</div> 
					<button id="submitMapping" dojoType="dijit.form.Button">Save Mapping</button>
				</td>
			</tr>
		</table>
	</div>
	<br />
</s:if>
<s:else>
	<div style="color:#FF0000">No connection defined , Please first define connection on ConnectionPage.</div>
</s:else>

<div id="deleteDialog" dojoType="dijit.Dialog" title="Confirm Deletion">
	Are you sure you want to delete? Deletion can not be undone.
	<br /><br />
	<input type="hidden" id="delete" dojoType="dijit.form.TextBox" />
	<button id="deleteTrue" dojoType="dijit.form.Button" onClick="deleteTrue()">Yes</button>
	<button id="deleteFalse" dojoType="dijit.form.Button" onClick="deleteFalse()">No</button>
</div> 

<br />
<jsp:include page="footer.jsp" />