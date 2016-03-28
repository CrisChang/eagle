package com.poison.ucenter.dao;

import com.poison.ucenter.model.Author;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/26
 * Time: 17:35
 */
public interface AuthorDAO {

    /**
     * 查询作者信息
     * @param id
     * @return
     */
    public Author findAuthorInfo(long id);

    /**
     * 根据邮箱和电话号码查询用户信息
     * @param mail
     * @param phonenum
     * @param password
     * @return
     */
    public Author findAuthorByMailOrPhone(String mail, String phonenum, String password);

    /**
     * 查询这个邮箱是否存在
     * @param mail
     * @return
     */
    public List<Author> findAuthorMailIsExisted(String mail);

    /**
     * 查询这个电话号码是否存在
     * @param phonenum
     * @return
     */
    public List<Author> findAuthorPhonenumIsExisted(String phonenum);

    /**
     * 修改作者密码
     * @param id
     * @param password
     * @return
     */
    public int updateAuthorPassword(long id, String password);

    /**
     * 修改作者笔名，qq号码
     * @param id
     * @param name
     * @param qqAddress
     * @return
     */
    public int updateAuthorInfo(long id, String name, String qqAddress);
}
