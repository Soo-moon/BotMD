package DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SkillOptions_Auctions {
    @SerializedName("Value")
    @Expose
    private Integer Value;

    @SerializedName("Class")
    @Expose
    private String Class;

    @SerializedName("Text")
    @Expose
    private String Text;

    @SerializedName("IsSkillGroup")
    @Expose
    private Boolean IsSkillGroup;

    @SerializedName("Tripods")
    @Expose
    private Tripods[] Tripods;


    public String classGet() {
        return Class;
    }

    public DTO.Tripods[] getTripods() {
        return Tripods;
    }

    public String getText() {
        return Text;
    }
}
