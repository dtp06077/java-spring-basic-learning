package hello.core.lifecycle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    @Test
    @DisplayName("빈 생명주기 테스트")
    public void lifeCycleTest() {
        ConfigurableApplicationContext ac=new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient networkClient=ac.getBean(NetworkClient.class);
        ac.close();
    }

    @Configuration
    static class LifeCycleConfig{

        @Bean(initMethod = "init", destroyMethod = "close")
        //종료 메서드 추론을 통해 메서드 명이 "close"이면 destroyMethod 생략 가능
        public NetworkClient networkClient(){
            NetworkClient networkClient=new NetworkClient();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }
    }
}
