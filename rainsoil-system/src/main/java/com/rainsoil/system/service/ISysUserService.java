package com.rainsoil.system.service;


import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;
import com.rainsoil.common.data.mybatis.IBaseService;
import com.rainsoil.core.module.system.vo.SysUser;

import java.util.List;

/**
 * 用户 业务层
 *
 * @author ruoyi
 */
public interface ISysUserService extends IBaseService<SysUser> {
	/**
	 * 根据条件分页查询用户列表
	 *
	 * @param user 用户信息
	 * @return 用户信息集合信息
	 */
	PageInfo<SysUser> selectUserList(PageRequestParams<SysUser> requestParams);

	/**
	 * 根据条件分页查询已分配用户角色列表
	 *
	 * @param requestParams 请求查询
	 * @return 用户信息集合信息
	 */
	PageInfo<SysUser> selectAllocatedList(PageRequestParams<SysUser> requestParams);

	/**
	 * 根据条件分页查询未分配用户角色列表
	 *
	 * @param user 用户信息
	 * @return 用户信息集合信息
	 */
	PageInfo<SysUser> selectUnallocatedList(PageRequestParams<SysUser> requestParams);

	/**
	 * 通过用户名查询用户
	 *
	 * @param userName 用户名
	 * @return 用户对象信息
	 */
	SysUser selectUserByLoginName(String userName);


	/**
	 * 批量删除用户信息
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 * @throws Exception 异常
	 */
	int deleteUserByIds(String ids);


	/**
	 * 注册用户信息
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	boolean registerUser(SysUser user);


	/**
	 * 用户授权角色
	 *
	 * @param userId  用户ID
	 * @param roleIds 角色组
	 */
	void insertUserAuth(Long userId, Long[] roleIds);

	/**
	 * 修改用户密码信息
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	int resetUserPwd(SysUser user);

	/**
	 * 校验用户名称是否唯一
	 *
	 * @param loginName 登录名称
	 * @return 结果
	 */
	void checkLoginNameUnique(String loginName);

	/**
	 * 校验手机号码是否唯一
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	void checkPhoneUnique(SysUser user);

	/**
	 * 校验email是否唯一
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	void checkEmailUnique(SysUser user);

	/**
	 * 校验用户是否允许操作
	 *
	 * @param user 用户信息
	 */
	void checkUserAllowed(SysUser user);

	/**
	 * 校验用户是否有数据权限
	 *
	 * @param userId 用户id
	 */
	void checkUserDataScope(Long userId);

	/**
	 * 根据用户ID查询用户所属角色组
	 *
	 * @param userId 用户ID
	 * @return 结果
	 */
	String selectUserRoleGroup(Long userId);

	/**
	 * 根据用户ID查询用户所属岗位组
	 *
	 * @param userId 用户ID
	 * @return 结果
	 */
	String selectUserPostGroup(Long userId);

	/**
	 * 导入用户数据
	 *
	 * @param userList        用户数据列表
	 * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
	 * @param operName        操作用户
	 * @return 结果
	 */
	String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName);

	/**
	 * 用户状态修改
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	int changeStatus(SysUser user);


	/**
	 * 修改密码
	 *
	 * @param user        用户
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @return void
	 * @since 2021/10/1
	 */
	void resetPwd(SysUser user, String oldPassword, String newPassword);
}
