package com.dt.user.service.BasePublicService;

import com.dt.user.dto.CurrencyDto;
import java.util.List;

public interface BasicPublicCurrencyService {

    /**
     * 查询币别所有相关信息
     *
     * @return
     */
    List<CurrencyDto> findByListCurrency();
}
