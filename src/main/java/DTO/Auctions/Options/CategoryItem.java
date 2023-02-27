package DTO.Auctions.Options;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryItem {
    @SerializedName("Code")
    @Expose
    public Integer Code;

    @SerializedName("CodeName")
    @Expose
    public String CodeName;
}
