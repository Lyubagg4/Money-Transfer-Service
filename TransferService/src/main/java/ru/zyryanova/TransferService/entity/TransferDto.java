package ru.zyryanova.TransferService.entity;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


public class TransferDto {
    @NotBlank(message = "поле не должно быть пустым")
    private String senderId;

    @NotBlank(message = "поле не должно быть пустым")
    private String recipientId;

    @Min(value = 1,  message = "сумма должна быть больше 0")
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
