package com.poison.ucenter.service;

import com.poison.ucenter.model.Author;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/27
 * Time: 14:58
 */
public interface AuthorService {

    /**
     * 查询用户信息
     * @param id
     * @return
     */
    public Author findAuthorInfo(long id);

    /**
     * 登录
     * @param phonenum
     * @param mail
     * @param password
     * @return
     */
    public Author loginAuthor(String phonenum, String mail, String password);

    /**
     * 修改作者密码
     * @param id
     * @param password
     * @return
     */
    public int updateAuthorPassword(long id, String password);

    /**
     * 更新作者信息
     * @param id
     * @param name
     * @param qqAddress
     * @return
     */
    public Author updateAuthorInfo(long id, String name, String qqAddress);
}
