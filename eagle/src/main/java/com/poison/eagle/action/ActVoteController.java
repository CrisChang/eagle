package com.poison.eagle.action;

import com.poison.eagle.manager.ActManager;
import com.poison.eagle.manager.ActVoteManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/12
 * Time: 16:26
 */
@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class ActVoteController extends BaseController{
    private static final Log LOG = LogFactory
            .getLog(ActVoteController.class);

    private ActVoteManager actVoteManager;

    public void setActVoteManager(ActVoteManager actVoteManager) {
        this.actVoteManager = actVoteManager;
    }

    @RequestMapping(value = "/clientaction/recommend_vote", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
    @ResponseBody
    public String collectResource(HttpServletRequest request,
                                  HttpServletResponse response) throws UnsupportedEncodingException {
        String reqs = "";
        long uid = 0;
        //获取用户id
        if(checkUserIsLogin()){
            uid = getUserId();
        }else{
            uid = 0;
            LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
            return RES_USER_NOTLOGIN;
        }
        //获取客户端json数据
        try {
            reqs = request.getParameter("req");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(CommentUtils.ERROR_DATANOTGET);
            return RES_DATA_NOTGET;
        }

        //调用manager方法获取返回数据
        String res =actVoteManager.recommendVote(reqs,uid);//actManager.collectResource(reqs,uid);
//		System.out.println(res);

        return res;
    }
}
