
package me.karishnu.hicycle;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CyclesResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("cycles")
    @Expose
    private List<Cycle> cycles = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Cycle> getCycles() {
        return cycles;
    }

    public void setCycles(List<Cycle> cycles) {
        this.cycles = cycles;
    }

}
