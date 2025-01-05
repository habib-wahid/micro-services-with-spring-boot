package org.example;

public class OrderEvent {
   private String orderId;
   private String email;

   public OrderEvent() {

   }
   public OrderEvent(String orderId, String email) {
       this.orderId = orderId;
       this.email = email;
   }

   public String getOrderId() {
       return orderId;
   }
   public String getEmail() {
       return email;
   }
}
