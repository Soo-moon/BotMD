package DTO.Auctions.items;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Auction {
    @SerializedName("PageNo")
    @Expose
    public Integer PageNo;

    @SerializedName("PageSize")
    @Expose
    public Integer PageSize;

    @SerializedName("TotalCount")
    @Expose
    public Integer TotalCount;

    @SerializedName("Items")
    @Expose
    public AuctionItem[] Items;
}
