package com.lotte.danuri.recommend.util;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class FileOut {
    public static void csvFileOut(Class<?> clazz, File csvFile, List<?> dataList) {
        try {
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema csvSchema = csvMapper.enable(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS)
                    .schemaFor(clazz).withColumnSeparator(',').withLineSeparator("\n");
            ObjectWriter writer = csvMapper.writer(csvSchema);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(csvFile),"MS949");
            writer.writeValues(outputStreamWriter).writeAll(dataList);
        } catch (Exception e) {
            System.out.println("파일 생성 실패");
        }
    }
}
