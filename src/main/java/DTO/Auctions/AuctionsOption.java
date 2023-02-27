package DTO.Auctions;

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
    private DTO.Auctions.SkillOptions_Auctions_Options[] SkillOptions_Auctions_Options;

    @SerializedName("EtcOptions")
    @Expose
    private EtcOptions_Auctions_Options[] EtcOptions_Auctions_Options;

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

    public SkillOptions_Auctions_Options[] getSkillOptions_Auctions() {
        return SkillOptions_Auctions_Options;
    }
}
