package com.poison.ucenter.dao.impl;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.AuthorDAO;
import com.poison.ucenter.model.Author;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/26
 * Time: 17:36
 */
public class AuthorDAOImpl extends SqlMapClientDaoSupport implements AuthorDAO {


    private static final Log LOG = LogFactory.getLog(AuthorDAOImpl.class);

    /**
     * 查询作者信息
     * @param id
     * @return
     */
    @Override
    public Author findAuthorInfo(long id) {
        Author author = new Author();
        int flag = ResultUtils.ERROR;
        try{
            author = (Author) getSqlMapClientTemplate().queryForObject("findAuthorByid",id);
            if(null==author){
                author = new Author();
                author.setFlag(ResultUtils.DATAISNULL);
                return author;
            }
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            author = new Author();
            flag = ResultUtils.ERROR;
        }
        author.setFlag(flag);
        return author;
    }

    /**
     * 查询用户登录信息
     * @param mail
     * @param phonenum
     * @param password
     * @return
     */
    @Override
    public Author findAuthorByMailOrPhone(String mail, String phonenum, String password) {
        Author author = new Author();
        int flag = ResultUtils.ERROR;
        try{
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("mail",mail);
            map.put("mobilephone",phonenum);
            map.put("password",password);
            author = (Author) getSqlMapClientTemplate().queryForObject("findAuthorByMailOrPhonenum",map);
            if(null==author){
                author = new Author();
                author.setFlag(ResultUtils.DATAISNULL);
                return author;
            }
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            e.printStackTrace();
            author = new Author();
            flag = ResultUtils.ERROR;
        }
        author.setFlag(flag);
        return author;
    }

    /**
     * 查询用户邮箱是否存在
     * @param mail
     * @return
     */
    @Override
    public List<Author> findAuthorMailIsExisted(String mail) {
        List<Author> authorList = new ArrayList<Author>();
        try{
            authorList = getSqlMapClientTemplate().queryForList("findAuthorByMail", mail);
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            authorList = null;
        }
        return authorList;
    }

    /**
     * 查询电话号码是否存在
     * @param phonenum
     * @return
     */
    @Override
    public List<Author> findAuthorPhonenumIsExisted(String phonenum) {
        List<Author> authorList = new ArrayList<Author>();
        try{
            authorList = getSqlMapClientTemplate().queryForList("findAuthorByPhonenum", phonenum);
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            authorList = null;
        }
        return authorList;
    }

    /**
     * 修改作者密码
     * @param id
     * @param password
     * @return
     */
    @Override
    public int updateAuthorPassword(long id, String password) {
        int flag = ResultUtils.ERROR;
        long systime = System.currentTimeMillis();
        try{
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("id",id);
            map.put("password",password);
            map.put("latestRevisionDate",systime);
            getSqlMapClientTemplate().update("updateAuthorPassword", map);
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            flag = ResultUtils.ERROR;
        }
        return flag;
    }

    /**
     * 更新作者信息
     * @param id
     * @param name
     * @param qqAddress
     * @return
     */
    @Override
    public int updateAuthorInfo(long id, String name, String qqAddress) {
        int flag = ResultUtils.ERROR;
        long systime = System.currentTimeMillis();
        try{
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("id",id);
            map.put("name",name);
            map.put("qqAddress",qqAddress);
            map.put("latestRevisionDate",systime);
            getSqlMapClientTemplate().update("updateAuthorInfo", map);
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            flag = ResultUtils.ERROR;
        }
        return flag;
    }
}
