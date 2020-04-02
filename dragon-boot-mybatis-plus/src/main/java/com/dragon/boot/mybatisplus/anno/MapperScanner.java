package com.dragon.boot.mybatisplus.anno;

import com.dragon.boot.mybatisplus.register.MapperScannerRegistrar;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @ClassName: MapperScanner
 * @Author: pengl
 * @Date: 2019-05-10 13:40
 * @Description: 自定义MapperScanner注解，解决MapperScan使用占位符（动态扫描）功能
 * @Version: 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MapperScannerRegistrar.class)
public @interface MapperScanner {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise
     * annotation declarations e.g.:
     * {@code @EnableMyBatisMapperScanner("org.my.pkg")} instead of
     * {@code @EnableMyBatisMapperScanner(basePackages= "org.my.pkg"})}.
     */
    String[] value() default {};

    /**
     * Base packages to scan for MyBatis interfaces. Note that only interfaces
     * with at least one method will be registered; concrete classes will be
     * ignored.
     */
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the
     * packages to scan for annotated components. The package of each class
     * specified will be scanned.
     * <p>
     * Consider creating a special no-op marker class or interface in each
     * package that serves no purpose other than being referenced by this
     * attribute.
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * The {@link BeanNameGenerator} class to be used for naming detected
     * components within the Spring container.
     */
    Class<? extends BeanNameGenerator> nameGenerator()

            default BeanNameGenerator.class;

    /**
     * This property specifies the annotation that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also
     * have the specified annotation.
     * <p>
     * Note this can be combined with markerInterface.
     */
    Class<? extends Annotation> annotationClass()

            default Annotation.class;

    /**
     * This property specifies the parent that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also
     * have the specified interface class as a parent.
     * <p>
     * Note this can be combined with annotationClass.
     */
    Class<?> markerInterface()

            default Class.class;

    /**
     * Specifies which {@code SqlSessionTemplate} to use in the case that there
     * is more than one in the spring context. Usually this is only needed when
     * you have more than one datasource.
     */
    String sqlSessionTemplateRef()

            default "";

    /**
     * Specifies which {@code SqlSessionFactory} to use in the case that there
     * is more than one in the spring context. Usually this is only needed when
     * you have more than one datasource.
     */
    String sqlSessionFactoryRef() default "";
}
