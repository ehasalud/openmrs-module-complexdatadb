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
package org.openmrs.module.complexdatadb.api.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Specific handler for binary files. It extends GenericDBHandler and doesn't provide extra functionality, but 
 * it is registered as a handler in obsServiceTarget. 
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class BinaryDBHandler extends GenericDBHandler {
	
	public static final String HANDLER_TYPE = "BinaryDBHandler";
	
}
