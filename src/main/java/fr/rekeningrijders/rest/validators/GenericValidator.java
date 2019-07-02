package fr.rekeningrijders.rest.validators;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.xml.ws.WebServiceException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ken
 */
public class GenericValidator<T> {

    private final ValidatorFactory factory;
    private final Validator validator;

    public GenericValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public void validate(T param) {
        Set<ConstraintViolation<T>> violations = validator.validate(param);
        if (!violations.isEmpty()) {
            Set<String> errors = new HashSet<>(violations.size());
            for (ConstraintViolation<T> violation : violations) {
                String message = violation.getMessage();
                errors.add(message);
            }


            throw new WebServiceException();
        }
    }

}

