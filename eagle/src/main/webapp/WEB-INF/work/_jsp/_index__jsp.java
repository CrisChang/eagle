/*
 * JSP generated by Resin-4.0.16 (built Thu, 17 Mar 2011 04:09:21 PDT)
 */

package _jsp;
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import java.util.*;

public class _index__jsp extends com.caucho.jsp.JavaPage
{
  private static final java.util.HashMap<String,java.lang.reflect.Method> _jsp_functionMap = new java.util.HashMap<String,java.lang.reflect.Method>();
  private boolean _caucho_isDead;
  private boolean _caucho_isNotModified;
  private com.caucho.jsp.PageManager _jsp_pageManager;
  
  public void
  _jspService(javax.servlet.http.HttpServletRequest request,
              javax.servlet.http.HttpServletResponse response)
    throws java.io.IOException, javax.servlet.ServletException
  {
    javax.servlet.http.HttpSession session = request.getSession(true);
    com.caucho.server.webapp.WebApp _jsp_application = _caucho_getApplication();
    com.caucho.jsp.PageContextImpl pageContext = _jsp_pageManager.allocatePageContext(this, _jsp_application, request, response, null, session, 8192, true, false);

    TagState _jsp_state = null;

    try {
      _jspService(request, response, pageContext, _jsp_application, session, _jsp_state);
    } catch (java.lang.Throwable _jsp_e) {
      pageContext.handlePageException(_jsp_e);
    } finally {
      _jsp_pageManager.freePageContext(pageContext);
    }
  }
  
  private void
  _jspService(javax.servlet.http.HttpServletRequest request,
              javax.servlet.http.HttpServletResponse response,
              com.caucho.jsp.PageContextImpl pageContext,
              javax.servlet.ServletContext application,
              javax.servlet.http.HttpSession session,
              TagState _jsp_state)
    throws Throwable
  {
    javax.servlet.jsp.JspWriter out = pageContext.getOut();
    final javax.el.ELContext _jsp_env = pageContext.getELContext();
    javax.servlet.ServletConfig config = getServletConfig();
    javax.servlet.Servlet page = this;
    javax.servlet.jsp.tagext.JspTag _jsp_parent_tag = null;
    com.caucho.jsp.PageContextImpl _jsp_parentContext = pageContext;
    response.setContentType("text/html;charset=UTF-8");

    out.write(_jsp_string0, 0, _jsp_string0.length);
    pageContext.include("/web_eagle/publicResource.jsp", false);out.write(_jsp_string1, 0, _jsp_string1.length);
      
    String path = request.getContextPath();  
    //String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

    out.write(_jsp_string2, 0, _jsp_string2.length);
    out.print((path ));
    out.write(_jsp_string3, 0, _jsp_string3.length);
    out.print((path ));
    out.write(_jsp_string4, 0, _jsp_string4.length);
  }

  private com.caucho.make.DependencyContainer _caucho_depends
    = new com.caucho.make.DependencyContainer();

  public java.util.ArrayList<com.caucho.vfs.Dependency> _caucho_getDependList()
  {
    return _caucho_depends.getDependencies();
  }

  public void _caucho_addDepend(com.caucho.vfs.PersistentDependency depend)
  {
    super._caucho_addDepend(depend);
    _caucho_depends.add(depend);
  }

  protected void _caucho_setNeverModified(boolean isNotModified)
  {
    _caucho_isNotModified = true;
  }

  public boolean _caucho_isModified()
  {
    if (_caucho_isDead)
      return true;

    if (_caucho_isNotModified)
      return false;

    if (com.caucho.server.util.CauchoSystem.getVersionId() != 4462798215799161553L)
      return true;

    return _caucho_depends.isModified();
  }

  public long _caucho_lastModified()
  {
    return 0;
  }

  public void destroy()
  {
      _caucho_isDead = true;
      super.destroy();
    TagState tagState;
  }

