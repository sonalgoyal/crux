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
var chartType;
var cruxChart = null;
var legend = null;
var yArray = [];
var d3 = [];
var d1 = [];
var xArray = [];
var xAxislabels = [];

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
			if (column == nameList[i])
				return dataList[i];
		}
	}
	d3 = [];
	for ( var i = 0; i < xArray.length; i++) {
		xAxislabels = [];
		var xData = getData(xArray[i]);
		for ( var j = 0; j < yArray.length; j++) {
			var yData = getData(yArray[j]);
			var d1 = [];
			if(chartType.indexOf("Columns") == -1 &&  chartType.indexOf("Stacked") == -1){	
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
		if(chartType.indexOf("Columns") != -1  ||  chartType.indexOf("Stacked") != -1){	
		for ( var k = 0; k < xData.length; k += 1) {
			xAxislabels.push({
				value : k+1,
				text : xData[k]
			});		
		}
		}
	}
}
makeCharts = function() {
	if (cruxChart == null) {
		cruxChart = new dojox.charting.Chart2D("simplechart");

	} else {
		cruxChart.destroy();
		cruxChart = new dojox.charting.Chart2D("simplechart");
	}
	cruxChart.setTheme(dojox.charting.themes.Claro);
	cruxChart.addPlot("default", {
		type : chartType,
		lines : true,
		areas : false,
		markers : true
	});
	cruxChart.addAxis("x", {rotation:270, labels:xAxislabels});
	if(chartType.indexOf("Columns") == -1 && chartType.indexOf("Stacked") == -1){	
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
	
	var magnify = new dojox.charting.action2d.Magnify(cruxChart, "default");
	var tip = new dojox.charting.action2d.Tooltip(cruxChart, "default");
	cruxChart.render();
	document.getElementById("simplechart").style.visibility = "visible";
	document.getElementById("legend").style.visibility = "visible";
	
	if (legend == null) {
		legend = new dojox.charting.widget.Legend({
			chart : cruxChart
		}, "legend");

	} else {
		legend.destroy(true);
		legend = new dojox.charting.widget.Legend({
			chart : cruxChart
		}, "legend");
	}	
};

function doPlot(dataList) {
	populate(dataList);
	if (chartType == "Pie") {
		if (d3.length > 1) {
			responseMessage("DataSet Invalid for Pie Chart");
		} else {
			dojo.ready(makeCharts);
		}
	} else {
		dojo.ready(makeCharts);
	}
}

var tableGrid=null;
function doPlotTable(dataList) {
	dojo.addOnLoad(function() {
		if(tableGrid!=null){
			tableGrid.destroy(true);
		}

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
		
		 tableGrid = new dojox.grid.DataGrid({
			query : {},
			store : tableStore,
			structure : tableLayout,
			selectionMode : 'single',
			autoHeight : 10
		}, 'tableNode');
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