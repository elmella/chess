package service;

import dataaccess.BadRequestException;

import java.lang.reflect.Field;

public class Service {

    public void hasNullFields(Object obj) throws BadRequestException {
        if (obj == null) {
            throw new BadRequestException("Error: bad request");
        }

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(obj) == null) {
                    throw new BadRequestException("Error: bad request");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
