package org.unhcr.osmosys.services.scheduledTasks;

import com.sagatechs.generics.appConfiguration.AppConfigurationKey;
import com.sagatechs.generics.appConfiguration.AppConfigurationService;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.service.EmailService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.enums.MonthEnum;
import org.unhcr.osmosys.model.enums.TimeStateEnum;
import org.unhcr.osmosys.services.IndicatorExecutionService;
import org.unhcr.osmosys.services.PeriodService;
import org.unhcr.osmosys.services.ProjectService;
import org.unhcr.osmosys.services.UtilsService;
import org.unhcr.osmosys.webServices.model.IndicatorExecutionWeb;
import org.unhcr.osmosys.webServices.model.MonthWeb;
import org.unhcr.osmosys.webServices.model.ProjectResumeWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
            List<PartnerAlertDTO> alerts = new ArrayList<>();
            // obtengo los proyectos por cada focal point
            LOGGER.info(focalPoint.getName());
            List<ProjectResumeWeb> projects = this.projectService.getProjectResumenWebByPeriodIdAndFocalPointId(currentPeriod.getId(), focalPoint.getId());
            for (ProjectResumeWeb project : projects) {

                LOGGER.info("   " + project.getOrganizationAcronym() + "-" + project.getName());
                List<IndicatorExecutionWeb> generalIndicators = this.indicatorExecutionService.getGeneralIndicatorExecutionsByProjectIdAndState(project.getId(), State.ACTIVO);
                LOGGER.info("       " + "General:" + generalIndicators.size());
                IndicatorExecutionWeb generaIndicator = generalIndicators.get(0);
                LOGGER.info("       " + "General:" + generaIndicator.getLate());
                if (generaIndicator.getLate().equals(TimeStateEnum.LATE) || generaIndicator.getLate().equals(TimeStateEnum.SOON_REPORT)) {
                    PartnerAlertDTO alertDTO = new PartnerAlertDTO();
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
            if (alerts.size() < 1) {
                continue;
            }

            SXSSFWorkbook wb = this.createPartnersAlertAttachment(alerts);
            ByteArrayOutputStream report = this.utilsService.getByteArrayOutputStreamFromWorkbook(wb);


            String copyAddresses = CollectionUtils.isNotEmpty(imProgramsEmail) ? String.join(", ", imProgramsEmail) : null;
            String message = this.getPartnerAlertMessage(focalPoint.getName(), projects.stream().map(ProjectResumeWeb::getOrganizationAcronym).collect(Collectors.joining(", ")));
            this.emailService.sendEmailMessageWithAttachment(focalPoint.getEmail(), copyAddresses, "Retrasos en reporte de indicadores - OSMOSYS", message, report, "reporte_retrasos.xlsx");
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
        // cada supervisor
        for (User supervisor : supervisors) {
            List<IndicatorExecutionWeb> performanceIndicators = this.indicatorExecutionService.getDirectImplementationsIndicatorExecutionsBySupervisorId(currentPeriod.getId(), supervisor.getId());
            List<IndicatorExecutionWeb> latePerformanceIndicators = performanceIndicators.stream()
                    .filter(indicatorExecutionWeb -> indicatorExecutionWeb.getLate().equals(TimeStateEnum.LATE) || indicatorExecutionWeb.getLate().equals(TimeStateEnum.SOON_REPORT))
                    .collect(Collectors.toList());
            if (latePerformanceIndicators.size() < 1) {
                continue;
            }

            List<DIAlertDTO> alerts = new ArrayList<>();
            for (IndicatorExecutionWeb latePerformanceIndicator : latePerformanceIndicators) {
                DIAlertDTO alert = new DIAlertDTO();
                alert.setOffice(latePerformanceIndicator.getReportingOffice().getAcronym());
                alert.setResponsableName(latePerformanceIndicator.getAssignedUser().getName());
                alert.setResponsableEmail(latePerformanceIndicator.getAssignedUser().getEmail());
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
            SXSSFWorkbook wb = this.createDirectImplementationAlertAttachment(alerts);
            ByteArrayOutputStream report = this.utilsService.getByteArrayOutputStreamFromWorkbook(wb);

            List<String> copyAddresses = new ArrayList<>();
            if (imProgramsEmail.size() > 0) {
                copyAddresses.addAll(imProgramsEmail);
            }
            copyAddresses.addAll(alerts.stream().map(DIAlertDTO::getResponsableEmail).distinct().collect(Collectors.toList()));
            String message = this.getDIAlertMessage(supervisor.getName(), alerts.stream().map(DIAlertDTO::getResponsableName).distinct().collect(Collectors.joining(", ")));
            this.emailService.sendEmailMessageWithAttachment(supervisor.getEmail(), copyAddresses.stream().collect(Collectors.joining(", ")), "Retrasos en reporte de indicadores - OSMOSYS", message, report, "reporte_retrasos.xlsx");
            //this.emailService.sendEmailMessageWithAttachment("salazart@unhcr.org", "salazart@unhcr.org", "Retrasos en reporte de indicadores - OSMOSYS", message, report, "reporte_retrasos.xlsx");
        }

    }

    private SXSSFWorkbook createDirectImplementationAlertAttachment(List<DIAlertDTO> alerts) {
        List<Integer> titlesWidth = new ArrayList<>(Arrays.asList(6000, 3000, 6000, 3000, 6000));
        List<String> titles = new ArrayList<>(Arrays.asList("Oficina", "Indicador", "Meses retrasados", "Responsable de reporte"));
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

        Row rowTitle = sheet.createRow(0);
        for (int i = 0; i < titles.size(); i++) {
            Cell cell = rowTitle.createCell(i);
            cell.setCellValue(titles.get(i));
            sheet.setColumnWidth(i, titlesWidth.get(i));
        }

        for (int i = 0; i < alerts.size(); i++) {
            SXSSFRow rowData = sheet.createRow(i + 1);
            DIAlertDTO alert = alerts.get(i);

            Cell cell = rowData.createCell(0);
            cell.setCellValue(alert.getOffice());

            Cell cell1 = rowData.createCell(1);
            cell1.setCellValue(alert.getIndicator());

            Cell cell2 = rowData.createCell(2);
            cell2.setCellValue(alert.getLateMonths());

            Cell cell3 = rowData.createCell(3);
            cell3.setCellValue(alert.getResponsableName());
        }

        return wb;


    }

    private String getPartnerAlertMessage(String focalPointName, String partnersList) {
        return
                "<p style=\"text-align:justify\">Estimado/a " + focalPointName + ":</p>" +
                        "<p style=\"text-align:justify\">" +
                        " Se han encontrado indicadores con retrasos en proyectos de socios en los cuales usted es punto focal: " + partnersList +
                        ". Rogamos su ayuda para poner al d&iacute;a estos datos en el sistema OSMOSYS-ACNUR.  En el archivo adjunto puede encontrar el detalle de los indicadores con retraso.</p>" +
                        "<p style=\"text-align:justify\">Este reporte ha sido generado automaticamente el por el sistema OSMOSYS. En caso de dudas por favor comunicarse con con la Unidad de Programas.</p>"
                ;
    }

    private String getDIAlertMessage(String supervisorName, String responsableList) {
        return
                "<p style=\"text-align:justify\">Estimado/a " + supervisorName + ":</p>" +
                        "<p style=\"text-align:justify\">" +
                        " Se han encontrado indicadores de implementación directa con retrasos para los cuales usted es supervisor/a" +
                        ". Rogamos su ayuda para poner al d&iacute;a estos datos en el sistema OSMOSYS-ACNUR.  En el archivo adjunto puede encontrar el detalle de los indicadores con retraso.</p>" +
                        "<p style=\"text-align:justify\">Los reponsables de reportar estos indicadores son los siguientes colegas: " +
                        responsableList +
                        "</p>" +
                        "<p style=\"text-align:justify\">Este reporte ha sido generado automaticamente el por el sistema OSMOSYS. En caso de dudas por favor comunicarse con con la Unidad de Programas.</p>"
                ;
    }

    private SXSSFWorkbook createPartnersAlertAttachment(List<PartnerAlertDTO> alerts) {
        List<Integer> titlesWidth = new ArrayList<>(Arrays.asList(6000, 3000, 6000, 3000, 6000));
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

        Row rowTitle = sheet.createRow(0);
        for (int i = 0; i < titles.size(); i++) {
            Cell cell = rowTitle.createCell(i);
            cell.setCellValue(titles.get(i));
            sheet.setColumnWidth(i, titlesWidth.get(i));
        }

        for (int i = 0; i < alerts.size(); i++) {
            SXSSFRow rowData = sheet.createRow(i + 1);
            PartnerAlertDTO alert = alerts.get(i);

            Cell cell = rowData.createCell(0);
            cell.setCellValue(alert.getProjectName());

            Cell cell1 = rowData.createCell(1);
            cell1.setCellValue(alert.getPartner());

            Cell cell2 = rowData.createCell(2);
            cell2.setCellValue(alert.getIndicator());

            Cell cell3 = rowData.createCell(3);
            cell3.setCellValue(alert.getIndicatorType());

            Cell cell4 = rowData.createCell(4);
            cell4.setCellValue(alert.getLateMonths());
        }

        return wb;


    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class PartnerAlertDTO {
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

        @Override
        public String toString() {
            return "PartnerAlertDTO{" +
                    "projectName='" + projectName + '\'' +
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

        @Override
        public String toString() {
            return "DIAlertDTO{" +
                    "office='" + office + '\'' +
                    ", indicator='" + indicator + '\'' +
                    ", lateMonths='" + lateMonths + '\'' +
                    ", responsableName='" + responsableName + '\'' +
                    ", responsableEmail='" + responsableEmail + '\'' +
                    '}';
        }
    }
}
