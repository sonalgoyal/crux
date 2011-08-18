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
<%@ taglib prefix="s" uri="/struts-tags"%>
<jsp:include page="header.jsp" />
<jsp:include page="topNavigation.jsp" />
<jsp:include page="progress.jsp" />
<jsp:include page="tableInclude.jsp" />
<h1 style="position: absolute; top: 10px; left: 45%;">Report Design</h1>
<br>
<br>
<script type="text/javascript" src="js/viewReport.js" ></script>
<%@ page import="org.apache.struts2.json.JSONUtil"%>
<s:set name="isEdit" value="%{edit}" />
<style type="text/css">
@import "js/dojo/dojo/resources/dnd.css";
.dojoDndItem{padding:3px;}
.dojoDndItemOver{
background-color:#D9E8F9;;
cursor:pointer;}
</style>
<div style="color:#FF0000"><s:property value='%{#parameters.errorMsg}'/></div>

<script type="text/javascript">
dojo.require("dijit.form.Form");
dojo.require("dijit.form.Button"); 
dojo.require("dijit.form.ValidationTextBox");
dojo.require("dojo.dnd.Source");
dojo.require("dijit.TitlePane");
dojo.require("dijit.form.Select");
dojo.require("dijit.layout.TabContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dojox.grid.DataGrid");
dojo.require("dojo.data.ItemFileWriteStore");
dojo.require("dijit.Dialog");
var dimensionList;
var measuresList ;
var xAxis;
var yAxis;
 
function showHelp(){
	 window.open ('help/designHelp.html', '', 'width=600,scrollbars=1');
}
 
 function changed(value){
	 document.getElementById("simplechart").style.visibility = "hidden";
	 document.getElementById("legend").style.visibility = "hidden";
	 if(value=="Table"){
	 dojo.style("yAxis","visibility","hidden");
	 dijit.byId("xAxis").set("title","Columns");
	 clearNodes(yAxis,true);
	// clearNodes(xAxis,true);
	 document.getElementById("ybutton").style.visibility = "hidden";
	 } else {
		 dojo.style("yAxis","visibility","visible");
		 dijit.byId("xAxis").set("title","X-Axis");
	 }
	 clearResponse();
	 chartType=value;
}
 
 function preview(){
		dojo.require('dojox.charting.widget.Chart2D');
		dojo.require('dojox.charting.widget.Legend');
		dojo.require('dojox.charting.DataSeries');
		dojo.require('dojox.charting.plot2d.Markers');
		dojo.require('dojox.charting.themes.ThreeD');
		dojo.require("dojox.json.query");
		dojo.require("dojox.charting.themes.Claro");
	 clearResponse();
  getDataFromHBase();
 }

dojo.addOnLoad(function(e) {
	document.getElementById("xbutton").style.visibility = "hidden";
	document.getElementById("ybutton").style.visibility = "hidden";
	document.getElementById("pane").style.visibility = "hidden";
	document.getElementById("pageDiv").style.visibility = "visible";
	var mappingName = dijit.byId("mappingName");
	var initReportForm = dijit.byId("populateDimensionAndMeasureList");
	dojo.style("dimensionList","visibility","hidden");
	dojo.style("measuresList","visibility","hidden");
	dimensionList = new dojo.dnd.Source("dimensionList");
	measuresList = new dojo.dnd.Source("measuresList");
	var xAxisPane = new dijit.TitlePane({title: "X-Axis", open: true}, "xAxis");
	var yAxisPane = new dijit.TitlePane({title: "Y-Axis", open: true}, "yAxis");
	 xAxis = new dojo.dnd.Target("xAxis");
	 yAxis = new dojo.dnd.Target("yAxis"); 
	 xAxis.parent = dojo.query("#xAxisNode tbody")[0];
	yAxis.parent = dojo.query("#yAxisNode tbody")[0]; 
	dojo.connect(mappingName, "onChange", populateList);
	dojo.connect(xAxisPane,"onMouseOut",highlightSelected);
	dojo.connect(yAxisPane,"onMouseOut",highlightSelected);
	var aliasSelect = dijit.byId("alias");
	dojo.connect(aliasSelect, "onChange", populateFilterType);	
});
function populateFilterType(){
	showProgressIndicator();
	clearResponse();
	var json = '{'+'"aliasName":"'+dijit.byId('alias').get('value')+'","mappingId":"'+dijit.byId('mappingName').get('value')+'"}';
	var aliasName = dojo.fromJson(json);
	 var xhrArgs = {
             url: "<s:url action='populateFilterType'/>",
             handleAs: "json",
             content: aliasName,
             load: function(data) {
            	 if(data.error.error){
             		responseMessage("Error: "+data.error.message);
                 }else{
                	 var filter_box  = dijit.byId('filterType');                    	
                    	filter_box.options.length = 0;
                    
                 	for(var i=0; i < data.filterTypeList.length; ++i){
                 		filter_box.addOption({value: data.filterTypeList[i].type, label: data.filterTypeList[i].type});                     		
                 	};
                 }
             } ,
             error: function(error) {
            	 responseMessage("Error " + error);
             }
	 };
	 var deferred = dojo.xhrPost(xhrArgs);
	 hideProgressIndicator();
}

