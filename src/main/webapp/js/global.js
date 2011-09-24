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
dojo.require("dojox.widget.Standby");

function showProgressIndicator(){
	document.body.appendChild(response.domNode);
	response.show();
}

function hideProgressIndicator(){
	response.hide();
}

function clearResponse(){
	var res=document.getElementById("responseText");
	 res.innerHTML="";
}

function responseMessage(message){
	var res=document.getElementById("responseText");
	 res.innerHTML=message;
}