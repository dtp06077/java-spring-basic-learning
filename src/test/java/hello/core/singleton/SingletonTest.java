package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonTest {

    @Test
    @DisplayName("스프링 없는 순수 DI 컨테이너")
    void pureContainer(){
        AppConfig appConfig=new AppConfig();
        //1. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService1= appConfig.memberService();
        //2. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService2= appConfig.memberService();

        //참조값이 다른 것 확인
        System.out.println("memberService1 = "+ memberService1);
        System.out.println("memberService2 = "+ memberService2);

        //memberService1 != memberService2
        assertThat(memberService1).isNotSameAs(memberService2);
    }

    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용")
    void singletonServiceTest(){
        //private으로 막아놔서 컴파일 오류 발생
        //new SingletonService();
        //1. 조회: 호출할 때 마다 같은 객체를 생성
        SingletonService singletonService1=SingletonService.getInstance();
        //2. 조회: 호출할 때 마다 같은 객체를 생성
        SingletonService singletonService2=SingletonService.getInstance();

        //참조값이 같은 것 확인
        System.out.println("singletonService1 = " + singletonService1);
        System.out.println("singletonService2 = " + singletonService2);

        //singletonService1 == singletonService2
        assertThat(singletonService1).isSameAs(singletonService2);

        singletonService1.logic();
        singletonService2.logic();
    }

    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springContainer(){
        ApplicationContext ac=new AnnotationConfigApplicationContext(AppConfig.class);
        //1. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService1=ac.getBean(MemberService.class);
        //2. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService2=ac.getBean(MemberService.class);

        //참조값이 같은 것 확인
        System.out.println("memberService1 = "+ memberService1);
        System.out.println("memberService2 = "+ memberService2);

        //memberService1 == memberService2
        assertThat(memberService1).isSameAs(memberService2);
    }

    @Test
    @DisplayName("상태 유지시 발생하는 오류")
    void statefulError(){

        ApplicationContext ac=new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1=ac.getBean(StatefulService.class);
        StatefulService statefulService2=ac.getBean(StatefulService.class);

        //ThreadA: 김희성 10000원 주문
        int huiseongPrice = statefulService1.order("김희성", 10000);
        //ThreadB: 김보성 20000원 주문
        int boseongPrice = statefulService2.order("김보성", 20000);

        //김희성은 10000원을 주문했지만 20000원이 출력됨 -> 공유 필드(price)로 인한 오류
        System.out.println(huiseongPrice);
        System.out.println(boseongPrice);
//        assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    @Configuration
    static class TestConfig{
        @Bean
        StatefulService statefulService(){
            return new StatefulService();
        }
    }
}
