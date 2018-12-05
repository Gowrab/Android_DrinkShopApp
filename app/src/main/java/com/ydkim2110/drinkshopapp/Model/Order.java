package com.ydkim2110.drinkshopapp.Model;

/**
 * Created by Kim Yongdae on 2018-12-05
 * email : ydkim2110@gmail.com
 */
public class Order {

    private long OrderId;
    private int OrderStatus;
    private float OrderPrice;
    private String OrderDetail;
    private String OrderComment;
    private String OrderAddress;
    private String UserPhone;

    public Order() {
    }

    public Order(long orderId, int orderStatus, float orderPrice, String orderDetail,
                 String orderComment, String orderAddress, String userPhone) {
        OrderId = orderId;
        OrderStatus = orderStatus;
        OrderPrice = orderPrice;
        OrderDetail = orderDetail;
        OrderComment = orderComment;
        OrderAddress = orderAddress;
        UserPhone = userPhone;
    }
}
