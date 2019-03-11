package com.mcn.honeydew.data.network.model.response;

public class CustomLocationData {


    public CustomLocationData(){

    }

    String mainData;

    public CustomLocationData(String mainData, String modifiedData) {
        this.mainData = mainData;
        this.modifiedData = modifiedData;
    }

    public String getMainData() {
        return mainData;
    }

    public void setMainData(String mainData) {
        this.mainData = mainData;
    }

    public String getModifiedData() {
        return modifiedData;
    }

    public void setModifiedData(String modifiedData) {
        this.modifiedData = modifiedData;
    }

    String modifiedData;
}
