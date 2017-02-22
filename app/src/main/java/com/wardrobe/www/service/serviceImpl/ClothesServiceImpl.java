package com.wardrobe.www.service.serviceImpl;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wardrobe.www.base.util.LogUtil;
import com.wardrobe.www.base.db.DatabaseHelper;
import com.wardrobe.www.base.model.Clothes;
import com.wardrobe.www.service.ClothesServiceI;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Name：ProductServiceImpl
 * Class Function（Chinese）：商品接口实现类
 * Created by admin on 2016/9/22.
 */
public class ClothesServiceImpl extends BaseServiceImpl implements ClothesServiceI {
    private static final String TAG = "ClothesServiceImpl";
    private static final String TABLE_NAME = "t_clothes";

    private SQLiteDatabase db;

    @Override
    public int insertClothes(DatabaseHelper databaseHelper, Clothes clothes) {
        LogUtil.d(TAG, TABLE_NAME + " --> insert clothes");

        db = databaseHelper.getReadableDatabase();
//        db.beginTransaction(); // 开始事务

        int result = 0;
        try {
            if (isClothesExist(databaseHelper, clothes) > 0) {
                result = -1;//添加失败，重复添加
            } else {
                ContentValues cValue = new ContentValues();
                cValue.put("division", clothes.getDivision());
                cValue.put("img_url", clothes.getImgUrl());
                cValue.put("time", clothes.getTime());
                cValue.put("name", clothes.getName());
                cValue.put("date", clothes.getDate());
                db.insert(TABLE_NAME, null, cValue);
                result = 1;//添加成功
//            db.setTransactionSuccessful(); // 设置事务成功完成
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
//            db.endTransaction(); // 结束事务
            db.close();
        }
        return result;//添加失败，重复添加
    }

    @Override
    public int isClothesExist(DatabaseHelper databaseHelper, Clothes clothes) {
        LogUtil.d(TAG, TABLE_NAME + " --> select product");

        db = databaseHelper.getReadableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            Cursor cursor = db.query(TABLE_NAME, new String[]{"id,division,img_url,time,name,date"},
                    "name=? and date=?", new String[]{clothes.getName(), clothes.getDate()}, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(4);
                if (StringUtils.equals(clothes.getName(), name)) {
                    return 1;//照片已存在
                }
                cursor.close();
            }

            db.setTransactionSuccessful(); // 设置事务成功完成
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            db.endTransaction(); // 结束事务
        }
        return 0;
    }

    @Override
    public List<Clothes> showClothesByDivision(DatabaseHelper databaseHelper, String division) {
        LogUtil.d(TAG, TABLE_NAME + " --> select clothes for show");

        db = databaseHelper.getReadableDatabase();
        db.beginTransaction(); // 开始事务

        List<Clothes> clothesList = new ArrayList<>();
        Clothes index;
        try {
            Cursor cursor = db.query(TABLE_NAME, new String[]{"id,img_url,division,time,name,date"},
                    "division=?", new String[]{division}, null, null, "time DESC", null);
            while (cursor.moveToNext()) {
                index = new Clothes();
                index.setId(cursor.getInt(0));
                index.setImgUrl(cursor.getString(1));
                index.setDivision(cursor.getString(2));
                index.setTime(cursor.getString(3));
                index.setName(cursor.getString(4));
                index.setDate(cursor.getString(5));
                LogUtil.d(TAG, " name=" + index.getName() + " time=" + index.getTime() + " date=" + index.getDate());
                clothesList.add(index);
            }
            cursor.close();

            db.setTransactionSuccessful(); // 设置事务成功完成
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            db.endTransaction(); // 结束事务
        }
        return clothesList;
    }

    @Override
    public int deleteClothesByName(DatabaseHelper databaseHelper, String name) {
        LogUtil.d(TAG, TABLE_NAME + " --> delete clothes by name");

        db = databaseHelper.getWritableDatabase();

        try {
            db.delete(TABLE_NAME, "name=?", new String[]{name});//物理删除

            //逻辑删除
//                ContentValues cValue = new ContentValues();
//                cValue.put("status", 0);
//                db.update(TABLE_NAME, cValue, "role_code=? and status=1", new String[]{role.getCode()});
            return 1;
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            db.close();
        }
        return 0;
    }

}
