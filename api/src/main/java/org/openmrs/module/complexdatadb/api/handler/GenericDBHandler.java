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

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Obs;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.complexdatadb.ComplexDataToDB;
import org.openmrs.module.complexdatadb.api.ComplexDataToDBService;
import org.openmrs.obs.ComplexData;
import org.openmrs.obs.ComplexObsHandler;

/**
 * Generic complex obs handler to persist complex data in the database. It is a fully functional handler that makes 
 * no interpretation about the data; it just handles it as a byte array. This handler is not registered 
 * as a handler in the obsServiceTarget, so it cannot be used directly. It is necessary to create a specific 
 * handler that extends this one, and to register the new handler. Data interpretations are performed in the specific
 * handlers.
 */
public class GenericDBHandler implements ComplexObsHandler {
	
	public static final Log log = LogFactory.getLog(GenericDBHandler.class);
	
	public static final String DISPLAY_LINK = "/complexObsServlet?obsId=";
	
	/**
	 * @see org.openmrs.obs.ComplexObsHandler#getObs(Obs, String)
	 */
	public Obs getObs(Obs obs, String view) {

		String dataUuid = getComplexDataUuid(obs);
		
		log.debug("value complex: " + obs.getValueComplex());
		log.debug("data uuid: " + dataUuid);
		
		ComplexData complexData = null;

		try {
			byte[] bData = Context.getService(ComplexDataToDBService.class).getComplexDataToDB(dataUuid).getData();
			complexData = new ComplexData(dataUuid + "." + getExtension(obs), bData);
			
		} catch (Exception e){
			throw new APIException("An error occurred while trying to get binary complex obs.", e);
		}
	
		obs.setComplexData(complexData);

		return obs;	
	}
	
	/**
	 * @see org.openmrs.obs.ComplexObsHandler#saveObs(Obs)
	 */
	@Override
	public Obs saveObs(Obs obs) throws APIException {
		
		try {
			String fileName = obs.getComplexData().getTitle();
			
			InputStream in = (InputStream) obs.getComplexData().getData();
		
			byte[] dataInBytes = IOUtils.toByteArray(in);
			
			// Create an object to persist it in the database
			ComplexDataToDB binaryFile = new ComplexDataToDB();
			binaryFile.setData(dataInBytes);
			
			// Save the complex data in the database
			Context.getService(ComplexDataToDBService.class).saveComplexDataToDB(binaryFile);
			
			// Set the URI in valueComplex field
			String extension = getExtension(fileName);
			obs.setValueComplex(extension + " file |" + binaryFile.getUuid());

			// Remove the ComlexData from the Obs
			obs.setComplexData(null);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return obs;
	}
	
	/**
	 * @see org.openmrs.obs.ComplexObsHandler#purgeComplexData(org.openmrs.Obs)
	 */
	public boolean purgeComplexData(Obs obs){
		Context.getService(ComplexDataToDBService.class).deleteComplexDataToDB(getComplexDataUuid(obs));
		return true;
	}
	
	/**
	 * Get the uuid vale associated to the observation
	 * @param obs Observation
	 * @return Uuid associated to the observation
	 */
	protected String getComplexDataUuid(Obs obs){
		String[] names = obs.getValueComplex().split("\\|");
		String uuid = names.length < 2 ? names[0] : names[names.length - 1];
		
		return uuid;
	}
	
	/**
	 * @see org.openmrs.obs.handler.AbstractHandler#getExtension(String)
	 */
	public String getExtension(String filename) {
		String[] filenameParts = filename.split("\\.");

		log.debug("titles length: " + filenameParts.length);

		String extension = (filenameParts.length < 2) ? filenameParts[0] : filenameParts[filenameParts.length - 1];
		extension = (null != extension && !"".equals(extension)) ? extension : "raw";

		return extension;
	}
	
	/**
	 * Get the extension associated to the obs. The extension is stored as the first word in valueComplex field.
	 * @param obs Observation
	 * @return Extension associated to the complex data of the observation
	 */
	public String getExtension(Obs obs){
		String[] nameParts = obs.getValueComplex().split(" ");
		
		String extension = nameParts[0];
		extension = extension != "raw" ? extension : "";
		
		return extension;
	}
}
