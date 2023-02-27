package DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuctionsOption {
    @SerializedName("MaxItemLevel")
    @Expose
    private Integer MaxItemLevel;

    @SerializedName("ItemGradeQualities")
    @Expose
    private Integer[] ItemGradeQualities;

    @SerializedName("SkillOptions")
    @Expose
    private SkillOptions_Auctions[] SkillOptions_Auctions;

    @SerializedName("EtcOptions")
    @Expose
    private EtcOptions[] EtcOptions;

    @SerializedName("Categories")
    @Expose
    private Categories_Auctions[] Categories_Auctions;

    @SerializedName("ItemGrades")
    @Expose
    private String[] ItemGrades;

    @SerializedName("ItemTiers")
    @Expose
    private Integer[] ItemTiers;

    @SerializedName("Classes")
    @Expose
    private String[] Classes;

    public DTO.SkillOptions_Auctions[] getSkillOptions_Auctions() {
        return SkillOptions_Auctions;
    }
}
