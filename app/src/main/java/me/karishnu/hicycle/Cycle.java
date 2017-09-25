
package me.karishnu.hicycle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cycle {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("cood_x")
    @Expose
    private String coodX;
    @SerializedName("cood_y")
    @Expose
    private String coodY;
    @SerializedName("cycle_id")
    @Expose
    private String cycleId;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("time_update")
    @Expose
    private String timeUpdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoodX() {
        return coodX;
    }

    public void setCoodX(String coodX) {
        this.coodX = coodX;
    }

    public String getCoodY() {
        return coodY;
    }

    public void setCoodY(String coodY) {
        this.coodY = coodY;
    }

    public String getCycleId() {
        return cycleId;
    }

    public void setCycleId(String cycleId) {
        this.cycleId = cycleId;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(String timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

}
