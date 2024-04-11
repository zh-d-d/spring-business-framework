package com.dogbody.spring.framework.web.common.constraints;

import org.springframework.util.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 枚举值校验，校验枚举类字段的值是否合法
 *
 * @author zhangdd on 2024/4/11
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValue.Validator.class)
public @interface EnumValue {

    String message() default "类型不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum<?>> enumClass();

    String fieldName() default "code";


    class Validator implements ConstraintValidator<EnumValue, Object> {

        private Class<? extends Enum<?>> enumClass;

        private String fieldName;

        @Override
        public void initialize(EnumValue enumValue) {
            enumClass = enumValue.enumClass();
            fieldName = enumValue.fieldName();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
            //默认对于null值不处理
            if (value == null) {
                return Boolean.TRUE;
            }

            Enum<?>[] enumConstants = enumClass.getEnumConstants();


            try {
                Method method = enumClass.getMethod("get" + StringUtils.capitalize(fieldName));
                boolean flag = Boolean.FALSE;

                for (Enum<?> constant : enumConstants) {
                    Object invoke = method.invoke(constant);
                    if (value.equals(invoke)) {
                        flag = Boolean.TRUE;
                        break;
                    }
                }
                return flag;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
