package br.com.portfolio.component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, LocalDate> {

    protected long minAge;

    @Override
    public void initialize(ValidBirthDate validBirthDate) {
        this.minAge = validBirthDate.value();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if ( date == null ) {
            return true;
        }
        LocalDate today = LocalDate.now();
        return ChronoUnit.YEARS.between(date, today) > minAge;
    }
}
