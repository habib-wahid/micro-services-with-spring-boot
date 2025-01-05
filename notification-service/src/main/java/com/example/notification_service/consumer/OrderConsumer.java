package com.example.notification_service.consumer;

import org.example.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    private final JavaMailSender mailSender;

    public OrderConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "order-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrderEvent(OrderEvent orderEvent) {
        System.out.println("Received order event: " + orderEvent.getOrderId() + " " + orderEvent.getEmail());
        if (orderEvent.getOrderId() == null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(orderEvent.getEmail());
            message.setSubject("Order Notification");
            message.setText("Your order has been processed. Order id: " + orderEvent.getOrderId());
            message.setFrom("spring_notification@email.com"); // optional, defaults to spring.mail.username
            mailSender.send(message);
        }
    }
}
