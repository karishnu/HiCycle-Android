
package me.karishnu.hicycle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CycleResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("cycle")
    @Expose
    private Cycle cycle;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

}
