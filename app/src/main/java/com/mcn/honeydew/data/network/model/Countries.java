package com.mcn.honeydew.data.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by atiwari on 6/30/2016.
 */
public class Countries {

    private String code;
    private String name;

    @SerializedName("dial_code")
    private String dialCode;

    public Countries(String code, String name, String dialCode) {
        this.code = code;
        this.name = name;
        this.dialCode = dialCode;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDialCode() {
        return dialCode;
    }
}