function highlightSelected(){
	var xNodes = xAxis.getAllNodes();
	var yNodes = yAxis.getAllNodes();
	var selectedXNodes = xAxis.getSelectedNodes();
	var selectedYNodes = yAxis.getSelectedNodes();
	
	dojo.forEach(xNodes, function(node) {
		dojo.style(node, "color", "#000000");
	});
	
	dojo.forEach(yNodes, function(node) {
		dojo.style(node, "color", "#000000");
	});
	
	dojo.forEach(selectedXNodes, function(node) {
		dojo.style(node, "color", "#01aa00");
	});
	
	dojo.forEach(selectedYNodes, function(node) {
		dojo.style(node, "color", "#01aa00");
	});
}
function clearNodes(srcId,all){
	if(all){
	srcId.selectAll();
	}
	srcId.deleteSelectedNodes();
	srcId.selectNone();
}
function checkGridItems(grid,i){
	if(grid.getItem(i)!=null){
		deleteGridItem(grid,i);
	}
}
function deleteGridItem(grid,i){
	 jsonStore.deleteItem(grid.getItem(i));
	 checkGridItems(grid,0);
}
function populateList(){
	showProgressIndicator();
	clearResponse();
	document.getElementById("simplechart").style.visibility = "hidden";
	document.getElementById("legend").style.visibility = "hidden";
	 var grid = dijit.byId('gridNode');
	 if(grid.getItem(0)!=null){
		 checkGridItems(grid,0);
		 jsonStore.save();
			document.getElementById("addFilter").style.visibility = "visible";
			document.getElementById("links").style.visibility = "hidden";
			document.getElementById("gridNode").style.visibility = "hidden";
		}
	var val = dijit.byId('mappingName').get('value');
	dijit.byId('mappingId').set('value',val);
	dijit.byId('mapping_Id').set('value',val);
	var initReportForm = dijit.byId("populateDimensionAndMeasureList");
	var clearXAxisButton = dijit.byId("clearXAxisNode");
	var clearYAxisButton = dijit.byId("clearYAxisNode");
	dojo.connect(clearXAxisButton, "onClick", function(e){
		clearNodes(xAxis,false);
		if(xAxis.getAllNodes().length==0){
		document.getElementById("xbutton").style.visibility = "hidden";
		}else if(xAxis.getAllNodes().length==1) {
			xAxis.selectAll();
			highlightSelected();
		}
	});
	
	dojo.connect(clearYAxisButton, "onClick", function(e){
		clearNodes(yAxis,false);
		if(yAxis.getAllNodes().length==0){
		document.getElementById("ybutton").style.visibility = "hidden";
		} else if(yAxis.getAllNodes().length==1) {
			yAxis.selectAll();
			highlightSelected();
		}
	});
	clearNodes(dimensionList,true);
	clearNodes(measuresList,true);
	clearNodes(xAxis,true);
    clearNodes(yAxis,true);
	if(val!=null && val!="" ){
		
	if (initReportForm.validate()) {
		
		var xhrArgs = {
                form: dojo.byId("populateDimensionAndMeasureList"),
                url: "<s:url action='populateDimensionAndMeasureList'/>",
                handleAs: "json",
                load: function(data) {
                	if(data.error.error){
                 		responseMessage("Error: "+data.error.message);
                     }else{
                	dojo.style("dimensionList","visibility","visible");
                	dojo.style("measuresList","visibility","visible");
                	document.getElementById("pane").style.visibility = "visible";
                	document.getElementById("reportTypeTable").style.visibility = "visible";
                	
                	dimensionList.copyOnly=true;
                	measuresList.copyOnly=true;
                	
            		dimensionList.insertNodes(false, data.dimensionsList);
            		measuresList.insertNodes(false, data.measuresList);
           		
            		xAxis.checkAcceptance = customCheckAcceptance;
            		yAxis.checkAcceptance = customCheckAcceptance;
            		dimensionList.checkAcceptance = NoneCheckAcceptance;
            		measuresList.checkAcceptance = NoneCheckAcceptance;
            		
            		dojo.subscribe("/dnd/start", null, highlightTargets);
            		dojo.subscribe("/dnd/cancel", null, unhighlightTargets);
            		dojo.subscribe("/dnd/drop", function(){
            			unhighlightTargets(dojo.dnd.manager().target);
            		});

            	   	var select_box  = dijit.byId('reportType');                    	
                	select_box.options.length = 0;
                	
                	for(var i=0; i < data.reportTypeList.length; ++i){
                		var key = data.reportTypeList[i].type;
                		select_box.addOption({value: key, label: key});                     		
                	};
                	
                	var alias_box  = dijit.byId('alias');                    	
                	alias_box.options.length = 0;
                	
                	for(var i=0; i < data.dimensionsList.length; ++i){
                		alias_box.addOption({value: data.dimensionsList[i], label: data.dimensionsList[i]});                     		
                	};
                	for(var i=0; i < data.measuresList.length; ++i){
                		alias_box.addOption({value: data.measuresList[i], label: data.measuresList[i]});                     		
                	};
                     }
                } ,
                    error: function(error) {
                    	 responseMessage("Error: "+error);
                    }
		};
			
		var deferred = dojo.xhrPost(xhrArgs);
		}
	} else {
			responseMessage("No mapping defined. Please define mapping on mapping page");
			document.getElementById("reportTypeTable").style.visibility = "hidden";
			document.getElementById("addFilter").style.visibility = "hidden";
	}
	hideProgressIndicator();
}
function NoneCheckAcceptance(source, nodes) {
	return false;
}
function clearInnerHTML(text){
	var array = text.split(">");
	if(array.length>1){
		return array[1].split("<")[0];
	} else {
		return array[0];
	}
}
function customCheckAcceptance(source, nodes) {
var xNodes=xAxis.getAllNodes();
var yNodes=yAxis.getAllNodes();
var allow=true;
	for(var i=0;i<xNodes.length;i++) {
		if(clearInnerHTML(xNodes[i].innerHTML)==clearInnerHTML(nodes[0].innerHTML)) {
			allow = false;	
		}
    }
	for(var i=0;i<yNodes.length;i++) {
		if(clearInnerHTML(yNodes[i].innerHTML)==clearInnerHTML(nodes[0].innerHTML)) {
			allow = false;
			
		}
    }
	
	return allow;
}
function getValuesFromPane(paneId){
	var array=[];
	
	var table = document.getElementById(paneId);
	var tableCells = table.getElementsByTagName("td");
	var tableDivCells = table.getElementsByTagName("div");
	
	for(var i=0;i<tableDivCells.length;i++){
		array.push(tableDivCells[i].innerHTML);
	}
	
	for(var i=0;i<tableCells.length;i++){
		array.push(tableCells[i].innerHTML);
	}
	return array;
}
		function getDataFromHBase() {
			
			var getDataForm = dijit.byId("saveReportForm");
			showProgressIndicator();
			if (getDataForm.validate()) {
			var xhrArgs = {
	                form: dojo.byId("saveReportForm"),
	                url: "<s:url action='getDataAction'/>",
	                handleAs: "json",
	                load: function(data) {
	                	if(data.error.error){
	                 		responseMessage("Error: "+data.error.message);
	                     }else{
	      
	                	  if(chartType!="Table"){
	                		 dijit.byId("chartDialog").show();
	 	                	 document.getElementById("preReportName").innerHTML=dijit.byId("reportName").get("value");
	                    	 doPlot(data.dataList);
	                	  } else {
	                		  dijit.byId("tableDialog").show();
		 	                  document.getElementById("preTableName").innerHTML=dijit.byId("reportName").get("value");
		 	                  doPlotTable(data.dataList);
	                	  }   
	                     }
	                	hideProgressIndicator();
	                },
	                error: function(error) {
	                	responseMessage("Error: "+error);
	                	hideProgressIndicator();
	                }
			};
		var deferred = dojo.xhrPost(xhrArgs);
		removeAppendedChild();
			}
		}	
		function removeAppendedChild(){
			var count = 0;
	    	var saveForm = document.getElementById("saveReportForm");
	    	 jsonStore.fetch({
	   	        query: { alias: '*' },
	   	       onItem: function(item) {
	   	    	 saveForm.removeChild(document.getElementById("filterList["+count+"].alias"));
	   	    	 saveForm.removeChild(document.getElementById("filterList["+count+"].filterType"));
	   	    	saveForm.removeChild(document.getElementById("filterList["+count+"].value"));
	  	    	count++;
	   	        }
	   	    });
		}
		function highlightTargets(){
			var props = {
					margin: { start: '0', end: '-5', unit: 'px' },
					borderWidth: { start: '0', end: '5', unit: 'px' }
			};
			
			dojo.style("xAxis", "color", "#01aa00");
			dojo.style("yAxis", "color", "#01aa00");
			clearResponse();
			document.getElementById("simplechart").style.visibility = "hidden";
			document.getElementById("legend").style.visibility = "hidden";
		}
		function unhighlightTargets(dropTarget){
		var props = {
				margin: { start: '-5', end: '0', unit: 'px' },
				borderWidth: { start: '5', end: '0', unit: 'px' }
		};
		dojo.style("xAxis", "color", "#000000");
		dojo.style("yAxis", "color", "#000000");
		dojo.query('.dojoDndAvatar').forEach(function(node, index, arr){
			dojo.style(node, "visibility","hidden");
		  });
		
		if(dropTarget==xAxis){
			document.getElementById("xbutton").style.visibility = "visible";
		} else if(dropTarget==yAxis){
			document.getElementById("ybutton").style.visibility = "visible";
		}
	}
		var storeData = {
		        label: 'alias',
		        items:<%out.println(JSONUtil.serialize(request.getAttribute("filterViewList")));%> 
		    };
		    
		var jsonStore = new dojo.data.ItemFileWriteStore({ data: storeData });
		
		dojo.addOnLoad(function(){
		    var layout = [[{   field: "alias",
		  					   name: "RowAlias/ColumnAlias.",
		 					   width: "auto"
		   						 },
		                      {
		                          field: "filterType",
		                          name: "FilterType",
		                          width: "auto"
		                      },
		                      {
		                          field: "value",
		                          name: "Value",
		                          width:  "auto"
		                       }]];

			var grid = new dojox.grid.DataGrid({
				query: { alias: '*' },
				store: jsonStore,
				structure: layout,
				selectionMode: 'single',
				autoHeight:5
			}, 'gridNode');
			grid.startup();
			document.getElementById("links").style.visibility = "visible";
			if(grid.getItem(0)==null){
				document.getElementById("addFilter").style.visibility = "visible";
				document.getElementById("links").style.visibility = "hidden";
				document.getElementById("gridNode").style.visibility = "hidden";
			}
				dojo.connect(grid, "onRowClick", function(e) {
					clearResponse();
					if(dijit.byId('isEdit').get('value')=="true"){
						editFilter();
					}
				});
		});
	function populateGridNodeForFilter(){
		clearResponse();
			 var grid = dijit.byId('gridNode');
			 if(dijit.byId('isEdit').get('value')=="true"){
				 var items = grid.selection.getSelected();
				 if(items.length==1){
	    		 dojo.forEach(items, function(selectedItem) {
	    				 jsonStore.deleteItem(selectedItem);
	             });
	     		jsonStore.save();
		 		}
				 grid.selection.clear();
				 dijit.byId('isEdit').set('value',"false");
			}
			 var save = true;
			 var aliasValue = dijit.byId("alias").get('value'); 
			 var filterTypeValue = dijit.byId("filterType").get('value'); 
			 jsonStore.fetch({
		 	        query: { alias: '*' },
		 	       onItem: function(item) {
		 	    	 var alias= jsonStore.getValue( item, 'alias');
		 	    	 var filterType = jsonStore.getValue( item, 'filterType');
		 	    	 if(alias==aliasValue && filterType == filterTypeValue){
		 	    		 save =false;
		 	    		responseMessage("Invalid data, Alias already exists with this filterType.");
		 	    	 }
		 	       }
			 });
		
		 if(save){
		 var filterType = dijit.byId("filterType").get('value');

		jsonStore.newItem({		
			alias:aliasValue,
			filterType:filterType, 
			value:dijit.byId("value").get('value')});
		 jsonStore.save();
		 document.getElementById("addFilter").style.visibility = "hidden";
		 dijit.byId('alias').set('value','');
    	 dijit.byId('filterType').set('value','');
    	 dijit.byId('value').set('value','');
		if(grid.getItem(0)==null){
			responseMessage("No Filter defined. Please define filter");
		} else {
			document.getElementById("links").style.visibility = "visible"; 
			document.getElementById("gridNode").style.visibility = "visible";
			clearResponse();
		};
	}
	}
	function addFilter(){
		clearResponse();
		document.getElementById("addFilter").style.visibility = "visible";
		dijit.byId('alias').set('value','');
   		 dijit.byId('filterType').set('value','');
   		 dijit.byId('value').set('value','');
   		 dijit.byId('isEdit').set('value',"false");
	}
	function deleteFilter(){
		clearResponse();
		dijit.byId('isEdit').set('value',"false");
		var grid = dijit.byId('gridNode');
		 var items = grid.selection.getSelected();		 
		 if(items.length==1){
			 dijit.byId("deleteDialog").show();			
		}else{            	
		responseMessage("Please select filter to delete");
  		} 
		 document.getElementById("addFilter").style.visibility = "hidden";
	}
	function deleteTrue(){
			var grid = dijit.byId('gridNode');
		 var items = grid.selection.getSelected();		 
		 if(items.length==1){
			 dojo.forEach(items, function(selectedItem) {
	    	 		jsonStore.deleteItem(selectedItem);
	            });
	     		jsonStore.save();
	     		grid.selection.clear();
	     		if(grid.getItem(0)==null){
	     			document.getElementById("links").style.visibility = "hidden";
					document.getElementById("addFilter").style.visibility = "visible";
					document.getElementById("gridNode").style.visibility = "hidden";
					responseMessage("No Filter defined. Please define filter");
				};
		}else{            	
		responseMessage("Please select filter to delete");
   		} 
		dijit.byId("deleteDialog").hide();
	}
	function deleteFalse(){	
		dijit.byId("deleteDialog").hide();
	}
	
	function editFilter(){
			clearResponse();
		 var grid = dijit.byId('gridNode');
		 var items = grid.selection.getSelected();
		 if(items.length==1){
	     dojo.forEach(items, function(selectedItem) {
	    	 dijit.byId('alias').set('value',grid.store.getValues(selectedItem, 'alias'));
	    	 dijit.byId('value').set('value',grid.store.getValues(selectedItem, 'value'));
	    	 var typeFilter = grid.store.getValues(selectedItem,'filterType');
	    	 setTimeout(function (typeFilter) {
	    		 dijit.byId('filterType').set('value',typeFilter);
	    	 },500,typeFilter);
	     });
	     dijit.byId('isEdit').set('value',"true");
	     document.getElementById("addFilter").style.visibility = "visible"; 
		} else {
			 document.getElementById("addFilter").style.visibility = "hidden"; 
			responseMessage("Please select filter to edit");
		}
	}
		
	function saveReport(save){
		var filterValue =true;
		 xArray=getValuesFromPane("xAxisNode");
		 yArray=getValuesFromPane("yAxisNode");
		var reportName=document.getElementById('reportName').value;
				
		if(reportName==""){
			dijit.byId('reportNameForm').validate();
			responseMessage("Please Name the Report");
		} else {
			if(isValidSaveForm()){			
		document.getElementById('report_Name').value=reportName;
		
		var axisValue = getAxisValue("x",xArray)+getAxisValue("y",yArray);
		var formAxisVal = document.getElementById("axisValues");
		formAxisVal.setAttribute("value", axisValue);
		var saveForm = document.getElementById("saveReportForm");
		var count = 0;
	 	 jsonStore.fetch({
 	        query: { alias: '*' },
 	       onItem: function(item) {
 	    	 var alias= jsonStore.getValue( item, 'alias');
 	    	var filterType= jsonStore.getValue( item, 'filterType');
 	    	var value= jsonStore.getValue( item, 'value');
 	    	var hiddenField = document.createElement("input");
 	        hiddenField.setAttribute("type", "hidden");
 	        hiddenField.setAttribute("name", "filterList["+count+"].alias");
 	        hiddenField.setAttribute("value", alias);
 	       hiddenField.setAttribute("id", "filterList["+count+"].alias");
 	       var hiddenField1 = document.createElement("input");
	        hiddenField1.setAttribute("type", "hidden");
	        hiddenField1.setAttribute("name", "filterList["+count+"].filterType");
	        hiddenField1.setAttribute("value", filterType);
	        hiddenField1.setAttribute("id", "filterList["+count+"].filterType");
	        var hiddenField2 = document.createElement("input");
	        hiddenField2.setAttribute("type", "hidden");
	        hiddenField2.setAttribute("name", "filterList["+count+"].value");
	        hiddenField2.setAttribute("value", value);
	        hiddenField2.setAttribute("id", "filterList["+count+"].value");
	        if(value==""){
	        	filterValue=false;
	        }
	    	count++;
	    	saveForm.appendChild(hiddenField1);
	    	saveForm.appendChild(hiddenField);
	    	saveForm.appendChild(hiddenField2);
	    	
 	        }
 	    });
	 	 
		if(save){
		saveForm.submit();
		} else {
			if(filterValue){
			preview();
			} else {
				responseMessage("For preview please define value for your filter");
				removeAppendedChild();
			}
		}
			
			}
		}
		}
	
	function getAxisValue(axis,array){
		var result = "";
		for(var i=0;i<array.length;i++){
			if(array[i]!=""){
			result=result+axis+","+array[i]+":";
			}
		}
		return result;
	}
	
	function isValidSaveForm(){
		var isValid = true;
		if(chartType=="Table"){
			if(xArray.length==0)	{
				responseMessage("Please drag and drop appropriate alias to define Columns for your report");
				isValid=false;
			}
		} else {
			if(xArray.length>1){
				responseMessage("Invalid data for X-Axis. Please add only one Alias for X-Axis");
				isValid=false;
			} else {
			if(xArray.length==0 || yArray.length==0 )	{
				responseMessage("Please drag and drop appropriate alias to define your report");
				isValid=false;
				}
			}
		}
		return isValid;
	}