  public void init(com.caucho.vfs.Path appDir)
    throws javax.servlet.ServletException
  {
    com.caucho.vfs.Path resinHome = com.caucho.server.util.CauchoSystem.getResinHome();
    com.caucho.vfs.MergePath mergePath = new com.caucho.vfs.MergePath();
    mergePath.addMergePath(appDir);
    mergePath.addMergePath(resinHome);
    com.caucho.loader.DynamicClassLoader loader;
    loader = (com.caucho.loader.DynamicClassLoader) getClass().getClassLoader();
    String resourcePath = loader.getResourcePathSpecificFirst();
    mergePath.addClassPath(resourcePath);
    com.caucho.vfs.Depend depend;
    depend = new com.caucho.vfs.Depend(appDir.lookup("index.jsp"), 3031616130952761239L, false);
    _caucho_depends.add(depend);
  }

  final static class TagState {

    void release()
    {
    }
  }

  public java.util.HashMap<String,java.lang.reflect.Method> _caucho_getFunctionMap()
  {
    return _jsp_functionMap;
  }

  public void caucho_init(ServletConfig config)
  {
    try {
      com.caucho.server.webapp.WebApp webApp
        = (com.caucho.server.webapp.WebApp) config.getServletContext();
      init(config);
      if (com.caucho.jsp.JspManager.getCheckInterval() >= 0)
        _caucho_depends.setCheckInterval(com.caucho.jsp.JspManager.getCheckInterval());
      _jsp_pageManager = webApp.getJspApplicationContext().getPageManager();
      com.caucho.jsp.TaglibManager manager = webApp.getJspApplicationContext().getTaglibManager();
      manager.addTaglibFunctions(_jsp_functionMap, "c", "http://java.sun.com/jsp/jstl/core");
      com.caucho.jsp.PageContextImpl pageContext = new com.caucho.jsp.InitPageContextImpl(webApp, this);
    } catch (Exception e) {
      throw com.caucho.config.ConfigException.create(e);
    }
  }

  private final static char []_jsp_string4;
  private final static char []_jsp_string0;
  private final static char []_jsp_string2;
  private final static char []_jsp_string3;
  private final static char []_jsp_string1;
  static {
    _jsp_string4 = "/css/duyao_font.css\" rel=\"stylesheet\" media=\"screen\" />\r\n	\r\n	\r\n	\r\n	<script type=\"text/javascript\">\r\n	function load(){\r\n		//window.location = \"/web_eagle/duyaoIndex.jsp\";\r\n		window.location = \"/web_eagle/k-duyao-index/index.html\";\r\n	}\r\n	</script>\r\n </head>\r\n\r\n\r\n\r\n\r\n<body style=\"margin:0px; padding:0px;\" onload=\"load()\" >\r\n	\r\n	\r\n	\r\n\r\n  </body>\r\n</html>\r\n\r\n".toCharArray();
    _jsp_string0 = "\r\n \r\n".toCharArray();
    _jsp_string2 = " \r\n<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head>\r\n    <meta charset=\"utf-8\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"author\" content=\"\">\r\n    <title>\u6bd2\u836f\u9996\u9875--\u56fd\u5185\u6700\u5927\u6587\u5b66\u9605\u8bfb\u4e0e\u5199\u4f5c\u5e73\u53f0\u4e4b\u4e00\uff0c\u662f\u76ee\u524d\u56fd\u5185\u9886\u5148\u7684\u539f\u521b\u6587\u5b66\u95e8\u6237\u7f51\u7ad9\u3002</title>\r\n    <title>\u6bd2\u836f\u9996\u9875</title>\r\n	<link href=\"".toCharArray();
    _jsp_string3 = "/css/admin.css\" rel=\"stylesheet\" media=\"screen\" />\r\n	<link href=\"".toCharArray();
    _jsp_string1 = "\r\n\r\n".toCharArray();
  }
}
