package DTO.Auctions.items;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestAuctionItems {
    @SerializedName("ItemLevelMin")
    @Expose
    public Integer ItemLevelMin;

    @SerializedName("ItemLevelMax")
    @Expose
    public Integer ItemLevelMax;

    @SerializedName("ItemGradeQuality")
    @Expose
    public Integer ItemGradeQuality;

    @SerializedName("SkillOptions")
    @Expose
    public SearchDetailOption[] SkillOptions;

    @SerializedName("EtcOptions")
    @Expose
    public SearchDetailOption[] EtcOptions;

    @SerializedName("Sort")
    @Expose
    public String Sort;

    @SerializedName("CategoryCode")
    @Expose
    public Integer CategoryCode = 170300;

    @SerializedName("CharacterClass")
    @Expose
    public String CharacterClass;

    @SerializedName("ItemTier")
    @Expose
    public Integer ItemTier = 3;

    @SerializedName("ItemGrade")
    @Expose
    public String ItemGrade = "유물";

    @SerializedName("ItemName")
    @Expose
    public String ItemName;

    @SerializedName("PageNo")
    @Expose
    public Integer PageNo;

    @SerializedName("SortCondition")
    @Expose
    public String SortCondition;
}
