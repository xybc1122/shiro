package com.dt.user.service;

import com.dt.user.model.UserUpload;

import java.util.List;

public interface UserUploadService {

    /**
     * 用户上传记录表
     *
     * @param userUpload
     * @return
     */
    int addUserUploadInfo(UserUpload userUpload);

    /**
     * @param uid    用户ID
     * @param sId    店铺ID
     * @param siteId 站点ID
     * @return
     */
    List<UserUpload> getUserUploadInfo(Long uid, Long sId, Long siteId);

    /**
     * 删除 上传记录 更新标示符
     */
    int delUploadInfo( Long id);

    /**
     * 更新用户信息
     */
    int upUploadInfo(UserUpload userUpload);
}
