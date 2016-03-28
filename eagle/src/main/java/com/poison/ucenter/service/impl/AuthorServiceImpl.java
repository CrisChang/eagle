package com.poison.ucenter.service.impl;

import com.poison.ucenter.domain.repository.AuthorDomainRepository;
import com.poison.ucenter.model.Author;
import com.poison.ucenter.service.AuthorService;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/27
 * Time: 14:59
 */
public class AuthorServiceImpl implements AuthorService{

    private AuthorDomainRepository authorDomainRepository;

    public void setAuthorDomainRepository(AuthorDomainRepository authorDomainRepository) {
        this.authorDomainRepository = authorDomainRepository;
    }

    /**
     * 查询作者信息
     * @param id
     * @return
     */
    @Override
    public Author findAuthorInfo(long id) {
        return authorDomainRepository.findAuthorInfo(id);
    }

    /**
     * 登录
     * @param phonenum
     * @param mail
     * @param password
     * @return
     */
    @Override
    public Author loginAuthor(String phonenum, String mail, String password) {
        return authorDomainRepository.loginAuthor(phonenum, mail, password);
    }

    /**
     * 修改作者密码
     * @param id
     * @param password
     * @return
     */
    @Override
    public int updateAuthorPassword(long id, String password) {
        return authorDomainRepository.updateAuthorPassword(id, password);
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
        return authorDomainRepository.updateAuthorInfo(id, name, qqAddress);
    }
}
