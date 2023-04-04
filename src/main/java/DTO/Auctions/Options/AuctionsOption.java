package DTO.Auctions.Options;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuctionsOption {
    @SerializedName("MaxItemLevel")
    @Expose
    public Integer MaxItemLevel;

    @SerializedName("ItemGradeQualities")
    @Expose
    public Integer[] ItemGradeQualities;

    @SerializedName("SkillOptions")
    @Expose
    public SkillOption[] SkillOption;

    @SerializedName("EtcOptions")
    @Expose
    public EtcOption[] EtcOption;

    @SerializedName("Categories")
    @Expose
    public Category[] Category;

    @SerializedName("ItemGrades")
    @Expose
    public String[] ItemGrades;

    @SerializedName("ItemTiers")
    @Expose
    public Integer[] ItemTiers;

    @SerializedName("Classes")
    @Expose
    public String[] Classes;
}