</script>


	<div id="pageDiv" style="width: 1000px; visibility: hidden;">
	
		<table>	<tr><td align="right">
     				Report Name: &nbsp; </td><td align="left">
     				<div id="reportNameForm"  dojoType="dijit.form.Form">   				
     						<input type="text" id="reportName" name="report.name" 
     							dojoType="dijit.form.ValidationTextBox" maxlength=100 required="true"
								trim="true" value="${report.name}" />
							</div> </td></tr>
					<tr><td align="right">		
					Mapping:&nbsp; </td><td align="left">
					<select id="mappingName" name="mappingId" dojoType="dijit.form.Select" >
						<s:iterator value="%{mappingList}" status="maps">
								<option id="<s:property value='id'/>" value="<s:property value='id'/>">
									<s:property value='name'/>
								</option>
						</s:iterator>
					</select> </td></tr>
					</table>
  <div id="reportDetail" dojoType="dijit.layout.TabContainer" style="width: 100%;  float: center;" tabPosition="left-h" doLayout="false">
  	 <div dojoType="dijit.layout.ContentPane" title="Design" selected="true" align="center">
			<div style="float: left;">
				<table id="dimensionList" class="container" frame="border" align="center">
					<tr>
						<td width="200"><b>Dimensions</b>
						</td>
					</tr>
				</table>
			</div>
	
			<div dojoType="dijit.form.Form"	id="populateDimensionAndMeasureList" jsId="populateDimensionAndMeasureList"
						encType="multipart/form-data" action="" method="">
						<input type="hidden" id="mappingId" name="mappingId" dojoType="dijit.form.TextBox" value="" />	
			</div>
		
		
			<div dojoType="dijit.form.Form" action="saveReport" id="saveReportForm" encType="multipart/form-data">
					<input type="hidden" name="report.id" dojoType="dijit.form.TextBox" value="${report.id}" />
					<input	type="hidden" id="axisValues" name="axisValues"	dojoType="dijit.form.TextBox" /> 
					<input type="hidden" id="report_Name" name="report.name" value="${report.name}" dojoType="dijit.form.TextBox" />
					<input type="hidden" id="mapping_Id" name="mappingId" dojoType="dijit.form.TextBox" />
				
		<table id="reportTypeTable" style="visibility: hidden;">	<tr><td align="right">	
  					Report Type: &nbsp; </td><td> 
  					<select id="reportType" name="reportType" dojoType="dijit.form.Select" onchange='changed(this.value)' >
					</select> <br /></td></tr>
			</table>
			<div id="pane" align="center" style="visibility: hidden;">		
			<table>
				<tr>
					<td width="200px">
						<div id="xAxis" style="width: 100px">
							<table id="xAxisNode">
								<tbody>
						
								</tbody>
							</table>
						</div>
					</td>
					<td width="200px">
						<div id="yAxis" style="width: 100px">
							<table id="yAxisNode">
								<tbody>
								
								</tbody>
							</table>
					</div></td>
				</tr>
				<tr>
					<td width="100px">
					<div id="xbutton">
						<button id="clearXAxisNode" dojoType="dijit.form.Button">Clear</button>
						</div></td>
					<td width="100px">
					<div id="ybutton">
						<button id="clearYAxisNode" dojoType="dijit.form.Button">Clear</button>
						</div>
					</td>
				</tr>
			</table> 
			</div>
		</div>
		
		<div style="float: left;">
				<table id="measuresList" class="container" frame="border" align="center">
					<tr >
						<td width="200"><b>Measures</b><td>
					</tr>
				</table>
			</div>
		</div>
	<div dojoType="dijit.layout.ContentPane" title="Filters">
		<br>
			<div id="gridNode" style="width: 200px; float: center;"></div>
		<br>
		<table id="links" style="visibility: hidden;">
  		<tr>
 			 <td width="50px">
 			 <div id='addButton'>
 			 		<a href="#" onClick='addFilter()'>Add</a>
 			 		</div>
 			 </td>
			<td width="50px">
			<div id='editButton'>
 					 <s:a href="#" onClick='editFilter()'>Edit</s:a>
 				</div>
			</td>
 		    <td width="50px">
 		    <div id='deleteButton'>
					<a href="#" onClick='deleteFilter()'>Delete</a>	
				</div>
			</td >
		</tr>
		</table>
			<br>
			<div id="addFilter" name="addFilter" style="visibility: hidden;">
								<input type="hidden" id="isEdit" dojoType="dijit.form.TextBox" value="" />
								<table frame="border" align="center">
									<tr>
										<td align="right">ColumnAlias/RowAlias:&nbsp;</td>
										<td align="left"><select id="alias"
											name="alias" dojoType="dijit.form.Select"></select>
										</td>
									</tr>
									<tr>
										<td align="right">FilterType:&nbsp;</td>
										<td align="left"><select id="filterType"
											name="filterType" dojoType="dijit.form.Select"></select>
										</td>
									</tr>
									<tr>
										<td align="right">Value:&nbsp;</td>
										<td align="left"><input id="value" type="text"
											name="value"
											dojoType="dijit.form.ValidationTextBox" maxlength=100 required="false"
											trim="true"/>
										</td>
									</tr>
									<tr>
										<td></td>
										<td>
											<button type="button"
												onClick="populateGridNodeForFilter()"
												dojoType="dijit.form.Button">Save</button>
										</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
			
			<div id="buttons" align="center">
			<button dojoType="dijit.form.Button" onClick='saveReport(true)'>Save Report</button>
			<button dojoType="dijit.form.Button" onClick='saveReport(false)'>Preview</button>
			</div>	

		   <div id="chartDialog" dojoType="dijit.Dialog" title="Report Preview" style="width: 800px; height: 400px; float: center;">
			<div style="height: 100%">
			<table align="center">
				<tr>
					<td>
					<b><label id="preReportName"></label></b>
					</td>
				</tr>
				<tr>
					<td>
						<div id="simplechart" style="width: 600px; height: 300px; float: center;"></div>
					</td>
				</tr>
				<tr>
					<td>
						<div id="legend"></div>
					</td>
				</tr>
			</table>
			</div>
		</div>
		
		<div id="tableDialog" dojoType="dijit.Dialog" title="Report Preview" style="width: 800px; height: 400px;">
			<div style="height: 100%">
			<table >
				<tr>
					<td>
					<b><label id="preTableName"></label></b>
					</td>
				</tr>
				</table>
				
				<div id="tableNode"></div>
			</div>
			
		</div>
			
	</div>
	<br><br><br><br>

