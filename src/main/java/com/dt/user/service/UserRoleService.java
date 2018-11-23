package com.dt.user.service;


import com.dt.user.model.UserRole;

public interface UserRoleService {

	/**
	 * 新增角色信息
	 * @param userRole
	 * @return
	 */
	int addUserRole(UserRole userRole);
}
