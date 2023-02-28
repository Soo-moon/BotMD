package DTO.Market;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * {
 *   "PageNo": 0,
 *   "PageSize": 0,
 *   "TotalCount": 0,
 *   "Items": [
 *     {
 *       "Id": 0,
 *       "Name": "string",
 *       "Grade": "string",
 *       "Icon": "string",
 *       "BundleCount": 0,
 *       "TradeRemainCount": 0,
 *       "YDayAvgPrice": 0,
 *       "RecentPrice": 0,
 *       "CurrentMinPrice": 0
 *     }
 *   ]
 * }
 * */

public class MarketList {
    @SerializedName("PageNo")
    @Expose
    public Integer pageNo;

    @SerializedName("PageSize")
    @Expose
    public Integer pageSize;

    @SerializedName("TotalCount")
    @Expose
    public Integer totalCount;

    @SerializedName("Items")
    @Expose
    public MarketItem[] marketItems;
}
