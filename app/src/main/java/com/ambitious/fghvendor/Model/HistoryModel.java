package com.ambitious.fghvendor.Model;

public class HistoryModel {
    String payment_id ;
    String vendor_id ;
    String user_id ;
    String userName ;
    String amount ;
    String txn_id ;
    String settled ;
    String datetime ;
    String entrydt ;

    /*public HistoryModel(String payment_id, String vendor_id, String user_id, String amount, String txn_id, String settled, String datetime, String entrydt) {
        this.payment_id = payment_id;
        this.vendor_id = vendor_id;
        this.user_id = user_id;
        this.amount = amount;
        this.txn_id = txn_id;
        this.settled = settled;
        this.datetime = datetime;
        this.entrydt = entrydt;
    }*/

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(String txn_id) {
        this.txn_id = txn_id;
    }

    public String getSettled() {
        return settled;
    }

    public void setSettled(String settled) {
        this.settled = settled;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getEntrydt() {
        return entrydt;
    }

    public void setEntrydt(String entrydt) {
        this.entrydt = entrydt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
