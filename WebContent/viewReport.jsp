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
<h1 align="center"><s:property value="%{report.name}" /></h1>
<br>
<br>
<jsp:include page="progress.jsp" />

<script type="text/javascript" src="js/viewReport.js" ></script>
<script type="text/javascript" src="js/d3/d3.js"></script>
<script type="text/javascript" src="js/polymaps/polymaps.js"></script>
<script type="text/javascript">
var filter = false;
</script>
<style type="text/css">
@import "js/polymaps/polyStyle.css";
svg {
  display: block;
}

circle {
  stroke: black;
  fill: brown;
  fill-opacity: .5;
}
</style>
<br>
<div dojoType="dijit.form.Form" id="getData" name="getData" method="post">
			<input type="hidden" id="report.id" name="report.id" dojoType="dijit.form.TextBox" value='<s:property value="%{report.id}" />' />
			<input type="hidden" id="mappingId" name="mappingId" dojoType="dijit.form.TextBox"  value='<s:property value="%{mappingId}" />'/>
			<%int count=0; %>
			<table>
		<s:iterator value="%{filterList}" status="filters">
			<tr><td width="80px" align="right"> Alias: &nbsp;</td>
			<td width="80px" align="left">
			<input type="hidden" name="filterList[<%=count%>].alias" dojoType="dijit.form.TextBox" value="<s:property value='alias'/>" />
			<s:property value='alias'/>
			</td>
			<td  width="80px" align="right"> FilterType: &nbsp;</td>
			<td  width="80px" align="left">
			<input type="hidden" name="filterList[<%=count%>].filterType" dojoType="dijit.form.TextBox" value="<s:property value='filterType'/>" />
			<s:property value='filterType'/>
	 		</td>
	 		<td width="80px" align="right"> Value: &nbsp;</td>
	 		<td width="80px"  align="left">
	 		<s:if test='%{value == ""}'>
			 <input type="text" name="filterList[<%=count%>].value" dojoType="dijit.form.ValidationTextBox" maxlength=100 required="true"
							trim="true"/>
				<script type="text/javascript">
					filter = true;
					</script>						
			</s:if>
			<s:else>
				<input type="hidden" name="filterList[<%=count%>].value" dojoType="dijit.form.TextBox" value="<s:property value='value'/>" />
				<s:property value='value'/>
			</s:else>
			</td>
			</tr>
			<%count++; %>
		</s:iterator>
		
		</table>
		<br>			
		</div>
		<table>
  					<tr>
   						 <td><button id="viewReportButton" dojoType="dijit.form.Button" onClick='populateData(false)' style="visibility: hidden;">View Report</button></td>
    						<td>
    						<div id="exportButton">
						<button id="exportCSV" dojoType="dijit.form.Button" onClick='exportToCSV()'>Export to CSV</button>
							</div>
    						</td>
  					</tr>
		</table>
<br>
<div style="width: 800px;">
<div id="tableNode"></div>
<div id="mapNode"></div>
</div>
<table align="center">
	<tr>
		<td><div id="simplechart"  style="width: 700px; height: 350px; float: center;"></div></td>
	</tr>
	<tr>
		<td><div id="legend"></div></td>
	</tr>
</table>
<script type="text/javascript">
var chartType = "<%=request.getAttribute("chartType") %>";

dojo.require("dijit.form.Form");
dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.Button");

function showHelp(){
	 	window.open ('help/viewReportHelp.html', '', 'width=400,height=200,scrollbars=1');
}
document.getElementById("exportButton").style.visibility = "hidden";
 			function populateData(isCsv) {
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
 				
 					document.getElementById("mapNode").style.visibility = "hidden";
					clearResponse();
					var getDataForm = dijit.byId("getData");
					var xList = [];
					var yList = [];
					var zList = [];
					var values = "<%=request.getAttribute("axisValues")%>";
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
					if (getDataForm.validate()) {
								showProgressIndicator();
					var xhrArgs = {
		                form: dojo.byId("getData"),
		                url: "<s:url action='getDataAction'/>",
		                handleAs: "json",
		                load: function(data) {
		                	if(data.error.error){
		                 		responseMessage("Error: "+data.error.message);
		                     }else{
		                    	 if(isCsv){
		                    		 doPlotCSV(xList,yList,data.dataList);
		                    	 }else if(chartType=="Table"){
		                    		 doPlotTable(xList,data.dataList,'tableNode');
		                    	 }else if(chartType=="Choropleth"){
		                    		 document.getElementById("mapNode").style.visibility = "visible";
		                    		 doPlotMap(data.dataList);
		                    	 }else {
		                    		 doPlot(xList,yList,data.dataList,chartType,'simplechart','legend');
		                    	 }
		                     }
		                	hideProgressIndicator();
		                	document.getElementById("exportButton").style.visibility = "visible";
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
 			
 			function exportToCSV(){
 				dojo.addOnLoad(function(e) {
 	 				populateData(true);
 	 				});
 			}
 			
 			if(!filter){
 				dojo.addOnLoad(function(e) {
 				populateData();
 				});
 			} else {
 				dojo.style("viewReportButton","visibility","visible");
 			}
 			
 			function doPlotCSV(xList,yList,valueList){
 				var dataList = [];
 				var nameList = xList.concat(yList);
 				var header = "";
 				var length = 0;
 				for ( var i = 0; i < nameList.length; i++) {
 					var results = dojox.json.query("..[?alias=\'" + nameList[i]
 							+ "\']..value", valueList);
 					dataList.push(results);
 					if (i==0){
 						header += nameList[i];
 					} else {
 						header += ','+nameList[i];
 					}
 					if(length<results.length){
 						length = results.length;
 					}
 				}

 				function getData(column) {
 					for ( var i = 0; i < nameList.length; i++) {
 						if (column == nameList[i])
 							return dataList[i];
 					}
 				}
 				
 				exportWindow = window.open("","ReportData","scrollbars=1");	
 				exportWindow.document.write(header+"<br>");
 				var csData = "";
 				for(var z =0;z<length;z++){
 					csData = "";
 					for ( var i = 0; i < nameList.length; i++) {
 						if (i==0){
 							csData += dataList[i][z];
 						} else {
 							csData += ','+dataList[i][z];
 						}
 						
 					}
 					exportWindow.document.write(csData+"<br>");
 				}
 			}
		
</script>
<jsp:include page="footer.jsp" />