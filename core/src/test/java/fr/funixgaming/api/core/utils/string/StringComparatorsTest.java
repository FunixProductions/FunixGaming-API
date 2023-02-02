package fr.funixgaming.api.core.utils.string;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringComparatorsTest {

    @Test
    void testStringsTimingSafeEquals() {
        final String one = "one";
        final String two = "one";

        assertTrue(StringComparators.timingSafeEqual(
                one.getBytes(StandardCharsets.UTF_8),
                two.getBytes(StandardCharsets.UTF_8)
        ));
    }

    @Test
    void testStringsTimingSafeNotEquals() {
        final String one = "one";
        final String two = "two";

        assertFalse(StringComparators.timingSafeEqual(
                one.getBytes(StandardCharsets.UTF_8),
                two.getBytes(StandardCharsets.UTF_8)
        ));
    }

    @Test
    void testStringsTimingSafeNotEquals2() {
        final String one = "one";
        final String two = "";

        assertFalse(StringComparators.timingSafeEqual(
                one.getBytes(StandardCharsets.UTF_8),
                two.getBytes(StandardCharsets.UTF_8)
        ));
    }

}
