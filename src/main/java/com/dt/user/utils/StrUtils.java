package com.dt.user.utils;

import com.dt.user.model.FinancialSalesBalance;
import org.apache.commons.lang3.StringUtils;

public class StrUtils {
    /**
     * 通用替换字符串转Double
     */
    public static Double repDouble(String number) {
        if (StringUtils.isBlank(number)) {
            return null;
        }
        return Double.parseDouble(number);
    }

    /**
     * 通用替换字符串判断
     */
    public static String repString(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        int l = str.indexOf("\\");
        int k = str.indexOf("'");
        if (k == -1 && l == -1) {
            return str;
        }
        return str.replace("'", "").replace("\\", "");
    }

    /**
     * 字符串替换Double
     *
     * @param number
     * @return
     */
    public static Double replaceDouble(String number) {
        if (StringUtils.isBlank(number)) {
            return null;
        }
        int i = number.indexOf(".");
        int j = number.indexOf(",");
        int k = number.indexOf("?");
        //如果都有 并且 j > i 等于德国的
        if (i != -1 && j != -1 && j > i) {
            String newNumber = number.replace(".", "").replace(',', '.');
            return Double.parseDouble(newNumber);
        }
        //如果都有 并且 j > i 等于加拿大的
        else if (i != -1 && j != -1 && j < i) {
            String newNumber = number.replace(",", "");
            return Double.parseDouble(newNumber);
        }
        //法国会出现,号
        else if (k != -1 && j != -1) {
            String newNumber = number.replace("?", "").replace(',', '.');
            return Double.parseDouble(newNumber);
        }
        return Double.parseDouble(number.replace(',', '.'));
    }

    /**
     * 返回Integer 类型
     *
     * @param number
     * @return
     */
    public static Integer replaceInteger(String number) {
        if (StringUtils.isBlank(number)) {
            return null;
        }
        return Integer.parseInt(number);
    }

    /**
     * 返回Long 类型
     *
     * @param number
     * @return
     */
    public static Long replaceLong(String number) {
        if (StringUtils.isBlank(number)) {
            return null;
        }
        return Long.parseLong(number);
    }

    /**
     * 封装Append
     */
    public static StringBuilder appBuider(StringBuilder sb, String str) {
        if (StringUtils.isEmpty(str)) {
            sb.append(str);
        } else {
            sb.append("'" + str + "'");
        }
        return sb;
    }

    /**
     * 判断TypeName是否== xxxxx  计算一些数据
     */
    public static void isService(String typeName, FinancialSalesBalance fsb) {
        if (StringUtils.isNotEmpty(typeName)) {
            //促销费用（abs(运费)<abs(促销费用)）
            if (Math.abs(fsb.getShippingCredits()) < Math.abs(fsb.getPromotionalRebates())) {
                fsb.setNewPromotionalRebates(fsb.getShippingCredits() + fsb.getPromotionalRebates());
            }
            //促销费用（abs(运费)>abs(促销费用)）
            if (Math.abs(fsb.getShippingCredits()) > Math.abs(fsb.getPromotionalRebates())) {
                fsb.setNewShippingCredits(fsb.getShippingCredits());
                fsb.setNewPromotionalRebates(fsb.getPromotionalRebates());
            }
            //促销费用（abs(运费)=abs(促销费用)）
            if (Math.abs(fsb.getShippingCredits()) == Math.abs(fsb.getPromotionalRebates())) {
                fsb.setNewShippingCredits(0.0);
                fsb.setNewPromotionalRebates(0.0);
            }
            if (fsb.getNewShippingCredits() == null) {
                fsb.setNewShippingCredits(0.0);
            }
            if (fsb.getFbaFee() == null) {
                fsb.setFbaFee(0.0);
            }
            fsb.setNewShippingFba(fsb.getNewShippingCredits() + fsb.getFbaFee());
            if (!typeName.equals("退货")) {
                fsb.setQuantity(fsb.getoQuantity());
            }
            if (typeName.equals("服务费")) {
                fsb.setServiceFee(fsb.getOtherTransactionFees());
            } else if (typeName.equals("转账")) {
                fsb.setTransfer(fsb.getTotal());
            } else if (typeName.equals("调整")) {
                fsb.setAdjustment(fsb.getOther());
                fsb.setAdjustmentQty(fsb.getoQuantity());
            } else if (typeName.equals("库存费")) {
                fsb.setFbaInventoryFee(fsb.getOther());
            } else if (typeName.equals("秒杀费")) {
                fsb.setLightningDealFee(fsb.getOtherTransactionFees());
            } else if (typeName.equals("退货")) {
                fsb.setRefundQuantity(fsb.getoQuantity());
            } else if (typeName.equals("订单")) {
                fsb.setOrderQty(fsb.getoQuantity());
            }
        } else {
            fsb.setQuantity(fsb.getoQuantity());
        }


    }

//    public static void main(String[] args) {
//        String b = "-12.8624,00";
//        String a = "-22,224.88";
//        System.out.println(StrUtils.replaceDouble(a));
//    }
}
