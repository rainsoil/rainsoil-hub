package com.rainsoil.system;


import com.rainsoil.core.message.GlobalCode;

/**
 * 系统模块异常码
 *
 * @author luyanan
 * @since 2021/9/21
 **/
public interface SystemCode extends GlobalCode {

	/**
	 * 系统内置参数
	 *
	 * @since 2021/9/22
	 */

	int CONFIG_SYSTEM = 100001;


	/**
	 * 部门已经分配
	 *
	 * @since 2021/9/23
	 */

	int POST_USER_EXIST = 100002;
	/**
	 * 上级部门不能是自己
	 *
	 * @since 2021/9/25
	 */

	int DEPT_PARENT_OWN = 100003;


	/**
	 * 部门包含未停用的子部门
	 *
	 * @author luyanan
	 * @since 2021/9/25
	 */
	int DEPT_HAVE_NORMAL_DEPT = 100004;


	/**
	 * 存在下级部门
	 *
	 * @since 2021/9/25
	 */

	int HAVE_SUN_DEPT = 100005;


	/**
	 * 部门存在用户
	 *
	 * @since 2021/9/25
	 */

	int DEPT_HAVE_USER = 100006;


	/**
	 * 存在子菜单
	 *
	 * @since 2021/9/27
	 */

	int MENU_HAVE_SUN_MENU = 100007;

	/**
	 * 菜单下存在角色
	 *
	 * @since 2021/9/27
	 */

	int MENU_HAVE_ROLE = 100008;


	/**
	 * 角色已经分配用户
	 *
	 * @since 2021/9/28
	 */

	int ROLE_HAVE_USER = 100009;


	/**
	 * 不能删除自己
	 *
	 * @since 2021/10/1
	 */

	int CANNOT_DELETE_YOURSELF = 100010;

	/**
	 * 旧密码错误
	 *
	 * @since 2021/10/1
	 */
	int OLD_PASSWORD_ERROR = 100011;


	/**
	 * 新旧密码一样
	 *
	 * @since 2021/10/1
	 */

	int NEW_OLD_PASSWORD_EQUAL = 100012;

	/**
	 * 部门已经停用
	 *
	 * @since 2022/2/21
	 */

	int DEPT_STATUS_DISABLE = 100013;
}
