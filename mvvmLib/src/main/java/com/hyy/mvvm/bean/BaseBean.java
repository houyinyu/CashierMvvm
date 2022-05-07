package com.hyy.mvvm.bean;

/**
 * 功能介绍:Bean基类
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/11/25 14:37
 * 最后编辑: 2020/11/25 - Hyy
 *
 * @author HouYinYu
 */
public class BaseBean {

    private int result;
    private String action;
    private String message;
    private String detail;
    private Object resultObject;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Object getResultObject() {
        return resultObject;
    }

    public void setResultObject(Object resultObject) {
        this.resultObject = resultObject;
    }
}
