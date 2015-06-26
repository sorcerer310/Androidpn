package com.bsu.system.db;

import java.sql.Connection;
import java.sql.SQLException;
/**
 * 执行数据库事务事件,将处理数据库连接的操作封装到一起,只需覆盖operate函数
 * 进行数据库事务操作就可以
 * @author fengchong
 *
 */
public abstract class DBTransactionEvent {
	public abstract void operate(Connection conn) throws SQLException ,Exception;
}
