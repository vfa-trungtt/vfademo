package com.asai24.golf.object;

/**
 * Created by huynq on 8/3/16.
 */
public class CallInfoRakuten extends ObjectParent {
    private String playDate;
    private int stockStatus;
    private int stockCount;
    private String reservePageUrlPC;
    private String reservePageUrlMobile;

    public CallInfoRakuten() {
    }

    public CallInfoRakuten(String playDate, int stockStatus, int stockCount, String reservePageUrlPC, String reservePageUrlMobile) {
        this.playDate = playDate;
        this.stockStatus = stockStatus;
        this.stockCount = stockCount;
        this.reservePageUrlPC = reservePageUrlPC;
        this.reservePageUrlMobile = reservePageUrlMobile;
    }
    public String getPlayDate() {
        return playDate;
    }
    public int getStockStatus() {
        return stockStatus;
    }

    public int getStockCount() {
        return stockCount;
    }

    public String getReservePageUrlPC() {
        return reservePageUrlPC;
    }

    public String getReservePageUrlMobile() {
        return reservePageUrlMobile;
    }

    public void setPlayDate(String playDate) {
        this.playDate = playDate;
    }

    public void setStockStatus(int stockStatus) {
        this.stockStatus = stockStatus;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public void setReservePageUrlPC(String reservePageUrlPC) {
        this.reservePageUrlPC = reservePageUrlPC;
    }

    public void setReservePageUrlMobile(String reservePageUrlMobile) {
        this.reservePageUrlMobile = reservePageUrlMobile;
    }
}
