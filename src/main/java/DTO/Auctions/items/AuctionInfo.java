package DTO.Auctions.items;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuctionInfo {
    @SerializedName("StartPrice")
    @Expose
    public Integer StartPrice;

    @SerializedName("BuyPrice")
    @Expose
    public Integer BuyPrice;

    @SerializedName("BidPrice")
    @Expose
    public Integer BidPrice;

    @SerializedName("EndDate")
    @Expose
    public String EndDate;

    @SerializedName("BidCount")
    @Expose
    public Integer BidCount;

    @SerializedName("BidStartPrice")
    @Expose
    public Integer BidStartPrice;

    @SerializedName("IsCompetitive")
    @Expose
    public Boolean IsCompetitive;

    @SerializedName("TradeAllowCount")
    @Expose
    public Integer TradeAllowCount;

}
