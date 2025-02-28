package com.sagatechs.generics.webservice;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;
import java.util.HashSet;

import com.sagatechs.generics.webservice.service.TestEndpoint;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.unhcr.osmosys.webServices.endpoints.CubeEndpoint;


/*****
 * Configracion de JAXRS
 * Si se quiere activar Swagger descomentar la clase getClass
 * Esto genera autmaticamente apiDoc en la URL http://localhost:8080/osmosys/api/openapi.json
 */
@ApplicationPath("/api")
public class JAXRSConfiguration extends Application {

//    @Override
//    public Set<Class<?>> getClasses() {
//        Set<Class<?>> resources = new HashSet<>();
//        resources.add(OpenApiResource.class);
//        resources.add(CubeEndpoint.class);
//        resources.add(TestEndpoint.class);
//        return resources;
//    }
}