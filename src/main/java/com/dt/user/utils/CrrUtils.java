package com.dt.user.utils;

public class CrrUtils {


    /**
     * Long 类型++
     *
     * @param numberCount
     */
    public static void inCreateNumberLong(ThreadLocal<Long> numberCount) {
        Long myNumberCount = numberCount.get();
        myNumberCount++;
        numberCount.set(myNumberCount);
    }

    /**
     * Long 类型++
     */
    public static void delCreateNumberLong(ThreadLocal<Long> numberCount) {
        Long myNumberCount = numberCount.get();
        myNumberCount--;
        numberCount.set(myNumberCount);
    }

    //    //并发++
//    public static void inCreateSumNoSku() {
//        Integer sumSku = sumErrorSku.get();
//        sumSku++;
//        sumErrorSku.set(sumSku);
//    }
//

    /**
     * Integer 类型++
     * @param numberCount
     */
    public static void inCreateNumberInteger(ThreadLocal<Integer> numberCount) {
        Integer myNumberCount = numberCount.get();
        myNumberCount++;
        numberCount.set(myNumberCount);
    }
//
//    public static void delCreateCount() {
//        Long typeCount = count.get();
//        typeCount--;
//        count.set(typeCount);
//    }
}
