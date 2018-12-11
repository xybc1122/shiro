package com.dt.user.utils;

import com.dt.user.model.FinancialSalesBalance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrUtils {
    /**
     * 两个List比较
     * @param arrOne
     * @param arrTwo
     * @return
     */
    public static boolean equalList(List arrOne, List arrTwo) {
        if (arrOne.size() != arrTwo.size()) {
            return false;
        } else {
            return arrOne.containsAll(arrTwo);
        }
    }
    public static void main(String [] args){
        String [] arr = new String []{"Datum/Uhrzeit","Typ","A"};
        List<String> headList = Arrays.asList(arr);
        List<String> fBalanceHead = new ArrayList<>();
        fBalanceHead.add("Datum/Uhrzeit");
        fBalanceHead.add("Typ");
        fBalanceHead.add("A");
        fBalanceHead.add("A");
       System.out.println(ArrUtils.equalList(headList, fBalanceHead));
    }
}
