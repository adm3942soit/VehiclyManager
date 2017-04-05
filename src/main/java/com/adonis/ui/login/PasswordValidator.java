package com.adonis.ui.login;

import com.adonis.data.persons.Person;
import com.adonis.data.persons.service.PersonService;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.AbstractValidator;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by oksdud on 28.03.2017.
 */
public class PasswordValidator extends AbstractValidator<String> {
        String userName;
        PersonService personService;

        public PasswordValidator(PersonService personService, String userName) {
            super("The password provided is not valid");
            this.userName = userName;
            this.personService = personService;
        }


    protected boolean isValidValue(String value) {
        if (value != null
                && (((String)value).length() < 8 || !((String)value).matches(".*\\d.*"))) {
            return false;
        }

        Person person  =  personService.findByCustomerLogin(this.userName);

        if (person==null) return false;

        if(person.getPassword().equals(value)) return true;

        return false;
    }

    @Override
    public ValidationResult apply(String value, ValueContext context) {
           if(isValidValue(value)) return ValidationResult.ok();
           else return ValidationResult.error("User with login "+userName +" and password "+value+" not found!");

    }

    @Override
    public <V> BiFunction<String, ValueContext, V> andThen(Function<? super ValidationResult, ? extends V> after) {
        return null;
    }
}
