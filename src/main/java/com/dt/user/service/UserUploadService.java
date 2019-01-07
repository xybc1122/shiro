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
     * @param upload
     * @return
     */
    List<UserUpload> getUserUploadInfo(UserUpload upload);

    /**
     * 删除 上传记录 更新标示符
     */
    int delUploadInfo(Long id);

    /**
     * 更新用户信息
     */
    int upUploadInfo(UserUpload userUpload);
}
