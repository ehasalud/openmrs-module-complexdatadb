/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.complexdatadb.web.handler;

import org.openmrs.Obs;
import org.openmrs.module.complexdatadb.api.handler.BinaryDBHandler;
import org.openmrs.obs.ComplexData;
import org.openmrs.web.WebConstants;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Extends functionality of {@link ImageDBHandler} for web specific views.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebBinaryDBHandler extends BinaryDBHandler {

	@Override
	public Obs getObs(Obs obs, String view) {
		
		if (WebConstants.HYPERLINK_VIEW.equals(view) || WebConstants.HTML_VIEW.equals(view)){
			String downloadHyperlink = getHyperlink(obs) + "&view=download&viewType=download";
			ComplexData cd = new ComplexData(obs.getValueAsString(null), downloadHyperlink);
			obs.setComplexData(cd);
			return obs;
		}
		
		return super.getObs(obs, view);
	}
	
	private String getHyperlink(Obs obs) {
		return "/" + WebConstants.WEBAPP_NAME + DISPLAY_LINK + obs.getObsId();
	}
	
}
