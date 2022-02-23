package com.rainsoil.system.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rainsoil.common.data.mybatis.impl.BaseServiceImpl;
import com.rainsoil.core.utils.StringUtil;
import com.rainsoil.system.SystemCode;
import com.rainsoil.system.domain.SysPost;
import com.rainsoil.system.domain.SysUserPost;
import com.rainsoil.system.exception.SystemException;
import com.rainsoil.system.mapper.SysPostMapper;
import com.rainsoil.system.service.ISysPostService;
import com.rainsoil.system.service.ISysUserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 岗位信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class SysPostServiceImpl extends BaseServiceImpl<SysPostMapper, SysPost> implements ISysPostService {

	@Autowired
	private ISysUserPostService sysUserPostService;


	/**
	 * 查询所有岗位
	 *
	 * @return 岗位列表
	 */
	@Override
	public List<SysPost> selectPostAll() {
		return super.list();
	}

	/**
	 * 根据用户ID查询岗位
	 *
	 * @param userId 用户ID
	 * @return 岗位列表
	 */
	@Override
	public List<SysPost> selectPostsByUserId(Long userId) {
		List<SysPost> userPosts = null;
		List<SysUserPost> sysUserPosts = sysUserPostService.list(new LambdaQueryWrapper<SysUserPost>().eq(SysUserPost::getUserId, userId));
		if (CollectionUtil.isNotEmpty(sysUserPosts)) {
			List<Long> postIds = sysUserPosts.stream().map(SysUserPost::getPostId).distinct().collect(Collectors.toList());

			userPosts = super.listByIds(postIds);
		} else {
			userPosts = new ArrayList<>();
		}
		List<SysPost> posts = list();
		for (SysPost post : posts) {
			for (SysPost userRole : userPosts) {
				if (post.getPostId().longValue() == userRole.getPostId().longValue()) {
					post.setFlag(true);
					break;
				}
			}
		}
		return posts;
	}


	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		for (Serializable postId : idList) {
			SysPost post = getById(postId);
			if (sysUserPostService.count(new LambdaQueryWrapper<SysUserPost>().eq(SysUserPost::getPostId, post.getPostId())) > 0) {
				throw new SystemException(SystemCode.POST_USER_EXIST, post.getPostName());
			}
		}
		return super.removeByIds(idList);
	}


	/**
	 * 校验岗位名称是否唯一
	 *
	 * @param post 岗位信息
	 * @return 结果
	 */
	@Override
	public void checkPostNameUnique(SysPost post) {
		Long postId = StringUtil.isNull(post.getPostId()) ? -1L : post.getPostId();
		SysPost info = getOne(new LambdaQueryWrapper<SysPost>().eq(SysPost::getPostName, post.getPostName()));
		if (StringUtil.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
			throw new SystemException(SystemCode.DATA_EXIST, post.getPostName());
		}
	}

	/**
	 * 校验岗位编码是否唯一
	 *
	 * @param post 岗位信息
	 * @return 结果
	 */
	@Override
	public void checkPostCodeUnique(SysPost post) {
		Long postId = StringUtil.isNull(post.getPostId()) ? -1L : post.getPostId();
		SysPost info = getOne(new LambdaQueryWrapper<SysPost>().eq(SysPost::getPostCode, post.getPostCode()));
		if (StringUtil.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
			throw new SystemException(SystemCode.DATA_EXIST, post.getPostCode());
		}
	}

	@Override
	public List<SysPost> listByUserId(Long userId) {

		List<SysUserPost> sysUserPosts = sysUserPostService.list(SysUserPost.builder().userId(userId).build());
		if (CollectionUtil.isEmpty(sysUserPosts)) {
			return new ArrayList<>();
		}
		return super.listByIds(sysUserPosts.stream().map(SysUserPost::getPostId).distinct().collect(Collectors.toList()));
	}

	@Override
	public boolean save(SysPost entity) {

		checkPostCodeUnique(entity);
		checkPostNameUnique(entity);
		return super.save(entity);
	}

	@Override
	public boolean updateById(SysPost entity) {
		checkPostCodeUnique(entity);
		checkPostNameUnique(entity);
		return super.updateById(entity);
	}
}
