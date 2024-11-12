package org.unhcr.osmosys.model.auditDTOs;

import com.sagatechs.generics.persistence.model.State;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectAuditDTO {
    private Long id;
    private String code;
    private String name;
    private String state;
    private String startDate;
    private String endDate;
    private String organizationId;
    private String organization;
    private List<FocalPointAssignationDTO> focalPointAssignations;
    private String periodId;
    private List<ProjectLocationAssigmentsDTO> projectLocationAssigments;
    private List<IndicatorExecutionDTO> indicatorExecutions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public List<ProjectLocationAssigmentsDTO> getProjectLocationAssigments() {
        return projectLocationAssigments;
    }

    public void setProjectLocationAssigments(List<ProjectLocationAssigmentsDTO> projectLocationAssigments) {
        this.projectLocationAssigments = projectLocationAssigments;
    }

    public List<IndicatorExecutionDTO> getIndicatorExecutions() {
        return indicatorExecutions;
    }

    public void setIndicatorExecutions(List<IndicatorExecutionDTO> indicatorExecutions) {
        this.indicatorExecutions = indicatorExecutions;
    }

    public List<FocalPointAssignationDTO> getFocalPointAssignations() {
        return focalPointAssignations;
    }

    public void setFocalPointAssignations(List<FocalPointAssignationDTO> focalPointAssignations) {
        this.focalPointAssignations = focalPointAssignations;
    }
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }


    public List<LabelValue> toLabelValueList() {
        List<LabelValue> labelValueList = new ArrayList<>();
        labelValueList.add(new LabelValue("ID", String.valueOf(id)));
        labelValueList.add(new LabelValue("Número de Acuerdo", code));
        labelValueList.add(new LabelValue("Nombre del Acuerdo", name));
        labelValueList.add(new LabelValue("Estado", state));
        labelValueList.add(new LabelValue("Fecha de inicio de implementación", startDate));
        labelValueList.add(new LabelValue("Fecha de Fin de Implementación", endDate));
        labelValueList.add(new LabelValue("Socio id", organizationId));
        labelValueList.add(new LabelValue("Socio", organization));
        labelValueList.add(new LabelValue("Año de Implementación", periodId));

        if (focalPointAssignations != null && !focalPointAssignations.isEmpty()) {
            // Ordenar la lista por el ID de FocalPoint
            Collections.sort(focalPointAssignations, new Comparator<FocalPointAssignationDTO>() {
                @Override
                public int compare(FocalPointAssignationDTO o1, FocalPointAssignationDTO o2) {
                    return Long.compare(o1.getId(), o2.getId());
                }
            });


            StringBuilder focalPoints = new StringBuilder();
            focalPoints.append("[");

            for (FocalPointAssignationDTO focalPoint : focalPointAssignations) {
                focalPoints.append(focalPoint.toString()).append(", ");
            }

            // Eliminar la última coma y espacio
            if (focalPoints.length() > 1) { // Comprobar que hay contenido
                focalPoints.setLength(focalPoints.length() - 2); // Eliminar la última coma y espacio
            }

            focalPoints.append("]");
            labelValueList.add(new LabelValue("Puntos Focales", focalPoints.toString()));
        }

        // Concatenar la lista de projectLocationAssigments
        if (projectLocationAssigments != null && !projectLocationAssigments.isEmpty()) {
            StringBuilder locations = new StringBuilder();
            locations.append("[");
            for (ProjectLocationAssigmentsDTO location : projectLocationAssigments) {
                locations.append(location.toString()).append(", ");
            }
            if (locations.length() > 0) {
                locations.setLength(locations.length() - 2);
            }
            locations.append("]");
            labelValueList.add(new LabelValue("Lugares de Ejecución", locations.toString()));
        }

        // Concatenar la lista de indicatorExecutions
        if (indicatorExecutions != null && !indicatorExecutions.isEmpty()) {
            StringBuilder indicators = new StringBuilder();
            indicators.append("[");
            for (IndicatorExecutionDTO indicator : indicatorExecutions) {
                indicators.append(indicator.toString()).append(", ");
            }
            if (indicators.length() > 0) {
                indicators.setLength(indicators.length() - 2);
            }
            indicators.append("]");
            labelValueList.add(new LabelValue("Indicadores de Ejecución", indicators.toString()));
        }

        return labelValueList;
    }
}
