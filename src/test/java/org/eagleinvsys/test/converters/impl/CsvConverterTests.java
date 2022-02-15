package org.eagleinvsys.test.converters.impl;

import org.eagleinvsys.test.converters.ConvertibleCollection;
import org.eagleinvsys.test.converters.ConvertibleMessage;
import org.eagleinvsys.test.converters.impl.CsvConverter;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;


public class CsvConverterTests {

    private static final List<String> headers = List.of("name", "date_of_birth", "team");

    private static final ConvertibleMessage record1 = elementId ->
            switch (elementId) {
                case "name" -> "Lewis Hamilton";
                case "date_of_birth" -> "7 January 1985";
                case "team" -> "Mercedes";
                default -> null;
            };

    private static final ConvertibleMessage record2 = elementId ->
            switch (elementId) {
                case "name" -> "Ayrton Senna";
                case "date_of_birth" -> "21 March 1960";
                case "date_of_death" -> "1 May 1994";
                case "team" -> "McLaren";
                default -> null;
            };


    // Object under test
    private CsvConverter converter = new CsvConverter();


    @Test
    public void convertTest() throws IOException {
        var collection = new ConvertibleCollection() {
            @Override
            public Collection<String> getHeaders() {
                return headers;
            }

            @Override
            public Iterable<ConvertibleMessage> getRecords() {
                return List.of(record1, record2);
            }
        };

        var writer = mock(Writer.class);
        converter.convert(collection, writer);

        verify(writer).write("Lewis Hamilton,7 January 1985,Mercedes\n");
        verify(writer).write("Ayrton Senna,21 March 1960,McLaren\n");
    }
}