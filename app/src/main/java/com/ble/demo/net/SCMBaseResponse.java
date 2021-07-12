package com.ble.demo.net;



public class SCMBaseResponse<W> {
    private int total;
    private String msg;
    private W data;
    private boolean result;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public W getData() {
        return data;
    }

    public void setData(W data) {
        this.data = data;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
