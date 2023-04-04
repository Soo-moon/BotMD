package DTO.Auctions.Options;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tripods {
    @SerializedName("Value")
    @Expose
    public Integer Value;

    @SerializedName("Text")
    @Expose
    public String Text;

    @SerializedName("IsGem")
    @Expose
    public Boolean IsGem;
}
