package com.dt.user.service.impl;

import com.dt.user.mapper.UserUploadMapper;
import com.dt.user.model.UserUpload;
import com.dt.user.service.UserUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserUploadServiceImpl implements UserUploadService {
    @Autowired
    private UserUploadMapper userUploadMapper;

    @Override
    public int addUserUploadInfo(UserUpload userUpload) {
        return userUploadMapper.addUserUploadInfo(userUpload);
    }

    @Override
    public List<UserUpload> getUserUploadInfo(UserUpload upload) {
        return userUploadMapper.getUserUploadInfo(upload);
    }

    @Override
    public int delUploadInfo(Long id) {
        return userUploadMapper.delUploadInfo(id);
    }

    @Override
    public int upUploadInfo(UserUpload userUpload) {
        return userUploadMapper.upUploadInfo(userUpload);
    }
}
