package io.github.williamandradesantana.sports.domain.audit;

import io.github.williamandradesantana.sports.domain.audit.exceptions.InvalidAccessLogException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccessLogTest {


    @Test
    @DisplayName("Test: a successful access log should have a userId and no failure reason")
    void test_SuccessfulAccessLog_ShouldHaveUserIdAndNoFailureReason() {
        AccessLog log = AccessLog.successful(
                UUID.randomUUID(), "wbs", "LOCAL", "127.0.0.1", "Mozilla/5.0"
        );

        assertTrue(log.isSuccess());
        assertTrue(log.getUserId().isPresent());
        assertTrue(log.getFailureReason().isEmpty());
    }

    @Test
    @DisplayName("Test: a failed access log should have no userId and a failure reason")
    void test_FailedAccessLog_ShouldHaveNoUserIdAndFailureReason() {
        AccessLog log = AccessLog.failed(
            "unknown-user", "LOCAL", "127.0.0.1",
            "Mozilla/5.0", "Bad credentials"
        );

        assertFalse(log.isSuccess());
        assertTrue(log.getUserId().isEmpty());
        assertEquals("Bad credentials", log.getFailureReason().orElseThrow());
    }

    @Test
    @DisplayName("Test: creating an access log with blank username should throw")
    void test_CreatingAccessLogWithBlankUsername_ShouldThrow() {
        assertThrows(InvalidAccessLogException.class, () ->
                AccessLog.failed(
                    "", "LOCAL", "127.0.0.1",
                    "Mozilla/5.0", "Bad credentials"
                )
        );
    }
}