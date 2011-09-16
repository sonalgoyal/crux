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
package co.nubetech.crux.interceptor;

import co.nubetech.crux.action.CruxAction;
import co.nubetech.crux.action.SaveReportAction;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class DemoInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	private boolean demo;

	public void setDemo(boolean demo) {
		this.demo = demo;
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Object obj = invocation.getAction();

		if (demo) {
			CruxAction action = (CruxAction) obj;
			action.getError()
					.setMessage(
							"This is a demo version. You are not allowed to save, edit or delete.");
			if (!(obj instanceof SaveReportAction)) {
				return "success";
			} else {
				return "error";
			}
		} else {
			return invocation.invoke();
		}
	}
}
