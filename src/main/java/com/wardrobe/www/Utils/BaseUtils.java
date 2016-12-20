package com.wardrobe.www.Utils;


import com.wardrobe.www.model.Clothes;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 时间工具类
 * Created by admin on 2016/10/18.
 */

public class BaseUtils {
    private static final String TAG = "BaseUtils";

    //快速排序
    public static List<Clothes> quickSort(List<Clothes> clothesList, int low, int high) {
        Clothes clothes;

        int left = low;
        int right = high;
        String indexDateStr = clothesList.get(low).getTime();
        if (StringUtils.isNotBlank(indexDateStr) && StringUtils.isNotEmpty(indexDateStr)) {
            while (left < right) {
                while (left < right && clothesList.get(right).getTime().compareTo(indexDateStr) < 0) {
                    right--;
                }

                if (left < right) {
                    clothes = clothesList.get(right);
                    clothesList.set(right, clothesList.get(left));
                    clothesList.set(left, clothes);
                    left++;
                }

                while (left < right && clothesList.get(left).getTime().compareTo(indexDateStr) >= 0) {
                    left++;
                }

                if (left < right) {
                    clothes = clothesList.get(right);
                    clothesList.set(right, clothesList.get(left));
                    clothesList.set(left, clothes);
                    right--;
                }

                if (left > low) {
                    quickSort(clothesList, low, left - 1); // 递归调用
                }
                if (right < high) {
                    quickSort(clothesList, right + 1, high);
                }
            }
        }
        return clothesList;
    }


    //    private List<Map<String, Object>> quickSort(List<Map<String, Object>> maps, int low, int high) {
//        Map<String, Object> tempMap;
//
//        int left = low;
//        int right = high;
//        String indexDateStr = maps.get(low).get("createTime").toString();
//
//        while (left < right) {
//            while (left < right && maps.get(right).get("createTime").toString().compareTo(indexDateStr) < 0) {
//                right--;
//            }
//
//            if (left < right) {
//                tempMap = maps.get(right);
//                maps.set(right, maps.get(left));
//                maps.set(left, tempMap);
//                left++;
//            }
//
//            while (left < right && maps.get(left).get("createTime").toString().compareTo(indexDateStr) >= 0) {
//                left++;
//            }
//
//            if (left < right) {
//                tempMap = maps.get(right);
//                maps.set(right, maps.get(left));
//                maps.set(left, tempMap);
//                right--;
//            }
//
//            if (left > low) {
//                quickSort(maps, low, left - 1); // 递归调用
//            }
//            if (right < high) {
//                quickSort(maps, right + 1, high);
//            }
//        }
//        return maps;
//    }


    /**
     * 获取原图片存储路径
     *
     * @return fileName
     */

//    private String getPhotoPath() {
//        String photoPath = Environment.getExternalStorageDirectory().getPath() + "/Wardrobe/Photo/";
//        // 照片全路径
//        String fileName;
//        // 文件夹路径
//        String str; //照片的命名，目标文件夹下，以当前时间数字串为名称，即可确保每张照片名称不相同。
//        Date date;
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
//        date = new Date();
//        str = format.format(date);
//        File file = new File(photoPath);
//        if (!file.exists()) {//文件夹不存在
//            if (!file.mkdirs()) {
//                Log.e(TAG, "File create is failed. PhotoPath= " + photoPath);
//            }
//        }
//        fileName = photoPath + str + ".jpg";
//        clothes.setImgUrl(fileName);//存入本地数据库中
//        Log.d(TAG, "fileName=" + fileName);
//        return fileName;
//    }
}

