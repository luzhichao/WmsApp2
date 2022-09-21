package com.linkingwin.education.studyonline.student.api.request;

import com.linkingwin.education.studyonline.student.vo.RoomRole;

import java.io.Serializable;

public class EnterRoomRequest implements Serializable {

    private String merchantId;

    private String channel;

    private String roomId;

    private String userName;

    private String roomRole;


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomRole() {
        return roomRole;
    }

    public void setRoomRole(String roomRole) {
        this.roomRole = roomRole;
    }
}
