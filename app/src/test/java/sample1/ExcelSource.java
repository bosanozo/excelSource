package sample1;

import java.lang.annotation.*;

import org.junit.jupiter.params.provider.ArgumentsSource;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(ExcelArgumentProvider.class)
@interface ExcelSource {
    String file();
    String sheet();
}
