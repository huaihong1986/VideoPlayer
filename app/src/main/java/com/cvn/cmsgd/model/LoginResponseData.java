package com.cvn.cmsgd.model;

public class LoginResponseData {

    /**
     * msgCondition :if the user exist, then login error
     * msg : 0
     */

    private String msgCondition;
    private String msg;

    public String getMsgCondition() {
        return msgCondition;
    }

    public void setMsgCondition(String msgCondition) {
        this.msgCondition = msgCondition;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
