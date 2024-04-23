package com.example.sbjconvert.service;

import com.example.sbjconvert.exception.UnableToParseException;
import com.example.sbjconvert.model.ResponseEntry;
import com.example.sbjconvert.validator.RequestEntryValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PipeSeparatedValueConverterTest {

    private final PipeSeparatedValueConverter classToTest;

    PipeSeparatedValueConverterTest() {
        classToTest = new PipeSeparatedValueConverter(new RequestEntryValidator());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldConvertPayload(boolean validate) {
        var payload = createCorrectPayload();
        var expected = createExpectedResult();

        var actual = classToTest.convert(payload, validate);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldThrowException() {
        var payload = "invalid|payload";
        assertThatThrownBy(() -> classToTest.convert(payload, true))
                .isInstanceOf(UnableToParseException.class);
    }

    @Test
    void shouldThrowExceptionWhenPayloadIsInvalid() {
        assertThatThrownBy(() -> classToTest.convert(null, true))
                .isInstanceOf(UnableToParseException.class);
    }

    @Test
    void shouldThrowExceptionWhenNumbersAreInvalid() {
        var payload = "uuid1|id1|name1|likes1|transport1|1.abc|def";
        assertThatThrownBy(() -> classToTest.convert(payload, true))
                .isInstanceOf(UnableToParseException.class);
    }

    @Test
    void shouldParseOnlyValidRecordsWhenValidationIsDisabled() {
        var payload = "uuid1|id1|name1|likes1|transport1|1|2.1\ninvalid|record";
        var expected = List.of(new ResponseEntry("name1", "transport1", 2.1));

        var actual = classToTest.convert(payload, false);

        assertThat(actual).isEqualTo(expected);
    }

    private List<ResponseEntry> createExpectedResult() {
        return List.of(
                new ResponseEntry("name1", "transport1", 2d),
                new ResponseEntry("name2", "transport2", 4d)
        );
    }

    private String createCorrectPayload() {
        return "uuid1|id1|name1|likes1|transport1|1|2\nuuid2|id2|name2|likes2|transport2|3|4";
    }
}