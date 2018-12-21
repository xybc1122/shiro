package com.dt.user.utils;

import com.dt.user.model.FinancialSalesBalance;
import org.apache.commons.lang3.StringUtils;

public class StrUtils {
    /**
     * 字符串替换Double
     *
     * @param number
     * @return
     */
    public static Double replaceDouble(String number) {
        if (org.apache.commons.lang3.StringUtils.isBlank(number)) {
            return null;
        }
        int i = number.indexOf(".");
        int j = number.indexOf(",");
        int k = number.indexOf("?");
        //如果都有 并且 j > i 等于德国的
        if (i != -1 && j != -1 && j > i) {
            String newNumber = number.replace(".", "").replace(',', '.');
            return Double.parseDouble(newNumber);
        } else
            //如果都有 并且 j > i 等于加拿大的
            if (i != -1 && j != -1 && j < i) {
                String newNumber = number.replace(",", "");
                return Double.parseDouble(newNumber);
            } else
                //法国会出现,号
                if (k != -1 && j != -1) {
                    String newNumber = number.replace("?", "").replace(',', '.');
                    return Double.parseDouble(newNumber);
                }
        return Double.parseDouble(number.replace(',', '.'));
    }

    /**
     * 返回字符串
     *
     * @param number
     * @return
     */
    public static String replaceString(String number) {
        if (org.apache.commons.lang3.StringUtils.isBlank(number)) {
            return null;
        }
        int i = number.indexOf("'");
        if (i == -1) {
            return number;
        }
        return number.replace("'", "");
    }

    /**
     * 返回Long 类型
     *
     * @param number
     * @return
     */
    public static Long replaceLong(String number) {
        if (org.apache.commons.lang3.StringUtils.isBlank(number)) {
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
     * 判断TypeName是否== xxxxx
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
        }


    }

    public static void main(String[] args) {
        String b = "-12.8624,00";
        String a = "-22,224.88";
        System.out.println(StrUtils.replaceDouble(a));
    }
}
