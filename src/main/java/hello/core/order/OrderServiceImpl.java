package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

    private  final MemberRepository memberRepository;
    private  final DiscountPolicy discountPolicy;

    //일반 메서드 주입
    //한번에 여러 필드를 주입 받을 수 있고 일반적으로 잘 사용하지 않음
//    @Autowired
//    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy){
//        System.out.println("memberRepository = "+memberRepository+" discountPolicy = "+discountPolicy);
//        this.memberRepository=memberRepository;
//        this.discountPolicy=discountPolicy;
//    }

//    수정자 주입
//    선택, 변경 가능성이 있는 의존관계에 사용
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository){
//        System.out.println("memberRepository =" +memberRepository);
//        this.memberRepository=memberRepository;
//    }
//
//    @Autowired
//    public void setDiscountPolicy(DiscountPolicy discountPolicy){
//        System.out.println("discountPolicy = "+discountPolicy);
//        this.discountPolicy=discountPolicy;
//    }

    //생성자 주입
    //불변, 필수인 의존관계에 사용
    //롬복을 사용함으로서 생성자 주입 불필요
    public OrderServiceImpl(MemberRepository memberRepository,@MainDiscountPolicy DiscountPolicy discountPolicy) {
        System.out.println("memberRepository = "+memberRepository+" discountPolicy = "+discountPolicy);
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member=memberRepository.findById(memberId);
        int discountPrice=discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
