package ru.zyryanova.TransferService.entity;

import jakarta.persistence.Column;

import java.util.Date;

public class TransferDto {

    private String senderId;

    private String recipientId;

    private int amount;

    public TransferDto() {
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
