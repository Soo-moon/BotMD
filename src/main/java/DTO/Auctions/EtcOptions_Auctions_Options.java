package DTO.Auctions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EtcOptions_Auctions_Options {
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
