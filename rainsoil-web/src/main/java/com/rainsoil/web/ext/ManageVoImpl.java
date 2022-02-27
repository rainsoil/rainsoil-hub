//package com.rainsoil.web.ext;
//
//import com.baomidou.mybatisplus.core.conditions.Wrapper;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.rainsoil.common.core.page.PageInfo;
//import com.rainsoil.common.core.page.PageRequestParams;
//import com.rainsoil.common.data.mybatis.BaseMapperPlus;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.io.Serializable;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//
///**
// * manage vo转换
// *
// * @author luyanan
// * @since 2021/9/19
// **/
//public class ManageVoImpl<M extends BaseMapperPlus<T>, T, V> extends ManageImpl<M, T> implements IManageVo<V, T> {
//	@Override
//	protected Class<T> currentMapperClass() {
//		return (Class<T>) this.getResolvableType().as(ManageVoImpl.class).getGeneric(0).getType();
//	}
//
//	@Override
//	protected Class<T> currentModelClass() {
//		return (Class<T>) this.getResolvableType().as(ManageVoImpl.class).getGeneric(1).getType();
//	}
//
//	protected Class<V> currentVoClass() {
//		return (Class<V>) this.getResolvableType().as(ManageVoImpl.class).getGeneric(2).getType();
//	}
//
//
//}
