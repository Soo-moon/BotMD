package DTO.Auctions.items;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchDetailOption {
    @SerializedName("FirstOption")
    @Expose
    public Integer FirstOption;

    @SerializedName("SecondOption")
    @Expose
    public Integer SecondOption;

    @SerializedName("MinValue")
    @Expose
    public Integer MinValue;

    @SerializedName("MaxValue")
    @Expose
    public Integer MaxValue;
}
