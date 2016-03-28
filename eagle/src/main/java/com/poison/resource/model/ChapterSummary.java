package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/10/8
 * Time: 15:38
 */
public class ChapterSummary extends BaseDO {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 6502107353161466L;

    private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat sf;

    private long id;// 主键id
    private String beginDate;// 创建时间
    private String updateDate;// 更新时间
    private String name;//创建章节名称
    private long uid;// 用户名

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(long beginDate) {
        if(sf == null){
            sf = new SimpleDateFormat(DATEFORMAT);
        }
        Date udate = new Date(beginDate);
        this.beginDate = sf.format(udate);
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        if(sf == null){
            sf = new SimpleDateFormat(DATEFORMAT);
        }
        Date udate = new Date(updateDate);
        this.updateDate = sf.format(udate);
    }


//    public long getBeginDate() {
//        return beginDate;
//    }
//
//    public void setBeginDate(long beginDate) {
//        this.beginDate = beginDate;
//    }
//
//    public long getUpdateDate() {
//        return updateDate;
//    }
//
//    public void setUpdateDate(long updateDate) {
//        this.updateDate = updateDate;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
