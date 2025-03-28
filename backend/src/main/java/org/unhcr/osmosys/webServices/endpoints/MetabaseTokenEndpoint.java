package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.CustomPrincipal;
import com.sagatechs.generics.security.annotations.Secured;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.unhcr.osmosys.services.OrganizacionService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Path("/metabase-token")
public class MetabaseTokenEndpoint {

    @Inject
    OrganizacionService organizacionService;

    @Inject
    UserService userService;
    private static final String METABASE_SITE_URL = "http://osmosys.trilogiconline.com:3000";
    private static final String METABASE_SECRET_KEY = "3459acdd7bf1e2a7876ce2226f46d2099cacc872ed29af36f4fbb97d1a022de2";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response getMetabaseToken(@Context SecurityContext securityContext) throws GeneralAppException {
        // Tiempo actual y fecha de expiración (10 minutos)

        Principal principal = securityContext.getUserPrincipal();
        CustomPrincipal customPrincipal = (CustomPrincipal) securityContext.getUserPrincipal();
        UserWeb userWeb = customPrincipal.getUser();
        String acronym =  "";
        if (userWeb != null) {
            acronym = userWeb.getOrganization().getAcronym();
        }

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        Date exp = new Date(nowMillis + (10 * 60 * 1000)); // 10 minutos en milisegundos

        // Construir el payload del token
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> resource = new HashMap<>();
        Map<String, Object> params = new HashMap<>();


        resource.put("dashboard", 2); // Id del dashboard a incrustar
        params.put("implementador", new String[]{acronym});

        payload.put("resource", resource);
        payload.put("params", params); // parámetros vacíos
        payload.put("exp", exp.getTime() / 1000); // exp en segundos

        // Generar el token firmado
        String token = Jwts.builder()
                .setClaims(payload)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, METABASE_SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .compact();

        // Construir el URL de incrustación
        String iframeUrl = METABASE_SITE_URL + "/embed/dashboard/" + token + "#bordered=true&titled=false";

        // Retornar la respuesta JSON
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("iframeUrl", iframeUrl);

        return Response.ok(responseMap).build();
    }
}