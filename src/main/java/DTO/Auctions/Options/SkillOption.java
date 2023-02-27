package DTO.Auctions.Options;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SkillOption {
    @SerializedName("Value")
    @Expose
    public Integer Value;

    @SerializedName("Class")
    @Expose
    public String Class;

    @SerializedName("Text")
    @Expose
    public String Text;

    @SerializedName("IsSkillGroup")
    @Expose
    public Boolean IsSkillGroup;

    @SerializedName("Tripods")
    @Expose
    public Tripods[] Tripods;
}
