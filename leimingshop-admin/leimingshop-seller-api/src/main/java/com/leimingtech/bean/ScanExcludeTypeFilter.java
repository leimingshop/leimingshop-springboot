/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.bean;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ScanExcludeTypeFilter implements TypeFilter {
    private static final Set<Class> excludeTypeAnnotations = new HashSet<>();

    static {
        excludeTypeAnnotations.add(SpringBootApplication.class);
    }

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //获得当前正在扫描的类的注解信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        boolean ret = excludeTypeAnnotations.stream().anyMatch(t -> annotationMetadata.hasAnnotation(t.getName()));
        return ret;
    }
}
