package com.dt.user.service.impl;

import com.dt.user.mapper.UserUploadMapper;
import com.dt.user.model.UserUpload;
import com.dt.user.service.UserUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUploadServiceImpl implements UserUploadService {
    @Autowired
    private UserUploadMapper userUploadMapper;

    @Override
    public int addUserUploadInfo(UserUpload userUpload) {
        return userUploadMapper.addUserUploadInfo(userUpload);
    }
}
