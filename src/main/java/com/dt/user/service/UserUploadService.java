package com.dt.user.service;

import com.dt.user.model.UserUpload;

public interface UserUploadService {

    /**
     * 用户上传记录表
     * @param userUpload
     * @return
     */
    int addUserUploadInfo(UserUpload userUpload);
}
