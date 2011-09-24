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
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<title>Crux</title>
<% String baseURL = application.getInitParameter( "baseURL" ); %>
<link rel="stylesheet" type="text/css" href="js/dojo/dijit/themes/claro/claro.css"/>
<link rel="stylesheet" type="text/css" href="js/dojo/dojo/resources/dojo.css"/>
<script type="text/javascript" src="js/dojo/dojo/dojo.js" djConfig="parseOnLoad:true,isDebug:false"></script>
<script type="text/javascript" src="js/global.js" ></script>
<style type="text/css">
a { 
	color: #000000; 
	padding:3px 16px 3px 16px;
	background-color: #D9E8F9; 
	}
a:hover {
         color: #000000;
         background-color: white;
       	padding:3px 16px 3px 16px;
         border-bottom: 2px solid #FEBE47;
         }
a:active {
         color: #000000;
        padding:3px 16px 3px 16px;
         background-color: #E8E1CF;
         }
</style>

