package ru.school.retailanalitycs_web_java.utils;

import com.opencsv.ICSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.school.retailanalitycs_web_java.exceptions.csvExceptions.CsvWriteException;

import java.io.Writer;
import java.util.List;

@Slf4j
@Component
public class CsvWriter<T> {
    public void exportCsv(Writer writer, List<T> entities, Class<T> clazz) {
        try {
            MappingStrategy<T> strategy = new MappingStrategy<>();
            strategy.setType(clazz);
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                    .withQuotechar(ICSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator('\t')
                    .withOrderedResults(true)
                    .withMappingStrategy(strategy)
                    .build();
            beanToCsv.write(entities);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CsvWriteException(ex.getMessage());
        }
    }
}
