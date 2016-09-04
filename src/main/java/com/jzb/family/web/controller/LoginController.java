package com.jzb.family.web.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jzb.family.common.enums.StatusEnum;
import com.jzb.family.domain.AppUser;

@Controller
public class LoginController extends BaseController {
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(LoginController.class);
	/**
     * 根据用户角色来决定默认的展现页面.
     * @return String
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginPage(HttpServletRequest request,Model model) {
    	try {
    		AppUser user = getAppUser();
        	if(user != null) {
        		return "redirect:/common/main";
        	}
    	}catch (Exception e) {
			logger.error("loginPage exception : {}",e);
		}
    	return "login";
    }
    
    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String forwardToDefaultPage(Model model) {
    	model.addAttribute("appUser", getAppUser());
        return "main";
    }

    /**
	 * 验证输入码的正确性
	 * author zhouhuiqun470
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value="resultServlet/validateCode",method=RequestMethod.POST)
    public void doPost(HttpServletRequest request, HttpServletResponse response)         
            throws ServletException, IOException {         
        response.setContentType("text/html;charset=utf-8");         
        String validateC = (String) request.getSession().getAttribute("validateCode");         
        String veryCode = request.getParameter("c");         
        PrintWriter out = response.getWriter();                  
        if(validateC.equalsIgnoreCase(veryCode)){         
            out.println(StatusEnum.success.getCode());         
        }else{         
            out.println(StatusEnum.fail.getCode());         
        }                  
        out.flush();         
        out.close();         
    }         	
}
