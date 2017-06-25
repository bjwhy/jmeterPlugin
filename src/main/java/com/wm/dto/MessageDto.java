package com.wm.dto;

/**
 * @description: netty dto for private
 * Created by peekaboo on 2016/3/28.
 */
public class MessageDto {
    int messageType;// 0:msg 1:heartbeat
    String message;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
