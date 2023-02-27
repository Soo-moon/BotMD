package DTO;

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
public class ItemSearchParam {
    @SerializedName("Sort")
    @Expose
    private String sort = "GRADE";

    @SerializedName("CategoryCode")
    @Expose
    private String categoryCode="";

    @SerializedName("CharacterClass")
    @Expose
    private String characterClass="";

    @SerializedName("ItemTier")
    @Expose
    private String itemTier="";

    @SerializedName("ItemGrade")
    @Expose
    private String itemGrade="";

    @SerializedName("ItemName")
    @Expose
    private String itemName="";

    @SerializedName("PageNo")
    @Expose
    private String pageNo="";

    @SerializedName("SortCondition")
    @Expose
    private String sortCondition="";

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    public String getItemTier() {
        return itemTier;
    }

    public void setItemTier(String itemTier) {
        this.itemTier = itemTier;
    }

    public String getItemGrade() {
        return itemGrade;
    }

    public void setItemGrade(String itemGrade) {
        this.itemGrade = itemGrade;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getSortCondition() {
        return sortCondition;
    }

    public void setSortCondition(String sortCondition) {
        this.sortCondition = sortCondition;
    }
}
