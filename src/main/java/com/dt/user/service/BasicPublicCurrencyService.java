package com.dt.user.service;

import com.dt.user.model.BasicPublicCurrency;
import java.util.List;

public interface BasicPublicCurrencyService {

    /**
     * 查询币别所有相关信息
     *
     * @return
     */
    List<BasicPublicCurrency> findByListCurrency();
}
