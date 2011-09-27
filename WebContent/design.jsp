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
<script type="text/javascript" src="js/viewReport.js"></script>
<script type="text/javascript" src="js/d3/d3.js"></script>
<script type="text/javascript" src="js/polymaps/polymaps.js"></script>
<%@ page import="org.apache.struts2.json.JSONUtil"%>
<s:set name="isEdit" value="%{edit}" />
<style type="text/css">
@import "js/dojo/dojo/resources/dnd.css";

@import "js/polymaps/polyStyle.css";

.dojoDndItem {
	padding: 3px;
}

.dojoDndItemOver {
	background-color: #D9E8F9;;
	cursor: pointer;
}

.claro .dijitArrowButtonInner {
	background-position: -54px 40%;
	background-repeat: no-repeat;
	height: 6px;
	margin: 0 auto;
	width: 9px;
}

svg {
	display: block;
}

circle {
	stroke: black;
	fill: brown;
	fill-opacity: .5;
}

#mapNode {
	width: 960px;
	height: 500px;
}

</style>
<div style="color: #FF0000">
	<s:property value='%{error.message}' />
</div>

<script type="text/javascript">
	dojo.require("dijit.form.Form");
	dojo.require("dijit.form.Button"); 
	dojo.require("dijit.form.ValidationTextBox");
	dojo.require("dijit.form.CheckBox");
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
	var zAxis;
	var chartType;
	function showHelp(){
		 window.open ('help/designHelp.html', '', 'width=600,scrollbars=1');
	}
 
	 function changed(value){
		 document.getElementById("simplechart").style.visibility = "hidden";
		 document.getElementById("legend").style.visibility = "hidden";
		 if(value=="Table"){
			 dojo.style("yAxis","visibility","hidden");
			 dojo.style("zAxis","visibility","hidden");
			 dijit.byId("xAxis").set("title","Columns");
			 clearNodes(yAxis,true);
			 clearNodes(zAxis,true);
	 		 document.getElementById("ybutton").style.visibility = "hidden";
			 document.getElementById("zbutton").style.visibility = "hidden";
	 	} else if(value=="Choropleth"){
		 	 dojo.style("zAxis","visibility","visible");
			 dojo.style("yAxis","visibility","visible");
			 dijit.byId("xAxis").set("title","Latitude");
			 dijit.byId("yAxis").set("title","Longitude");
   		} else {
			 document.getElementById("zbutton").style.visibility = "hidden";
			 dojo.style("zAxis","visibility","hidden");
			 dojo.style("yAxis","visibility","visible");
			 dijit.byId("xAxis").set("title","X-Axis");
			 dijit.byId("yAxis").set("title","Y-Axis");
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
		dojo.require("dojox.charting.widget.SelectableLegend");
	    clearResponse();
        getDataFromHBase();
 	}

	dojo.addOnLoad(function(e) {
		document.getElementById("xbutton").style.visibility = "hidden";
		document.getElementById("ybutton").style.visibility = "hidden";
		document.getElementById("zbutton").style.visibility = "hidden";
		dojo.style("zAxis","visibility","hidden");
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
		var zAxisPane = new dijit.TitlePane({title: "Data", open: true}, "zAxis");
	 	xAxis = new dojo.dnd.Source("xAxisNode");
	 	yAxis = new dojo.dnd.Source("yAxisNode");
	 	zAxis = new dojo.dnd.Source("zAxisNode");
	 	xAxis.parent = dojo.query("#xAxisNode tbody")[0];
		yAxis.parent = dojo.query("#yAxisNode tbody")[0];
		zAxis.parent = dojo.query("#zAxisNode tbody")[0];
		dojo.connect(xAxisPane,"onMouseOut",highlightSelected);
		dojo.connect(yAxisPane,"onMouseOut",highlightSelected);
		dojo.connect(zAxisPane,"onMouseOut",highlightSelected);
		var aliasSelect = dijit.byId("alias");
		dojo.connect(aliasSelect, "onChange", populateFilterType);	
	});
	
	function populateFilterType(){
		 clearResponse();
		 var json = '{'+'"aliasName":"'+dijit.byId('alias').get('value')+'","mappingId":"'+dijit.byId('mappingName').get('value')+'"}';
		 var aliasName = dojo.fromJson(json);
		 showProgressIndicator();
		 var xhrArgs = {
             url: "<s:url action='populateFilterType'/>",
             handleAs: "json",
             content: aliasName,
             load: function(data) {
            	 hideProgressIndicator();
            	 if(data.error.error){
             		responseMessage("Error: "+data.error.message);
                 }else{
                		var filter_box  = dijit.byId('filterType');                    	
                    	filter_box.options.length = 0;
                 		for(var i=0; i < data.filterTypeList.length; ++i){
                 		filter_box.addOption({value: data.filterTypeList[i].type, label: data.filterTypeList[i].type});                     		
                 		}
                  }
              } ,
              error: function(error) {
            	 hideProgressIndicator();
            	 responseMessage("Error " + error);
              }
	 	};
	    var deferred = dojo.xhrPost(xhrArgs);
  	}

	function highlightSelected(){
		var xNodes = xAxis.getAllNodes();
		var yNodes = yAxis.getAllNodes();
		var zNodes = zAxis.getAllNodes();
		var selectedXNodes = xAxis.getSelectedNodes();
		var selectedYNodes = yAxis.getSelectedNodes();
		var selectedZNodes = zAxis.getSelectedNodes();
		dojo.forEach(xNodes, function(node) {
			dojo.style(node, "color", "#000000");
		});
		dojo.forEach(yNodes, function(node) {
			dojo.style(node, "color", "#000000");
		});
		dojo.forEach(zNodes, function(node) {
			dojo.style(node, "color", "#000000");
		});
		dojo.forEach(selectedXNodes, function(node) {
			dojo.style(node, "color", "#01aa00");
		});
		dojo.forEach(selectedYNodes, function(node) {
			dojo.style(node, "color", "#01aa00");
		});
		dojo.forEach(selectedZNodes, function(node) {
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
	var aliasValueType = [];
	var functionView = [];
	
	function populateList(callEdit){
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
		var clearZAxisButton = dijit.byId("clearZAxisNode");
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
		dojo.connect(clearZAxisButton, "onClick", function(e){
			clearNodes(zAxis,false);
			if(zAxis.getAllNodes().length==0){
				document.getElementById("zbutton").style.visibility = "hidden";
			} else if(zAxis.getAllNodes().length==1) {
				zAxis.selectAll();
				highlightSelected();
			}
		});
		clearNodes(dimensionList,true);
		clearNodes(measuresList,true);
		clearNodes(xAxis,true);
   		clearNodes(yAxis,true);
		if(val!=null && val!="" ){
			if (initReportForm.validate()) {
			showProgressIndicator();
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
	                	var dimensionArray = [];
  		              	var measureArray = [];
       	            	aliasValueType = data.dimensionAndMeasureViewList;
                		functionView = data.functionViewList;
	                	for (var i=0;i<data.dimensionAndMeasureViewList.length;i++){
                			if (data.dimensionAndMeasureViewList[i].isDimension=="true"){
                				dimensionArray.push(data.dimensionAndMeasureViewList[i].alias);
                			} else {
                				measureArray.push(data.dimensionAndMeasureViewList[i].alias);
                			}
                		}
            			dimensionList.insertNodes(false, dimensionArray);
            			measuresList.insertNodes(false, measureArray);
	            		//xAxis.checkAcceptance = customCheckAcceptance;
     		       		//yAxis.checkAcceptance = customCheckAcceptance;
            			dimensionList.checkAcceptance = NoneCheckAcceptance;
            			measuresList.checkAcceptance = NoneCheckAcceptance;
	            		dojo.subscribe("/dnd/start", null, highlightTargets);
            			dojo.subscribe("/dnd/cancel", null, unhighlightTargets);
            			/*dojo.subscribe("/dnd/drop", function(){
            					unhighlightTargets(dojo.dnd.manager().target);
            			});*/
            			dojo.subscribe("/dnd/drop", function(source, nodes, copy, target){
            				 unhighlightTargets(dojo.dnd.manager().target);
            				 target.forInSelectedItems(function(item, id){
            			     //alert("Children length: " + dojo.byId(id).childNodes.length);
            				 	if(dojo.byId(id).childNodes.length == 1){
            							displayFunctionMenu(id);
            				  	}
            				 });
            			 	 if(zAxis.getAllNodes().length==0){
         							document.getElementById("zbutton").style.visibility = "hidden";
         					  } else if(zAxis.getAllNodes().length==1) {
         							zAxis.selectAll();
         							highlightSelected();
         					  }
	            			 if(yAxis.getAllNodes().length==0){
            						document.getElementById("ybutton").style.visibility = "hidden";
            				 } else if(yAxis.getAllNodes().length==1) {
            						yAxis.selectAll();
            						highlightSelected();
            				 }
            			 	if(xAxis.getAllNodes().length==0){
            					document.getElementById("xbutton").style.visibility = "hidden";
            				}else if(xAxis.getAllNodes().length==1) {
            					xAxis.selectAll();
            					highlightSelected();
            				}
            		   });
                  	   var select_box  = dijit.byId('reportType');                    	
                	   select_box.options.length = 0;
                	   for(var i=0; i < data.reportTypeList.length; ++i){
                	  		 var key = data.reportTypeList[i].type;
                	   		 select_box.addOption({value: key, label: key});                     		
                		};
	                	var alias_box  = dijit.byId('alias');                    	
             		   	alias_box.options.length = 0;
	                	for(var i=0; i < data.dimensionAndMeasureViewList.length; ++i){
       			         		alias_box.addOption({value: data.dimensionAndMeasureViewList[i].alias, label: data.dimensionAndMeasureViewList[i].alias});                     		
                		};
                		if(callEdit){
                			populateEditData();
                		}
                     }
                	hideProgressIndicator();
                 } ,
                 error: function(error) {
                    	 hideProgressIndicator();
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
 }
		function displayFunctionMenu(id){
				if(dojo.byId(id).childNodes.length == 2){
						dojo.destroy(dojo.byId(id).childNodes[1]);
				}
				var menu = new dijit.Menu({
      					  style: "display: none;"
   					 });
				//alert("Creating sub-menu for functions");
  				var subMenu = new dijit.Menu();
				subMenu.addChild(new dijit.MenuItem({
						label: "None",
						onClick: function() {
						removeFunctionIfExists(id);
						}
				}));
				var valueTypeFunc = "";
				var table = document.getElementById(id);
				var tableCells = table.getElementsByTagName("td");
				var alias = "";
				if(tableCells.length!=0){
					alias = tableCells[0].innerHTML;
				} else {
					alias = table.innerHTML;
				}
				var length = alias.split("(").length;
				var lastFunc = "";
				if(length > 1){
					lastFunc = trim(alias.split("(")[0]);
					for(var i=0;i<functionView.length;i++){
							if(functionView[i].functionName==lastFunc){	
							valueTypeFunc=functionView[i].returnType;
							}
					}
				} else {
					for(var k=0;k<aliasValueType.length;k++){
						if(alias==aliasValueType[k].alias){
							valueTypeFunc = aliasValueType[k].valueType;
						}
					}
				}
				for(var i=0;i<functionView.length;i++){
						if(valueTypeFunc == functionView[i].valueType){
							var name = functionView[i].functionName;
							var menuItem = new dijit.MenuItem({	
									label: name,
    								onClick: function(e) {
    									//removeFunctionIfExists(id);
    									//alert(e.target.innerHTML);
    									assignFunctionToAlias(id, e.target.innerHTML);
    									displayFunctionMenu(id);
   									 }
								});
							subMenu.addChild(menuItem);
						  }
					}
				menu.addChild(new dijit.PopupMenuItem({
						label: "Functions",
						 popup: subMenu
					}));
 				//alert("Creating drop-down button");
				var button = new dijit.form.DropDownButton({
							name: "function",
							dropDown: menu,
							showLabel: false
						});
				//alert("itemName is: " + item.name);            			    
				//alert("id is: " + id);           			    
				dojo.byId(id).appendChild(button.domNode);
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
			function trim(s){
				var l=0; var r=s.length -1;
				while(l < s.length && s[l] == ' '){
					l++; 
					}
				while(r > l && s[r] == ' '){	
					r-=1;
					}
					return s.substring(l, r+1);
				}

			function assignFunctionToAlias(id, functionName){
				var table = document.getElementById(id);
				var tableCells = table.getElementsByTagName("td");
				var alias = tableCells[0].innerHTML;
				tableCells[0].innerHTML = functionName + "(" + alias + ")";
			}

			function removeFunctionIfExists(id){
				var table = document.getElementById(id);
				var tableCells = table.getElementsByTagName("td");
				var alias = tableCells[0].innerHTML;
				var length = alias.split("(").length;
				if(length > 1){
					tableCells[0].innerHTML = trim(alias.split("(")[length - 1].split(")")[0]);
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
				if(source==xAxis || source==yAxis){
		 			allow=true;
					}
					return allow;
				}

			function getValuesFromPane(paneId){
				var array=[];	
				var table = document.getElementById(paneId);
				var tableCells = table.getElementsByTagName("td");	
				for(var i=0;i<tableCells.length;i++){
					array.push(tableCells[i].innerHTML);
				}
				return array;
				}
			function getDataFromHBase() {
				var getDataForm = dijit.byId("saveReportForm");
				if (getDataForm.validate()) {
						showProgressIndicator();
						var xhrArgs = {
	                		form: dojo.byId("saveReportForm"),
	               			 url: "<s:url action='getPreviewDataAction'/>",
	               			 handleAs: "json",
	                		 load: function(data) {
	                				if(data.error.error){
	                 					responseMessage("Error: "+data.error.message);
	                   				  }else{
					                	  if(chartType=="Table"){
	                						 dijit.byId("tableDialog").show();
		 	                 			     document.getElementById("preTableName").innerHTML=dijit.byId("reportName").get("value");
		 	                                 doPlotTable(getValuesFromPane("xAxisNode"),data.dataList,'tableNode');
				                	  }else if(chartType=="Choropleth"){
	                						  dijit.byId("mapDialog").show();
		 	             					  document.getElementById("preMapName").innerHTML=dijit.byId("reportName").get("value");
		 	                 		   		  doPlotMap(data.dataList);
	                	 			  } else {
	                		 				 dijit.byId("chartDialog").show();
		 	                				 document.getElementById("preReportName").innerHTML=dijit.byId("reportName").get("value");
		                    	 			 doPlot(getValuesFromPane("xAxisNode"),getValuesFromPane("yAxisNode"),data.dataList,chartType,'simplechart','legend');
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
			} else if(dropTarget==zAxis){
				document.getElementById("zbutton").style.visibility = "visible";
			}
		}	
		var storeData = {
		        	label: 'alias',
		        	items:<%out.println(JSONUtil.serialize(request
						.getAttribute("filterViewList")));%> 
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
						}
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
			var xArray=getValuesFromPane("xAxisNode");
			var yArray=getValuesFromPane("yAxisNode");
			var zArray=getValuesFromPane("zAxisNode");
			var reportName=document.getElementById('reportName').value;
			if(reportName==""){
				dijit.byId('reportNameForm').validate();
				responseMessage("Please Name the Report");
			} else {
				if(isValidSaveForm(xArray,yArray)){			
					document.getElementById('report_Name').value=reportName;
					var isDashBoard = dijit.byId('dashBoard').get('value');
					dijit.byId('addToDashBoard').set('value',isDashBoard);
					var axisValue = getAxisValue("x",xArray)+getAxisValue("y",yArray)+getAxisValue("z",zArray);
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
	}}}
	
	function getAxisValue(axis,array){
		var result = "";
		for(var i=0;i<array.length;i++){
			if(array[i]!=""){
				result=result+axis+","+array[i]+":";
				}
			}
		return result;
	}
	
	function isValidSaveForm(xArray,yArray){
		var isValid = true;
		if(chartType=="Table"){
			if(xArray.length==0)	{
				responseMessage("Please drag and drop appropriate alias to define Columns for your report");
				isValid=false;
			}
		} else if(value=="Choropleth"){
			if(xArray.length>1 || yArray.length>1 || zArray.length>1){
				responseMessage("Invalid data for Latitude or Longitude or Data. Please add only one Alias to these Panes");
				isValid=false;
			}
		}else if(chartType=="Pie"){
			if(xArray.length>1 || yArray.length>1)	{
				responseMessage("DataSet Invalid for Pie Chart");
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

	<table>
		<tr>
			<td align="right">Report Name:&nbsp;</td>
			<td align="left">
				<div id="reportNameForm" dojoType="dijit.form.Form">
					<input type="text" id="reportName" name="report.name"
						dojoType="dijit.form.ValidationTextBox" maxlength=100
						required="true" trim="true" value="${report.name}" />
				</div></td>
		</tr>
		<tr>
			<td align="right">Mapping:&nbsp;</td>
			<td align="left"><select id="mappingName" name="mappingId"
				dojoType="dijit.form.Select">
					<s:iterator value="%{mappingList}" status="maps">
						<option id="<s:property value='id'/>"
							value="<s:property value='id'/>">
							<s:property value='name' />
						</option>
					</s:iterator>
			</select></td>
		</tr>
		<tr>
			<td><label for="dashBoard"> Add To DashBoard:&nbsp; </label>
			</td>
			<td><input id="dashBoard" name="dashBoard"
				dojoType="dijit.form.CheckBox" value="true"></td>
		</tr>
	</table>
	<div id="reportDetail" dojoType="dijit.layout.TabContainer"
		style="width: 100%; float: center;" tabPosition="left-h"
		doLayout="false">
		<div dojoType="dijit.layout.ContentPane" title="Design"
			selected="true" align="center">
			<div style="float: left;"></div>

			<div dojoType="dijit.form.Form" id="populateDimensionAndMeasureList"
				jsId="populateDimensionAndMeasureList" action="" method="post">
				<input type="hidden" id="mappingId" name="mappingId"
					dojoType="dijit.form.TextBox" value="" />
			</div>


			<div dojoType="dijit.form.Form" action="saveReport"
				id="saveReportForm" method="post">
				<input type="hidden" name="report.id" dojoType="dijit.form.TextBox"
					value="${report.id}" />
					 <input type="hidden" id="axisValues"
					name="axisValues" dojoType="dijit.form.TextBox" /> 
					<input
					type="hidden" id="report_Name" name="report.name"
					value="${report.name}" dojoType="dijit.form.TextBox" /> <input
					type="hidden" id="mapping_Id" name="mappingId"
					dojoType="dijit.form.TextBox" /> <input type="hidden"
					id="addToDashBoard" name="addToDashBoard"
					dojoType="dijit.form.TextBox" />

				<table width="100%">
					<tr>
						<td align="left" rowspan="2">
						<table frame="border">
							<tr>	<td width="150px" align="center">
										<table id="dimensionList" class="container">
											<tbody align="center">
												<tr>
													<td width="150px"><b>Dimensions</b></td>
												</tr>
											</tbody>
										</table>
							<br />
										<table id="measuresList" class="container">
											<tbody align="center">
												<tr>
													<td width="150px"><b>Measures</b><td>
												</tr>
											</tbody>
										</table>
							</table></td>
						<td align="center">
							<table id="reportTypeTable" style="visibility: hidden;">
								<tr>
									<td align="right">Report Type: &nbsp;</td>
									<td><select id="reportType" name="reportType"
										dojoType="dijit.form.Select" onchange='changed(this.value)'>
									</select> <br />
									</td>
								</tr>
							</table></td>
					</tr>
					<tr>
						<td align="right">
							<div id="pane" align="center" style="visibility: hidden;">
								<table>
									<tr>
										<td width="200px">
											<div id="xAxis" style="width: 100%">
												<table id="xAxisNode" width=100% height=100%>
													<tbody align="center">
														<tr height=100%>
															<th width=100%></th>
														</tr>
													</tbody>
												</table>
											</div></td>
										<td width="200px">
											<div id="yAxis" style="width: 100%">
												<table id="yAxisNode" width=100% height=100%>
													<tbody align="center">
														<tr height=100%>
															<th width=100%></th>
														</tr>
													</tbody>
												</table>
											</div>
										</td>
									</tr>
									<tr>
										<td width="100px">
											<div id="xbutton">
												<button id="clearXAxisNode" dojoType="dijit.form.Button">Clear</button>
											</div>
										</td>
										<td width="100px">
											<div id="ybutton">
												<button id="clearYAxisNode" dojoType="dijit.form.Button">Clear</button>
											</div></td>
									</tr>
									<tr>
										<td width="200px">
											<div id="zAxis" style="width: 100%">
												<table id="zAxisNode" width=100% height=100%>
													<tbody align="center">
														<tr height=100%>
															<th width=100%></th>
														</tr>
													</tbody>
												</table>
											</div></td>
									</tr>
									<tr>
										<td width="100px">
											<div id="zbutton">
												<button id="clearZAxisNode" dojoType="dijit.form.Button">Clear</button>
											</div>
										</td>
									</tr>
								</table>
							</div></td>
					</tr>
				</table>

			</div>

			<div style="float: left;"></div>
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
						</div></td>
					<td width="50px">
						<div id='editButton'>
							<s:a href="#" onClick='editFilter()'>Edit</s:a>
						</div></td>
					<td width="50px">
						<div id='deleteButton'>
							<a href="#" onClick='deleteFilter()'>Delete</a>
						</div></td>
				</tr>
			</table>
			<br>
			<div id="addFilter" name="addFilter" style="visibility: hidden;">
				<input type="hidden" id="isEdit" dojoType="dijit.form.TextBox"
					value="" />
				<table frame="border" align="center">
					<tr>
						<td align="right">ColumnAlias/RowAlias:&nbsp;</td>
						<td align="left"><select id="alias" name="alias"
							dojoType="dijit.form.Select"></select></td>
					</tr>
					<tr>
						<td align="right">FilterType:&nbsp;</td>
						<td align="left"><select id="filterType" name="filterType"
							dojoType="dijit.form.Select"></select></td>
					</tr>
					<tr>
						<td align="right">Value:&nbsp;</td>
						<td align="left"><input id="value" type="text" name="value"
							dojoType="dijit.form.ValidationTextBox" maxlength=100
							required="false" trim="true" /></td>
					</tr>
					<tr>
						<td></td>
						<td>
							<button type="button" onClick="populateGridNodeForFilter()"
								dojoType="dijit.form.Button">Save</button></td>
					</tr>
				</table>
			</div>
		</div>
	</div>

	<div id="buttons" align="center">
		<button dojoType="dijit.form.Button" onClick='saveReport(true)'>Save
			Report</button>
		<button dojoType="dijit.form.Button" onClick='saveReport(false)'>Preview</button>
	</div>

	<div id="chartDialog" dojoType="dijit.Dialog" title="Report Preview"
		style="width: 800px; height: 450px; float: center;">
		<div style="height: 100%">
			<table align="center">
				<tr>
					<td><b><label id="preReportName"></label>
					</b></td>
				</tr>
				<tr>
					<td>
						<div id="simplechart"
							style="width: 700px; height: 350px; float: center;"></div></td>
				</tr>
				<tr>
					<td>
						<div id="legend"></div></td>
				</tr>
			</table>
		</div>
	</div>

	<div id="tableDialog" dojoType="dijit.Dialog" title="Report Preview"
		style="width: 800px; height: 400px;">
		<div style="height: 100%">
			<table>
				<tr>
					<td><b><label id="preTableName"></label>
					</b></td>
				</tr>
			</table>

			<div id="tableNode"></div>
		</div>
	</div>
	<div id="mapDialog" dojoType="dijit.Dialog" title="Report Preview"
		style="width: 800px; height: 400px;">
		<div style="height: 100%">
			<table>
				<tr>
					<td><b><label id="preMapName"></label>
					</b></td>
				</tr>
			</table>

			<div id="mapNode"></div>
		</div>

	</div>

</div>
<br>
<br>
<br>
<br>

<s:if test="%{#isEdit=='true'}">
	<script type="text/javascript">
 
dojo.addOnLoad(function(e) {
	var mapName = "<%=request.getAttribute("mappingId")%>";
	var mapSelect = dijit.byId("mappingName");
	mapSelect.set("value",mapName);
	populateList(true);
	var isDashBoard = '<%=request.getAttribute("addToDashBoard")%>';
	if(isDashBoard=='true'){
			dijit.byId("dashBoard").set('value',isDashBoard);
		}
	});

	function populateEditData() {
		dijit.byId("reportType").set('value','<%=request.getAttribute("reportType")%>');
		var xArray = [];
		var yArray = [];
		var zArray = [];
		var values = "<%=request.getAttribute("axisValues")%>";
		var axisValue = values.split(":");
		for(var i=0;i<axisValue.length;i++){
			var val = axisValue[i].split(",");
			if(val[0]=="x"){
				xArray.push(val[1]);
			} else if(val[0]=="y"){
				yArray.push(val[1]);
			} else if(val[0]=="z"){
				zArray.push(val[1]);
			}
		}	

			xAxis.insertNodes(false, xArray);
			yAxis.insertNodes(false, yArray);
			zAxis.insertNodes(false, zArray);
		var items=<%out.println(JSONUtil.serialize(request
						.getAttribute("filterViewList")));%> ;
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
		document.getElementById("zbutton").style.visibility = "visible";
		xAxis.forInItems(function(item, id){
				displayFunctionMenu(id);
		 });
		yAxis.forInItems(function(item, id){
			displayFunctionMenu(id);
 		});
		var mapSelect = dijit.byId("mappingName");
		dojo.connect(mapSelect, "onChange", function(){
			populateList(false);
			});
	}
</script>
</s:if>
<s:else>
	<script type="text/javascript">
dojo.addOnLoad(function(e) {
var mapSelect = dijit.byId("mappingName");
dojo.connect(mapSelect, "onChange", function(){
	populateList(false);
	});
mapSelect.onChange();
});
</script>
</s:else>
<div id="deleteDialog" dojoType="dijit.Dialog" title="Confirm Deletion">
	Are you sure you want to delete? Deletion can not be undone. <br />
	<br />
	<button id="deleteTrue" dojoType="dijit.form.Button"
		onClick="deleteTrue()">Yes</button>
	<button id="deleteFalse" dojoType="dijit.form.Button"
		onClick="deleteFalse()">No</button>
</div>
<jsp:include page="footer.jsp" />