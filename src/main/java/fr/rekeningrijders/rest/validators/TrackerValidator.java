package fr.rekeningrijders.rest.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class TrackerValidator implements ConstraintValidator<TrackerValid, String>
{
    public static final Pattern idRegex = Pattern.compile("^[a-zA-Z]{2}_[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$");

    private TrackerValid annotation;

    public void initialize(TrackerValid annotation)
    {
        this.annotation = annotation;
    }

    public boolean isValid(String trackerId, ConstraintValidatorContext context)
    {
        if (trackerId == null) {
            return annotation.allowNull();
        }
        return idRegex.matcher(trackerId).matches();
    }
}
