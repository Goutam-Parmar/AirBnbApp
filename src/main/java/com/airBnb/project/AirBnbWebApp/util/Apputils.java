package com.airBnb.project.AirBnbWebApp.util;

import com.airBnb.project.AirBnbWebApp.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class Apputils {

    public static  User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
