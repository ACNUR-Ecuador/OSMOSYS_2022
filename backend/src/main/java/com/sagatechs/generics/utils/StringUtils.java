package com.sagatechs.generics.utils;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class StringUtils {

    @SuppressWarnings("unused")
    public String maskEmail(String email) {

        String result ;

        // 4 primeros caracteres
        String init = org.apache.commons.lang3.StringUtils.substring(email, 0, 3);

        String last = org.apache.commons.lang3.StringUtils.substringAfterLast(email, "@");

        result = init + "xxxxxxxx" + last;

        return result;
    }

    @SuppressWarnings("unused")
    public String maskPhone(String phone) {

        String result = "";

        // 4 primeros caracteres

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(phone) && phone.length() > 4) {

            return "xxxxxx"+phone.substring(phone.length() - 4);
        } else {
            return phone;
        }
    }

}