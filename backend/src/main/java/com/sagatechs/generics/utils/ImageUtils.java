package com.sagatechs.generics.utils;


import com.sagatechs.generics.exceptions.GeneralAppException;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response.Status;
import java.util.Base64;


@Named
@ApplicationScoped
public class ImageUtils {

	@Inject
	FileUtils fileUtils;

	/**
	 * Convierte una imagen en base 64 a rray de bits
	 * 
	 * @param imageBase64
	 * @return
	 * @throws GeneralAppException
	 */
	@SuppressWarnings("WeakerAccess")
	public byte[] decodeBase64ImageToBytes(String imageBase64) throws GeneralAppException {
		if (StringUtils.isNotBlank(imageBase64)) {
			String[] imagenParts = StringUtils.split(imageBase64, ",");// (imageBase64, "data:image/jpeg;base64,");
			if (imagenParts != null && imagenParts.length == 2) {
				String imageS = imagenParts[1];
				Base64.Decoder dec = Base64.getDecoder();
				try {
					return dec.decode(imageS);
				} catch (Exception e) {
					throw new GeneralAppException("No se pudo decodificar la imagen",
							Status.BAD_REQUEST.getStatusCode());
				}
			}else {
				throw new GeneralAppException("No se pudo decodificar la imagen",
						Status.BAD_REQUEST.getStatusCode());
			}

		} else {
			return null;
		}
	}

	@SuppressWarnings("unused")
	public void saveImageBase64ToFile(String path, String imageBase64) throws GeneralAppException {
		byte[] image = this.decodeBase64ImageToBytes(imageBase64);

		this.fileUtils.saveFile(image, path);

	}

}
