package org.eagleinvsys.test.converters.impl;

import org.eagleinvsys.test.converters.ConvertibleMessage;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StandardCsvConverterTests {

    private static void assertEquals(Map<String, String> expected, ConvertibleMessage actual, Collection<String> headers) {
        for (var header : headers) {
            Assert.assertEquals(expected.get(header), actual.getElement(header));
        }
    }

    @Test
    public void adaptConvertibleMessageTest() {
        var headers = Set.of("name", "date_of_birth", "date_of_death", "team");

        var original = Map.of(
                "name", "Lewis Hamilton",
                "date_of_birth", "7 January 1985",
                "team", "Mercedes"
        );

        var adapted = StandardCsvConverter.adaptConvertibleMessage(original);

        assertEquals(original, adapted, headers);
    }

    @Test
    public void adaptConvertibleCollectionTest() {
        var headers = Set.of("name", "date_of_birth", "team");

        var original = List.of(
                Map.of(
                        "name", "Lewis Hamilton",
                        "date_of_birth", "7 January 1985",
                        "team", "Mercedes"
                ),
                Map.of(
                        "name", "Ayrton Senna",
                        "date_of_birth", "21 March 1960",
                        "team", "McLaren"
                )
        );

        var adapted = StandardCsvConverter.adaptConvertibleCollection(original);
        var adaptedRecords = StreamSupport
                .stream(adapted.getRecords().spliterator(), false)
                .collect(Collectors.toUnmodifiableList());

        Assert.assertEquals(headers, adapted.getHeaders());

        Assert.assertEquals(original.size(), adaptedRecords.size());
        assertEquals(original.get(0), adaptedRecords.get(0), headers);
        assertEquals(original.get(1), adaptedRecords.get(1), headers);
    }
}