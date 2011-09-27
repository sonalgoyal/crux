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
<jsp:include page="header.jsp" />
<jsp:include page="topNavigation.jsp" />
<jsp:include page="tableInclude.jsp" />
<jsp:include page="progress.jsp" />
<%@ page import="org.apache.struts2.json.JSONUtil"%>
<div align="center" style="color:#FF0000"><s:property value='%{error.message}'/></div>
<script type="text/javascript" src="js/viewReport.js"></script>
  <script type="text/javascript">
  dojo.require("dojox.widget.Portlet");
  dojo.require("dojox.layout.GridContainerLite");
  dojo.require("dijit.form.Form");
  dojo.require("dijit.form.TextBox");
  dojo.require("dijit.form.Button");
  
  function showHelp(){
		 window.open ('help/dashboardHelp.html', '', 'width=600,scrollbars=1');
	}
  
  var gridContainer;
  dojo.addOnLoad(function(){
	   gridContainer = new dojox.layout.GridContainerLite({
           nbZones: 2,
           opacity: .5,
           hasResizableColumns: true,
           allowAutoScroll: true,
           withHandles: true,
           dragHandleClass: 'dijitTitlePaneTitle',
           style: {
               width: '100%'
           },
           acceptTypes: ['Portlet'],
           isOffset: true,
           isAutoOrganized:true
       },
       'gridContainer');
	   
	   var reportList = <%out.println(JSONUtil.serialize(request.getAttribute("reportListForDashBoard")));%> ;
	   for(var i=0;i<reportList.length;i++){
		 var emptyFilter = false;
		 var formDiv =  dojo.create("form", {
				  id: "form"+i,
				  name: "getData"+i
				  });
		   var portletContent = [
								  formDiv,
                                  dojo.create('div', {
                                      id: 'div'+i,
                                      height:'400px',
                                      width:'700px'
                                   }),
                                   dojo.create('div', {
                                       id: 'legend'+i
                                    })
                                  ]; 
		  
		  var portlet = new dojox.widget.Portlet({
              id: 'dash_'+reportList[i].dashboard.id,
              closable: false,
              dndType: 'Portlet',
              title: reportList[i].name,
              content: portletContent
          });
		  var mapId ;
		  if( reportList[i].designs[0].rowAlias != null){
			  mapId = reportList[i].designs[0].rowAlias.mapping.id;
		  } else {
			  mapId = reportList[i].designs[0].columnAlias.mapping.id;
		  }
		  var form=  new dijit.form.Form({
             							 method: "POST",
             							 name: "form"+i,
             							 action: ""
      						    },  formDiv  ); 
		  
		  	
		  var hiddenField1 = new dijit.form.TextBox({
            						  id: 'report'+i,
             						  name: "report.id",
             						  value: reportList[i].id,
               						  type: 'hidden'
         	 						},
         							 document.createElement("input")
      								); 
	      form.containerNode.appendChild(hiddenField1.domNode);
	      
	      var hiddenField2 = new dijit.form.TextBox({
			  							  id: 'mapId'+i,
				  						  name: "mappingId",
				  						  value:mapId,
				  						  type: 'hidden'
										},
										 document.createElement("input")
										); 
	      form.containerNode.appendChild(hiddenField2.domNode);
	      
         
	      var hiddenField3 = new dijit.form.TextBox({
							  id: 'chartType'+i,
							  name: "chartType",
							  value: reportList[i].reportType.type,
							  type: 'hidden'
							},
							document.createElement("input")
							); 
	      form.containerNode.appendChild(hiddenField3.domNode);
	      
	      var rowFilter = reportList[i].rowAliasFilters;
	      var columnFilter = reportList[i].columnFilters;
	      
	      for(var j=0;j<rowFilter.length;j++){
	    	 	 var hiddenField4 = new dijit.form.TextBox({
				 						 id: "filterList["+j+"].alias"+i,
				 						 name: "filterList["+j+"].alias",
									     value: rowFilter[j].rowAlias.alias,
									     type: 'hidden'
										},
											 document.createElement("input")
										); 
	    	  	form.containerNode.appendChild(hiddenField4.domNode);
	    	  	form.containerNode.appendChild(dojo.create("label", { innerHTML: rowFilter[j].rowAlias.alias+'&nbsp;&nbsp;'})); 
	    	  	var hiddenField5 = new dijit.form.TextBox({
									 id: "filterList["+j+"].filterType"+i,
									 name: "filterList["+j+"].filterType",
				    				 value: rowFilter[j].filterType.type,
				   				     type: 'hidden'
									},
									 document.createElement("input")
									); 
				 form.containerNode.appendChild(hiddenField5.domNode);
				 form.containerNode.appendChild(dojo.create("label", { innerHTML: rowFilter[j].filterType.type+'&nbsp;&nbsp;'}));
				
 				if(rowFilter[j].value==null || rowFilter[j].value ==""){
 					
					 emptyFilter = true;
					 var hiddenField6 = new dijit.form.TextBox({
					 					      id: "filterList["+j+"].value"+i,
		 			   						  name: "filterList["+j+"].value",
		 			   						  value: rowFilter[j].value,
		    			     				  type: 'text'
				     						},
												  document.createElement("input")
					    					 ); 
					 form.containerNode.appendChild(dojo.create("label", {'for': "filterList["+j+"].value"+i, innerHTML: "value:&nbsp;"}));
					 form.containerNode.appendChild(hiddenField6.domNode);
					
				 } else {
					 var hiddenField6 = new dijit.form.TextBox({
					      id: "filterList["+j+"].value"+i,
   						  name: "filterList["+j+"].value",
   						  value: rowFilter[j].value,
	     				  type: 'hidden'
						},
							  document.createElement("input")
   					 ); 
					 form.containerNode.appendChild(dojo.create("label", { innerHTML: rowFilter[j].value}));
					form.containerNode.appendChild(hiddenField6.domNode);
						
				 }
 				form.containerNode.appendChild(dojo.create("br"));
 				
	      }
	      
	      for(var j=rowFilter.length;j<columnFilter.length+rowFilter.length;j++){
	    	 	 var hiddenField4 = new dijit.form.TextBox({
										 id: "filterList["+j+"].alias"+i,
										 name: "filterList["+j+"].alias",
				    					 value: columnFilter[j-rowFilter.length].columnAlias.alias,
				    					 type: 'hidden'
										},
											 document.createElement("input")
										); 
							form.containerNode.appendChild(hiddenField4.domNode);
							form.containerNode.appendChild(dojo.create("label", {innerHTML: columnFilter[j-rowFilter.length].columnAlias.alias+'&nbsp;&nbsp;'})); 
							
				 var hiddenField5 = new dijit.form.TextBox({
										  id: "filterList["+j+"].filterType"+i,
				 						  name: "filterList["+j+"].filterType",
										  value: columnFilter[j-rowFilter.length].filterType.type,
			    						  type: 'hidden'
										},
											 document.createElement("input")
										); 
								form.containerNode.appendChild(hiddenField5.domNode);
								form.containerNode.appendChild(dojo.create("label", {innerHTML: columnFilter[j-rowFilter.length].filterType.type+'&nbsp;&nbsp;'})); 

				
				 if(columnFilter[j].value==null || columnFilter[j].value ==""){
					 emptyFilter = true;
					 var hiddenField6 = new dijit.form.TextBox({
						 id: "filterList["+j+"].value"+i,
						 name: "filterList["+j+"].value",
						 value: columnFilter[j-rowFilter.length].value,
						  type: 'text'
						},
			 				 document.createElement("input")
						 ); 
					 form.containerNode.appendChild(dojo.create("label", {'for': "filterList["+j+"].value"+i, innerHTML: "value:&nbsp;"}));
					form.containerNode.appendChild(hiddenField6.domNode);
					 
				 } else {
					 var hiddenField6 = new dijit.form.TextBox({
										 id: "filterList["+j+"].value"+i,
										 name: "filterList["+j+"].value",
										 value: columnFilter[j-rowFilter.length].value,
										  type: 'hidden'
										},
							 				 document.createElement("input")
										 ); 
						form.containerNode.appendChild(hiddenField6.domNode);
						form.containerNode.appendChild(dojo.create("label", { innerHTML: columnFilter[j-rowFilter.length].value}));
						
					 }
				 form.containerNode.appendChild(dojo.create("br"));
	   		   }
	      if(emptyFilter){
	    	  var button = new dijit.form.Button({
					id: 'button_'+i,
					label: "View Report",
		            onClick: function() {
		            	var x = this.id.substring(7, this.id.length);
		            	 populateData('form'+x,'chartType'+x,'div'+x,'legend'+x); 
		            }
		        },
		        document.createElement("button"));
				form.containerNode.appendChild(button.domNode);
	      }
	      form.startup();
	     // alert(reportList[i].dashboard.column+","+reportList[i].dashboard.index);
	      gridContainer.addChild(portlet,reportList[i].dashboard.column);
	      if (!emptyFilter){
		      populateData('form'+i,'chartType'+i,'div'+i,'legend'+i);
	      }
	  	}
	 	 gridContainer.startup();
	  	 dojo.subscribe("/dojox/mdnd/drop", null,saveAttributes);	  	
  });
  
  function saveAttributes(){
	   var children =  gridContainer.getChildren();
	   var json = '{'+'"dashboardInfo":["'+children[0].id.substring(5)+':'+gridContainer.getIndexOfChild(children[0])+':'+children[0].column+'"';
	   for(var i=1;i<children.length;i++){
			json+=',"'+children[i].id.substring(5)+':'+gridContainer.getIndexOfChild(children[i])+':'+children[i].column+'"';
	   }
	   json+=']}';		
	   showProgressIndicator();
	   var xhrArgs = {	
	             url: "<s:url action='saveDashBoard'/>",
	             handleAs: "json",
	             content: dojo.fromJson(json),
	             load: function(data) {
	            	 hideProgressIndicator();
	            	 if(data.error.error){
	              		responseMessage("Error: "+data.error.message);
	                  }
	             } ,
	             error: function(error) {
	            	 hideProgressIndicator();
	            	 responseMessage("Error " + error);
	             }
		 	};
		 var deferred = dojo.xhrPost(xhrArgs);
    }
  
  function populateData(formId,chartTypeId,divId,legendId) {
		dojo.require('dojox.charting.widget.Chart2D');
		dojo.require('dojox.charting.widget.Legend');
		dojo.require('dojox.charting.DataSeries');
		dojo.require('dojox.charting.plot2d.Markers');
		dojo.require('dojox.charting.themes.ThreeD');
		dojo.require("dojox.json.query");
		dojo.require("dojox.charting.themes.Claro");
		dojo.require("dojox.grid.DataGrid");
		dojo.require("dojo.data.ItemFileWriteStore");
		dojo.require("dojox.charting.widget.SelectableLegend");
	
		clearResponse();
		var getDataForm = dijit.byId(formId);
		var xList = [];
		var yList = [];
		var zList = [];
		var chartType = document.getElementById(chartTypeId).value;
		
		if (getDataForm.validate()) {
					showProgressIndicator();
		var xhrArgs = {
          form: dojo.byId(formId),
          url: "<s:url action='getDataAction'/>",
          handleAs: "json",
          load: function(data) {
          	if(data.error.error){
           		responseMessage("Error: "+data.error.message);
               }else{
            	    var values = data.axisValues;
					var axisValue = values.split(":");
					for(var i=0;i<axisValue.length;i++){
							var val = axisValue[i].split(",");
							if(val[0]=="x"){
								xList.push(val[1]);
							} else if(val[0]=="y"){
								yList.push(val[1]);
							} else if(val[0]=="z"){
								zList.push(val[1]);
							}
						} 
              	 if(chartType=="Table"){
              		 doPlotTable(xList,data.dataList,divId);
              	  }else {
              		 doPlot(xList,yList,data.dataList,chartType,divId,legendId);
              	 }
               }
          	hideProgressIndicator();
          },
          error: function(error) {
          	hideProgressIndicator();
          	responseMessage("error:"+error);
          }
	};
	var deferred = dojo.xhrPost(xhrArgs);

	} else {
		responseMessage("Please define filter value");
	}
}
  
  </script>    
        <style type="text/css">
            @import "js/dojo/dojox/widget/Portlet/Portlet.css";
            @import "js/dojo/dojox/layout/resources/GridContainer.css";
        </style>

<div id="gridContainer"></div>
  
    
                  
            