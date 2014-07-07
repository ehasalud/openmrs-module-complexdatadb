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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.openmrs.Obs;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.complexdatadb.ComplexDataToDB;
import org.openmrs.module.complexdatadb.api.ComplexDataToDBService;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Specific complex obs handler for images that persists data in the database. It checks if complex data represents 
 * a valid image before storing it in the database.
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ImageDBHandler extends GenericDBHandler  {

	public static final String HANDLER_TYPE = "ImageDBHandler";
		
	/**
	 * @see org.openmrs.obs.ComplexObsHandler#saveObs(org.openmrs.Obs)
	 */
	@Override
	public Obs saveObs(Obs obs) throws APIException {
		
		// Get the buffered image from the ComplexData.
		BufferedImage img = null;

		Object data = obs.getComplexData().getData();
		if (data instanceof BufferedImage) {
			img = (BufferedImage) data;
		} else if (data instanceof InputStream) {
			try {
				img = ImageIO.read((InputStream) data);
				if (img == null) {
					throw new IllegalArgumentException("Invalid image file");
				}
			}
			catch (IOException e) {
				throw new APIException(
				        "Unable to convert complex data to a valid input stream and then read it into a buffered image", e);
			}
		}

		if (img == null) {
			throw new APIException("Cannot save complex obs where obsId=" + obs.getObsId()
			        + " because its ComplexData.getData() is null.");
		}

		try {
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			String extension = getExtension(obs.getComplexData().getTitle());
			ImageIO.write( img, extension, baos );
			baos.flush();
			byte[] bImage = baos.toByteArray();
			baos.close();
			
			ComplexDataToDB image = new ComplexDataToDB();
			
			image.setData(bImage);
			
			Context.getService(ComplexDataToDBService.class).saveComplexDataToDB(image);
			
			// Set the Title and URI for the valueComplex
			obs.setValueComplex(extension + " image |" + image.getUuid());

			// Remove the ComlexData from the Obs
			obs.setComplexData(null);

		}
		catch (IOException ioe) {
			throw new APIException("Trying to write complex obs to the database. ", ioe);
		}

		return obs;
	}
	

}
