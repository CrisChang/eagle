package com.poison.eagle.action;

import com.poison.eagle.manager.ResourceRelationManager;
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
 * Date: 15/12/4
 * Time: 15:05
 */
@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class ResourceRelationController extends BaseController{

    private static final Log LOG = LogFactory
            .getLog(ResourceRelationController.class);


    private ResourceRelationManager resourceRelationManager;

    public void setResourceRelationManager(ResourceRelationManager resourceRelationManager) {
        this.resourceRelationManager = resourceRelationManager;
    }

    @RequestMapping(value = "/clientview/view_related_reading", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
    @ResponseBody
    public String viewTagList(HttpServletRequest request,
                              HttpServletResponse response) throws UnsupportedEncodingException {
        String reqs = "";
        long uid = 16l;
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
        String res =resourceRelationManager.viewRelationReading(reqs,uid);

        return res;
    }
}
