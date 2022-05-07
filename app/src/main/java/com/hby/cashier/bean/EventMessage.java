package com.hby.cashier.bean;

/**
 * 功能介绍:EventBus使用的类
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/12/9 15:29
 * 最后编辑: 2020/12/9 - Hyy
 *
 * @author HouYinYu
 */
public class EventMessage {
    private String messageCode;
    private String messageText;
    private int returnCode;
    private boolean isRequest;

    public EventMessage(String messageCode) {
        this.messageCode = messageCode;
    }

    public EventMessage(String messageCode, int returnCode) {
        this.messageCode = messageCode;
        this.returnCode = returnCode;
    }

    public EventMessage(String messageCode, boolean isRequest) {
        this.messageCode = messageCode;
        this.isRequest = isRequest;
    }

    public EventMessage(String messageCode, String messageText) {
        this.messageCode = messageCode;
        this.messageText = messageText;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public boolean isRequest() {
        return isRequest;
    }

    public void setRequest(boolean request) {
        isRequest = request;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
