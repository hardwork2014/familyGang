package com.jzb.family.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;


/**
 * XSS Request Wrapper
 * @author LIJUNTAO739
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {  
    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }
    
    @Override  
    public String getQueryString() {  
        return cleanXSS(super.getQueryString());  
    }  
   
    @Override
	public String[] getParameterValues(String parameter) {
      String[] values = super.getParameterValues(parameter);
      if (values==null)  {
         return null;
      }
      int count = values.length;
      String[] encodedValues = new String[count];
      for (int i = 0; i < count; i++) {
         encodedValues[i] = cleanXSS(values[i]);
      }
      return encodedValues;
    }

	@Override
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		if (value == null) {
			return null;
		}
		return cleanXSS(value);
	}
	
	public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null)
            return null;
        return cleanXSS(value);
    }
    //
    private String cleanXSS(String value) {
    	if(StringUtils.isBlank(value)) {
    		return value;
    	}
        //You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
        value = value.replaceAll("'", "& #39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        return value;
    }

} 