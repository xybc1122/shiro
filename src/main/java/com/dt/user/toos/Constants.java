package com.dt.user.toos;

public interface Constants {
	/**
	 * 分页页数
	 */
	int PAGE_SIEZ = 10;
	/**
	 * 文件存放地址
	 */
	String SAVEFILEPATH="D:/csv/";
	/**
	 * 响应请求成功
	 */
	String HTTP_RES_CODE_200_VALUE = "success";
	/**
	 * 响应请求成功code
	 */
	Integer HTTP_RES_CODE_200 = 200;
	/**
	 *  系统错误
	 */
	Integer HTTP_RES_CODE = -1;
	/**
	 * 美国时间解析
	 */
	String USA_TIME="MMM d, yyyy HH:mm:ss a";
	/**
	 * 加拿大时间解析
	 */
	String CANADA_TIME = "yyyy-MM-dd HH:mm:ss a";
	/**
	 * 澳大利亚时间转换
	 */
	String AUSTRALIA_TIME ="dd/MM/yyyy HH:mm:ss a";

}
