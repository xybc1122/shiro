package com.dt.user.provider;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class BasicSalesAmazonCsvTxtXslHeaderProvider {

    public String findHeadInfo(Map<String, Object> mapHead) {
        Long seId = (Long) mapHead.get("seId");
        Integer tbId = (Integer) mapHead.get("tbId");
        Integer areaId = (Integer) mapHead.get("areaId");
        return new SQL() {{
            SELECT("import_templet");
            FROM("`basic_sales_amazon_csv_txt_xsl_header`");
            if (seId != null) {
                WHERE("site_id=" + seId);
            }
            if (tbId != null) {
                WHERE("tb_id=" + tbId);
            }
            if (areaId != null) {
                WHERE("area_id=" + areaId);
            }
        }}.toString();

    }
}
