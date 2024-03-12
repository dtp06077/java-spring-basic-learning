package hello.core.order;

import hello.core.AppConfig;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {
    MemberService memberService;
    OrderService orderService;

    @BeforeEach
    void beforeEach(){
        AppConfig appCofig=new AppConfig();
        memberService=appCofig.memberService();
        orderService= appCofig.orderService();
    }

    @Test
    void createOrder(){
        //given
        Member member=new Member(1L,"memberB", Grade.VIP);

        //when
        memberService.join(member);
        Order order=orderService.createOrder(member.getId(),"itemB",10000);

        //then
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }

    @Test
    @DisplayName("수정자 주입의 문제점 테스트")
    void settterInjection(){
        MemoryMemberRepository memberRepository=new MemoryMemberRepository();
        memberRepository.save(new Member(1L,"name", Grade.VIP));

        OrderServiceImpl orderService=new OrderServiceImpl(memberRepository, new FixDiscountPolicy());
        Order order = orderService.createOrder(1L, "itemA", 10000);
        org.assertj.core.api.Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);

    }
}
