package com.example.demo.order.integration;

import com.example.demo.order.entity.Order;
import com.example.demo.order.entity.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderNotificationPublisher {

    public void sendOrderConfirmation(Order order, String userEmail) {
        // In a real system, this would send an SMTP email or publish an event.
        // Here, we simulate it by logging the "Email" to the console.

        StringBuilder emailBody = new StringBuilder();
        emailBody.append("\n========================================\n");
        emailBody.append("        ORDER CONFIRMATION EMAIL        \n");
        emailBody.append("========================================\n");
        emailBody.append("To: ").append(userEmail).append("\n");
        emailBody.append("Subject: Your Order #").append(order.getId()).append(" is Confirmed!\n\n");

        emailBody.append("Hello,\n");
        emailBody.append("Thank you for shopping with us. Here are your order details:\n\n");

        emailBody.append(String.format("%-30s %-10s %-10s\n", "Item ID", "Qty", "Price"));
        emailBody.append("--------------------------------------------------\n");

        for (OrderItem item : order.getItems()) {
            emailBody.append(String.format("%-30s %-10d $%-10.2f\n",
                    "Prod-" + item.getMerchantProductId(), // In real app, you'd fetch the product name here
                    item.getQuantity(),
                    item.getPrice()));
        }

        emailBody.append("--------------------------------------------------\n");
        emailBody.append("Total Amount: $").append(order.getTotalAmount()).append("\n");
        emailBody.append("Status: ").append(order.getStatus()).append("\n");
        emailBody.append("========================================\n");

        // "Send" the email
        log.info(emailBody.toString());
    }
}