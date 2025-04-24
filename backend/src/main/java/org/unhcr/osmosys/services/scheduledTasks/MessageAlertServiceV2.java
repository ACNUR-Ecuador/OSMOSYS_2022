package org.unhcr.osmosys.services.scheduledTasks;

import com.sagatechs.generics.appConfiguration.AppConfigurationKey;
import com.sagatechs.generics.appConfiguration.AppConfigurationService;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.RoleType;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.service.EmailService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.Organization;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.Project;
import org.unhcr.osmosys.model.enums.MonthEnum;
import org.unhcr.osmosys.model.enums.TimeStateEnum;
import org.unhcr.osmosys.services.IndicatorExecutionService;
import org.unhcr.osmosys.services.PeriodService;
import org.unhcr.osmosys.services.ProjectService;
import org.unhcr.osmosys.services.UtilsService;
import org.unhcr.osmosys.webServices.model.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class MessageAlertServiceV2 {
    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(MessageAlertServiceV2.class);


    @Inject
    AppConfigurationService appConfigurationService;

    @Inject
    ProjectService projectService;

    @Inject
    PeriodService periodService;

    @Inject
    UtilsService utilsService;

    @Inject
    EmailService emailService;

    @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Inject
    UserService userService;


    public void sendPartnersAlertsToFocalPoints() throws GeneralAppException {
        Period currentPeriod = this.periodService.getByYear(this.utilsService.getCurrentYear());
        // obtengo los focal points de este periodo
        List<User> focalPoints = this.projectService.getFocalPointByPeriodId(currentPeriod.getId());
        List<String> imProgramsEmail = new ArrayList<>();
        if (StringUtils.isNotBlank(this.appConfigurationService.findValorByClave(AppConfigurationKey.IM_EMAIL))) {
            imProgramsEmail.add(this.appConfigurationService.findValorByClave(AppConfigurationKey.IM_EMAIL));
        }
        if (StringUtils.isNotBlank(this.appConfigurationService.findValorByClave(AppConfigurationKey.PROGRAMS_EMAIL))) {
            imProgramsEmail.add(this.appConfigurationService.findValorByClave(AppConfigurationKey.PROGRAMS_EMAIL));
        }

        for (User focalPoint : focalPoints) {
            // obtengo los proyectos por cada focal point
            LOGGER.info(focalPoint.getName());
            List<ProjectResumeWeb> projects = this.projectService.getProjectResumenWebByPeriodIdAndFocalPointId(currentPeriod.getId(), focalPoint.getId());
            List<PartnerAlertDTO> alerts=this.getPartnerAlerts(projects);
            if (alerts.isEmpty()) {
                continue;
            }

            SXSSFWorkbook wb = this.createPartnersAlertAttachment(alerts);
            ByteArrayOutputStream report = this.utilsService.getByteArrayOutputStreamFromWorkbook(wb);


            String copyAddresses = CollectionUtils.isNotEmpty(imProgramsEmail) ? String.join(", ", imProgramsEmail) : null;

            String message = this.getProjectManagerAlertMessage(String.join(", ", projects.stream()
                    .map(ProjectResumeWeb::getOrganizationAcronym)
                    .collect(Collectors.toSet())));
            this.emailService.sendEmailMessageWithAttachment(focalPoint.getEmail(), copyAddresses, "Retrasos en reporte de indicadores - OSMOSYS", message, report, "reporte_retrasos.xlsx");
        }
    }
    public void sendPartnersAlertsToPartners() throws GeneralAppException {
        Period currentPeriod = this.periodService.getByYear(this.utilsService.getCurrentYear());
        List<String> imProgramsEmail = new ArrayList<>();
        if (StringUtils.isNotBlank(this.appConfigurationService.findValorByClave(AppConfigurationKey.IM_EMAIL))) {
            imProgramsEmail.add(this.appConfigurationService.findValorByClave(AppConfigurationKey.IM_EMAIL));
        }
        if (StringUtils.isNotBlank(this.appConfigurationService.findValorByClave(AppConfigurationKey.PROGRAMS_EMAIL))) {
            imProgramsEmail.add(this.appConfigurationService.findValorByClave(AppConfigurationKey.PROGRAMS_EMAIL));
        }

        // obtengo los supervisores socios de este periodo
        List<User> partnerSupervisors = this.projectService.getPartnerSupervisorsByPeriodId(currentPeriod.getId());
        if(!partnerSupervisors.isEmpty()) {
            for (User partnerSupervisor : partnerSupervisors) {
                // obtengo los proyectos por cada partner Supervisor
                LOGGER.info(partnerSupervisor.getName());
                List<ProjectResumeWeb> projects = this.projectService.getProjectResumenWebByPeriodIdAndPartnerSupervisorId(currentPeriod.getId(), partnerSupervisor.getId());
                List<PartnerAlertDTO> alerts=this.getPartnerAlerts(projects);
                if (alerts.isEmpty()) {
                    continue;
                }
                StringBuilder projectManagerEmails = new StringBuilder();
                Set<Long> projectIds = alerts.stream().map(PartnerAlertDTO::getProjectId).collect(Collectors.toSet());
                //lista de project managers y correos
                for(Long projectId: projectIds){
                    Project project = this.projectService.getById(projectId);
                    projectManagerEmails.append("<li><strong>").append(project.getCode()).append("</strong>")
                            .append(" - ")
                            .append(project.getFocalPointAssignations().stream()
                                    .map(focalPointAssignation -> focalPointAssignation.getFocalPointer().getEmail())
                                    .findFirst()
                                    .orElse(""))
                            .append("</li>"); // fin del item
                }
                String resultString = "</br>\n<ul>\n" + projectManagerEmails + "\n</ul>";

                String copyAddresses = CollectionUtils.isNotEmpty(imProgramsEmail) ? String.join(", ", imProgramsEmail) : null;

                SXSSFWorkbook wb = this.createPartnersAlertAttachment(alerts);
                ByteArrayOutputStream report = this.utilsService.getByteArrayOutputStreamFromWorkbook(wb);

                String message = this.getPartnerAlertMessage(partnerSupervisor.getOrganization().getAcronym(),resultString);
                this.emailService.sendEmailMessageWithAttachment(partnerSupervisor.getEmail(), copyAddresses, "Retrasos en reporte de indicadores - OSMOSYS", message, report, "reporte_retrasos.xlsx");

            }
        }
        /*--Envío de correos a Socios responsables de reporte--*/
        List<Organization> organizations = this.projectService.getActiveProjectsPartnersByPeriodId(currentPeriod.getId());
        for (Organization organization : organizations) {
            LOGGER.info(organization.getAcronym());
            // obtengo los proyectos por cada organización
            List<ProjectResumeWeb> projects = this.projectService.getProjectResumenWebByPeriodIdAndOrganizationId(currentPeriod.getId(), organization.getId());
            List<PartnerAlertDTO> alerts=this.getPartnerAlerts(projects);
            if (alerts.isEmpty()) {
                continue;
            }
            //obtengo todos los responsables de reporte de la organización
            List<User> partnerUsers = userService.getActivePartnerUsers(organization.getId()).stream()
                    .filter(user -> user.getRoleAssigments().stream()
                            .anyMatch(roleAssigment -> roleAssigment.getRole().getRoleType().equals(RoleType.EJECUTOR_PROYECTOS) && roleAssigment.getState().equals(State.ACTIVO)))
                    .collect(Collectors.toList());
            StringBuilder projectManagerEmails = new StringBuilder();
            Set<Long> projectIds = alerts.stream().map(PartnerAlertDTO::getProjectId).collect(Collectors.toSet());
            //lista de project managers y correos
            for(Long projectId: projectIds){
                Project project = this.projectService.getById(projectId);
                projectManagerEmails.append("<li><strong>").append(project.getCode()).append("</strong>")
                        .append(" - ") // Separador entre los atributos
                        .append(project.getFocalPointAssignations().stream()
                                .map(focalPointAssignation -> focalPointAssignation.getFocalPointer().getEmail())
                                .findFirst()
                                .orElse(""))
                        .append("</li>"); // fin del item
            }
            String resultString = "</br>\n<ul>\n" + projectManagerEmails + "\n</ul>";
            SXSSFWorkbook wb = this.createPartnersAlertAttachment(alerts);
            ByteArrayOutputStream report = this.utilsService.getByteArrayOutputStreamFromWorkbook(wb);

            for(User partnerUser: partnerUsers){
                LOGGER.debug("user: " + partnerUser.getEmail());
                String message = this.getPartnerAlertMessage(partnerUser.getOrganization().getAcronym(),resultString);
                String copyAddresses = CollectionUtils.isNotEmpty(imProgramsEmail) ? String.join(", ", imProgramsEmail) : null;
                this.emailService.sendEmailMessageWithAttachment(partnerUser.getEmail(), copyAddresses, "Retrasos en reporte de indicadores - OSMOSYS", message, report, "reporte_retrasos.xlsx");
            }
        }


    }

    public void sendDirectImplementationAlertsToSupervisors() throws GeneralAppException {
        Period currentPeriod = this.periodService.getByYear(this.utilsService.getCurrentYear());
        List<String> imProgramsEmail = new ArrayList<>();
        if (StringUtils.isNotBlank(this.appConfigurationService.findValorByClave(AppConfigurationKey.IM_EMAIL))) {
            imProgramsEmail.add(this.appConfigurationService.findValorByClave(AppConfigurationKey.IM_EMAIL));
        }
        if (StringUtils.isNotBlank(this.appConfigurationService.findValorByClave(AppConfigurationKey.PROGRAMS_EMAIL))) {
            imProgramsEmail.add(this.appConfigurationService.findValorByClave(AppConfigurationKey.PROGRAMS_EMAIL));
        }
        // busco indicadores por supervisores
        List<User> supervisors = this.indicatorExecutionService.getSupervisorsByPeriodId(currentPeriod.getId());
        /*--Envio de correos a supervisores--*/
        for (User supervisor : supervisors) {
            List<IndicatorExecutionWeb> performanceIndicators = this.indicatorExecutionService.getDirectImplementationsIndicatorExecutionsBySupervisorId(currentPeriod.getId(), supervisor.getId());
            List<IndicatorExecutionWeb> latePerformanceIndicators = performanceIndicators.stream()
                    .filter(indicatorExecutionWeb -> indicatorExecutionWeb.getLate().equals(TimeStateEnum.LATE) || indicatorExecutionWeb.getLate().equals(TimeStateEnum.SOON_REPORT))
                    .collect(Collectors.toList());
            if (latePerformanceIndicators.size() < 1) {
                continue;
            }
            List<DIAlertDTO> alerts = this.getDIAlerts(latePerformanceIndicators);
            SXSSFWorkbook wb = this.createDirectImplementationAlertAttachment(alerts);
            ByteArrayOutputStream report = this.utilsService.getByteArrayOutputStreamFromWorkbook(wb);

            List<String> copyAddresses = new ArrayList<>();
            if (imProgramsEmail.size() > 0) {
                copyAddresses.addAll(imProgramsEmail);
            }
            copyAddresses.addAll(alerts.stream().map(DIAlertDTO::getResponsableEmail).distinct().collect(Collectors.toList()));
            String message = this.getDIAlertMessage(supervisor.getName());
            this.emailService.sendEmailMessageWithAttachment(supervisor.getEmail(), copyAddresses.stream().collect(Collectors.joining(", ")), "Retrasos en reporte de indicadores - OSMOSYS", message, report, "reporte_retrasos.xlsx");
        }
        /*--Envio de correos a responsables de reporte y alternos--*/
        List<User> reportUsers = this.userService.getActiveResponsableAndAlternsDirectImplementationUsers(currentPeriod.getId());
        for (User reportUser : reportUsers) {
            List<IndicatorExecutionWeb> performanceIndicators = this.indicatorExecutionService.getDirectImplementationsIndicatorExecutionsByReportUserId(currentPeriod.getId(), reportUser.getId());
            List<IndicatorExecutionWeb> latePerformanceIndicators = performanceIndicators.stream()
                    .filter(indicatorExecutionWeb -> indicatorExecutionWeb.getLate().equals(TimeStateEnum.LATE) || indicatorExecutionWeb.getLate().equals(TimeStateEnum.SOON_REPORT))
                    .collect(Collectors.toList());
            if (latePerformanceIndicators.size() < 1) {
                continue;
            }
            List<DIAlertDTO> alerts = this.getDIAlerts(latePerformanceIndicators);
            SXSSFWorkbook wb = this.createDirectImplementationAlertAttachment(alerts);
            ByteArrayOutputStream report = this.utilsService.getByteArrayOutputStreamFromWorkbook(wb);

            List<String> copyAddresses = new ArrayList<>();
            if (imProgramsEmail.size() > 0) {
                copyAddresses.addAll(imProgramsEmail);
            }
            //copyAddresses.addAll(alerts.stream().map(DIAlertDTO::getResponsableEmail).distinct().collect(Collectors.toList()));
            String message = this.getDIAlertMessage(reportUser.getName());
            this.emailService.sendEmailMessageWithAttachment(reportUser.getEmail(), copyAddresses.stream().collect(Collectors.joining(", ")), "Retrasos en reporte de indicadores - OSMOSYS", message, report, "reporte_retrasos.xlsx");
        }


    }

    public void sendIndicatorAlertsToResultManagers() throws GeneralAppException {
        Period currentPeriod = this.periodService.getByYear(this.utilsService.getCurrentYear());
        Integer rmLimitDay = this.appConfigurationService.getResultManagerLimitDay();
        List<String> imProgramsEmail = new ArrayList<>();
        if (StringUtils.isNotBlank(this.appConfigurationService.findValorByClave(AppConfigurationKey.IM_EMAIL))) {
            imProgramsEmail.add(this.appConfigurationService.findValorByClave(AppConfigurationKey.IM_EMAIL));
        }
        if (StringUtils.isNotBlank(this.appConfigurationService.findValorByClave(AppConfigurationKey.PROGRAMS_EMAIL))) {
            imProgramsEmail.add(this.appConfigurationService.findValorByClave(AppConfigurationKey.PROGRAMS_EMAIL));
        }

        Integer currentMonthYearOrder = this.utilsService.getCurrentMonthYearOrder();
        int mes = currentMonthYearOrder % 100;  // Esto nos da los dos últimos dígitos (mes)

        // Determinar el trimestre a reportar
        Integer quarterReport;
        Calendar now = Calendar.getInstance();
        Calendar limitDay = Calendar.getInstance();
        if (mes >= 1 && mes <= 3) {
            quarterReport = 4;  // Primer trimestre (Enero, Febrero, Marzo)
            currentPeriod = this.periodService.getByYear(Calendar.getInstance().get(Calendar.YEAR)-1);
            limitDay.set(now.get(Calendar.YEAR), 0, rmLimitDay, 6, 0, 0);
        } else if (mes >= 4 && mes <= 6) {
            quarterReport = 1; // Segundo trimestre (Abril, Mayo, Junio)
            limitDay.set(now.get(Calendar.YEAR), 3, rmLimitDay, 6, 0, 0);
        } else if (mes >= 7 && mes <= 9) {
            quarterReport = 2;  // Tercer trimestre (Julio, Agosto, Septiembre)
            limitDay.set(now.get(Calendar.YEAR), 6, rmLimitDay, 6, 0, 0);
        } else if (mes >= 10 && mes <= 12) {
            quarterReport = 3;  // Cuarto trimestre (Octubre, Noviembre, Diciembre)
            limitDay.set(now.get(Calendar.YEAR), 9, rmLimitDay, 6, 0, 0);
        } else {
            quarterReport = 0;  // Si el mes no es válido (aunque no se espera que ocurra)
            limitDay.set(now.get(Calendar.YEAR), 0, rmLimitDay, 6, 0, 0);
        }
        if (currentPeriod == null) {
            return;
        }

        boolean isOverLimit = now.after(limitDay);

        // obtengo los Usuarios resultManagers
        List<User> resultManagers = this.userService.getActiveResultManagerUsers();
        for (User resultManager : resultManagers) {

            List<ResultManagerIndicatorWeb> rmiw= indicatorExecutionService.getResultManagerIndicators(resultManager.getId(), currentPeriod.getId());
            if(rmiw.isEmpty()){
                continue;
            }
            List<RMAlertDTO> alerts = new ArrayList<>();
            for (ResultManagerIndicatorWeb rmi : rmiw) {
                if(!rmi.isHasExecutions()){
                    continue;
                }
                List<ResultManagerIndicatorQuarterWeb> resultManagerIndicatorQuarterWebList =rmi.getResultManagerIndicatorQuarter();
                for(ResultManagerIndicatorQuarterWeb resultManagerIndicatorQuarterWeb : resultManagerIndicatorQuarterWebList){
                    if(resultManagerIndicatorQuarterWeb.getQuarter()<=quarterReport){
                        List<ResultManagerQuarterPopulationTypeWeb> rmqpws= resultManagerIndicatorQuarterWeb.getResultManagerQuarterPopulationType();
                        for(ResultManagerQuarterPopulationTypeWeb rmqpw: rmqpws){
                            if(!rmqpw.isConfirmation() && isOverLimit){
                                RMAlertDTO alertDTO = new RMAlertDTO();
                                alertDTO.setIndicator(rmi.getIndicator().getDescription());
                                alertDTO.setCode(rmi.getIndicator().getCode());
                                alertDTO.setQuarter("Q"+resultManagerIndicatorQuarterWeb.getQuarter());
                                alertDTO.setPopulationType(rmqpw.getPopulationType().getName());
                                alertDTO.setPeriod(currentPeriod.getYear().toString());
                                alerts.add(alertDTO);
                            }
                        }

                    }
                }

            }
            if (alerts.isEmpty()) {
                continue;
            }
            LOGGER.debug("user: " + resultManager.getEmail());
            SXSSFWorkbook wb = this.createResultManagerAlertAttachment(alerts);
            ByteArrayOutputStream report = this.utilsService.getByteArrayOutputStreamFromWorkbook(wb);

            List<String> copyAddresses = new ArrayList<>();
            if (imProgramsEmail.size() > 0) {
                copyAddresses.addAll(imProgramsEmail);
            }
            String message = this.getResultManagerAlertMessage("Q"+quarterReport);
            this.emailService.sendEmailMessageWithAttachment(resultManager.getEmail(), copyAddresses.stream().collect(Collectors.joining(", ")), "Retrasos en validación de indicadores - OSMOSYS", message, report, "reporte_retrasos.xlsx");



        }

    }

    private SXSSFWorkbook createDirectImplementationAlertAttachment(List<DIAlertDTO> alerts) {
        List<Integer> titlesWidth = new ArrayList<>(Arrays.asList(6000, 6000, 6000, 6000, 8000, 6000));
        List<String> titles = new ArrayList<>(Arrays.asList("Oficina", "Indicador", "Meses retrasados", "Responsable de reporte", "Responsable alterno de reporte", "Supervisor de reporte"));

        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet();
        // Set which area the table should be placed in
        int NB_ROWS = alerts.size();
        int NB_COLS = titles.size() - 1;
        AreaReference reference = wb.getCreationHelper()
                .createAreaReference(
                        new CellReference(0, 0),
                        new CellReference(NB_ROWS, NB_COLS));
        // title rows
        sheet.setAutoFilter(CellRangeAddress.valueOf(reference.formatAsString()));

        // Llamar a los métodos para obtener los estilos
        CellStyle headerStyle = this.createHeaderStyle(wb);
        CellStyle dataStyle = this.createDataStyle(wb);


        // Establecer anchos de columnas
        for (int i = 0; i < titles.size(); i++) {
            sheet.setColumnWidth(i, titlesWidth.get(i));
        }

        // Crear fila de título
        Row rowTitle = sheet.createRow(0);
        for (int i = 0; i < titles.size(); i++) {
            Cell cell = rowTitle.createCell(i);
            cell.setCellValue(titles.get(i));
            cell.setCellStyle(headerStyle);  // Aplica el estilo al encabezado
        }

        // Poner los datos de la alerta con filas alternadas de colores
        for (int i = 0; i < alerts.size(); i++) {
            SXSSFRow rowData = sheet.createRow(i + 1);
            DIAlertDTO alert = alerts.get(i);


            // Asignar valores a las celdas y aplicar el estilo con bordes
            createStyledCell(rowData, 0, alert.getOffice(), dataStyle);
            createStyledCell(rowData, 1, alert.getIndicator(), dataStyle);
            createStyledCell(rowData, 2, alert.getLateMonths(), dataStyle);
            createStyledCell(rowData, 3, alert.getResponsableName(), dataStyle);
            createStyledCell(rowData, 4, alert.getResponsableBackupName(), dataStyle);
            createStyledCell(rowData, 5, alert.getSupervisorName(), dataStyle);
        }

        return wb;
    }

    // Método auxiliar para aplicar el estilo a las celdas
    private void createStyledCell(Row row, int columnIndex, String value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    // Método para crear el estilo del encabezado
    private  CellStyle createHeaderStyle(SXSSFWorkbook wb) {
        CellStyle headerStyle = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.WHITE.getIndex()); // Color blanco para el texto del encabezado
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex()); // Fondo azul claro
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // Establece el color de fondo sólido
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return headerStyle;
    }

    // Método para crear el estilo de datos
    private  CellStyle createDataStyle(SXSSFWorkbook wb) {
        CellStyle dataStyle = wb.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setWrapText(true);
        return dataStyle;
    }

    private SXSSFWorkbook createResultManagerAlertAttachment(List<RMAlertDTO> alerts) {
        List<Integer> titlesWidth = new ArrayList<>(Arrays.asList(6000, 3000, 3000, 6000, 3000));
        List<String> titles = new ArrayList<>(Arrays.asList("Indicador", "Código", "Trimestre", "Tipo de Población", "Año"));

        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet();
        // Set which area the table should be placed in
        int NB_ROWS = alerts.size();
        int NB_COLS = titles.size() - 1;
        AreaReference reference = wb.getCreationHelper()
                .createAreaReference(
                        new CellReference(0, 0),
                        new CellReference(NB_ROWS, NB_COLS));
        // title rows
        sheet.setAutoFilter(CellRangeAddress.valueOf(reference.formatAsString()));

        // Llamar a los métodos para obtener los estilos
        CellStyle headerStyle = this.createHeaderStyle(wb);
        CellStyle dataStyle = this.createDataStyle(wb);


        // Establecer anchos de columnas
        for (int i = 0; i < titles.size(); i++) {
            sheet.setColumnWidth(i, titlesWidth.get(i));
        }

        // Crear fila de título
        Row rowTitle = sheet.createRow(0);
        for (int i = 0; i < titles.size(); i++) {
            Cell cell = rowTitle.createCell(i);
            cell.setCellValue(titles.get(i));
            cell.setCellStyle(headerStyle);  // Aplica el estilo al encabezado
        }

        // Poner los datos de la alerta con filas alternadas de colores
        for (int i = 0; i < alerts.size(); i++) {
            SXSSFRow rowData = sheet.createRow(i + 1);
            RMAlertDTO alert = alerts.get(i);


            // Asignar valores a las celdas y aplicar el estilo con bordes
            createStyledCell(rowData, 0, alert.getIndicator(), dataStyle);
            createStyledCell(rowData, 1, alert.getCode(), dataStyle);
            createStyledCell(rowData, 2, alert.getQuarter(), dataStyle);
            createStyledCell(rowData, 3, alert.getPopulationType(), dataStyle);
            createStyledCell(rowData, 4, alert.getPeriod(), dataStyle);
        }

        return wb;
    }




    private String getProjectManagerAlertMessage(String partnersList) {
        String message= this.appConfigurationService.findValorByClave(AppConfigurationKey.PROJECT_MANAGER_ALERT_EMAIL);
                message= message.replace("%partnersList%",partnersList);
        return message;
    }
    private String getPartnerAlertMessage(String partnerName, String projectManagerEmails) {
        String message= this.appConfigurationService.findValorByClave(AppConfigurationKey.PARTNERS_ALERT_EMAIL);
        message= message.replace("%partnerName%",partnerName)
                        .replace("%projectManagerEmails%",projectManagerEmails);
        return message;

    }

    private String getDIAlertMessage(String userName) {
        String message= this.appConfigurationService.findValorByClave(AppConfigurationKey.DIRECT_IMPLS_ALERT_EMAIL);
        message= message.replace("%userName%",userName);
        return message;

    }

    private String getResultManagerAlertMessage(String quarter) {
        String message= this.appConfigurationService.findValorByClave(AppConfigurationKey.RESULT_MANAGER_ALERT_EMAIL);
        message= message.replace("%quarter%",quarter);
        return message;

    }

    private SXSSFWorkbook createPartnersAlertAttachment(List<PartnerAlertDTO> alerts) {
        List<Integer> titlesWidth = new ArrayList<>(Arrays.asList(6000, 3000, 6000, 6000, 6000));
        List<String> titles = new ArrayList<>(Arrays.asList("Proyecto", "Socio", "Indicador", "Tipo de Indicador", "Meses retrasados"));
        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet();
        // Set which area the table should be placed in
        int NB_ROWS = alerts.size();
        int NB_COLS = titles.size() - 1;
        AreaReference reference = wb.getCreationHelper()
                .createAreaReference(
                        new CellReference(0, 0),
                        new CellReference(NB_ROWS, NB_COLS));
        // title rows
        sheet.setAutoFilter(CellRangeAddress.valueOf(reference.formatAsString()));
        // Set the values for the table

        // Llamar a los métodos para obtener los estilos
        CellStyle headerStyle = this.createHeaderStyle(wb);
        CellStyle dataStyle = this.createDataStyle(wb);


        // Establecer anchos de columnas
        for (int i = 0; i < titles.size(); i++) {
            sheet.setColumnWidth(i, titlesWidth.get(i));
        }

        // Crear fila de título
        Row rowTitle = sheet.createRow(0);
        for (int i = 0; i < titles.size(); i++) {
            Cell cell = rowTitle.createCell(i);
            cell.setCellValue(titles.get(i));
            cell.setCellStyle(headerStyle);  // Aplica el estilo al encabezado
        }

        for (int i = 0; i < alerts.size(); i++) {
            SXSSFRow rowData = sheet.createRow(i + 1);
            PartnerAlertDTO alert = alerts.get(i);

            // Asignar valores a las celdas y aplicar el estilo con bordes
            createStyledCell(rowData, 0, alert.getProjectName(), dataStyle);
            createStyledCell(rowData, 1, alert.getPartner(), dataStyle);
            createStyledCell(rowData, 2, alert.getIndicator(), dataStyle);
            createStyledCell(rowData, 3, alert.getIndicatorType(), dataStyle);
            createStyledCell(rowData, 4, alert.getLateMonths(), dataStyle);
        }

        return wb;


    }

    private List<PartnerAlertDTO> getPartnerAlerts(List<ProjectResumeWeb> projects) throws GeneralAppException{
        List<PartnerAlertDTO> alerts = new ArrayList<>();
        for (ProjectResumeWeb project : projects) {
            LOGGER.info("   " + project.getOrganizationAcronym() + "-" + project.getName());
            List<IndicatorExecutionWeb> generalIndicators = this.indicatorExecutionService.getGeneralIndicatorExecutionsByProjectIdAndState(project.getId(), State.ACTIVO);
            if (CollectionUtils.isNotEmpty(generalIndicators)) {
                LOGGER.info("       " + "General:" + generalIndicators.size());
                IndicatorExecutionWeb generaIndicator = generalIndicators.get(0);
                LOGGER.info("       " + "General:" + generaIndicator.getLate());
                if (generaIndicator.getLate().equals(TimeStateEnum.LATE) || generaIndicator.getLate().equals(TimeStateEnum.SOON_REPORT)) {
                    PartnerAlertDTO alertDTO = new PartnerAlertDTO();
                    alertDTO.setProjectId(project.getId());
                    alertDTO.setPartner(project.getOrganizationAcronym());
                    alertDTO.setProjectName(project.getName());
                    alertDTO.setIndicator(generaIndicator.getIndicator().getDescription());
                    alertDTO.setIndicatorType(generaIndicator.getIndicatorType().getLabel());
                    alertDTO.setLateMonths(generaIndicator.getQuarters().stream().flatMap(quarterWeb -> quarterWeb.getMonths().stream()).filter(monthWeb -> monthWeb.getLate().equals(TimeStateEnum.LATE) || monthWeb.getLate().equals(TimeStateEnum.SOON_REPORT))
                            .map(MonthWeb::getMonth)
                            .sorted()
                            .map(MonthEnum::getLabel)
                            .collect(Collectors.joining(", ")));
                    alerts.add(alertDTO);
                }
            }
            List<IndicatorExecutionWeb> performanceIndicators = this.indicatorExecutionService.getPerformanceIndicatorExecutionsByProjectId(project.getId(), State.ACTIVO);
            LOGGER.info("       " + "Performance:" + performanceIndicators.size());
            // filtro lo que tengan retrasos
            List<IndicatorExecutionWeb> latePerformanceIndicators = performanceIndicators.stream().filter(indicatorExecutionWeb -> indicatorExecutionWeb.getLate().equals(TimeStateEnum.LATE) || indicatorExecutionWeb.getLate().equals(TimeStateEnum.SOON_REPORT)).collect(Collectors.toList());

            LOGGER.info("       " + "LatePerformance:" + latePerformanceIndicators.size());
            for (IndicatorExecutionWeb latePerformanceIndicator : latePerformanceIndicators) {

                String lateMonths = latePerformanceIndicator.getQuarters().stream().flatMap(quarterWeb -> quarterWeb.getMonths().stream())
                        .filter(monthWeb -> monthWeb.getState().equals(State.ACTIVO))
                        .filter(monthWeb -> monthWeb.getLate().equals(TimeStateEnum.LATE) || monthWeb.getLate().equals(TimeStateEnum.SOON_REPORT))
                        .map(MonthWeb::getMonth)
                        .sorted()
                        .map(MonthEnum::getLabel)
                        .collect(Collectors.joining(", "));
                LOGGER.info("          " + "LatePerformance:" + latePerformanceIndicator.getIndicator().getCode() + lateMonths);
                PartnerAlertDTO alertDTO = new PartnerAlertDTO();
                alertDTO.setProjectId(project.getId());
                alertDTO.setPartner(project.getOrganizationAcronym());
                alertDTO.setProjectName(project.getName());
                alertDTO.setIndicator(latePerformanceIndicator.getIndicator().getCode() + "-" + latePerformanceIndicator.getIndicator().getDescription() +
                        (latePerformanceIndicator.getIndicator().getCategory() != null ? "( categoría: " + latePerformanceIndicator.getIndicator().getCategory() + ")" : ""
                        ));
                alertDTO.setIndicatorType(latePerformanceIndicator.getIndicatorType().getLabel());
                alertDTO.setLateMonths(lateMonths);
                alerts.add(alertDTO);

            }
        }
        return alerts;
    }

    private List<DIAlertDTO> getDIAlerts(List<IndicatorExecutionWeb> latePerformanceIndicators) throws GeneralAppException{
        List<DIAlertDTO> alerts = new ArrayList<>();
        for (IndicatorExecutionWeb latePerformanceIndicator : latePerformanceIndicators) {
            DIAlertDTO alert = new DIAlertDTO();
            alert.setOffice(latePerformanceIndicator.getReportingOffice().getAcronym());
            alert.setResponsableName(latePerformanceIndicator.getAssignedUser().getName());
            alert.setResponsableEmail(latePerformanceIndicator.getAssignedUser().getEmail());
            alert.setSupervisorName(latePerformanceIndicator.getSupervisorUser().getName());
            alert.setResponsableBackupName(latePerformanceIndicator.getAssignedUserBackup() != null ? latePerformanceIndicator.getAssignedUserBackup().getName():"");
            alert.setIndicator(latePerformanceIndicator.getIndicator().getCode() + "-" + latePerformanceIndicator.getIndicator().getDescription() +
                    (latePerformanceIndicator.getIndicator().getCategory() != null ? "( categoría: " + latePerformanceIndicator.getIndicator().getCategory() + ")" : ""
                    ));
            String lateMonths = latePerformanceIndicator.getQuarters().stream().flatMap(quarterWeb -> quarterWeb.getMonths().stream()).filter(monthWeb -> monthWeb.getLate().equals(TimeStateEnum.LATE) || monthWeb.getLate().equals(TimeStateEnum.SOON_REPORT))
                    .map(MonthWeb::getMonth)
                    .sorted()
                    .map(MonthEnum::getLabel)
                    .collect(Collectors.joining(", "));
            alert.setLateMonths(lateMonths);
            alerts.add(alert);
        }
        return alerts;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class PartnerAlertDTO {
        Long projectId;
        String projectName;
        String partner;
        String indicatorType;
        String indicator;
        String lateMonths;

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getPartner() {
            return partner;
        }

        public void setPartner(String partner) {
            this.partner = partner;
        }

        public String getIndicator() {
            return indicator;
        }

        public void setIndicator(String indicator) {
            this.indicator = indicator;
        }

        public String getLateMonths() {
            return lateMonths;
        }

        public void setLateMonths(String lateMonths) {
            this.lateMonths = lateMonths;
        }

        public String getIndicatorType() {
            return indicatorType;
        }

        public void setIndicatorType(String indicatorType) {
            this.indicatorType = indicatorType;
        }

        public Long getProjectId() {
            return projectId;
        }

        public void setProjectId(Long projectId) {
            this.projectId = projectId;
        }

        @Override
        public String toString() {
            return "PartnerAlertDTO{" +
                    "projectId=" + projectId +
                    ", projectName='" + projectName + '\'' +
                    ", partner='" + partner + '\'' +
                    ", indicatorType='" + indicatorType + '\'' +
                    ", indicator='" + indicator + '\'' +
                    ", lateMonths='" + lateMonths + '\'' +
                    '}';
        }
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class DIAlertDTO {

        String office;
        String indicator;
        String lateMonths;
        String responsableName;
        String responsableEmail;
        String supervisorName;
        String responsableBackupName;


        public String getOffice() {
            return office;
        }

        public void setOffice(String office) {
            this.office = office;
        }

        public String getIndicator() {
            return indicator;
        }

        public void setIndicator(String indicator) {
            this.indicator = indicator;
        }

        public String getLateMonths() {
            return lateMonths;
        }

        public void setLateMonths(String lateMonths) {
            this.lateMonths = lateMonths;
        }

        public String getResponsableName() {
            return responsableName;
        }

        public void setResponsableName(String responsableName) {
            this.responsableName = responsableName;
        }

        public String getResponsableEmail() {
            return responsableEmail;
        }

        public void setResponsableEmail(String responsableEmail) {
            this.responsableEmail = responsableEmail;
        }

        public String getSupervisorName() {
            return supervisorName;
        }

        public void setSupervisorName(String supervisorName) {
            this.supervisorName = supervisorName;
        }

        public String getResponsableBackupName() {
            return responsableBackupName;
        }

        public void setResponsableBackupName(String responsableBackupName) {
            this.responsableBackupName = responsableBackupName;
        }

        @Override
        public String toString() {
            return "DIAlertDTO{" +
                    "office='" + office + '\'' +
                    ", indicator='" + indicator + '\'' +
                    ", lateMonths='" + lateMonths + '\'' +
                    ", responsableName='" + responsableName + '\'' +
                    ", responsableEmail='" + responsableEmail + '\'' +
                    ", supervisorName='" + supervisorName + '\'' +
                    ", responsableBackupName='" + responsableBackupName + '\'' +
                    '}';
        }
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class RMAlertDTO{
        String indicator;
        String code;
        String quarter;
        String populationType;
        String period;

        public String getIndicator() {
            return indicator;
        }

        public void setIndicator(String indicator) {
            this.indicator = indicator;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getQuarter() {
            return quarter;
        }

        public void setQuarter(String quarter) {
            this.quarter = quarter;
        }

        public String getPopulationType() {
            return populationType;
        }

        public void setPopulationType(String populationType) {
            this.populationType = populationType;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        @Override
        public String toString() {
            return "RMAlertDTO{" +
                    "indicator='" + indicator + '\'' +
                    ", code='" + code + '\'' +
                    ", quarter='" + quarter + '\'' +
                    ", populationType='" + populationType + '\'' +
                    ", period='" + period + '\'' +
                    '}';
        }
    }
}
