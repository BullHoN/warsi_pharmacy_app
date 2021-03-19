package com.avit.warsipharmacy.ui.category;

public class CategoryItem {
    private String itemName;
    private String categoryName;
    private int discount;
    private int price;

    public CategoryItem(String itemName, String categoryName, int discount, int price) {
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.discount = discount;
        this.price = price;
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

    public int getPrice() {
        return price;
    }
}
