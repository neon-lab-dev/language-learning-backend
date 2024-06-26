package com.neonlab.common.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.neonlab.common.expectations.InvalidInputException;
import lombok.Getter;



@Getter
public enum OrderStatus {

    PROCESSING("Processing"),
    PACKAGING("Packaging"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    CANCELED("Canceled");

    public final String orderStatus;
    OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @JsonCreator
    public static OrderStatus fromString(String inputStatus) throws InvalidInputException {
        for (OrderStatus orderStatus1 : OrderStatus.values()) {
            if (orderStatus1.orderStatus.equalsIgnoreCase(inputStatus)) {
                return orderStatus1;
            }
        }
        throw new InvalidInputException("Unknown Status: " + inputStatus);
    }
}