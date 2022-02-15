package com.rainsoil.common.framework.jdbc;

import cn.hutool.db.ds.simple.AbstractDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 简单实现DataSource
 *
 * @author luyanan
 * @since 2021/3/9
 **/
public class SimpleDataSource extends AbstractDataSource {


	private String url;

	private String userName;

	private String passWord;

	public SimpleDataSource(String driverClassName, String url, String userName, String passWord) {
		this.url = url;
		this.userName = userName;
		this.passWord = passWord;

	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(this.url, this.userName, this.passWord);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return DriverManager.getConnection(this.url, username, password);
	}

}
