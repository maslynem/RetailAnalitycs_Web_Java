package ru.school.retailanalitycs_web_java.utils;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.school.retailanalitycs_web_java.exceptions.csvExceptions.CsvReadException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Slf4j
@Component
public class CsvReader<T> {
    public List<T> importCsv(InputStream inputStream, Class<T> clazz) {
        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            MappingStrategy<T> strategy = new MappingStrategy<>();
            strategy.setType(clazz);

            return new CsvToBeanBuilder<T>(reader)
                    .withSkipLines(1)
                    .withMappingStrategy(strategy)
                    .withSeparator('\t')
                    .withType(clazz)
                    .build()
                    .parse();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CsvReadException(e.getMessage());
        }
    }
}
