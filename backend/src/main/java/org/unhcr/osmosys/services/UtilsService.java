package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.enums.TotalIndicatorCalculationType;

import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;

@Stateless
public class UtilsService {


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(UtilsService.class);

    public BigDecimal calculetTotalExecution(TotalIndicatorCalculationType totalIndicatorCalculationType, List<BigDecimal> values) throws GeneralAppException {
        BigDecimal totalExecution;
        if (CollectionUtils.isEmpty(values)) {
            return null;

        } else {
            //noinspection DuplicatedCode
            switch (totalIndicatorCalculationType) {
                //TODO revisar otros valores desde mes
                case SUMA:
                    totalExecution = values.stream().reduce(BigDecimal::add).orElse(null);
                    break;
                case PROMEDIO:
                    BigDecimal total = values.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                    totalExecution = total.divide(new BigDecimal(values.size()), RoundingMode.HALF_UP);
                    break;
                case MAXIMO:
                    totalExecution = values.stream().reduce(BigDecimal::max).orElse(null);
                    break;
                case MINIMO:
                    totalExecution = values.stream().reduce(BigDecimal::min).orElse(null);
                    break;
                default:
                    throw new GeneralAppException("Tipo de calculo no soportado, por favor comuniquese con el administrador del sistema", Response.Status.INTERNAL_SERVER_ERROR);
            }
            return totalExecution;
        }
    }

    public Integer getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public Integer getCurrentMonthYearOrder(){
        return  Calendar.getInstance().get(Calendar.MONTH)+1;
    }
}
