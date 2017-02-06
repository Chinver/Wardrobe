package com.wardrobe.www.service;

import com.wardrobe.www.db.DatabaseHelper;
import com.wardrobe.www.model.Clothes;

import java.util.List;

/**
 * 商品表
 * Created by admin on 2016/9/22.
 */
public interface ClothesServiceI extends BaseServiceI {
    int insertClothes(DatabaseHelper databaseHelper, Clothes clothes);

    int isClothesExist(DatabaseHelper databaseHelper, Clothes clothes);

    List<Clothes> showClothesByDivision(DatabaseHelper databaseHelper, String division);

    int deleteClothesByName(DatabaseHelper databaseHelper, String namr);

}
