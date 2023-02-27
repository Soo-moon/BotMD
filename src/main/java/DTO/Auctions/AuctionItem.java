package DTO.Auctions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuctionItem {
    @SerializedName("Name")
    @Expose
    public String Name;

    @SerializedName("Grade")
    @Expose
    public String Grade;

    @SerializedName("Tier")
    @Expose
    public Integer Tier;

    @SerializedName("Level")
    @Expose
    public Integer Level;

    @SerializedName("Icon")
    @Expose
    public String Icon;

    @SerializedName("GradeQuality")
    @Expose
    public Integer GradeQuality;

    @SerializedName("AuctionInfo")
    @Expose
    public AuctionInfo AuctionInfo;

    @SerializedName("Options")
    @Expose
    public ItemOption[] Options;
}
