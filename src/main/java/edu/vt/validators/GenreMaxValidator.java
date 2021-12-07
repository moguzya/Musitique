package edu.vt.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.List;

@FacesValidator("genreMaxValidator")
public class GenreMaxValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        List<String> selectedItemscheckbox = (List<String>) value;

        if (selectedItemscheckbox.size() > 5) {
            throw new ValidatorException(new FacesMessage("Max 5 genres allowed"));
        }
    }

}