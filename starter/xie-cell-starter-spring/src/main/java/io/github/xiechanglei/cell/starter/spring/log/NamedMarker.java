package io.github.xiechanglei.cell.starter.spring.log;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Marker;

import java.util.*;

/**
 * 创建一个基础的Marker实现，自定义的marker可以继承这个类，减少不必要的方法实现
 *
 * @author xie
 * @date 2026/2/12
 */
@Getter
@RequiredArgsConstructor
public class NamedMarker implements Marker {
    private final String name;
    private final List<Marker> references = new ArrayList<>();

    private static final Map<String, NamedMarker> markerCache = new HashMap<>();

    /**
     * 获取一个NamedMarker实例，使用缓存避免重复创建
     * @param name marker的名称
     * @return NamedMarker实例
     */
    public static Marker of(String name) {
        return markerCache.computeIfAbsent(name, NamedMarker::new);
    }

    @Override
    public void add(Marker reference) {
        references.add(reference);
    }

    @Override
    public boolean remove(Marker reference) {
        return references.remove(reference);
    }

    @Deprecated
    @Override
    public boolean hasChildren() {
        return !references.isEmpty();
    }

    @Override
    public boolean hasReferences() {
        return !references.isEmpty();
    }

    @Override
    public Iterator<Marker> iterator() {
        return references.iterator();
    }

    @Override
    public boolean contains(Marker other) {
        if (other == null) {
            return false;
        }
        if (this == other || this.name.equals(other.getName())) {
            return true;
        }
        for (Marker reference : references) {
            if (reference.contains(other)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(String name) {
        if (name == null) {
            return false;
        }
        if (this.name.equals(name)) {
            return true;
        }
        for (Marker reference : references) {
            if (reference.contains(name)) {
                return true;
            }
        }
        return false;
    }
}
