package com.example.tunganh.duan1_app.General;

import com.example.tunganh.duan1_app.Model.User;

public class General {
    public static User currentUser;
    public static String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "Shipping";
        else
            return "Finish Deliver";
    }
}
