package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import org.unhcr.osmosys.model.reportDTOs.IndicatorExecutionDetailedDTO;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(schema = "public", name = "helper")
@SqlResultSetMapping(
        name = "IndicatorExecutionDetailedMapping",
        classes = {
                @ConstructorResult(
                        targetClass = IndicatorExecutionDetailedDTO.class,
                        columns = {
                                @ColumnResult(name = "ie_id", type = Long.class),
                                @ColumnResult(name = "implementation_type", type = String.class),
                                @ColumnResult(name = "area", type = String.class),
                                @ColumnResult(name = "statement", type = String.class),
                                @ColumnResult(name = "statement_project", type = String.class),
                                @ColumnResult(name = "indicator_type", type = String.class),
                                @ColumnResult(name = "indicator", type = String.class),
                                @ColumnResult(name = "category", type = String.class),
                                @ColumnResult(name = "frecuency", type = String.class),
                                @ColumnResult(name = "implementers", type = String.class),
                                @ColumnResult(name = "total_execution", type = BigDecimal.class),
                                @ColumnResult(name = "target", type = BigDecimal.class),
                                @ColumnResult(name = "execution_percentage", type = BigDecimal.class),
                                @ColumnResult(name = "quarter_order", type = Integer.class),
                                @ColumnResult(name = "quarter", type = String.class),
                                @ColumnResult(name = "quarter_execution", type = BigDecimal.class),
                                @ColumnResult(name = "quarter_target", type = BigDecimal.class),
                                @ColumnResult(name = "quarter_percentage", type = BigDecimal.class),
                                @ColumnResult(name = "month_order", type = String.class),
                                @ColumnResult(name = "month", type = String.class),
                                @ColumnResult(name = "month_execution", type = BigDecimal.class),
                                @ColumnResult(name = "iv_id", type = Long.class),
                                @ColumnResult(name = "ivc_id", type = Long.class),
                                @ColumnResult(name = "dissagregation_type", type = String.class),
                                @ColumnResult(name = "dissagregation_level1", type = String.class),
                                @ColumnResult(name = "dissagregation_level2", type = String.class),
                                @ColumnResult(name = "value", type = BigDecimal.class),
                        }
                )
        }
)
public class Helper extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
