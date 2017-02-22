package com.wardrobe.www.service;

import com.wardrobe.www.base.db.DatabaseHelper;
import com.wardrobe.www.base.model.Clothes;

import java.util.List;

/**
 * 商品表
 * Created by admin on 2016/9/22.
 */
public interface ClothesServiceI extends BaseServiceI {
    int insertClothes(DatabaseHelper databaseHelper, Clothes clothes);

    int isClothesExist(DatabaseHelper databaseHelper, Clothes clothes);

    List<Clothes> showClothesByDivision(DatabaseHelper databaseHelper, String division);

    int deleteClothesByName(DatabaseHelper databaseHelper, String name);

}
