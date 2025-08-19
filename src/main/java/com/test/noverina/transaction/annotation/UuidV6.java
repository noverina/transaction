package com.test.noverina.transaction.annotation;

import com.test.noverina.transaction.util.UuidV6Generator;
import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(FIELD)
@IdGeneratorType(UuidV6Generator.class)
public @interface UuidV6 {
}
