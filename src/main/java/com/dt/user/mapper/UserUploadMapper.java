package com.dt.user.mapper;

import com.dt.user.model.UserUpload;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserUploadMapper {

    /**
     * 用户上传记录表
     *
     * @param userUpload
     * @return
     */
    @Insert("INSERT INTO`system_user_upload`" +
            "(`uid`," +
            "`name`," +
            "`create_date`," +
            "`del_date`,`del_date_id`,`remark`,`file_path`,`write_file_path`,`status`,`shop_id`,`site_id`)" +
            "VALUES (#{uid},#{name},#{createDate},#{delDate},#{delDateId},#{remark},#{filePath},#{writeFilePath},#{status},#{shopId},#{siteId})")
    int addUserUploadInfo(UserUpload userUpload);

    /**
     * 查询用户上传记录
     */
    @Select("SELECT`id`,`name`,`create_date`,`del_date`,`del_date_id`,`remark`,`file_path`,`write_file_path`,`status`\n" +
            "FROM `system_user_upload`\n" +
            "WHERE uid=#{uid} and shop_id=#{sId} and site_id=#{siteId} and del_mark=0")
    List<UserUpload> getUserUploadInfo(@Param("uid") Long uid, @Param("sId") Long sId, @Param("siteId") Long siteId);

    /**
     * 删除 上传记录 更新标示符
     */
    @Update("UPDATE `system_user_upload`\n" +
            "SET \n" +
            "`del_mark` = 1\n" +
            "WHERE `id` = #{id};")
    int delUploadInfo(@Param("id") Long id);
}
