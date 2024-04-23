package com.example.sbjconvert.validator;

import com.example.sbjconvert.model.RequestEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.*;

class RequestEntryValidatorTest {

    private RequestEntryValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RequestEntryValidator();
    }

    @Test
    void shouldSupportOnlyRequestEntryClass() {
        assertTrue(validator.supports(RequestEntry.class));
        assertFalse(validator.supports(Object.class));
    }

    @Test
    void shouldPassValidationWhenAllFieldsAreCorrect() {
        RequestEntry entry = new RequestEntry(
                "uuid",
                "id",
                "name",
                "likes",
                "transport",
                1.11,
                2.22
        );

        Errors errors = new BindException(entry, "entry");
        validator.validate(entry, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    void shouldFailValidationWhenStringFieldsAreEmpty() {
        RequestEntry entry = new RequestEntry(
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Errors errors = new BindException(entry, "entry");
        validator.validate(entry, errors);

        assertTrue(errors.hasErrors());
        assertEquals(7, errors.getErrorCount());
    }

    @Test
    void shouldFailValidationWhenNumberFieldsAreNegative() {
        RequestEntry entry = new RequestEntry(
                "uuid",
                "id",
                "name",
                "likes",
                "transport",
                -1.11,
                -2.22
        );

        Errors errors = new BindException(entry, "entry");
        validator.validate(entry, errors);

        assertTrue(errors.hasErrors());
        assertEquals(2, errors.getErrorCount());
    }
}