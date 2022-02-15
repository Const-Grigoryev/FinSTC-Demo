package org.eagleinvsys.test.converters.impl;

import org.eagleinvsys.test.converters.ConvertibleCollection;
import org.eagleinvsys.test.converters.ConvertibleMessage;
import org.eagleinvsys.test.converters.StandardConverter;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StandardCsvConverter implements StandardConverter {

    static ConvertibleMessage adaptConvertibleMessage(Map<String, String> record) {
        return record::get;
    }

    static ConvertibleCollection adaptConvertibleCollection(List<Map<String, String>> records) {
        return new ConvertibleCollection() {
            @Override
            public Collection<String> getHeaders() {
                if (!records.isEmpty()) {
                    return records.get(0).keySet();
                }
                else {
                    return Collections.emptySet();
                }
            }

            @Override
            public Iterable<ConvertibleMessage> getRecords() {
                return () -> records.stream()
                        .map(StandardCsvConverter::adaptConvertibleMessage)
                        .iterator();
            }
        };
    }


    private final CsvConverter csvConverter;

    public StandardCsvConverter(CsvConverter csvConverter) {
        this.csvConverter = csvConverter;
    }

    /**
     * Converts given {@link List<Map>} to CSV and outputs result as a text to the provided {@link OutputStream}
     *
     * @param collectionToConvert collection to convert to CSV format. All maps must have the same set of keys
     * @param outputStream        output stream to write CSV conversion result as text to
     */
    @Override
    public void convert(List<Map<String, String>> collectionToConvert, OutputStream outputStream) {
        csvConverter.convert(adaptConvertibleCollection(collectionToConvert), outputStream);
    }

}