package com.shiro.demoshiro.config;

public interface Constants {
	// userSession
	String USER_SESSION = "userSession";
	// 分页页数
	int PAGE_SIEZ = 10;

	// 响应请求成功
	String HTTP_RES_CODE_200_VALUE = "success";
	// 系统错误
	String HTTP_RES_CODE_500_VALUE = "fial";
	// 响应请求成功code
	Integer HTTP_RES_CODE_200 = 200;
	// 系统错误
	Integer HTTP_RES_CODE = -1;


	// COOKIE
	int COOKIE_TOKEN_MEMBER_TIME = (60 * 60 * 24 * 3);

	// cookie token名称
	String COOKIE_MEMBER_TOKEN = "cookie_member_token";

}
