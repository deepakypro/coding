
package com.lernr.rxjavaexample.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLocationResponse {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("mLocationCord")
    @Expose
    private LocationCord mLocationCord;

    public UserLocationResponse(String userId, String userName,
        LocationCord locationCord) {
        this.userId = userId;
        this.userName = userName;
        this.mLocationCord = locationCord;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocationCord getLocationCord() {
        return mLocationCord;
    }

    public void setLocationCord(LocationCord locationCord) {
        this.mLocationCord = locationCord;
    }

}
