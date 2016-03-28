package com.poison.ucenter.domain.repository;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.AuthorDAO;
import com.poison.ucenter.model.Author;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/26
 * Time: 17:55
 */
public class AuthorDomainRepository {


    private AuthorDAO authorDAO;

    public void setAuthorDAO(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    /**
     * 查询作者信息
     * @param id
     * @return
     */
    public Author findAuthorInfo(long id){
        return authorDAO.findAuthorInfo(id);
    }

    /**
     * 查询用户登录信息
     * @param phonenum
     * @param mail
     * @param password
     * @return
     */
    public Author loginAuthor(String phonenum,String mail,String password){
//        boolean isExist = false;//是否已经存在 默认不存在
//        List<Author> authorList = new ArrayList<Author>();
//        if(null==phonenum){//如果邮箱登录的话
//            auathorList = authorDAO.findAuthorMailIsExisted(mail);
//        }else if(null==mail){//如果电话号码登录的话
//            authorList = authorDAO.findAuthorPhonenumIsExisted(phonenum);
//        }
//
//        if(null==authorList||authorList.size()==0){//不存在这个电话号码或者邮箱
//
//        }
        int flag = ResultUtils.ERROR;
        Author author = authorDAO.findAuthorByMailOrPhone(phonenum, mail, password);
        if(author.getFlag()== ResultUtils.SUCCESS&&author.getIsInit()==0){//当用户存在
            //author.setFlag(ResultUtils.ERROR_INIT);
        }
        return author;
    }

    /**
     * 修改密码
     * @param id
     * @param password
     * @return
     */
    public int updateAuthorPassword(long id, String password){
        return authorDAO.updateAuthorPassword(id, password);
    }

    /**
     * 更新作者信息
     * @param id
     * @param name
     * @param qqAddress
     * @return
     */
    public Author updateAuthorInfo(long id, String name, String qqAddress){
        int flag = authorDAO.updateAuthorInfo(id, name, qqAddress);
        Author author = new Author();
        author.setFlag(flag);
        if(flag==ResultUtils.SUCCESS){//更新成功时
            author = authorDAO.findAuthorInfo(id);
        }
        return author;
    }
}
