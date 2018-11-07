package com.dt.user.service;

import com.dt.user.model.TableHead;

import java.util.List;

public interface TableHeadService {

    List<TableHead> findByMenuIdHeadList(Long id);
}
