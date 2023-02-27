package DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tripods {
    @SerializedName("Value")
    @Expose
    private Integer Value;

    @SerializedName("Text")
    @Expose
    private String Text;

    @SerializedName("IsGem")
    @Expose
    private Boolean IsGem;

    public Integer getValue() {
        return Value;
    }

    public String getText() {
        return Text;
    }

    public Boolean getGem() {
        return IsGem;
    }
}
