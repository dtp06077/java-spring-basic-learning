package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest {

    @Test
    @DisplayName("프로토타입 빈 테스트")
    public void prototypeFind(){
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(PrototypeBean.class);

        PrototypeBean prototypeBean1=ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        int count1 = prototypeBean1.getCount();
        //프로토타입 빈을 요청할 때 마다 새로운 인스턴스가 생성되므로 공유필드를 사용하지 않음.
        assertThat(count1).isEqualTo(1);

        PrototypeBean prototypeBean2=ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        int count2 = prototypeBean1.getCount();
        //값이 일정한 것을 알 수 있음.
        assertThat(count2).isEqualTo(1);

        ac.close();
    }

    @Test
    @DisplayName("프로토타입 빈을 주입받은 싱글톤 빈 테스트")
    public void singletonWithPrototype(){
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(SingletonBean.class, PrototypeBean.class);

        SingletonBean singletonBean1=ac.getBean(SingletonBean.class);
        int count1=singletonBean1.logic();
        //싱글톤 빈은 컨테이너 생성시에 의존관계를 주입받고 그 때 주입받은 프로토타입 빈을 계속 사용함.
        //프로토타입 빈의 공유 필드를 요청시마다 사용하므로 값이 변경되는 문제점을 가짐.
        assertThat(count1).isEqualTo(1);

        SingletonBean singletonBean2=ac.getBean(SingletonBean.class);
        int count2=singletonBean2.logic();
        //변경된 값
        assertThat(count2).isEqualTo(2);

        ac.close();
    }

    @Scope("singleton")
    static class SingletonBean{
        private final PrototypeBean prototypeBean;

        public SingletonBean(PrototypeBean prototypeBean){
            this.prototypeBean=prototypeBean;
        }

        public int logic(){
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
        @PostConstruct
        public void init() {
            System.out.println("SingletonBean.init " + this);
        }

        @PreDestroy
        public void close() {
            System.out.println("SingletonBean.close " + this);
        }
    }

    @Scope("prototype")
    static class PrototypeBean{

        private int count=0;

        public void addCount() {
            this.count++;
        }

        public int getCount(){
            return this.count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }

        @PreDestroy
        public void close() {
            System.out.println("PrototypeBean.close " + this);
        }
    }
}
