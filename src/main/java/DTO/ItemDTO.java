package DTO;

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

public class ItemDTO {
    @SerializedName("PageNo")
    @Expose
    private String pageNo;

    @SerializedName("PageSize")
    @Expose
    private String pageSize;

    @SerializedName("TotalCount")
    @Expose
    private String totalCount;

    @SerializedName("Items")
    @Expose
    private Item[] items;

    public String getPageNo() {
        return pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public Item[] getItems() {
        return items;
    }
}
