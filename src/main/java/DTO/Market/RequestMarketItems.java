package DTO.Market;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**{
 "Sort": "GRADE",
 "CategoryCode": 0,
 "CharacterClass": "string",
 "ItemTier": 0,
 "ItemGrade": "string",
 "ItemName": "string",
 "PageNo": 0,
 "SortCondition": "ASC"
 }**/
public class RequestMarketItems {
    @SerializedName("Sort")
    @Expose
    public String sort = "GRADE";

    @SerializedName("CategoryCode")
    @Expose
    public Integer categoryCode;

    @SerializedName("CharacterClass")
    @Expose
    public String characterClass="";

    @SerializedName("ItemTier")
    @Expose
    public Integer itemTier;

    @SerializedName("ItemGrade")
    @Expose
    public String itemGrade="";

    @SerializedName("ItemName")
    @Expose
    public String itemName="";

    @SerializedName("PageNo")
    @Expose
    public Integer pageNo=1;

    @SerializedName("SortCondition")
    @Expose
    public String sortCondition="";
}
