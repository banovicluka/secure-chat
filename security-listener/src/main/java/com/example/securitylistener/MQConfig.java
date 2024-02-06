package com.example.securitylistener;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConfig {

    public static final String QUEUE = "message_queue";
    public static final String QUEUE2= "message_queue2";
    public static final String EXCHANGE = "message_exchange";

    public static final String ROUTING_KEY = "message_routingKey";
    public static final String ROUTING_KEY2 = "message_routingKey2";
    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }

    @Bean
    public Queue queue2(){
        return new Queue(QUEUE2);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding2(Queue queue2, TopicExchange exchange){
        return BindingBuilder.bind(queue2).to(exchange).with(ROUTING_KEY2);
    }



    @Bean
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name = "connectionFactory1")
    public CachingConnectionFactory connectionFactory1(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("rabbitmq-1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("luka");
        connectionFactory.setPassword("lukaluka123");
        return connectionFactory;
    }

//    @Bean(name = "connectionFactory2")
//    public CachingConnectionFactory connectionFactory2(){
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setHost("rabbitmq-2");
//        connectionFactory.setPort(5672);
//        connectionFactory.setUsername("luka");
//        connectionFactory.setPassword("lukaluka123");
//        return connectionFactory;
//    }
//
//    @Bean(name = "connectionFactory3")
//    public CachingConnectionFactory connectionFactory3(){
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setHost("rabbitmq-3");
//        connectionFactory.setPort(5672);
//        connectionFactory.setUsername("luka");
//        connectionFactory.setPassword("lukaluka123");
//        return connectionFactory;
//    }




    @Bean
    public RabbitTemplate template1 (@Qualifier("connectionFactory1") ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
   }



//    @Bean
//    public RabbitTemplate template2 (@Qualifier("connectionFactory2") ConnectionFactory connectionFactory){
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        template.setMessageConverter(messageConverter());
//        return template;
//    }
//
//    @Bean
//    public RabbitTemplate template3 (@Qualifier("connectionFactory3") ConnectionFactory connectionFactory){
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        template.setMessageConverter(messageConverter());
//        return template;
//    }
}