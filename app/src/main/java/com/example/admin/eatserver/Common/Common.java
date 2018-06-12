package com.example.admin.eatserver.Common;

import com.example.admin.eatserver.Model.User;

public class Common {
    public static User currentUser;
    public  static String UPDATE="Update";

    public  static String DELETE="Delete";
    public static final int PICK_IMAGE_REQUEST = 71;
    public static String convertCodeToStatus(String code)
    {
        if(code.equals("0"))
            return "Placed";
        else if(code.equals("1"))
            return "On my way";
        else
            return "Shipped";
    }
}