<s:if test="%{#isEdit=='true'}">
<script type="text/javascript">
var mapName = '<%=request.getAttribute("mappingId")%>';
document.getElementById(mapName).selected=true;
 
dojo.addOnLoad(function(e) {
	dijit.byId("mappingName").onChange();
	setTimeout("populateEditData()",800);
});

function populateEditData() {
	dijit.byId("reportType").set('value','<%=request.getAttribute("reportType")%>');
	var xArray = [];
	var yArray = [];
	var values = "<%=request.getAttribute("axisValues")%>";
	var axisValue = values.split(":");
	for(var i=0;i<axisValue.length;i++){
		var val = axisValue[i].split(",");
		if(val[0]=="x"){
			xArray.push(val[1]);
		} else if(val[0]=="y"){
			yArray.push(val[1]);
		}
	}	

					xAxis.insertNodes(false, xArray);
					yAxis.insertNodes(false, yArray);
					
	var items=<%out.println(JSONUtil.serialize(request.getAttribute("filterViewList")));%> ;
	for(var i=0;i<items.length;i++){
		jsonStore.newItem(items[i]);
	}
	jsonStore.save();		
	 var grid = dijit.byId('gridNode');
	if(grid.getItem(0)!=null){
		document.getElementById("addFilter").style.visibility = "hidden";
		document.getElementById("links").style.visibility = "visible";
		document.getElementById("gridNode").style.visibility = "visible";
	}	
	
	document.getElementById("xbutton").style.visibility = "visible";
	document.getElementById("ybutton").style.visibility = "visible";
	}
</script> </s:if> 
<s:else>
<script type="text/javascript">
dojo.addOnLoad(function(e) {
dijit.byId("mappingName").onChange();
});
</script> 
</s:else>
<div id="deleteDialog" dojoType="dijit.Dialog" title="Confirm Deletion">
	Are you sure you want to delete? Deletion can not be undone.
	<br /><br />
	<button id="deleteTrue" dojoType="dijit.form.Button" onClick="deleteTrue()">Yes</button>
	<button id="deleteFalse" dojoType="dijit.form.Button" onClick="deleteFalse()">No</button>
</div> 
<jsp:include page="footer.jsp" />