package ru.s21school.retailanalytics_web.utils;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByName;

class MappingStrategy<T> extends ColumnPositionMappingStrategy<T> {

    @Override
    public String[] generateHeader(T bean) {
        final int numColumns = getFieldMap().values().size();

        String[] header = new String[numColumns];
        super.setColumnMapping(header);
        for (int i = 0; i < numColumns; i++) {
            header[i] = findField(i).getField().getDeclaredAnnotation(CsvBindByName.class).column();
        }

        return header;
    }
}
