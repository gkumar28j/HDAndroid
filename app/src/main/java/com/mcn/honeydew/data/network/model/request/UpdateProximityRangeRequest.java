package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 7/3/18.
 */

public class UpdateProximityRangeRequest {

    @SerializedName("ProximityRange")
    @Expose
    private int proximityRange;

    public UpdateProximityRangeRequest(int proximityRange) {
        this.proximityRange = proximityRange;
    }

    public int getProximityRange() {
        return proximityRange;
    }

    public void setProximityRange(int proximityRange) {
        this.proximityRange = proximityRange;
    }
}
