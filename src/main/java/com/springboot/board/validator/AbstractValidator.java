package com.springboot.board.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
public abstract class AbstractValidator<T> implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void validate(Object target, Errors errors){

        try {
            doValidate((T) target, errors);
        }catch(RuntimeException e) {
            log.error("Duplication Validation Error", e);
            throw e;
        }

    }

    protected abstract void doValidate(final T dto, final Errors errors);

}
