package com.omni.app.fs;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LogStorageSystemTest {

    private LogStorageSystem logStorage;

    @BeforeEach
    void setUp() {
        logStorage = new LogStorageSystem();
    }

    @Nested
    class PutTests {

        @Test
        void put_singleLog_stored() {
            logStorage.put(1, "2017:01:01:23:59:59");
            assertEquals(1, logStorage.size());
            assertEquals(1, logStorage.totalLogs());
        }

        @Test
        void put_multipleSameTimestamp_allStored() {
            logStorage.put(1, "2017:01:01:00:00:00");
            logStorage.put(2, "2017:01:01:00:00:00");
            logStorage.put(3, "2017:01:01:00:00:00");

            assertEquals(1, logStorage.size()); // One unique timestamp
            assertEquals(3, logStorage.totalLogs());
        }
    }

    @Nested
    class RetrieveTests {

        @BeforeEach
        void setUpLogs() {
            logStorage.put(1, "2017:01:01:23:59:59");
            logStorage.put(2, "2017:01:01:22:59:59");
            logStorage.put(3, "2016:01:01:00:00:00");
        }

        @Test
        void retrieve_yearGranularity_matchesYear() {
            List<Integer> result = logStorage.retrieve("2016:01:01:01:01:01", "2017:01:01:23:00:00", "Year");

            assertEquals(3, result.size());
            assertTrue(result.containsAll(List.of(1, 2, 3)));
        }

        @Test
        void retrieve_hourGranularity_matchesHour() {
            List<Integer> result = logStorage.retrieve("2017:01:01:23:00:00", "2017:01:01:23:59:59", "Hour");

            assertEquals(1, result.size());
            assertTrue(result.contains(1));
        }

        @Test
        void retrieve_secondGranularity_exactMatch() {
            List<Integer> result = logStorage.retrieve("2017:01:01:23:59:59", "2017:01:01:23:59:59", "Second");

            assertEquals(1, result.size());
            assertTrue(result.contains(1));
        }

        @Test
        void retrieve_noMatch_emptyList() {
            List<Integer> result = logStorage.retrieve("2020:01:01:00:00:00", "2020:12:31:23:59:59", "Year");

            assertTrue(result.isEmpty());
        }

        @Test
        void retrieve_dayGranularity_matchesSameDay() {
            List<Integer> result = logStorage.retrieve("2017:01:01:00:00:00", "2017:01:01:23:59:59", "Day");

            assertEquals(2, result.size());
            assertTrue(result.containsAll(List.of(1, 2)));
        }
    }

    @Nested
    class EdgeCases {

        @Test
        void retrieve_emptyStorage_emptyResult() {
            List<Integer> result = logStorage.retrieve("2017:01:01:00:00:00", "2017:12:31:23:59:59", "Year");

            assertTrue(result.isEmpty());
        }

        @Test
        void put_sameIdMultipleTimes_allStored() {
            logStorage.put(1, "2017:01:01:00:00:00");
            logStorage.put(1, "2017:01:02:00:00:00");

            assertEquals(2, logStorage.size());
            assertEquals(2, logStorage.totalLogs());
        }
    }
}
