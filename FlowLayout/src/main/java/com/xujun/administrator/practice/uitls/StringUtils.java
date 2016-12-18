package com.xujun.administrator.practice.uitls;

import java.util.ArrayList;
import java.util.List;

/**
 * @ explain:
 * @ author：xujun on 2016/6/25 11:25
 * @ email：gdutxiaoxu@163.com
 */
public class StringUtils {

    static String[] strArray = new String[]{
            "女神", "偶像", "超级美女", "么么哒", "气质", "颜值高",
            "坦率", "天真", "活泼可爱", "超级么么哒", "姐妹气质", "唯美",
            "请问去哪里找得到这么漂亮的女生", "真的是美若天仙，人间油品，人世间再也找不到这么漂亮的美女了",
            "活泼",  "气质", "颜值高",
            "唐嫣这么漂亮的女生", "人间油品，人世间再也找不到这么漂亮的美女了",
            "唐嫣女神", "赵丽颖女神", "刘亦菲女神", "赵敏",
    };

/*
    static String[] strArray = new String[]{
            "女神", "偶像", "超级美女", "么么哒", "气质", "颜值高"

    };
*/


    public static List<String> getDatas(String[] strs) {
        if (strs == null || strs.length == 0) {
            return null;
        }
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            stringArrayList.add(str);

        }
        return stringArrayList;
    }

    public static List<String> getDatas(){
        return getDatas(strArray);
    }
}
