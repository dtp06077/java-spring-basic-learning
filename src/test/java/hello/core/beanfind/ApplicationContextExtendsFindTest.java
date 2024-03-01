package hello.core.beanfind;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextExtendsFindTest {

    AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("부모 타입으로 조회시, 자식이 둘 이상 존재하면 오류 발생")
    void findBeanByParentTypeDuplicate(){
        //DiscountPolicy discountPolicy=ac.getBean(DiscountPolicy.class);
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> ac.getBean(DiscountPolicy.class));
    }

    @Test
    @DisplayName("부모 타입으로 조회시, 자식이 둘 이상 존재하면 빈 이름 지정")
    void findBeanByName(){
        DiscountPolicy discountPolicy=ac.getBean("fixDiscountPolicy", FixDiscountPolicy.class);
        assertThat(discountPolicy).isInstanceOf(FixDiscountPolicy.class);
    }

    //구현체에 의존하므로 바람직하지 않음
    @Test
    @DisplayName("특정 하위 타입으로 조회")
    void findBeanBySubType(){
        DiscountPolicy discountPolicy=ac.getBean(RateDiscountPolicy.class);
        assertThat(discountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("부모 타입으로 모든 빈 조회")
    void findBeanByParentType(){
        Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);
        for(String key : beansOfType.keySet()){
            System.out.println("key = "+key+" value = "+beansOfType.get(key));
        }
        assertThat(beansOfType.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Object 타입으로 모든 빈 조회")
    void fintBeanByObjectType(){
        Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);
        for(String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " + beansOfType.get(key));
        }
    }

    @Configuration
    static class AppConfig{

        @Bean
        DiscountPolicy rateDiscountPolicy(){
            return new RateDiscountPolicy();
        }
        @Bean
        DiscountPolicy fixDiscountPolicy(){
            return new FixDiscountPolicy();
        }
    }
}
