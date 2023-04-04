package DTO.Market;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *  "Items": [
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
 **/
public class MarketItem {
    @SerializedName("Id")
    @Expose
    public Integer id;

    @SerializedName("Name")
    @Expose
    public String name;

    @SerializedName("Grade")
    @Expose
    public String grade;

    @SerializedName("Icon")
    @Expose
    public String icon;

    @SerializedName("BundleCount")
    @Expose
    public Integer bundleCount;

    @SerializedName("TradeRemainCount")
    @Expose
    public Integer tradeRemainCount;

    @SerializedName("YDayAvgPrice")
    @Expose
    public Double yDayAvgPrice;

    @SerializedName("RecentPrice")
    @Expose
    public Integer recentPrice;

    @SerializedName("CurrentMinPrice")
    @Expose
    public Integer currentMinPrice;

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }
}
