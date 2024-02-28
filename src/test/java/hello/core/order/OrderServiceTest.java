package hello.core.order;

import hello.core.AppConfig;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
}
