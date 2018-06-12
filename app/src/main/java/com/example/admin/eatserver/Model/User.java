package com.example.admin.eatserver.Model;

public class User {

    private String Name,Password,Phone,IsStaff;

    public User() {
    }

    public User(String name, String password) {

        Name = name;
        Password = password;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getName() {

        return Name;
    }

    public String getPassword() {
        return Password;
    }

    public String getPhone() {
        return Phone;
    }

    public String getIsStaff() {
        return IsStaff;
    }
}
