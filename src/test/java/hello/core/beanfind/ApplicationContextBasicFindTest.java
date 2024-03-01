package hello.core.beanfind;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationContextBasicFindTest {
    AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName(){
        MemberService memberService=ac.getBean("memberService", MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("타입으로 조회")
    void findBeanByType(){
        MemberService memberService=ac.getBean(MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    //구체클래스에 의존하게 되므로 바람직하지 않음
    @Test
    @DisplayName("구체 타입으로 조회")
    void findBeanByImpl(){
        MemberServiceImpl memberService=ac.getBean("memberService", MemberServiceImpl.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    //빈 이름이 존재하지 않을 경우
    @Test
    @DisplayName("빈 이름으로 조회X")
    void findBeanByNameX(){
        //람다식 활용
        Assertions.assertThrows(NoSuchBeanDefinitionException.class,
                () -> ac.getBean("noName",MemberService.class));
                
    }
}
