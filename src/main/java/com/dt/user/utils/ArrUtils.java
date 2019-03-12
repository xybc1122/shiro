package com.dt.user.utils;

import com.dt.user.model.TableHead;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrUtils {
    /**
     * 引用 拼接新数据
     *
     * @param menuIds
     * @param mId
     * @return
     */
    public static String reference(String menuIds, String mId) {
        String mIds[] = menuIds.split(",");
        List<String> newMenuList = new ArrayList<>(Arrays.asList(mIds));
        newMenuList.add(mId);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < newMenuList.size(); i++) {
            sb.append(newMenuList.get(i));
            sb.append(",");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    /**
     * 两个List比较 顺序不一样也没事
     *
     * @param oneList
     * @param twoList
     * @return
     */
    public static boolean equalList(List<String> oneList, List twoList) {
        if (twoList == null) {
            return false;
        }
        if (oneList.size() != twoList.size()) {
            return false;
        } else {
            return oneList.containsAll(twoList);
        }
    }

    /**
     * 两个List比较 顺序也必须一样
     *
     * @return
     */
    public static boolean eqOrderList(List<String> oneList, List<String> twoList) {
        if (twoList == null) {
            return false;
        }
        if (oneList.size() != twoList.size()) {
            return false;
        }
        for (int i = 0; i < oneList.size(); i++) {
            // System.out.println(twoList.get(i).trim());
            if (!(oneList.get(i)).equals(twoList.get(i).trim())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 泛型List
     *
     * @param <T>
     * @return
     */
    public static <T> T listT(List<?> tList) {
        return (T) tList;
    }

    /**
     * 重新排序 获得数组
     *
     * @param strMid
     * @param strTopOrder 之前数据库查出来的数组
     * @param i
     * @param sort
     * @return
     */
    public static String[] getArr(String[] strMid, String[] strTopOrder, int i, TableHead sort) {
        String[] strNewTopOrder = new String[strMid.length];
        for (int j = 0; j < strNewTopOrder.length; j++) {
            //把 strTopOrder 赋值给strNewTopOrder
            System.out.println(strTopOrder.length);
            if (strTopOrder.length > j) {
                strNewTopOrder[j] = strTopOrder[j];
            } else {
                strNewTopOrder[j] = null;
            }
            if (j == i) {
                strNewTopOrder[j] = sort.getIndex().toString();
            }
        }
        return strNewTopOrder;
    }
}