package com.bhavik.entity;

//public enum Gender {
//    MALE, FEMALE, OTHERS;
//}

public enum Gender {
    MALE("M", 100),     // you can add any no. for parameters
    FEMALE("F", 200);

    private String value;
    private int code;

    private Gender(String value, int code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }
}

// bg of enum
// public final class Gender{
//      final static Genger MALE = new Gender("MALE");
//      final static Genger FEMALE = new Gender("FEMALE");
//  }
// final to stop inheritance.