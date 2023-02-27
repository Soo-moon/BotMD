package DTO.Auctions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemOption {
    @SerializedName("Type")
    @Expose
    public String Type;

    @SerializedName("OptionName")
    @Expose
    public String OptionName;

    @SerializedName("OptionNameTripod")
    @Expose
    public String OptionNameTripod;

    @SerializedName("Value")
    @Expose
    public Integer Value;

    @SerializedName("IsPenalty")
    @Expose
    public Boolean IsPenalty;

    @SerializedName("ClassName")
    @Expose
    public String ClassName;
}
