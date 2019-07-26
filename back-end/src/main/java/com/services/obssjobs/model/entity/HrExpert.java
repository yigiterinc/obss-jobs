package com.services.obssjobs.model.entity;

public class HrExpert {

    private String uid;
    private String fullName;
    private String lastName;
    private String password;

    public HrExpert() {
    }

    public HrExpert(String fullName, String lastName, String password) {
        this.fullName = fullName;
        this.lastName = lastName;
        this.password = password;
    }

    public HrExpert(String uid, String fullName, String lastName, String password) {
        this.uid = uid;
        this.fullName = fullName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "HrExpert{" +
                "fullName='" + fullName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
