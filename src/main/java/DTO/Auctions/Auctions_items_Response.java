package DTO.Auctions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Auctions_items_Response {
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
