package DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EtcSubs {
    @SerializedName("Value")
    @Expose
    private Integer Value;

    @SerializedName("Text")
    @Expose
    private String Text;

    @SerializedName("Class")
    @Expose
    private String Class;
}
