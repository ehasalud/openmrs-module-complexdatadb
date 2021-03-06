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
package org.openmrs.module.complexdatadb.api.db;

import org.openmrs.module.complexdatadb.ComplexDataToDB;
import org.openmrs.module.complexdatadb.api.ComplexDataToDBService;

/**
 *  Database methods for {@link ComplexDataToDBService}.
 */
public interface ComplexDataToDBDAO {
	
	/**
	 * Save a ComplexDataToDB object into the database.
	 * @param obsImage ComplexDataToDB to store/update in the database
	 */
	public void saveComplexDataToDB(ComplexDataToDB obsImage);
	
	/**
	 * Returns a ComplexDataToDB object from the database based on its uuid.
	 * @param uuid Uuid of the ComplexDataToDB object
	 * @return
	 */
	public ComplexDataToDB getComplexDataToDB(String uuid);
	
	/**
	 * Delete a ComplexDataToDB object from the database based on its uuid.
	 * @param uuid Uuid of the ComplexDataToDB object
	 */
	public void deleteComplexDataToDB(String uuid);
}