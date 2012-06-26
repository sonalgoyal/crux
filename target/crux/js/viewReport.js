/**
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
 */
var charts;
var legend = null;
var yArray = [];
var d3 = [];
var d1 = [];
var xArray = [];
var xAxislabels = [];
var simpleChartDiv=[];
var chartArray = [];
var legendDiv;

function populateData(isCsv,formId,chartTypeId,divId,legendId) {
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
        url: "<s:url action='findReportData'/>",
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
            	 if(isCsv){
            		 doPlotCSV(xList,yList,data.dataList);
            	 }else if(chartType=="Table"){
            		 doPlotTable(xList,data.dataList,divId);
            	 }else if(chartType=="Choropleth"){
            		 document.getElementById("mapNode").style.visibility = "visible";
            		 doPlotMap(data.dataList);
            	 }else {
            		 doPlot(xList,yList,data.dataList,chartType,divId,legendId);
            	 }
             }
        	hideProgressIndicator();
        	dojo.publish("fetchedData");
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

function populatePie(valueList) {
		
		var xResults = dojox.json.query("..[?alias=\'" + xArray[0]
				+ "\']..value", valueList);
		var yResults = dojox.json.query("..[?alias=\'" + yArray[0]
		+ "\']..value", valueList);		
		d3 = [];
		d1= [];		
		for(var i=0;i<yResults.length;i++){
			var data = new Object();
			data.y=yResults[i];
			data.legend=xResults[i];
			d1.push(data);
		}
		d3.push(d1);
		
	}


