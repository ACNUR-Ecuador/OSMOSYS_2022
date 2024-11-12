package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.type.LocalDateType;
import org.unhcr.osmosys.webServices.model.ProjectResumeWeb;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "projects")
@SqlResultSetMapping(
        name = "ProjectResumeWebMapping",
        classes = {
                @ConstructorResult(
                        targetClass = ProjectResumeWeb.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "code", type = String.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "state", type = String.class),
                                @ColumnResult(name = "organizationId", type = Long.class),
                                @ColumnResult(name = "organizationDescription", type = String.class),
                                @ColumnResult(name = "organizationAcronym", type = String.class),
                                @ColumnResult(name = "periodId", type = Long.class),
                                @ColumnResult(name = "periodYear", type = Integer.class),
                                @ColumnResult(name = "startDate", type = LocalDateType.class),
                                @ColumnResult(name = "endDate", type = LocalDateType.class),
                        }
                )
        }
)
@NamedEntityGraph(name = "projectWithData",
        attributeNodes = {
                @NamedAttributeNode("organization"),
                @NamedAttributeNode("focalPoint"),
                @NamedAttributeNode("period"),
                @NamedAttributeNode("projectLocationAssigments"),
        }
)
public class Project extends BaseEntityIdState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "code", unique = true, nullable = false)
    private String code;


    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", foreignKey = @ForeignKey(name = "fk_projet_organization"))
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "focal_point_id", foreignKey = @ForeignKey(name = "fk_project_focal_point"))
    private User focalPoint;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
    private Set<FocalPointAssignation> focalPointAssignations = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "period_id", foreignKey = @ForeignKey(name = "fk_project_period"))
    private Period period;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
    private Set<ProjectLocationAssigment> projectLocationAssigments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
    private Set<IndicatorExecution> indicatorExecutions = new HashSet<>();


    @Override
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Set<ProjectLocationAssigment> getProjectLocationAssigments() {
        return projectLocationAssigments;
    }

    public void setProjectLocationAssigments(Set<ProjectLocationAssigment> projectLocationAssigments) {
        this.projectLocationAssigments = projectLocationAssigments;
    }

    public void addProjectLocationAssigment(ProjectLocationAssigment projectLocationAssigment) {
        projectLocationAssigment.setProject(this);
        if (!this.projectLocationAssigments.add(projectLocationAssigment)) {
            this.projectLocationAssigments.remove(projectLocationAssigment);
            this.projectLocationAssigments.add(projectLocationAssigment);
        }
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<IndicatorExecution> getIndicatorExecutions() {
        return indicatorExecutions;
    }

    public void setIndicatorExecutions(Set<IndicatorExecution> indicatorExecutions) {
        this.indicatorExecutions = indicatorExecutions;
    }

    public void addIndicatorExecution(IndicatorExecution indicatorExecution) {
        indicatorExecution.setProject(this);
        if (!this.indicatorExecutions.add(indicatorExecution)) {
            this.indicatorExecutions.remove(indicatorExecution);
            this.indicatorExecutions.add(indicatorExecution);
        }
    }

    public User getFocalPoint() {
        return focalPoint;
    }

    public void setFocalPoint(User focalPoint) {
        this.focalPoint = focalPoint;
    }

    public Project deepCopy() {
        Project copy = new Project();
        copy.setId(this.id);
        copy.setOrganization(this.organization); // Si es una entidad compleja, considera también copiar
        copy.setCode(this.code);
        copy.setPeriod(this.period);
        copy.setState(this.state);
        copy.setName(this.name);
        copy.setFocalPoint(this.focalPoint); // Igual que arriba, verifica si necesitas una copia
        copy.setStartDate(this.startDate);
        copy.setEndDate(this.endDate);
        copy.setIndicatorExecutions(this.indicatorExecutions);
        copy.setProjectLocationAssigments(this.projectLocationAssigments);

        return copy;
    public Set<FocalPointAssignation> getFocalPointAssignations() {
        return focalPointAssignations;
    }

    public void setFocalPointAssignations(Set<FocalPointAssignation> focalPointAssignations) {
        this.focalPointAssignations = focalPointAssignations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        return new EqualsBuilder().append(id, project.id).append(code, project.code).append(organization, project.organization).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(code).append(organization).toHashCode();
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", organization=" + organization +
                ", period=" + period +
                '}';
    }


}
