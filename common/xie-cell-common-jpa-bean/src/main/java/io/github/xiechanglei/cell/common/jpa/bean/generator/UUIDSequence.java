package io.github.xiechanglei.cell.common.jpa.bean.generator;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@IdGeneratorType(UUIDGenerator.class)
@Retention(RUNTIME)
@Target({METHOD, FIELD})
public @interface UUIDSequence {
}
