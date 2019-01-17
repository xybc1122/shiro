package com.dt.user.provider;

import com.dt.user.model.UserUpload;
import com.dt.user.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class UserUploadProvider {

    public String findUpInfo(UserUpload upload) {
        return new SQL() {{
            SELECT("`id`,`name`,`create_date`,`del_date`,`del_date_id`,`remark`,`file_path`,`write_file_path`,`status`");
            FROM("system_user_upload");
            if (upload.getUid() != null) {
                WHERE("uid=" + upload.getUid());
            }
            if (upload.getShopId() != null) {
                WHERE("shop_id=" + upload.getShopId());
            }
            if (upload.getSiteId() != null) {
                WHERE("site_id=" + upload.getSiteId());
            }
            if (upload.getAreaId() != null) {
                WHERE("area_id=" + upload.getAreaId());
            }
            if (upload.getTbId() != null) {
                WHERE("tb_id=" + upload.getTbId());
            }
            WHERE("del_mark=0");
        }}.toString();
    }

    /**
     * 更新用户信息
     * @param upload
     * @return
     */
    public String upUploadInfo(UserUpload upload) {
//        UPDATE `system_user_upload`\n" +
//        "SET\n" +
//                "`remark` = #{remark},\n" +
//                "`status` = #{status}\n" +
//                "WHERE `id` = #{id}
        return new SQL() {{
            UPDATE("`system_user_upload`");
            if (StringUtils.isNotBlank(upload.getRemark())) {

            }

           // WHERE("uid=" + uid);
        }}.toString();

    }
}
