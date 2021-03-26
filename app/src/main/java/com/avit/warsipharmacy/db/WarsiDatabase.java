package com.avit.warsipharmacy.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.avit.warsipharmacy.ui.cart.CartItem;

@Database(entities = {CartItem.class},version = 1)
public abstract class WarsiDatabase extends RoomDatabase {
    public static final String DB_NAME = "warsi_db";
    private static WarsiDatabase instance;
    public abstract CartDao cartDao();

    public static synchronized WarsiDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),WarsiDatabase.class,DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

//    private static RoomDatabase.Callback callback =
//            new RoomDatabase.Callback(){
//                @Override
//                public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                    super.onCreate(db);
//                    new PopulatedbAsyncTask(instance).execute();
//                }
//            };
//
//    private static class PopulatedbAsyncTask extends AsyncTask<Void,Void,Void>{
//
//        private CartDao cartDao;
//
//        private PopulatedbAsyncTask(WarsiDatabase database){
//            cartDao = database.cartDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            CartItem cartItem = new CartItem("aasfafs","zeher",1,12,100);
//            cartDao.insert(cartItem);
//
//            return null;
//        }
//    }

}
