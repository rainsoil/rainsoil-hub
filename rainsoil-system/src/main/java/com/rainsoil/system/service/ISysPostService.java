package com.rainsoil.system.service;



import com.rainsoil.common.data.mybatis.IBaseService;
import com.rainsoil.system.domain.SysPost;

import java.util.List;

/**
 * 岗位信息 服务层
 *
 * @author ruoyi
 */
public interface ISysPostService extends IBaseService<SysPost> {


	/**
	 * 查询所有岗位
	 *
	 * @return 岗位列表
	 */
	public List<SysPost> selectPostAll();

	/**
	 * 根据用户ID查询岗位
	 *
	 * @param userId 用户ID
	 * @return 岗位列表
	 */
	public List<SysPost> selectPostsByUserId(Long userId);


	/**
	 * 校验岗位名称
	 *
	 * @param post 岗位信息
	 * @return 结果
	 */
	void checkPostNameUnique(SysPost post);

	/**
	 * 校验岗位编码
	 *
	 * @param post 岗位信息
	 * @return 结果
	 */
	void checkPostCodeUnique(SysPost post);

	/**
	 * 根据用户id查询岗位
	 *
	 * @param userId
	 * @return java.util.List<com.rainsoil.system.domain.SysPost>
	 * @since 2021/10/1
	 */
	List<SysPost> listByUserId(Long userId);
}
