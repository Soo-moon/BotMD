package DTO.Auctions.Options;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EtcOption {
    @SerializedName("Value")
    @Expose
    public Integer Value;

    @SerializedName("Text")
    @Expose
    public String Text;

    @SerializedName("EtcSubs")
    @Expose
    public EtcSubs[] EtcSubs;
}
