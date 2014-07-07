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
package org.openmrs.module.complexdatadb.api.impl;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.complexdatadb.ComplexDataToDB;
import org.openmrs.module.complexdatadb.api.ComplexDataToDBService;
import org.openmrs.module.complexdatadb.api.db.ComplexDataToDBDAO;

/**
 * Implementation of {@link ComplexDataToDBService}.
 */
public class ComplexDataToDBServiceImpl extends BaseOpenmrsService implements ComplexDataToDBService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private ComplexDataToDBDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(ComplexDataToDBDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public ComplexDataToDBDAO getDao() {
	    return dao;
    }

    /**
     * @see ComplexDataToDBService#saveComplexDataToDB(ComplexDataToDB)
     */
	@Override
	public void saveComplexDataToDB(ComplexDataToDB obsImage) {
		dao.saveComplexDataToDB(obsImage);
	}

	/**
	 * @see ComplexDataToDBService#getComplexDataToDB(String)
	 */
	@Override
	public ComplexDataToDB getComplexDataToDB(String uuid) {	
		return dao.getComplexDataToDB(uuid);
	}

	
	/**
	 * @see ComplexDataToDBService#deleteComplexDataToDB(String)
	 */
	@Override
	public void deleteComplexDataToDB(String uuid) {
		dao.deleteComplexDataToDB(uuid);
		
	}
}