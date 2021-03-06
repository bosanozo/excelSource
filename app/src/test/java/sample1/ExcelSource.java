package sample1;

import java.lang.annotation.*;

import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * @ExcelSource
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(ExcelArgumentProvider.class)
@interface ExcelSource {
    /** excel file path */
    String file();
    /** sheet name */
    String sheet();
}
