package com.clau.util;

import com.clau.annotation.NotBlank;
import com.clau.annotation.NotNull;
import com.clau.annotation.NotPast;
import com.clau.annotation.Size;
import com.clau.exception.BadRequestException;

import java.lang.reflect.Field;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Date;

public class ValidatorUtil {
  public void validateObject(Object obj) throws Exception {
    if (obj == null) {
      return;
    }

    Method[] methods = obj.getClass().getMethods();

    for (Method method : methods) {
      if (MethodUtil.isGetter(method)) {
        Object value = method.invoke(obj);

        String fieldName = MethodUtil.getFieldNameFromGetter(method.getName());
        Field field = null;

        try {
          field = obj.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
          continue;
        }

        if (field.isAnnotationPresent(NotNull.class)) {
          NotNull notNull = field.getAnnotation(NotNull.class);
          if (value == null) {
            throw new BadRequestException(notNull.message());
          }
        }

        if(field.isAnnotationPresent(NotPast.class)){
          NotPast notPast = field.getAnnotation(NotPast.class);
          if (value instanceof LocalDateTime) {
            LocalDateTime dateValue = (LocalDateTime) value;
            if (dateValue.isBefore(LocalDateTime.now())) {
              throw new BadRequestException(notPast.message());
            }
          } else if (value instanceof Date) {
            Date dateValue = (Date) value;
            if (dateValue.before(new Date())) {
              throw new BadRequestException(notPast.message());
            }
          } else {
            throw new BadRequestException("O campo " + fieldName + " deve ser do tipo LocalDateTime ou Date.");
          }
        }

        if (field.isAnnotationPresent(NotBlank.class)) {
          NotBlank notBlank = field.getAnnotation(NotBlank.class);
          if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
            throw new BadRequestException(notBlank.message());
          }
        }
        if (field.isAnnotationPresent(Size.class)) {
          Size size = field.getAnnotation(Size.class);
          if (value instanceof String) {
            String strValue = (String) value;
            if (strValue.length() < size.min() || strValue.length() > size.max()) {
              throw new BadRequestException("O campo " + fieldName + " deve ter entre " + size.min() + " e " + size.max() + " caracteres.");
            }
          } else if (value instanceof Number) {
            Number numValue = (Number) value;
            if (numValue.intValue() < size.min() || numValue.intValue() > size.max()) {
              throw new BadRequestException("O campo " + fieldName + " deve estar entre " + size.min() + " e " + size.max() + ".");
            }
          }
        }
      }
    }
  }

}
