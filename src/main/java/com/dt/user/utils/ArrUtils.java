package com.dt.user.utils;
import java.util.List;

public class ArrUtils {
    /**
     * 两个List比较
     * @param arrOne
     * @param arrTwo
     * @return
     */
    public static boolean equalList(List<String> arrOne, List arrTwo) {
        if (arrOne.size() != arrTwo.size()) {
            return false;
        } else {
            return arrOne.containsAll(arrTwo);
        }
    }

}
