package com.rainsoil.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;
import com.rainsoil.common.data.mybatis.impl.BaseServiceImpl;
import com.rainsoil.core.constant.Constants;
import com.rainsoil.core.constant.UserConstants;
import com.rainsoil.core.utils.StringUtil;
import com.rainsoil.system.SystemCode;
import com.rainsoil.system.domain.SysConfig;
import com.rainsoil.system.exception.SystemException;
import com.rainsoil.system.mapper.SysConfigMapper;
import com.rainsoil.system.service.ISysConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 参数配置 服务层实现
 *
 * @author ruoyi
 */
@Service
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

	/**
	 * 项目启动时，初始化参数到缓存
	 */
	@PostConstruct
	public void init() {
		loadingConfigCache();
	}


	@Override
	public PageInfo<SysConfig> page(PageRequestParams<SysConfig> pageRequestParams) {
		LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper();
		SysConfig params = pageRequestParams.getParams();
		queryWrapper.setEntity(params);
		queryWrapper.apply(!StrUtil.isBlankIfStr(params.getBeginDate()),
				"date_format(create_time,'%y%m%d') >= date_format({0},'%y%m%d')", params.getBeginDate());
		queryWrapper.apply(!StrUtil.isBlankIfStr(params.getEndDate()),
				"date_format(create_time,'%y%m%d') <= date_format({0},'%y%m%d')", params.getEndDate());

		return super.page(pageRequestParams, queryWrapper);
	}

	/**
	 * 根据键名查询参数配置信息
	 *
	 * @param configKey 参数key
	 * @return 参数键值
	 */
	@Override
	public String selectConfigByKey(String configKey) {

		SysConfig retConfig = getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, configKey));
		if (ObjectUtil.isNotNull(retConfig)) {
			return retConfig.getConfigValue();
		}
		return StringUtil.EMPTY;
	}

	/**
	 * 查询参数配置列表
	 *
	 * @param config 参数配置信息
	 * @return 参数配置集合
	 */
	@Override
	public List<SysConfig> selectConfigList(SysConfig config) {

		return list(new LambdaQueryWrapper<>(config));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean save(SysConfig entity) {
		checkConfigKeyUnique(entity);
		boolean save = super.save(entity);

		return save;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean updateById(SysConfig entity) {
		checkConfigKeyUnique(entity);
		boolean update = super.updateById(entity);

		return update;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean removeByIds(Collection<?> idList) {
		for (Object configId : idList) {
			SysConfig config = getById((Serializable) configId);
			if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
				throw new SystemException(SystemCode.CONFIG_SYSTEM, config.getConfigName());
			}
		}
		return super.removeByIds(idList);
	}

	/**
	 * 加载参数缓存数据
	 */
	@Override
	public void loadingConfigCache() {
	}

	/**
	 * 清空参数缓存数据
	 */
	@Override
	public void clearConfigCache() {

	}

	/**
	 * 重置参数缓存数据
	 */
	@Override
	public void resetConfigCache() {
		clearConfigCache();
		loadingConfigCache();
	}

	/**
	 * 校验参数键名是否唯一
	 *
	 * @param config 参数配置信息
	 * @return 结果
	 */
	@Override
	public void checkConfigKeyUnique(SysConfig config) {
		if (StrUtil.isBlank(config.getConfigKey())) {
			log.error("configKey为空");
			throw new SystemException(SystemCode.BAD_REQUEST);
		}

		Long configId = StringUtil.isNull(config.getConfigId()) ? Long.valueOf(-1L) : config.getConfigId();
		SysConfig info = getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, config.getConfigKey()));
		if (StringUtil.isNotNull(info) && info.getConfigId().longValue() != configId.longValue()) {
			throw new SystemException(SystemCode.DATA_EXIST, config.getConfigKey());
		}
	}

	/**
	 * 获取cache name
	 *
	 * @return 缓存名
	 */
	private String getCacheName() {
		return Constants.SYS_CONFIG_CACHE;
	}

	/**
	 * 设置cache key
	 *
	 * @param configKey 参数键
	 * @return 缓存键key
	 */
	private String getCacheKey(String configKey) {
		return Constants.SYS_CONFIG_KEY + configKey;
	}
}
