package com.dt.user.mapper;

import com.dt.user.model.UserUpload;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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
            "`file_name`," +
            "`create_date`," +
            "`del_date`,`del_date_id`,`remark`,`file_path`,`write_file_path`,`status`)" +
            "VALUES (#{uid},#{fileName},#{createDate},#{delDate},#{delDateId},#{remark},#{filePath},#{writeFilePath},#{status})")
    int addUserUploadInfo(UserUpload userUpload);
}
