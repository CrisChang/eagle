package com.poison.resource.dao;

import java.util.List;
import java.util.Map;

public interface SensitiveWordDAO {

	public List<String> selectSensitiveWord(Map<String, Object> map);
}
