package com.example.cartservice.util;

import com.example.cartservice.model.NotificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Util {
    private static  String sender;

    @Value("${notification.sender}")
    public void setSender(String sender){
        Util.sender=sender;
    }

    public static NotificationRequest createNotificationRequest(String email){
        String messageBody="Your cart has been updated";
        NotificationRequest notificationRequest= new NotificationRequest();
        notificationRequest.setSender(sender);
        notificationRequest.setReceiver(email);
        notificationRequest.setSubject("Cart updated");
        notificationRequest.setMessageBody(messageBody);
        return notificationRequest;
    }
}
