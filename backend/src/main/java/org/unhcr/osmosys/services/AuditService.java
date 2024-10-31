package org.unhcr.osmosys.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.AuditAction;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.AuditDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.auditDTOs.IndicatorExecutionDTO;
import org.unhcr.osmosys.model.auditDTOs.ProjectAuditDTO;
import org.unhcr.osmosys.model.auditDTOs.ProjectLocationAssigmentsDTO;
import org.unhcr.osmosys.model.cubeDTOs.ProjectDTO;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.webServices.model.AreaWeb;
import org.unhcr.osmosys.webServices.model.AuditWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        if (auditWeb.getRecordId() == null) {
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
            throw new GeneralAppException("Resgistro no válido", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(auditWeb.getNewData())) {
            throw new GeneralAppException("Resgistro no válido", Response.Status.BAD_REQUEST);
        }




    }

    public void logAction(String entity, Long recordId, AuditAction action, User responsibleUser, Object oldData, Object newData, State state) {
        String oldDataJson = convertToJson(convertToProjectAuditDTO((Project) oldData));
        String newDataJson = convertToJson(convertToProjectAuditDTO((Project) newData));

        // Comparar las representaciones en JSON
        if (!oldDataJson.equals(newDataJson)) {
            Audit audit = new Audit();
            audit.setEntity(entity);
            audit.setRecordId(recordId);
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
        projectAuditDTO.setFocalPointId(project.getFocalPoint().getId().toString());
        projectAuditDTO.setPeriodId(project.getPeriod().getId().toString());
        List<ProjectLocationAssigmentsDTO> projectLocationAssigmentsDTOS = project.getProjectLocationAssigments().stream()
                .map(projectLocationAssigment -> {
                    ProjectLocationAssigmentsDTO dto = new ProjectLocationAssigmentsDTO();
                    dto.setId(projectLocationAssigment.getId());
                    dto.setCantonId(projectLocationAssigment.getLocation().getId().toString());
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
                                Long statementId = indicatorExecution.getProjectStatement().getId();
                                dto.setProjectStatementId(statementId != null ? statementId.toString() : "");
                            }
                            if (indicatorExecution.getIndicator() != null) {
                                Long indicatorId = indicatorExecution.getIndicator().getId();
                                dto.setIndicatorId(indicatorId != null ? indicatorId.toString() : "");
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
                                Long periodId = indicatorExecution.getPeriod().getId();
                                dto.setPeriodId(periodId != null ? periodId.toString() : "");
                            }
                            if (indicatorExecution.getTotalExecution() != null) {
                                BigDecimal totalExecution = indicatorExecution.getTotalExecution();
                                dto.setTotalExecution(totalExecution != null ? totalExecution.toString() : "");
                            }
                            if (indicatorExecution.getExecutionPercentage() != null) {
                                BigDecimal execution = indicatorExecution.getExecutionPercentage();
                                dto.setExecutionPercentage(execution != null ? execution.toString() : "");
                            }
                            if (indicatorExecution.getProject() != null) {
                                Long projectId = indicatorExecution.getProject().getId();
                                dto.setProjectId(projectId != null ? projectId.toString() : "");
                            }
                            if (indicatorExecution.getReportingOffice() != null) {
                                Long reportingOfficeId = indicatorExecution.getReportingOffice().getId();
                                dto.setReportingOfficeId(reportingOfficeId != null ? reportingOfficeId.toString() : "");
                            }
                            if (indicatorExecution.getSupervisorUser() != null) {
                                Long supervisorId = indicatorExecution.getSupervisorUser().getId();
                                dto.setSupervisorUserId(supervisorId != null ? supervisorId.toString() : "");
                            }
                            if (indicatorExecution.getAssignedUser() != null) {
                                Long assignedUserId = indicatorExecution.getAssignedUser().getId();
                                dto.setAssignedUserId(assignedUserId != null ? assignedUserId.toString() : "");
                            }
                            if (indicatorExecution.getAssignedUserBackup() != null) {
                                Long assignedUserBackupId = indicatorExecution.getAssignedUserBackup().getId();
                                dto.setAssignedUserBackupId(assignedUserBackupId != null ? assignedUserBackupId.toString() : "");
                            }
                            if (indicatorExecution.getAvailableBudget() != null) {
                                BigDecimal availableBudget = indicatorExecution.getAvailableBudget();
                                dto.setAvailableBudget(availableBudget != null ? availableBudget.toString() : "");
                            }
                            if (indicatorExecution.getTotalUsedBudget() != null) {
                                BigDecimal totalUsedBudget = indicatorExecution.getTotalUsedBudget();
                                dto.setTotalUsedBudget(totalUsedBudget != null ? totalUsedBudget.toString() : "");
                            }
                            return dto;
                        })
                        .collect(Collectors.toList());
        projectAuditDTO.setIndicatorExecutions(indicatorExecutionDTOS);
        return projectAuditDTO;
    }



    private String convertToJson(Object data) {
        if (data == null) {return null;}
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error converting to JSON", e);
        }
    }












}
