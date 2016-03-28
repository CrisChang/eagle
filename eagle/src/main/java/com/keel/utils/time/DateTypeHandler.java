package com.keel.utils.time;

import java.sql.SQLException;
import java.util.Date;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * ibatis层对日期的处理，数据库使用的类型为bigint,java使用java.util.Date
 * @author songdan
 * @date 2016年3月1日
 * @Description TODO
 * @version V1.0
 */
public class DateTypeHandler implements TypeHandlerCallback{

	@Override
	public void setParameter(ParameterSetter setter, Object parameter) throws SQLException {
		setter.setLong(((Date)parameter).getTime());
	}

	@Override
	public Object getResult(ResultGetter getter) throws SQLException {
		return TimeUtils.timestamp2Date(getter.getLong());
	}

	@Override
	public Object valueOf(String s) {
		return new Date(Long.parseLong(s));
	}


}
