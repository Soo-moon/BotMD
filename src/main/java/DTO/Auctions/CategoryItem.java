package DTO.Auctions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryItem {
    @SerializedName("Code")
    @Expose
    private Integer Code;

    @SerializedName("CodeName")
    @Expose
    private String CodeName;
}
