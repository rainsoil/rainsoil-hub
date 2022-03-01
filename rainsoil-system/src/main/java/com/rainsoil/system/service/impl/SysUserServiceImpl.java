package com.rainsoil.system.service.impl;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;
import com.rainsoil.common.data.mybatis.impl.BaseServiceImpl;
import com.rainsoil.common.security.core.core.LoginUserServices;
import com.rainsoil.core.constant.UserConstants;
import com.rainsoil.core.datascope.DataScopeHolder;
import com.rainsoil.core.datascope.annotation.DataScope;
import com.rainsoil.core.module.system.vo.SysRole;
import com.rainsoil.core.module.system.vo.SysUser;
import com.rainsoil.core.utils.StringUtil;
import com.rainsoil.system.SystemCode;
import com.rainsoil.system.domain.SysPost;
import com.rainsoil.system.domain.SysUserPost;
import com.rainsoil.system.domain.SysUserRole;
import com.rainsoil.system.exception.SystemException;
import com.rainsoil.system.mapper.*;
import com.rainsoil.system.service.*;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
	private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

	@Autowired
	private SysUserMapper userMapper;

	@Autowired
	private SysRoleMapper roleMapper;

	@Autowired
	private SysPostMapper postMapper;

	@Autowired
	private SysUserPostMapper userPostMapper;

	@Autowired
	private SysUserRoleMapper userRoleMapper;

	@Autowired
	private ISysConfigService configService;


	@Autowired
	private ISysUserRoleService sysUserRoleService;


	@Autowired
	private ISysPostService sysPostService;

	@Autowired
	private ISysRoleService sysRoleService;


	@Autowired
	private ISysUserPostService sysUserPostService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public PageInfo<SysUser> page(PageRequestParams<SysUser> pageRequestParams) {
		return super.page(pageRequestParams);
	}

	/**
	 * 根据条件分页查询用户列表
	 *
	 * @param requestParams 用户信息
	 * @return 用户信息集合信息
	 */
	@Override
	@DataScope(deptAlias = "d", userAlias = "u")
	public PageInfo<SysUser> selectUserList(PageRequestParams<SysUser> requestParams) {

		IPage<SysUser> page = userMapper.selectUserList(converIPage(requestParams), requestParams.getParams(), DataScopeHolder.get());
		return converPageInfo(page, requestParams);
	}

	/**
	 * 根据条件分页查询已分配用户角色列表
	 *
	 * @param requestParams 用户信息
	 * @return 用户信息集合信息
	 */
	@Override
	@DataScope(deptAlias = "d", userAlias = "u")
	public PageInfo<SysUser> selectAllocatedList(PageRequestParams<SysUser> requestParams) {
		IPage<SysUser> page = userMapper.selectAllocatedList(converIPage(requestParams), requestParams.getParams(), DataScopeHolder.get());
		return converPageInfo(page, requestParams);
	}

	/**
	 * 根据条件分页查询未分配用户角色列表
	 *
	 * @param requestParams 用户信息
	 * @return 用户信息集合信息
	 */
	@Override
	@DataScope(deptAlias = "d", userAlias = "u")
	public PageInfo<SysUser> selectUnallocatedList(PageRequestParams<SysUser> requestParams) {
		IPage<SysUser> iPage = userMapper.selectUnallocatedList(converIPage(requestParams), requestParams.getParams(), DataScopeHolder.get());
		return converPageInfo(iPage, requestParams);
	}

	/**
	 * 通过用户名查询用户
	 *
	 * @param userName 用户名
	 * @return 用户对象信息
	 */
	@Override
	public SysUser selectUserByLoginName(String userName) {
		return getOne(SysUser.builder().loginName(userName).build());
	}


	/**
	 * 批量删除用户信息
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteUserByIds(String ids) {
		// 校验是否为当前用户
		if (ArrayUtils.contains(Convert.toLongArray(ids), LoginUserServices.getUserId())) {
			throw new SystemException(SystemCode.CANNOT_DELETE_YOURSELF);
		}
		Long[] userIds = Convert.toLongArray(ids);
		for (Long userId : userIds) {
			checkUserAllowed(new SysUser(userId));
		}
		// 删除用户与角色关联
		userRoleMapper.deleteUserRole(userIds);
		// 删除用户与岗位关联
		userPostMapper.deleteUserPost(userIds);
		return userMapper.deleteUserByIds(userIds);
	}


	@Override
	public boolean save(SysUser entity) {
//		entity.setSalt(ShiroUtils.randomSalt());
		entity.setPassword(passwordEncoder.encode(entity.getPassword()));
//		entity.setPassword(passwordService.encryptPassword(entity.getLoginName(), entity.getPassword(), entity.getSalt()));
		//校验登录账号是否存在
		checkLoginNameUnique(entity.getLoginName());
		// 校验手机号是否存在
		checkPhoneUnique(entity);
		//校验邮箱是否存在
		checkEmailUnique(entity);
		boolean save = super.save(entity);
		// 新增用户岗位关联
		insertUserPost(entity);
		// 新增用户与角色管理
		insertUserRole(entity.getUserId(), entity.getRoleIds());
		return save;
	}

	/**
	 * 注册用户信息
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	public boolean registerUser(SysUser user) {
		user.setUserType(UserConstants.REGISTER_USER_TYPE);
		return super.save(user);
	}


	@Override
	public boolean updateById(SysUser entity) {
//		checkUserAllowed(entity);
		// 校验手机号
		checkPhoneUnique(entity);
		// 校验邮箱
		checkEmailUnique(entity);

		Long userId = entity.getUserId();
		// 删除用户与角色关联
		userRoleMapper.deleteUserRoleByUserId(userId);
		// 新增用户与角色管理
		insertUserRole(entity.getUserId(), entity.getRoleIds());
		// 删除用户与岗位关联
		userPostMapper.deleteUserPostByUserId(userId);
		// 新增用户与岗位管理
		insertUserPost(entity);
		return super.updateById(entity);

	}


	/**
	 * 用户授权角色
	 *
	 * @param userId  用户ID
	 * @param roleIds 角色组
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertUserAuth(Long userId, Long[] roleIds) {
		sysUserRoleService.remove(SysUserRole.builder().userId(userId).build());
		insertUserRole(userId, roleIds);
	}

	/**
	 * 修改用户密码
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	public int resetUserPwd(SysUser user) {
		checkUserAllowed(user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return super.updateById(user) ? 1 : 0;
	}

	/**
	 * 新增用户角色信息
	 *
	 * @param userId  用户ID
	 * @param roleIds 角色组
	 */
	public void insertUserRole(Long userId, Long[] roleIds) {
		if (StringUtil.isNotNull(roleIds)) {
			// 新增用户与角色管理
			List<SysUserRole> list = new ArrayList<SysUserRole>();
			for (Long roleId : roleIds) {
				SysUserRole ur = new SysUserRole();
				ur.setUserId(userId);
				ur.setRoleId(roleId);
				list.add(ur);
			}
			if (list.size() > 0) {
				sysUserRoleService.saveBatch(list);
			}
		}
	}

	/**
	 * 新增用户岗位信息
	 *
	 * @param user 用户对象
	 */
	public void insertUserPost(SysUser user) {
		Long[] posts = user.getPostIds();
		if (StringUtil.isNotNull(posts)) {
			// 新增用户与岗位管理
			List<SysUserPost> list = new ArrayList<SysUserPost>();
			for (Long postId : posts) {
				SysUserPost up = new SysUserPost();
				up.setUserId(user.getUserId());
				up.setPostId(postId);
				list.add(up);
			}
			if (list.size() > 0) {
				sysUserPostService.saveBatch(list);
			}
		}
	}

	/**
	 * 校验登录名称是否唯一
	 *
	 * @param loginName 用户名
	 * @return
	 */
	@Override
	public void checkLoginNameUnique(String loginName) {
		long count = super.count(SysUser.builder().loginName(loginName).build());
		if (count > 0) {
			throw new SystemException(SystemCode.DATA_EXIST, loginName);
		}
	}

	/**
	 * 校验手机号码是否唯一
	 *
	 * @param user 用户信息
	 * @return
	 */
	@Override
	public void checkPhoneUnique(SysUser user) {
		Long userId = StringUtil.isNull(user.getUserId()) ? Long.valueOf(-1L) : user.getUserId();
		SysUser info = super.getOne(SysUser.builder().phonenumber(user.getPhonenumber()).build());
		if (StringUtil.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
			throw new SystemException(SystemCode.DATA_EXIST, user.getPhonenumber());
		}
	}

	/**
	 * 校验email是否唯一
	 *
	 * @param user 用户信息
	 * @return
	 */
	@Override
	public void checkEmailUnique(SysUser user) {
		Long userId = StringUtil.isNull(user.getUserId()) ? Long.valueOf(-1L) : user.getUserId();
		SysUser info = getOne(SysUser.builder().email(user.getEmail()).build());
		if (StringUtil.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
			throw new SystemException(SystemCode.DATA_EXIST, user.getEmail());
		}
	}

	/**
	 * 校验用户是否允许操作
	 *
	 * @param user 用户信息
	 */
	@Override
	public void checkUserAllowed(SysUser user) {
		if (StringUtil.isNotNull(user.getUserId()) && user.isAdmin()) {
			throw new SystemException(SystemCode.BUSINESS_UNAUTHORIZED);
		}
	}

	/**
	 * 校验用户是否有数据权限
	 *
	 * @param userId 用户id
	 */
	@Override
	public void checkUserDataScope(Long userId) {
		if (!SysUser.isAdmin(LoginUserServices.getUserId())) {
			SysUser user = new SysUser();
			user.setUserId(userId);
			List<SysUser> users = this.list(user);
			if (StringUtil.isEmpty(users)) {
				throw new SystemException(SystemCode.BUSINESS_UNAUTHORIZED);
			}
		}
	}

	/**
	 * 查询用户所属角色组
	 *
	 * @param userId 用户ID
	 * @return 结果
	 */
	@Override
	public String selectUserRoleGroup(Long userId) {
		List<SysRole> list = sysRoleService.listByUserId(userId);
		StringBuffer idsStr = new StringBuffer();
		for (SysRole role : list) {
			idsStr.append(role.getRoleName()).append(",");
		}
		if (StringUtil.isNotEmpty(idsStr.toString())) {
			return idsStr.substring(0, idsStr.length() - 1);
		}
		return idsStr.toString();
	}

	/**
	 * 查询用户所属岗位组
	 *
	 * @param userId 用户ID
	 * @return 结果
	 */
	@Override
	public String selectUserPostGroup(Long userId) {
		List<SysPost> list = sysPostService.listByUserId(userId);
		StringBuffer idsStr = new StringBuffer();
		for (SysPost post : list) {
			idsStr.append(post.getPostName()).append(",");
		}
		if (StringUtil.isNotEmpty(idsStr.toString())) {
			return idsStr.substring(0, idsStr.length() - 1);
		}
		return idsStr.toString();
	}

	/**
	 * 导入用户数据
	 *
	 * @param userList        用户数据列表
	 * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
	 * @param operName        操作用户
	 * @return 结果
	 */
	@Override
	public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
		if (StringUtil.isNull(userList) || userList.size() == 0) {
			throw new NullPointerException("导入用户数据不能为空！");
		}
		int successNum = 0;
		int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		for (SysUser user : userList) {
			try {
				// 验证是否存在这个用户
				SysUser u = userMapper.selectUserByLoginName(user.getLoginName());
				if (StringUtil.isNull(u)) {
//					user.setPassword(Md5Utils.hash(user.getLoginName() + password));
					user.setCreateBy(operName);
					this.save(user);
					successNum++;
					successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 导入成功");
				} else if (isUpdateSupport) {
					user.setUpdateBy(operName);
					this.updateById(user);
					successNum++;
					successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 更新成功");
				} else {
					failureNum++;
					failureMsg.append("<br/>" + failureNum + "、账号 " + user.getLoginName() + " 已存在");
				}
			} catch (Exception e) {
				failureNum++;
				String msg = "<br/>" + failureNum + "、账号 " + user.getLoginName() + " 导入失败：";
				failureMsg.append(msg + e.getMessage());
				log.error(msg, e);
			}
		}
		if (failureNum > 0) {
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
			throw new RuntimeException(failureMsg.toString());
		} else {
			successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
		}
		return successMsg.toString();
	}

	/**
	 * 用户状态修改
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	public int changeStatus(SysUser user) {
		checkUserAllowed(user);
		return super.updateById(user) ? 1 : 0;
	}

	/**
	 * 修改密码
	 *
	 * @param user        用户
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @return void
	 * @since 2021/10/1
	 */
	@Override
	public void resetPwd(SysUser user, String oldPassword, String newPassword) {
		if (!passwordEncoder.matches(user.getPassword(), oldPassword)) {
			throw new SystemException(SystemCode.OLD_PASSWORD_ERROR);
		}
		if (passwordEncoder.matches(user.getPassword(), newPassword)) {
			throw new SystemException(SystemCode.NEW_OLD_PASSWORD_EQUAL);
		}
		user.setPassword(passwordEncoder.encode(newPassword));
//		user.setSalt(ShiroUtils.randomSalt());
//		user.setPassword(passwordService.encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
		user.setPwdUpdateDate(DateUtil.date());

		resetUserPwd(user);
	}
}
