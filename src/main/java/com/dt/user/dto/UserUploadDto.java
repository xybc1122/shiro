package com.dt.user.dto;

import com.dt.user.model.UserUpload;

import java.util.List;

public class UserUploadDto extends UserUpload {

    private List<UserUpload> uploadSuccessList;

    public List<UserUpload> getUploadSuccessList() {
        return uploadSuccessList;
    }

    public void setUploadSuccessList(List<UserUpload> uploadSuccessList) {
        this.uploadSuccessList = uploadSuccessList;
    }
}