function populate(valueList) {
	var dataList = [];
	var nameList = xArray.concat(yArray);
	for ( var i = 0; i < nameList.length; i++) {
		var results = dojox.json.query("..[?alias=\'" + nameList[i]
				+ "\']..value", valueList);
		dataList.push(results);
	}

	function getData(column) {
		for ( var i = 0; i < nameList.length; i++) {
			if (column == nameList[i])	return dataList[i];
			}
		}
		d3 = [];
		if(charts!="Spider"){
				for ( var i = 0; i < xArray.length; i++) {
						xAxislabels = [];
						var xData = getData(xArray[i]);
						for ( var j = 0; j < yArray.length; j++) {
							var yData = getData(yArray[j]);
							var d1 = [];
							if(charts.indexOf("Columns") == -1 && charts.indexOf("Stacked") == -1){	
								for ( var k = 0; k < xData.length; k += 1) {
									d1.push({
										x : xData[k],
										y : yData[k]
									});
								 }
							} else {
								d1 = yData;
							}
							d3.push(d1);
						}
						if(charts.indexOf("Columns") == -1 &&  charts.indexOf("Stacked") == -1){
							for ( var k = 0; k < xData.length; k += 1) {
								xAxislabels.push({
									value: xData[k],
									text : xData[k]
									});		
							}
						} else {
							for ( var k = 0; k < xData.length; k += 1) {
								xAxislabels.push({
									value : k+1,
									text : xData[k]
									});		
							}
						}	
				   }
		} else {
			var xAliasName = xArray[0];
			xArray = [];
			xArray = getData(xAliasName);
			for (var k=0;k<xArray.length;k++){
				var data = new Object();
				for(var i=0;i<nameList.length;i++){			
					if(nameList[i]!=xAliasName){
						eval("data." + nameList[i] + "='" + dataList[i][k] + "'");
					}
				}
				var newData = new Object();
				newData.data = data;
				d3.push(newData);
			}
		}
	}


	makeCharts = function() {
			
			var cruxChart = new dojox.charting.Chart2D(simpleChartDiv[simpleChartDiv.length-1]);
			chartArray.push(cruxChart);
			
			cruxChart.setTheme(dojox.charting.themes.Claro);
			if(charts!="Spider"){
					cruxChart.addPlot("default", {
						type : charts,
						lines : true,
						areas : false,
						markers : true,
						gap:5
					});
					cruxChart.addAxis("x", {rotation:270, labels:xAxislabels,natural:false,fixed:true,minorTicks:false,microTicks:false});
					if(charts.indexOf("Columns") == -1 && charts.indexOf("Stacked") == -1){	
						cruxChart.addAxis("y", {vertical: true});
					} else {
						cruxChart.addAxis("y", {vertical: true, includeZero: true});
					}
					//	cruxChart.addAxis("x");
					//	cruxChart.addAxis("y",{vertical: true});
					//	cruxChart.setAxisWindow("x",2,100).render();
					//	cruxChart.setAxisWindow("y",2,100).render();
					for ( var i = 0; i < d3.length; i++) {
						cruxChart.addSeries(yArray[i], d3[i]);
					}
					if(charts=='Pie'){
						new dojox.charting.action2d.MoveSlice(cruxChart, "default");
					} else {
						var magnify = new dojox.charting.action2d.Magnify(cruxChart, "default");
					}
					 var tip = new dojox.charting.action2d.Tooltip(cruxChart, "default");
				} else {
						cruxChart.addPlot("default", {
							type:               "Spider",
							labelOffset:         1,
							divisions:           5,
							seriesFillAlpha:     0.2,
							markerSize:          3,
							precision:           0,
							spiderType:          "circle"
						});
						for(var i=0;i<xArray.length;i++){
								cruxChart.addSeries(xArray[i],d3[i], { fill: "transparent" });
						}
				}
			cruxChart.render();
			document.getElementById(simpleChartDiv[simpleChartDiv.length-1]).style.visibility = "visible";
			document.getElementById(legendDiv).style.visibility = "visible";
	
			if (legend == null) {
				if(charts=="Spider"){
					legend = new dojox.charting.widget.SelectableLegend({chart: cruxChart, horizontal: true}, legendDiv);
				} else {
					legend = new dojox.charting.widget.Legend({	chart : cruxChart}, legendDiv);
				}
			} else {
				legend.destroy(true);
				if(charts=="Spider"){
						legend = new dojox.charting.widget.SelectableLegend({chart: cruxChart, horizontal: true},legendDiv);
				} else {
					legend = new dojox.charting.widget.Legend({chart : cruxChart}, legendDiv);
			}
		}	
	};
	
	function chartDivCheck(chartDiv){
		for (var i=0;i<simpleChartDiv.length;i++){
			if(simpleChartDiv[i]==chartDiv){
				chartArray[i].destroy(true);
				simpleChartDiv[i]="xyz";
			}
		}
	}

	function doPlot(xList,yList,dataList,type,chartDiv,legendDivId) {
		xArray=xList;
		yArray=yList;
		charts=type;
		chartDivCheck(chartDiv);
		simpleChartDiv.push(chartDiv);
		legendDiv=legendDivId;
		if(charts=='Pie'){
			populatePie(dataList);
		} else {
			populate(dataList);
		}
		dojo.ready(makeCharts);
	}

		
		function doPlotTable(xArray,dataList,girdDiv) {
				dojo.addOnLoad(function() {
					chartDivCheck(girdDiv);
					simpleChartDiv.push(girdDiv);
						var chartData = {
								items : []
							};
						var tableStore = new dojo.data.ItemFileWriteStore({
								data : chartData
							});
						var tableLayout = [];
						var valueList = [];
						var columns = xArray;
						var length = 0;
						for ( var i = 0; i < columns.length; i++) {
								tableLayout.push({
										field : columns[i].replace(" ", ""),
										 name : columns[i],
										 width : "auto"
										});
						 var results = dojox.json.query("..[?alias=\'" + columns[i]
						 		+ "\']..value", dataList);
						 	valueList.push(results);
						 	length = results.length;
						}
		
					var	tableGrid = new dojox.grid.DataGrid({
								query : {},
								store : tableStore,
								structure : tableLayout,
								selectionMode : 'single',
								autoHeight : 10
							}, girdDiv);
					chartArray.push(tableGrid);
						tableGrid.startup();
						for ( var i = 0; i < length; i++) {
								var newItem = new Object();
								for ( var j = 0; j < columns.length; j++) {
										eval("newItem." + columns[j].replace(" ", "") + "='" + valueList[j][i] + "'");
								}
								tableStore.newItem(newItem);
						}
					});
				}

		function doPlotMap(valueList) {
				var po = org.polymaps;
				var longitude = dojox.json.query("..[?alias=\'" + yArray[0]
					+ "\']..value", valueList);
				var value = dojox.json.query("..[?alias=\'" + zArray[0]
					+ "\']..value", valueList);
				var latitude = dojox.json.query("..[?alias=\'" + xArray[0]
					+ "\']..value", valueList);
				var maps=new Object();
				for(var i=0;i<value.length;i++){
						eval("maps." + value[i] + "=[" +longitude[i]+','+latitude[i]+ "]");
				}
				// Create the map object, add it to #map…
				var map = po.map()
						.container(d3.select("#mapNode").append("svg:svg").node())
						.center({lat: latitude[0], lon: longitude[0]})
						.zoom(4)
						.add(po.interact());
						// Add the CloudMade image tiles as a base layer…
					map.add(po.image()
							.url(po.url("http://{S}tile.cloudmade.com"
							+ "/1a1b06b230af4efdbb989ea99e9841af" // http://cloudmade.com/register
							+ "/998/256/{Z}/{X}/{Y}.png")
							.hosts(["a.", "b.", "c.", ""])));
					// Add the compass control on top.
					map.add(po.compass()
							.pan("none"));
					//alert(maps);
					// Load the maps data. When the data comes back, display it.
					//d3.json("maps", function(data) {
					// Insert our layer beneath the compass.
					var layer = d3.select("#mapNode svg").insert("svg:g", ".compass");
					// Add an svg:g for each station.
					var marker = layer.selectAll("g")
							.data(d3.entries(maps))
							.enter().append("svg:g")
							.attr("transform", transform);
					// Add a circle.
					marker.append("svg:circle")
						.attr("r", 4.5);
					// Add a label.
					marker.append("svg:text")
						.attr("x", 7)
						.attr("dy", ".31em")
						.text(function(d) { return d.key; });
					// Whenever the map moves, update the marker positions.
					map.on("move", function() {
							layer.selectAll("g").attr("transform", transform);
						});

	  function transform(d) {
		  		d = map.locationPoint({lon: d.value[0], lat: d.value[1]});
		  		return "translate(" + d.x + "," + d.y + ")";
	  		}
	//});
		}