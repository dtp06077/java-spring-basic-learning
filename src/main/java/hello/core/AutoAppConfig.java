package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.springframework.context.annotation.ComponentScan.*;

@Configuration
@ComponentScan(
//        탐색할 패키지의 시작 위치 지정, 아래 코드는 member패키지만 스캔
//        basePackages="hello.core.Member",
        excludeFilters = @Filter(type= FilterType.ANNOTATION, classes=
            Configuration.class))
public class AutoAppConfig {

}

