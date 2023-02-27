package DTO;

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
public class Item {
    @SerializedName("Id")
    @Expose
    private String id;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("Grade")
    @Expose
    private String grade;

    @SerializedName("Icon")
    @Expose
    private String icon;

    @SerializedName("BundleCount")
    @Expose
    private String bundleCount;

    @SerializedName("TradeRemainCount")
    @Expose
    private String tradeRemainCount;

    @SerializedName("YDayAvgPrice")
    @Expose
    private String yDayAvgPrice;

    @SerializedName("RecentPrice")
    @Expose
    private String recentPrice;

    @SerializedName("CurrentMinPrice")
    @Expose
    private String currentMinPrice;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public String getIcon() {
        return icon;
    }

    public String getBundleCount() {
        return bundleCount;
    }

    public String getTradeRemainCount() {
        return tradeRemainCount;
    }

    public String getyDayAvgPrice() {
        return yDayAvgPrice;
    }

    public String getRecentPrice() {
        return recentPrice;
    }

    public String getCurrentMinPrice() {
        return currentMinPrice;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setBundleCount(String bundleCount) {
        this.bundleCount = bundleCount;
    }

    public void setTradeRemainCount(String tradeRemainCount) {
        this.tradeRemainCount = tradeRemainCount;
    }

    public void setyDayAvgPrice(String yDayAvgPrice) {
        this.yDayAvgPrice = yDayAvgPrice;
    }

    public void setRecentPrice(String recentPrice) {
        this.recentPrice = recentPrice;
    }

    public void setCurrentMinPrice(String currentMinPrice) {
        this.currentMinPrice = currentMinPrice;
    }
}
