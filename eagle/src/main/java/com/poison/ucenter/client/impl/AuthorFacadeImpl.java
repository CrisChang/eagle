package com.poison.ucenter.client.impl;

import com.poison.ucenter.client.AuthorFacade;
import com.poison.ucenter.model.Author;
import com.poison.ucenter.service.AuthorService;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/27
 * Time: 15:00
 */
public class AuthorFacadeImpl implements AuthorFacade {

    private AuthorService authorService;

    public void setAuthorService(AuthorService authorService) {
        this.authorService = authorService;
    }

    /**
     * 查询作者信息
     * @param id
     * @return
     */
    @Override
    public Author findAuthorInfo(long id) {
        return authorService.findAuthorInfo(id);
    }

    /**
     *
     * @param phonenum
     * @param mail
     * @param password
     * @return
     */
    @Override
    public Author loginAuthor(String phonenum, String mail, String password) {
        return authorService.loginAuthor(phonenum, mail, password);
    }

    /**
     * 修改作者密码
     * @param id
     * @param password
     * @return
     */
    @Override
    public int updateAuthorPassword(long id, String password) {
        return authorService.updateAuthorPassword(id, password);
    }

    /**
     * 更新作者信息
     * @param id
     * @param name
     * @param qqAddress
     * @return
     */
    @Override
    public Author updateAuthorInfo(long id, String name, String qqAddress) {
        return authorService.updateAuthorInfo(id, name, qqAddress);
    }
}
