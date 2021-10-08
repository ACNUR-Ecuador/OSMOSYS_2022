package com.sagatechs.generics.utils;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.io.IOException;


@Named
@ApplicationScoped
public class FileUtils {

    Base64 base64;

    @PostConstruct
    public void init() {
        base64 = new Base64();
    }

    public void saveFile(byte[] byteArray, String path) throws GeneralAppException {
        File file = new File(path);
        try {
            org.apache.commons.io.FileUtils.writeByteArrayToFile(file, byteArray);
        } catch (IOException e) {
            throw new GeneralAppException("No se pudo guardar el archivo en el servidor",
                    Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }

    }

    @SuppressWarnings("unused")
    public File getFile(String path) throws GeneralAppException {
        if (StringUtils.isBlank(path)) {
            throw new GeneralAppException("Dirección de archivo inválida");
        }

        return new File(path);

    }

    public byte[] decodeBase64ToBytes(String base64String) {
        if (StringUtils.indexOf( base64String,",") > 0) {
            base64String = StringUtils.substringAfter(base64String, ",");
        }
        return base64.decode(base64String.getBytes());
    }

    public String encodeFileToBase64(byte[] file) {

        return new String(this.base64.encode(file));
    }

}
