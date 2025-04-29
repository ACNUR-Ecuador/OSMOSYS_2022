package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.appConfiguration.AppConfigurationKey;
import com.sagatechs.generics.appConfiguration.AppConfigurationService;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.CustomPrincipal;
import com.sagatechs.generics.security.annotations.Secured;
import com.sagatechs.generics.security.model.RoleType;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.webservice.webModel.RoleWeb;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Path("/metabase-token")
public class MetabaseTokenEndpoint {

    @Inject
    OrganizacionService organizacionService;
    @Inject
    AppConfigurationService appConfigurationService;

    @Inject
    UserService userService;

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response getMetabaseToken(@Context SecurityContext securityContext) throws GeneralAppException {

        final String METABASE_SITE_URL = appConfigurationService.findValorByClave(AppConfigurationKey.METABASE_SITE_URL);
        final String METABASE_SECRET_KEY = appConfigurationService.findValorByClave(AppConfigurationKey.METABASE_SECRET_KEY);

        if(METABASE_SECRET_KEY == null || METABASE_SITE_URL == null) {
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("iframeUrl", null);
            return Response.ok(responseMap).build();
        }

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

        long dashboardId = 0;
        for (RoleWeb role : userWeb.getRoles()) {
            dashboardId = role.getName().equals(RoleType.MONITOR_PROYECTOS.getStringValue()) && dashboardId  < 5 ? 5 :
             role.getName().equals(RoleType.EJECUTOR_PROYECTOS.getStringValue()) && dashboardId  < 5 ? 5 :
             role.getName().equals(RoleType.MONITOR_ID.getStringValue()) && dashboardId  < 5 ? 5 :
             role.getName().equals(RoleType.EJECUTOR_ID.getStringValue()) && dashboardId  < 5 ? 5 :
             role.getName().equals(RoleType.RESULT_MANAGER.getStringValue()) && dashboardId  < 4 ? 4 :
             role.getName().equals(RoleType.ADMINISTRADOR_LOCAL.getStringValue()) && dashboardId  < 4 ? 4 :
             role.getName().equals(RoleType.ADMINISTRADOR_REGIONAL.getStringValue()) && dashboardId  < 4 ? 4 : dashboardId;
        }

        resource.put("dashboard", dashboardId);
        if (!acronym.equals("ACNUR"))// Id del dashboard a incrustar
        {
            params.put("implementador", new String[]{acronym});
        }

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
        String iframeUrl = METABASE_SITE_URL + "/embed/dashboard/" + token + "#bordered=false&titled=false";

        // Retornar la respuesta JSON
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("iframeUrl", iframeUrl);

        return Response.ok(responseMap).build();
    }
}