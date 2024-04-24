package com.example.sbjconvert.validator;

import com.example.sbjconvert.model.RequestEntry;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

@Component
public class RequestEntryValidator implements Validator {

    private static final List<String> stringFields = List.of(
            "uuid", "id", "name", "likes", "transport"
    );
    private static final List<String> numberFields = List.of(
            "avgSpeed", "topSpeed"
    );

    public boolean supports(@NonNull Class<?> clazz) {
        return RequestEntry.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        var fields = new ArrayList<String>();
        fields.addAll(stringFields);
        fields.addAll(numberFields);
        fields.forEach(field -> {
            ValidationUtils.rejectIfEmpty(errors, field, field + ".empty", field + " must not be empty.");
        });
        numberFields.forEach(field -> {
            var value = (Number) errors.getFieldValue(field);
            if (value != null && value.doubleValue() < 0) {
                errors.rejectValue(field, field + ".negative", field + " must not be negative.");
            }
        });
    }
}
