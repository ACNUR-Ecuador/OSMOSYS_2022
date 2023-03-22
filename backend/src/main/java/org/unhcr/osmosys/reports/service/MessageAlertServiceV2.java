package org.unhcr.osmosys.reports.service;

import com.sagatechs.generics.appConfiguration.AppConfigurationService;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.service.EmailService;
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
    UserService userService;
    @Inject
    IndicatorExecutionService indicatorExecutionService;


    public void sendPartnersAlertsToFocalPoints() throws GeneralAppException {
        Period currentPeriod = this.periodService.getByYear(this.utilsService.getCurrentYear());
        Integer currentMonthYearOrder = this.utilsService.getCurrentMonthYearOrder();
        // obtengo los focal points de este periodo
        List<User> focalPoints = this.projectService.getFocalPointByPeriodId(currentPeriod.getId());

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
                List<IndicatorExecutionWeb> latePerformanceIndicators = performanceIndicators.stream().filter(indicatorExecutionWeb -> {
                    return indicatorExecutionWeb.getLate().equals(TimeStateEnum.LATE) || indicatorExecutionWeb.getLate().equals(TimeStateEnum.SOON_REPORT);
                }).collect(Collectors.toList());

                LOGGER.info("       " + "LatePerformance:" + latePerformanceIndicators.size());
                for (IndicatorExecutionWeb latePerformanceIndicator : latePerformanceIndicators) {

                    String lateMonths = latePerformanceIndicator.getQuarters().stream().flatMap(quarterWeb -> quarterWeb.getMonths().stream()).filter(monthWeb -> monthWeb.getLate().equals(TimeStateEnum.LATE) || monthWeb.getLate().equals(TimeStateEnum.SOON_REPORT))
                            .map(MonthWeb::getMonth)
                            .sorted()
                            .map(MonthEnum::getLabel)
                            .collect(Collectors.joining(", "));
                    LOGGER.info("          " + "LatePerformance:" + latePerformanceIndicator.getIndicator().getCode() + lateMonths);
                    PartnerAlertDTO alertDTO = new PartnerAlertDTO();
                    alertDTO.setPartner(project.getOrganizationAcronym());
                    alertDTO.setProjectName(project.getName());
                    alertDTO.setIndicator(latePerformanceIndicator.getIndicator().getCode() + "-" + latePerformanceIndicator.getIndicator().getDescription() +
                            latePerformanceIndicator.getIndicator().getCategory() != null ? "( categoría: " + latePerformanceIndicator.getIndicator().getCategory() + ")" : "");
                    alertDTO.setIndicatorType(latePerformanceIndicator.getIndicatorType().getLabel());
                    alertDTO.setLateMonths(lateMonths);
                    alerts.add(alertDTO);

                }


            }

            SXSSFWorkbook wb = this.createPartnersAlertAttachment(alerts);

            this.emailService.sendEmailMessage("salazart@unhcr.org","salazart@unhcr.org_1;salazart@unhcr.org_2","test","message");

        }


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
}
