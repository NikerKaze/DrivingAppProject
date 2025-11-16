package com.example.firebase2;

public class Messege {

    private String messege, sId, rId;

    public Messege() {}

    public Messege(String messege, String senderId, String receiverId) {

        this.messege = messege;
        this.sId = senderId;
        this.rId = receiverId;
    }

    public String getSender() {
        return sId;
    }

    public String getReceiver() {
        return rId;
    }

    public String getMessege() {
        return messege;
    }

    public void setSender(String sender) {
        this.sId=sender;
    }

    public void setReceiver(String receiver) {
        this.rId=receiver;
    }

    public void setMessege(String messege) {
        this.messege=messege;
    }

}
