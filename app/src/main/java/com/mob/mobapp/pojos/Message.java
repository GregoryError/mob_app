package com.mob.mobapp.pojos;

public class Message {
    private Long id;
    private Boolean fromUser;
    private Long uId;
    private Long cId;
    private String dateTime;
    private String content;
    private boolean wasRead;

    public Message(Boolean fromUser, Long uId, Long cId, String dateTime, String content, boolean wasRead) {
        this.fromUser = fromUser;
        this.uId = uId;
        this.cId = cId;
        this.dateTime = dateTime;
        this.content = content;
        this.wasRead = wasRead;
    }

    public Message() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getFromUser() {
        return fromUser;
    }

    public void setFromUser(Boolean fromUser) {
        this.fromUser = fromUser;
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public Long getcId() {
        return cId;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isWasRead() {
        return wasRead;
    }

    public void setWasRead(boolean wasRead) {
        this.wasRead = wasRead;
    }
}