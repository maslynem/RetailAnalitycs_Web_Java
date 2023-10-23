package ru.school.retailanalitycs_web_java.utils;

import com.opencsv.ICSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

@Slf4j
@Component
public class CsvWriter<T> {
    public void exportCsv(OutputStream outputStream, List<T> entities, Class<T> clazz) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            MappingStrategy<T> strategy = new MappingStrategy<>();
            strategy.setType(clazz);
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                    .withQuotechar(ICSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(ICSVWriter.DEFAULT_SEPARATOR)
                    .withOrderedResults(true)
                    .withMappingStrategy(strategy)
                    .build();

            beanToCsv.write(entities);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
