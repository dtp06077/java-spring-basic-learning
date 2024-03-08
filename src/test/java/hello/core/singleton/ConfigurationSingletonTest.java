package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationSingletonTest {

    @Test
    @DisplayName("Configuration 어노테이션 싱글톤 패턴 테스트")
    void configurationTest(){
        ApplicationContext ac=new AnnotationConfigApplicationContext(AppConfig.class);
        MemberServiceImpl memberService=ac.getBean(MemberServiceImpl.class);
        OrderServiceImpl orderService=ac.getBean(OrderServiceImpl.class);
        MemberRepository memberRepository=ac.getBean(MemberRepository.class);

        //모두 같은 인스턴스를 참고하고 있다. -> @Configuration은 싱글톤 패턴을 적용함
        System.out.println(memberService.getMemberRepository());
        System.out.println(orderService.getMemberRepository());
        System.out.println(memberRepository);

        //동일 인스턴스인지 확인 테스트
        assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);
    }

    @Test
    @DisplayName("CGLIB 라이브러리 확인")
    void configurationDeep(){
        ApplicationContext ac=new AnnotationConfigApplicationContext(AppConfig.class);
        //AppCofig도 스프링 빈으로 등록됨.
        AppConfig appConfig=ac.getBean(AppConfig.class);

        //class hello.core.AppConfig를 예상했지만 xxxCGLIB이 출력되는 것을 확인
        System.out.println("appConfig = "+appConfig.getClass());
        //AppConfig@CGLIB은 AppConfig의 자식타입으로, AppConfig 타입으로 조회 가능
    }
}
