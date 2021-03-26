package com.avit.warsipharmacy.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.avit.warsipharmacy.ui.cart.CartItem;

import java.util.List;

public class CartRepository {

    private CartDao cartDao;
    private LiveData<List<CartItem>> allItems;

    public CartRepository(Application application){
        WarsiDatabase database = WarsiDatabase.getInstance(application);
        cartDao = database.cartDao();

        allItems = cartDao.getAllItems();
    }

    public LiveData<List<CartItem>> getAllItems(){
        return allItems;
    }

    public void insert(CartItem cartItem){
        new InsertItemAsyncTask(cartDao).execute(cartItem);
    }

    public void delete(CartItem cartItem){
        new DeleteItemAsyncTask(cartDao).execute(cartItem);
    }

    public boolean findCartItem(String name){
        List<CartItem> cartItems =  cartDao.findCartItemWithName(name);
        if(cartItems.size() > 0){
            return true;
        }
        return false;
    }

    public void deleteCartItemByName(String name){
        new DeleteItemByNameAsyncTask(cartDao).execute(name);
    }

    public void update(CartItem cartItem){
        new UpdateItemAsyncTask(cartDao).execute(cartItem);
    }

    public void deleteAll(){
        new DeleteAllItemAsyncTask(cartDao).execute();
    }


    public static class DeleteItemByNameAsyncTask extends AsyncTask<String,Void,Void>{

        private CartDao cartDao;

        public DeleteItemByNameAsyncTask(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            cartDao.deleteCartItemByName(strings[0]);
            return null;
        }
    }

    public static class InsertItemAsyncTask extends AsyncTask<CartItem,Void,Void>{

        private CartDao cartDao;

        public InsertItemAsyncTask(CartDao cartDao){
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            cartDao.insert(cartItems[0]);
            return null;
        }
    }

    public static class DeleteItemAsyncTask extends AsyncTask<CartItem,Void,Void>{

        private CartDao cartDao;

        public DeleteItemAsyncTask(CartDao cartDao){
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            cartDao.delete(cartItems[0]);
            return null;
        }
    }

    public static class UpdateItemAsyncTask extends AsyncTask<CartItem,Void,Void>{

        private CartDao cartDao;

        public UpdateItemAsyncTask(CartDao cartDao){
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            cartDao.update(cartItems[0]);
            return null;
        }
    }

    public static class DeleteAllItemAsyncTask extends AsyncTask<Void,Void,Void>{

        private CartDao cartDao;

        public DeleteAllItemAsyncTask(CartDao cartDao){
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cartDao.deleteAll();
            return null;
        }
    }

}
