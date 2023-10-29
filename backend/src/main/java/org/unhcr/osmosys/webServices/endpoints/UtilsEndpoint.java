package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.jboss.logging.Logger;
import org.primefaces.json.JSONObject;
import org.unhcr.osmosys.reports.service.ReportService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.ws.rs.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;
import java.util.Base64;
import javax.ws.rs.core.MultivaluedHashMap;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

@Path("/utils")
@RequestScoped
public class UtilsEndpoint {
    private static final Logger LOGGER = Logger.getLogger(UtilsEndpoint.class);
    @Inject
    ReportService reportService;

    @Path("/koboview")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String test(
            KobocollectFormRequest requestData

    ) throws GeneralAppException {


        String username = requestData.getUsername();
        String password = requestData.getPassword();

        String viewURL = "https://kobo.unhcr.org/api/v2/assets/"+requestData.formId+"/data/"+requestData.submissionId+"/enketo/view/?return_url=false";
        String basicAuth = this.getBasicAuthenticationHeader(username, password);

        Builder request = ResteasyClientBuilder.newClient().target(viewURL).request();
        request.header("Authorization", basicAuth);
        request.header("Content-Type", MediaType.APPLICATION_JSON);

        Response response = request
                .get();
        String entity = response.readEntity(String.class);
        JSONObject jsonObj = new JSONObject(entity);
        String newUrl=jsonObj.getString("url");
        LOGGER.info(newUrl);

        return entity;
    }

    private String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

    public static class  KobocollectFormRequest {

        public KobocollectFormRequest() {
        }

        String username;
        String password;
        String formId;
        String submissionId;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFormId() {
            return formId;
        }

        public void setFormId(String formId) {
            this.formId = formId;
        }

        public String getSubmissionId() {
            return submissionId;
        }

        public void setSubmissionId(String submissionId) {
            this.submissionId = submissionId;
        }
    }

}
