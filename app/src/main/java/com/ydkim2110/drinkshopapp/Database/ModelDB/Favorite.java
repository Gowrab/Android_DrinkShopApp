package com.ydkim2110.drinkshopapp.Database.ModelDB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

/**
 * Created by Kim Yongdae on 2018-12-04
 * email : ydkim2110@gmail.com
 */

@Entity(tableName = "Favorite")
public class Favorite {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    public String id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "link")
    public String link;

    @ColumnInfo(name = "price")
    public String price;

    @ColumnInfo(name = "menuId")
    public String  menuId;

}
