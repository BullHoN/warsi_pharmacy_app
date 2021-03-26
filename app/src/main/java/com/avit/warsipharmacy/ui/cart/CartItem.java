package com.avit.warsipharmacy.ui.cart;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.avit.warsipharmacy.ui.category.CategoryItem;

import java.util.List;

@Entity(tableName = "cart")
public class CartItem{

    @PrimaryKey(autoGenerate = true)
    private int ID;

    private String itemName;
    private String categoryName;
    private int selectedPriceIndex;
    private int discount;

    @TypeConverters(PriceItemConvertor.class)
    private List<CategoryItem.PriceItem> priceItems;


    // TODO: REMOVE POSSIBLE VALUES, PRICE AND CHANGE NoOFItems TO SELECTEDITEMINDEX

    public CartItem(String itemName, String categoryName, int selectedPriceIndex, int discount
            , List<CategoryItem.PriceItem> priceItems) {
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.selectedPriceIndex = selectedPriceIndex;
        this.discount = discount;
        this.priceItems = priceItems;
    }

    public List<CategoryItem.PriceItem> getPriceItems() {
        return priceItems;
    }


    public void setSelectedPriceIndex(int selectedPriceIndex) {
        this.selectedPriceIndex = selectedPriceIndex;
    }

    public String[] getPossibleValues(){
        String items[] = new String[priceItems.size()];

        for(int i=0;i<priceItems.size();i++){
            items[i] = priceItems.get(i).getPriceTag();
        }

        return items;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getDiscount() {
        return discount;
    }

    public int getSelectedPriceIndex() {
        return selectedPriceIndex;
    }
}
