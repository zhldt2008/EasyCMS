package cn.easycms.action;

import cn.easycms.base.BaseAction;
import cn.easycms.model.Func;
import cn.easycms.model.Site;
import cn.easycms.service.FuncService;
import cn.easycms.service.SiteService;
import freemarker.template.utility.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by hackingwu on 2014/4/4.
 */
public class AdminAction extends BaseAction {
    List<Func> funcList;
    private FuncService funcService;

    public void setFuncService(FuncService funcServie) {
        this.funcService = funcService;
    }

    private SiteService siteService;
    private String funcid;
    private String siteid;

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public String left() {
        Site manageSite = null;
        if (siteid != null && siteid.trim().length() > 0) {
            manageSite = siteService.findById(siteid);
        } else {
            if (getHttpSession().getAttribute("manageSite") != null) {
                manageSite = (Site) getHttpSession().getAttribute("manageSite");
            } else {
                if (isAdminLogin()) {
                    manageSite = siteService.selectFirstByParId("");
                } else {
                    //普通用户
                }

            }
        }
        getHttpSession().setAttribute("manageSite", manageSite);
        if (funcid == null || funcid.trim().length() == 0) {
            if (getHttpSession().getAttribute("funid") != null && StringUtils.isNotEmpty(getHttpSession().getAttribute("funcid").toString())) {
                funcid = getHttpSession().getAttribute("funid").toString();
            } else {
                if (isAdminLogin()) {
                    funcList = funcService.selectRoot();
                } else {
                    //普通用户的操作
                }
                if (funcList != null && funcList.size() > 0) {
                    //菜单管理func.jsp
                    funcid = funcList.get(0).getId();
                }
            }
        }
        //已经有的更新，没有的存进去。
        getHttpSession().setAttribute("funcid", funcid);
        if (isAdminLogin()) {
            getHttpSession().setAttribute("siteAdmin", true);
        } else {
            //非管理员操作
        }
        if (getHttpSession().getAttribute("funcs") == null) {
            if (isAdminLogin()) {
                funcList = funcService.selectAll();
            } else {
                //非管理员操作
            }
            if (funcList != null && funcList.size() > 0) {
                for (int i = 0; i < funcList.size(); i++) {
                    if (funcService.haveSon(funcList.get(i).getId())) {
                        funcList.get(i).setHasChildren(true);
                    }
                }
            }
            getHttpSession().setAttribute("funcs", funcList);
        }
        return "left";
    }

    /**
     * 头部
     */
    public String top() {
        if (isAdminLogin()) {
            funcList = funcService.selectRoot();
        } else {
            //非管理员的操作。
        }
        if (funcList != null && funcList.size() > 0) {
            funcid = funcList.get(0).getId();
        }
        return "top";
    }

}
