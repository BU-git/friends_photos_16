package com.bionic.fp.util;

import com.bionic.fp.domain.Coordinate;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static com.bionic.fp.util.GeoUtils.DistanceUnitPerDegree.KM;
import static com.bionic.fp.util.GeoUtils.getDistance;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

/**
 * Contains unit-tests for {@link GeoUtils} class
 *
 * @author Sergiy Gabriel
 */
public class GeoUtilsTest {

    @Test
    public void testGetDistance() throws Exception {
        float epsilon = 0.003f; // 3m
        float step = 0.1f;      // 100m
        List<Coordinate> coordinates = Stream.of(
                new Coordinate(50.445385, 30.501502),   // ~0
                new Coordinate(50.445173, 30.502908),   // ~100m
                new Coordinate(50.444961, 30.504249),   // ~200m
                new Coordinate(50.444727, 30.505630),   // ~300m
                new Coordinate(50.444507, 30.507001)    // ~400m
        ).sequential().collect(toList());

        for (int i = 1; i < coordinates.size(); i++) {
            assertTrue(getDistance(coordinates.get(i), coordinates.get(i - 1), KM) < step + epsilon);
        }
        for (int i = 0; i < coordinates.size(); i++) {
            assertTrue(getDistance(coordinates.get(0), coordinates.get(i), KM) < (step * i) + epsilon);
        }
        for (int i = coordinates.size() - 1; i >= 0; i--) {
            assertTrue(getDistance(coordinates.get(coordinates.size() - 1), coordinates.get(i), KM) < (step * (coordinates.size() - 1 - i)) + epsilon);
        }
    }
}