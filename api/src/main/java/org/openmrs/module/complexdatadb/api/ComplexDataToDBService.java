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
package org.openmrs.module.complexdatadb.api;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.complexdatadb.ComplexDataToDB;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service exposes methods for managing complex data with tha database.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(ComplexDataToDBService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface ComplexDataToDBService extends OpenmrsService {
     
	/**
	 * Save an object in the database.
	 * @param obsImage
	 */
	public void saveComplexDataToDB(ComplexDataToDB obsImage);
	
	/**
	 * Retrieve an object from the database.
	 * @param uuid Uuid of the object
	 * @return
	 */
	public ComplexDataToDB getComplexDataToDB(String uuid);
	
	/**
	 * Delete an object from the database.
	 * @param uuid Uuid of the object
	 */
	public void deleteComplexDataToDB(String uuid);

}