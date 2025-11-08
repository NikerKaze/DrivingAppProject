package com.example.firebase2;

import java.util.ArrayList;

public class Chat {
    private String Id1, Id2, chatId;
    private ArrayList<Messege> msgs;

    public Chat() {
        msgs = new ArrayList<>();
    }

    public Chat(String Id1, String Id2,String chatId) {
        this.Id1 = Id1;
        this.Id2 = Id2;
        this.chatId=chatId;
        msgs = new ArrayList<>();
    }

    public void addMessege(Messege msg) {
        msgs.add(msg);
    }

    public void addMessege(String msg,String senderId) {
        Messege messege;
        String id = java.util.UUID.randomUUID().toString();
        if(senderId.equals(Id1)) {
            messege = new Messege(msg, Id1, Id2,id);
        }
        else {
            messege = new Messege(msg, Id2, Id1,id);
        }
        msgs.add(messege);
    }

    public boolean checkUid(String uid1,String uid2)
    {
        if(uid1.equals(Id1)&&uid2.equals(Id2)){
            return true;
        }
        if(uid1.equals(Id2)&&uid2.equals(Id1)){
            return true;
        }
        return false;
    }

    public String getSenderId() {
        return Id1;
    }

    public String getReceiverId() {
        return Id2;
    }

    public ArrayList <Messege> getMesseges() {
        return msgs;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId=chatId;
    }

    public void setSenderId(String Id1) {
        this.Id1=Id1;
    }

    public void setReceiverId(String Id2) {
        this.Id2=Id2;
    }

    public void setMesseges(ArrayList <Messege> msgs) {
        this.msgs=msgs;
    }
}
