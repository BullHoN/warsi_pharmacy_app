package com.avit.warsipharmacy.ui.category;

import java.util.List;

public class CategoryItem {
    private String itemName;
    private String categoryName;
    private int discount;
    private List<PriceItem> priceItems;
    private String _id;


    public CategoryItem(String itemName, String categoryName, int discount, List<PriceItem> priceItems, String _id) {
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.discount = discount;
        this.priceItems = priceItems;
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public List<PriceItem> getPriceItems() {
        return priceItems;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getItemName() {
        return itemName;
    }

    public int getDiscount() {
        return discount;
    }

//    public int getPrice() {
//        return price;
//    }

    public static class PriceItem{
        private String priceTag;
        private int price;

        public PriceItem(String priceTag, int price) {
            this.priceTag = priceTag;
            this.price = price;
        }

        public String getPriceTag() {
            return priceTag;
        }

        public int getPrice() {
            return price;
        }
    }

}
