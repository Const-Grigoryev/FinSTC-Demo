package org.eagleinvsys.test.converters.impl;

import org.eagleinvsys.test.converters.Converter;
import org.eagleinvsys.test.converters.ConvertibleCollection;
import org.eagleinvsys.test.converters.ConvertibleMessage;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CsvConverter implements Converter {

    private static final Charset OUTPUT_CHARSET = StandardCharsets.UTF_8;

    private static final String FIELD_SEPARATOR = ",";
    private static final String RECORD_SEPARATOR = "\n";
    private static final String NULL_VALUE_PLACEHOLDER = "";

    void convert(ConvertibleCollection collectionToConvert, Writer writer) {
        var headers = collectionToConvert.getHeaders();

        try {
            for (var record : collectionToConvert.getRecords()) {
                var line = headers.stream()
                        .map(record::getElement)
                        .map(v -> v != null ? v : NULL_VALUE_PLACEHOLDER)
                        .collect(Collectors.joining(FIELD_SEPARATOR, "", RECORD_SEPARATOR));
                writer.write(line);
            }
            writer.flush();     // Not `close`, because we do not want to the underlying `outputStream` be closed
        }
        catch (IOException ex) {
            throw new UncheckedIOException("Failed to write CSV", ex);
        }
    }

    /**
     * Converts given {@link ConvertibleCollection} to CSV and outputs result as a text to the provided {@link OutputStream}
     *
     * @param collectionToConvert collection to convert to CSV format
     * @param outputStream        output stream to write CSV conversion result as text to
     */
    @Override
    public void convert(ConvertibleCollection collectionToConvert, OutputStream outputStream) {
        convert(collectionToConvert, new OutputStreamWriter(outputStream, OUTPUT_CHARSET));
    }
}