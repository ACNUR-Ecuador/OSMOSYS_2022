package org.unhcr.osmosys.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.AuditAction;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.AuditDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.auditDTOs.*;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.webServices.model.AuditWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class AuditService {
    @Inject
    AuditDao auditDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(AuditService.class);

    public Audit getById(Long id) {
        return this.auditDao.find(id);
    }

    public Audit saveOrUpdate(Audit audit) {
        if (audit.getId() == null) {
            this.auditDao.save(audit);
        } else {
            this.auditDao.update(audit);
        }
        return audit;
    }

    public Long save(AuditWeb auditWeb) throws GeneralAppException {
        if (auditWeb == null) {
            throw new GeneralAppException("No se puede guardar un audit null", Response.Status.BAD_REQUEST);
        }
        if (auditWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un audit con id", Response.Status.BAD_REQUEST);
        }
        this.validate(auditWeb);
        Audit audit = this.saveOrUpdate(this.modelWebTransformationService.auditWebToAudit(auditWeb));
        return audit.getId();
    }

    public List<AuditWeb> getAll() {
        return this.modelWebTransformationService.auditsToAuditsWeb(this.auditDao.findAll());
    }

    public List<AuditWeb> getByState(State state) {
        return this.modelWebTransformationService.auditsToAuditsWeb(this.auditDao.getByState(state));
    }

    public Long update(AuditWeb auditWeb) throws GeneralAppException {
        if (auditWeb == null) {
            throw new GeneralAppException("No se puede actualizar un audit null", Response.Status.BAD_REQUEST);
        }
        if (auditWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un audit sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(auditWeb);
        Audit audit = this.saveOrUpdate(this.modelWebTransformationService.auditWebToAudit(auditWeb));
        return audit.getId();
    }

    public void validate(AuditWeb auditWeb) throws GeneralAppException {
        if (auditWeb == null) {
            throw new GeneralAppException("Audit es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(auditWeb.getEntity())) {
            throw new GeneralAppException("Entidad no válido", Response.Status.BAD_REQUEST);
        }
        if (auditWeb.getProjectCode() == null) {
            throw new GeneralAppException("Registro id no válido", Response.Status.BAD_REQUEST);
        }
        if (auditWeb.getAction() == null) {
            throw new GeneralAppException("Acción no válida", Response.Status.BAD_REQUEST);
        }
        if (auditWeb.getResponsibleUser() == null) {
            throw new GeneralAppException("Usuario no válido", Response.Status.BAD_REQUEST);
        }
        if (auditWeb.getChangeDate() == null) {
            throw new GeneralAppException("Fecha no válida", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(auditWeb.getOldData())) {
            throw new GeneralAppException("Registro no válido", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(auditWeb.getNewData())) {
            throw new GeneralAppException("Registro no válido", Response.Status.BAD_REQUEST);
        }




    }

    public void logAction(String entity, String projectCode, String indicatorCode, AuditAction action, User responsibleUser, Object oldData, Object newData, State state) {

        Gson gson = new Gson();
        String oldDataJson="";
        String newDataJson="";

        if(entity.equals("Proyecto")){
            if(oldData!=null){
                oldDataJson= gson.toJson(oldData);
            }
            newDataJson= gson.toJson(newData);
        }
        if(entity.equals("Bloqueo de Mes Indicador")){
            if(oldData!=null){
                oldDataJson= convertMapToString((Map)oldData);
            }
            newDataJson= convertMapToString((Map)newData);
        }
        if(entity.equals("Bloqueo de Mes Masivo")){
            if(oldData!=null){
                oldDataJson= convertListToString((List)oldData);
            }
            newDataJson= convertListToString((List)newData);
        }
        if(entity.equals("Reporte")){
            if(oldData!=null){
                oldDataJson=convertListToString((List)oldData);
            }
            newDataJson=convertListToString((List)newData);
        }



        // Comparar las representaciones en JSON
        if (!oldDataJson.equals(newDataJson)) {
            Audit audit = new Audit();
            audit.setEntity(entity);
            audit.setProjectCode(projectCode);
            audit.setIndicatorCode(indicatorCode);
            audit.setAction(action);
            audit.setResponsibleUser(responsibleUser);
            audit.setChangeDate(LocalDateTime.now());
            audit.setOldData(oldDataJson);
            audit.setNewData(newDataJson);
            audit.setState(state);

            auditDao.save(audit);
        }
    }

    public ProjectAuditDTO convertToProjectAuditDTO(Project project) {
        if (project == null) {return null; }
        ProjectAuditDTO projectAuditDTO= new ProjectAuditDTO();
        projectAuditDTO.setId(project.getId());
        projectAuditDTO.setCode(project.getCode());
        projectAuditDTO.setName(project.getName());
        projectAuditDTO.setState(project.getState().toString());
        projectAuditDTO.setStartDate(project.getStartDate().toString());
        projectAuditDTO.setEndDate(project.getEndDate().toString());
        projectAuditDTO.setOrganizationId(project.getOrganization().getId().toString());
        projectAuditDTO.setOrganization(project.getOrganization().getAcronym());
        List<FocalPointAssignationDTO> focalPointAssignationDTOS= project.getFocalPointAssignations().stream()
                        .map(focalPointAssignation -> {
                            FocalPointAssignationDTO dto = new FocalPointAssignationDTO();
                            dto.setId(focalPointAssignation.getId());
                            dto.setFocalPoint(focalPointAssignation.getFocalPointer().getName());
                            dto.setProjectId(focalPointAssignation.getProject().getId().toString());
                            dto.setMainFocalPoint(focalPointAssignation.getMainFocalPointer().toString());
                            dto.setState(focalPointAssignation.getState().toString());
                            return dto;
                        })
                .collect(Collectors.toList());
        projectAuditDTO.setFocalPointAssignations(focalPointAssignationDTOS);
        projectAuditDTO.setPeriodId(project.getPeriod().getYear().toString());
        List<ProjectLocationAssigmentsDTO> projectLocationAssigmentsDTOS = project.getProjectLocationAssigments().stream()
                .map(projectLocationAssigment -> {
                    ProjectLocationAssigmentsDTO dto = new ProjectLocationAssigmentsDTO();
                    dto.setId(projectLocationAssigment.getId());
                    dto.setCantonId(projectLocationAssigment.getLocation().getName());
                    dto.setProjectId(projectLocationAssigment.getProject().getId().toString());
                    dto.setState(projectLocationAssigment.getState().toString());
                    return dto;
                })
                .collect(Collectors.toList());
        projectAuditDTO.setProjectLocationAssigments(projectLocationAssigmentsDTOS);
        List<IndicatorExecutionDTO> indicatorExecutionDTOS = project.getIndicatorExecutions().stream()
                        .map(indicatorExecution -> {
                            IndicatorExecutionDTO dto = new IndicatorExecutionDTO();
                            dto.setId(indicatorExecution.getId());
                            if (indicatorExecution.getActivityDescription() != null) {
                                String activityDescription = indicatorExecution.getActivityDescription();
                                dto.setActivityDescription(activityDescription != null ? activityDescription : "");
                            }
                            if (indicatorExecution.getTarget() != null) {
                                BigDecimal target = indicatorExecution.getTarget();
                                dto.setTarget(target != null ? target.toString() : "");
                            }
                            if (indicatorExecution.getProjectStatement() != null) {
                                String statement = indicatorExecution.getProjectStatement().getDescription();
                                dto.setProjectStatement(statement != null ? statement : "");
                            }
                            if (indicatorExecution.getIndicator() != null) {
                                String indicatorId = indicatorExecution.getIndicator().getDescription();
                                dto.setIndicator(indicatorId != null ? indicatorId : "");
                            }
                            if (indicatorExecution.getCompassIndicator() != null) {
                                Boolean compassIndicator = indicatorExecution.getCompassIndicator();
                                dto.setCompassIndicator(compassIndicator != null ? compassIndicator.toString() : "");
                            }
                            if (indicatorExecution.getIndicatorType() != null) {
                                IndicatorType indicatorType = indicatorExecution.getIndicatorType();
                                dto.setIndicatorType(indicatorType != null ? indicatorType.toString() : "");
                            }
                            if (indicatorExecution.getState() != null) {
                                State state = indicatorExecution.getState();
                                dto.setState(state != null ? indicatorExecution.getState().toString() : "");
                            }
                            if (indicatorExecution.getPeriod() != null) {
                                Integer period = indicatorExecution.getPeriod().getYear();
                                dto.setPeriod(period != null ? period.toString() : "");
                            }
                            if (indicatorExecution.getTotalExecution() != null) {
                                BigDecimal totalExecution = indicatorExecution.getTotalExecution();
                                dto.setTotalExecution(totalExecution != null ? totalExecution.toString() : "");
                            }
                            if (indicatorExecution.getExecutionPercentage() != null) {
                                BigDecimal execution = indicatorExecution.getExecutionPercentage();
                                execution = execution.setScale(2, RoundingMode.HALF_UP);
                                dto.setExecutionPercentage(execution != null ? execution.toString() : "");
                            }
                            if (indicatorExecution.getProject() != null) {
                                Long projectId = indicatorExecution.getProject().getId();
                                dto.setProjectId(projectId != null ? projectId.toString() : "");
                            }
                            if (indicatorExecution.getReportingOffice() != null) {
                                String reportingOffice = indicatorExecution.getReportingOffice().getAcronym();
                                dto.setReportingOffice(reportingOffice != null ? reportingOffice : "");
                            }
                            if (indicatorExecution.getSupervisorUser() != null) {
                                String supervisor = indicatorExecution.getSupervisorUser().getName();
                                dto.setSupervisorUser(supervisor != null ? supervisor : "");
                            }
                            if (indicatorExecution.getAssignedUser() != null) {
                                String assignedUser = indicatorExecution.getAssignedUser().getName();
                                dto.setAssignedUser(assignedUser != null ? assignedUser.toString() : "");
                            }
                            if (indicatorExecution.getAssignedUserBackup() != null) {
                                String assignedUserBackup = indicatorExecution.getAssignedUserBackup().getName();
                                dto.setAssignedUserBackup(assignedUserBackup != null ? assignedUserBackup : "");
                            }
                            if (indicatorExecution.getKeepBudget() != null) {
                                Boolean keepBudget = indicatorExecution.getKeepBudget();
                                dto.setKeepBudget(keepBudget != null ? keepBudget.toString() : "");
                            }
                            if (indicatorExecution.getAssignedBudget() != null) {
                                BigDecimal assignedBudget = indicatorExecution.getAssignedBudget();
                                dto.setAssignedBudget(assignedBudget != null ? assignedBudget.toString() : "");
                            }
                            if (indicatorExecution.getAvailableBudget() != null) {
                                BigDecimal availableBudget = indicatorExecution.getAvailableBudget();
                                dto.setAvailableBudget(availableBudget != null ? availableBudget.toString() : "");
                            }
                            if (indicatorExecution.getTotalUsedBudget() != null) {
                                BigDecimal totalUsedBudget = indicatorExecution.getTotalUsedBudget();
                                dto.setTotalUsedBudget(totalUsedBudget != null ? totalUsedBudget.toString() : "");
                            }
                            if (indicatorExecution.getIndicatorExecutionLocationAssigments() != null) {
                                Set<IndicatorExecutionLocationAssigment> locationAssigments = indicatorExecution.getIndicatorExecutionLocationAssigments();
                                StringBuilder locationString = new StringBuilder();

                                for (IndicatorExecutionLocationAssigment assignment : locationAssigments) {
                                    locationString.append(assignment.getLocation().getName()).append(", ");
                                }

                                if (locationString.length() > 0) {
                                    locationString.setLength(locationString.length() - 2);
                                }

                                dto.setLocationAssigments(locationString.toString());
                            }
                            return dto;
                        })
                        .collect(Collectors.toList());
        projectAuditDTO.setIndicatorExecutions(indicatorExecutionDTOS);
        return projectAuditDTO;
    }




    public String convertListToString(List<Map<String, Object>> list) {

        ObjectMapper objectMapper = new ObjectMapper();

        if (list == null || list.isEmpty()) {
            return "[]";
        }

        List<Map<String, Object>> simplifiedList = list.stream().map(obj -> {

            Map<String, Object> simplifiedMap = objectMapper.convertValue(obj, Map.class);

            simplifiedMap.keySet().forEach(key -> {
                // Si un campo contiene un objeto, puedes reemplazarlo con su valor
                Object value = simplifiedMap.get(key);
                if (value != null && value instanceof Map) {
                    // Si el valor es un objeto anidado, extraemos solo la propiedad necesaria
                    simplifiedMap.put(key, ((Map<?, ?>) value).get("name")); // Ejemplo: solo extraemos el campo "name"
                }
            });
            return simplifiedMap;
        }).collect(Collectors.toList());


        try {
            return objectMapper.writeValueAsString(simplifiedList);
        } catch (Exception e) {
            // Manejo de excepciones en caso de error durante la conversión
            e.printStackTrace();
            return "[]";
        }
    }

    private String convertMapToString(Map<String, Object> map) {
        /*ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convertir el Map a una cadena JSON
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}"; // En caso de error, devolver un objeto vacío
        }*/
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Crear una lista que contenga el map como único objeto
            return objectMapper.writeValueAsString(Collections.singletonList(map));
        } catch (Exception e) {
            e.printStackTrace();
            return "[]"; // En caso de error, devolver un array vacío
        }
    }

    public List<AuditWeb> getAuditsByTableName(String tableName) {
        List<Audit> audits = auditDao.getAuditsByTableName(tableName);
        return modelWebTransformationService.auditsToAuditsWeb(audits);
    }

    public List<AuditWeb> getAuditsByTableNamePeriodAndMonth(String tableName, int year, int month) {
        List<Audit> audits = auditDao.getAuditsByTableNamePeriodAndMonth(tableName, year, month);
        return modelWebTransformationService.auditsToAuditsWeb(audits);
    }













}
