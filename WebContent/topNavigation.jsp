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
</head>
<body class="claro">
<p>
<div name="topBar" style="position: absolute; left: 0px; top: 0px; width: 100%; background-color: #D9E8F9;" >
<table style="width: 100%;">
		<tr>
			<td align="left" width="10px">
				<a style="padding:5px 6px 5px 6px;" href="<s:url  action='initializeConnections'/>">Connection</a>
			</td>
			<td align="left" width="10px">
				<a style="padding:5px 6px 5px 6px;" href="<s:url action='initializeMappings'/>">Mapping</a>
			</td>
			<td align="left" width="10px">
				<a  style="padding:5px 6px 5px 6px;" href="<s:url action='displayReportList'/>">Report</a>
			</td>
			<td align="right">
			<a  style="padding:5px 6px 5px 6px;" href="#" onClick='showHelp()'>Help</a>
				<img align="right" height="22" width="50" src="Images/crux_transparent.gif" alt="Crux" /> 
				
			</td>			
		</tr>
</table>
</div>
<p>
<br />
