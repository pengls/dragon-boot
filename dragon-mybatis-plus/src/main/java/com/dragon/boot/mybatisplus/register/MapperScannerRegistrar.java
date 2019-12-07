package com.dragon.boot.mybatisplus.register;

import com.dragon.boot.mybatisplus.anno.MapperScanner;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MapperScannerRegistrar
 * @Author pengl
 * @Date 2019/1/10 15:21
 * @Description 自定义注册mapper扫描器，解决MapperScan使用占位符（动态扫描）功能，支持逗号分割扫描多个包
 * @Version 1.0
 */
public class MapperScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
    private ResourceLoader resourceLoader;
    private Environment env;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        AnnotationAttributes annoAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(MapperScanner.class.getName()));
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }

        Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
        if (!Annotation.class.equals(annotationClass)) {
            scanner.setAnnotationClass(annotationClass);
        }

        Class<?> markerInterface = annoAttrs.getClass("markerInterface");
        if (!Class.class.equals(markerInterface)) {
            scanner.setMarkerInterface(markerInterface);
        }

        Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            scanner.setBeanNameGenerator(BeanUtils.instantiateClass(generatorClass));
        }

        scanner.setSqlSessionTemplateBeanName(annoAttrs.getString("sqlSessionTemplateRef"));
        scanner.setSqlSessionFactoryBeanName(annoAttrs.getString("sqlSessionFactoryRef"));

        List<String> basePackages = new ArrayList<>();
        for (String pkg : annoAttrs.getStringArray("value")) {
            if (StringUtils.hasText(pkg)) {
                addBasePackages(parsePlaceHolder(pkg), basePackages);
            }
        }
        for (String pkg : annoAttrs.getStringArray("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                addBasePackages(parsePlaceHolder(pkg), basePackages);
            }
        }
        for (Class<?> clazz : annoAttrs.getClassArray("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));

    }

    private void addBasePackages(String pkg, List<String> basePackages){
        String placeStr = parsePlaceHolder(pkg);
        if(!StringUtils.hasText(placeStr)){
            return;
        }
        String[] pks = placeStr.split(",");
        for(String pk : pks){
            if(StringUtils.hasText(pk)){
                basePackages.add(pk);
            }
        }
    }

    private String parsePlaceHolder(String pro) {
        if (pro != null && pro.contains(PropertySourcesPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX)) {
            String value = env.getProperty(pro.substring(2, pro.length() - 1));

            if (null == value) {
                throw new IllegalArgumentException("property " + pro + " can not find!!!");
            }
            return value;
        }
        return pro;
    }

    @Override
    public void setResourceLoader(ResourceLoader loader) {
        this.resourceLoader = loader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}
