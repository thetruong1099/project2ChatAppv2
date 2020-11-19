package com.example.project2chatappv2.model;

import java.util.ArrayList;

public class GroupChatModel {
    private String groupID;
    private String groupName;
    private String imageURL;
    private String admin;
    private ArrayList<String> member;

    public GroupChatModel() {
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public ArrayList<String> getMember() {
        return member;
    }

    public void setMember(ArrayList<String> member) {
        this.member = member;
    }

    public String getAdmin() {
        return admin;
    }
}
