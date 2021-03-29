package com.avit.warsipharmacy.utility;

import java.util.regex.Pattern;

public class Validation {
    public static boolean isValidName(String s){
        String expr = "[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
        if(Pattern.compile(expr).matcher(s).matches()){
            return true;
        }
        return false;
    }

    public static boolean addressValidation(String s){
        if(s.length() < 1){
            return false;
        }
        return true;
    }

    public static boolean pinCodeValidation(String s){
        if(s.length() != 6){
            return false;
        }
        return true;
    }

    public static boolean phoneNoValidation(String s){
        if(s.length() != 10){
            return false;
        }

        return true;
    }

}
