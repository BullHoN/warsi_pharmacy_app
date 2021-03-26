package com.avit.warsipharmacy.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.avit.warsipharmacy.ui.cart.CartItem;

import java.util.List;

@Dao
public interface CartDao {
    @Insert
    void insert(CartItem cartItem);

    @Update
    void update(CartItem cartItem);

    @Delete
    void delete(CartItem cartItem);

    @Query("SELECT * FROM cart")
    LiveData<List<CartItem>> getAllItems();

    @Query("SELECT * FROM cart WHERE itemName == :name")
    List<CartItem> findCartItemWithName(String name);

    @Query("DELETE FROM cart WHERE itemName == :name")
    void deleteCartItemByName(String name);

    @Query("DELETE FROM cart")
    void deleteAll();

}
