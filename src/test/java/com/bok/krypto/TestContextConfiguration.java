package com.bok.krypto;

import com.bok.krypto.messaging.consumer.UserConsumer;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@TestConfiguration
public class TestContextConfiguration {

    private static final AtomicReference<String> received = new AtomicReference<>();
    private static final CountDownLatch latch = new CountDownLatch(1);

    @Bean
    public static MethodInterceptor interceptor() {
        return invocation -> {
            received.set((String) invocation.getArguments()[0]);
            return invocation.proceed();
        };
    }

    @Bean
    public static BeanPostProcessor listenerAdvisor() {
        return new ListenerWrapper(interceptor());
    }

    @Bean
    public UserConsumer messageConsumer() {
        return new UserConsumer();
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        //jmsTemplate.setPubSubDomain(true);  // enable for Pub Sub to topic. Not Required for Queue.
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        //factory.setPubSubDomain(true);
        return factory;
    }

    // this bean will be injected into the OrderServiceTest class
    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }

    public static class ListenerWrapper implements BeanPostProcessor, Ordered {

        private final MethodInterceptor interceptor;

        public ListenerWrapper(MethodInterceptor interceptor) {
            this.interceptor = interceptor;
        }

        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof JmsProperties.Listener) {
                ProxyFactory pf = new ProxyFactory(bean);
                NameMatchMethodPointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor(this.interceptor);
                advisor.addMethodName("listen");
                pf.addAdvisor(advisor);
                return pf.getProxy();
            }
            return bean;
        }
    }
}