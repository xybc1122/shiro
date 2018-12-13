package com.dt.user.service.BasePublicService;

import com.dt.user.model.BasePublicModel.BasicPublicCurrency;
import java.util.List;

public interface BasicPublicCurrencyService {

    /**
     * 查询币别所有相关信息
     *
     * @return
     */
    List<BasicPublicCurrency> findByListCurrency();
}
