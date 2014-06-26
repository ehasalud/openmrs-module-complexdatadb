package org.openmrs.module.complexdatadb.api.handler;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Obs;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.complexdatadb.ComplexDataToDB;
import org.openmrs.module.complexdatadb.api.ComplexDataToDBService;
import org.openmrs.obs.ComplexData;
import org.openmrs.obs.ComplexObsHandler;
import org.openmrs.obs.handler.AbstractHandler;
import org.openmrs.web.WebConstants;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Victor Garcia
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ImageDBHandler extends AbstractHandler implements ComplexObsHandler {

	public static final Log log = LogFactory.getLog(ImageDBHandler.class);
	
	public static final String DISPLAY_LINK = "/complexObsServlet?obsId=";
	
	@Override
	public Obs getObs(Obs obs, String view) {

		if (WebConstants.HYPERLINK_VIEW.equals(view)) {
			ComplexData cd = new ComplexData(obs.getValueAsString(null), getHyperlink(obs));
			obs.setComplexData(cd);
			return obs;
		} else if (WebConstants.HTML_VIEW.equals(view)) {
			String imgtag = "<img src='" + getHyperlink(obs) + "'/>";
			ComplexData cd = new ComplexData(obs.getValueAsString(null), imgtag);
			obs.setComplexData(cd);
			return obs;
		} else {
			String imageUuid = getImageUuid(obs); 
			
			log.debug("value complex: " + obs.getValueComplex());
			log.debug("image uuid: " + imageUuid);
			ComplexData complexData = null;
			try {
				byte[] bImage = Context.getService(ComplexDataToDBService.class).getComplexDataToDB(imageUuid).getData();
				complexData = new ComplexData(imageUuid, bImage);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			obs.setComplexData(complexData);

			return obs;
		}
	}
	
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
			throw new APIException("Trying to write complex obs to the file system. ", ioe);
		}

		return obs;
	}
	
	private String getHyperlink(Obs obs) {
		return "/" + WebConstants.WEBAPP_NAME + "/complexObsServlet?obsId=" + obs.getObsId();
	}
	
	private String getImageUuid(Obs obs){
		String[] names = obs.getValueComplex().split("\\|");
		String uuid = names.length < 2 ? names[0] : names[names.length - 1];
		
		return uuid;
	}
	
}
