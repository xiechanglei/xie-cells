package io.github.xiechanglei.cell.common.lang.string;

import lombok.AllArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * String的空值处理，
 * 代码中经常会遇到String的空值处理，如果为空则执行某些操作，如果不为空则执行某些操作，
 * 为了避免每次都要判断是否为空，可以使用StringOptional来处理
 *
 *
 */
@AllArgsConstructor
public class StringOptional {
    private String value;

    // =================构建StringOptional对象=================
    /**
     * 构建StringOptional对象
     * @param value 值
     * @return StringOptional对象
     */
    public static StringOptional of(String value) {
        return new StringOptional(value);
    }

    /**
     * 如果值为空，使用新值构建StringOptional对象
     */
    public StringOptional or(String defaultValue) {
        if (StringHelper.isBlank(value)) {
            this.value = defaultValue;
        }
        return this;
    }


    /**
     * 如果值为空，使用新的supplier构建StringOptional对象
     */
    public StringOptional orWith(Supplier<String> supplier) {
        if (!StringHelper.isNotBlank(value)) {
            return new StringOptional(supplier.get());
        }
        return this;
    }

    // =================依赖StringOptional对象中的值进行一些操作=================
    /**
     * 如果值不为空，则执行consumer
     */
    public void ifPresent(Consumer<String> consumer) {
        if (StringHelper.isNotBlank(value)) {
            consumer.accept(value);
        }
    }

    /**
     * 如果值为空，则执行consumer
     */
    public void ifNotPresent(Consumer<String> consumer) {
        if (StringHelper.isBlank(value)) {
            consumer.accept(value);
        }
    }


    // =================获取StringOptional对象中的值=================
    /**
     * 获取当前String对象
     */
    public String get() {
        return value;
    }

    /**
     * 判断当前String对象的值是否为空
     * @return 是否为空
     */
    public boolean isEmpty() {
        return StringHelper.isBlank(value);
    }


    /**
     * orThrow
     *
     */
    public <X extends Throwable> StringOptional orElseThrow(X e) throws X {
        if (StringHelper.isNotBlank(value)) {
            return this;
        } else {
            throw e;
        }
    }

}
