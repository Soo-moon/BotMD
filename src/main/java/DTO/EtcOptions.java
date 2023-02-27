package DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EtcOptions {
    @SerializedName("Value")
    @Expose
    private Integer Value;

    @SerializedName("Text")
    @Expose
    private String Text;

    @SerializedName("EtcSubs")
    @Expose
    private EtcSubs[] EtcSubs;
}
