package com.poison.story.model;

import com.keel.common.lang.BaseDO;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/8
 * Time: 18:53
 */
public class StoryEnumerate extends BaseDO{

    private static final long serialVersionUID = 5233845182417707320L;
    private ObjectMapper objectMapper;

    private int id;//主键
    private String channel;//频道
    private String autoType;//排行榜类型
    private String caseType;//枚举类型
    private String style;//风格类型
    private String name;//名字
    private String customKey;//个性化字段
    private String interfaceName;//接口名称
    private Map<String,Object> para;//自定义参数

    public Map<String, Object> getPara() {
        return para;
    }

    public void setPara(String para) {
        Map<String,Object> map = new HashMap<String, Object>();
        if(objectMapper == null) {
            objectMapper = new ObjectMapper();

            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            objectMapper.configure(DeserializationConfig.Feature.WRAP_EXCEPTIONS, false);
        }
        try {
            map = objectMapper.readValue(para,Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            map = new HashMap<String, Object>();
        }
        this.para = map;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getAutoType() {
        return autoType;
    }

    public void setAutoType(String autoType) {
        this.autoType = autoType;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomKey() {
        return customKey;
    }

    public void setCustomKey(String customKey) {
        this.customKey = customKey;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }
}
