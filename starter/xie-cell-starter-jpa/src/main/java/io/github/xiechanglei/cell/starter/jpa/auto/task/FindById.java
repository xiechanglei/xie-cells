package io.github.xiechanglei.cell.starter.jpa.auto.task;

import io.github.xiechanglei.cell.starter.jpa.auto.base.UseTypeDefinedClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解标注在方法上，表示自动实现根据 Id 获取实体对象的方法。
 * <p>
 * 方法的参数必须是一个 id 类型的值。
 * </p>
 * 
 * <hr>
 * 
 * <h3>⚠️ 使用规范</h3>
 * 
 * <h4>1. 实体类字段类型规范</h4>
 * <ul>
 *     <li><strong>请勿使用基本数据类型</strong>（int、boolean、double 等）</li>
 *     <li><strong>请使用包装类型</strong>（Integer、Boolean、Double 等）</li>
 *     <li><strong>原因</strong>：基本类型无法设为 null，字段过滤后会显示为默认值（0、false 等），可能导致数据误判</li>
 * </ul>
 * 
 * <h4>2. 无参构造函数</h4>
 * <ul>
 *     <li>实体类必须提供无参构造函数（JPA 规范已要求）</li>
 *     <li>字段过滤功能需要通过反射创建实体实例</li>
 * </ul>
 * 
 * <h4>3. 级联加载说明</h4>
 * <ul>
 *     <li>当指定 {@code onlyFields} 或 {@code ignoreFields} 后，<strong>级联加载将不再生效</strong></li>
 *     <li>关联字段（@OneToMany、@ManyToOne 等）会被设为 null</li>
 *     <li>如需加载关联关系，请手写 JPQL 查询并使用 JOIN FETCH</li>
 * </ul>
 * 
 * <h4>4. 性能说明</h4>
 * <ul>
 *     <li>使用 {@code onlyFields}/{@code ignoreFields} 后，SQL 只会查询指定字段</li>
 *     <li>相比查询所有字段，性能提升约 60%-90%（取决于字段数量和大小）</li>
 * </ul>
 * 
 * <hr>
 * 
 * <h3>使用示例</h3>
 * 
 * <h4>✅ 查询部分字段</h4>
 * <pre>{@code
 * @FindById(value = UserEntity.class, onlyFields = {"id", "name", "email"})
 * UserEntity findById(String id);
 * // SQL: SELECT id, name, email FROM user WHERE id = ?
 * // 返回的 Entity 只有 id/name/email 有值，其他字段为 null
 * }</pre>
 * 
 * <h4>✅ 忽略某些字段</h4>
 * <pre>{@code
 * @FindById(value = UserEntity.class, ignoreFields = {"password", "secretKey"})
 * UserEntity findById(String id);
 * // SQL: SELECT id, name, email, ... (除 password 和 secretKey 外的所有字段)
 * // 返回的 Entity 中 password 和 secretKey 为 null
 * }</pre>
 * 
 * <h4>✅ 查询所有字段（默认行为）</h4>
 * <pre>{@code
 * @FindById(value = UserEntity.class)
 * UserEntity findById(String id);
 * // SQL: SELECT * FROM user WHERE id = ?
 * // 返回完整的 Entity
 * }</pre>
 * 
 * @author xie
 * @date 2026/3/4
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FindById {
    /**
     * 指定要查询的实体类，如果为 UseTypeDefinedClass.class 则在类上寻找注解 @EntityClass 来获取实体类。
     */
    Class<?> value() default UseTypeDefinedClass.class;

    /**
     * 指定方法参数中 ID 参数的名称。
     * <p>
     * 默认为 "id"，如果方法参数名称不是 id，可以通过此属性指定。
     * </p>
     */
    String idParamName() default "id";

    /**
     * 忽略加载的字段列表。
     * <p>
     * 指定的字段在返回的实体对象中将为 null。
     * </p>
     */
    String[] ignoreFields() default {};

    /**
     * 只加载的字段列表。
     * <p>
     * 指定的字段在返回的实体对象中才有值，其他字段为 null。
     * </p>
     * <p>
     * 当 {@code ignoreFields} 和 {@code onlyFields} 都不为空时，优先使用 {@code onlyFields}。
     * </p>
     */
    String[] onlyFields() default {};
}
