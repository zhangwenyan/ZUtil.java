package top.appx.eweb;

import com.alibaba.fastjson.JSONObject;
import top.appx.zutil.StringUtil;
import top.appx.zutil.info.ResultMap;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq799 on 2017/2/25.
 */

public class MainFilter implements Filter {

    private  String controllerPath;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        controllerPath = filterConfig.getInitParameter("controllerPath");
    }

    private Object deal(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception{
        HttpServletRequest req =(HttpServletRequest)servletRequest;
        HttpServletResponse rsp = (HttpServletResponse)servletResponse;

        req.setCharacterEncoding("utf-8");
        rsp.setCharacterEncoding("utf-8");


        String url = req.getRequestURL().toString();
        String controllerName = url.substring(url.lastIndexOf("/")+1);
        controllerName = controllerName.substring(0,controllerName.indexOf("."));

        String controllerFullName = controllerPath+"."+controllerName+"Controller";

        String action = req.getParameter("action");
        if(StringUtil.isNullOrEmpty(action)){
            action = "main";
        }

            Class cl = Class.forName(controllerFullName);
            if(cl==null){
                throw new MsgException("没有找到该类:"+controllerFullName);
            }
            Object obj = cl.newInstance();

            Method method = null;
            for(Method m : obj.getClass().getMethods()){
                if(m.getName().equals(action)){
                    method = m;
                    break;
                }
            }
            if(method==null){
                throw new MsgException("没有找到方法:"+controllerFullName+"."+action);
            }

            Class<?>[] typs = method.getParameterTypes();
            List<Object> ps = new ArrayList<Object>();
            for(int i=0;i<typs.length;i++){
                Class c1 = typs[i];
                String pName = c1.getName();
                if(c1.equals(HttpServletRequest.class)){
                    ps.add(req);
                }
                else if(c1.equals(HttpServletResponse.class)){
                    ps.add(rsp);
                }
                else if(c1.equals(ServletRequest.class)){
                    ps.add(servletRequest);
                }
                else if(c1.equals(ServletResponse.class)){
                    ps.add(servletResponse);
                }
                else if(c1.equals(HttpSession.class)){
                    ps.add(req.getSession());
                }
                else if(c1.equals(String.class)){
                    System.out.println("pName:"+pName);
                    ps.add(req.getParameter(pName));
                }
                else if(c1.equals(int.class)){
                    String valStr = req.getParameter(pName);
                    if(StringUtil.isNullOrEmpty(valStr)){
                        ps.add(0);
                    }else{
                        ps.add(Integer.parseInt(valStr));
                    }
                }
                else{

                    Method[] methods = c1.getMethods();
                    Object entity = c1.newInstance();

                    for (Method m : methods) {
                        System.out.println(m);
                        if (!m.getName().startsWith("set") || m.getParameterTypes().length != 1) {
                            continue;
                        }
                        String paramName = m.getName().substring(3);
                        System.out.println(paramName);
                        paramName = (paramName.charAt(0) + "").toLowerCase() + paramName.substring(1);

                        String valueStr = req.getParameter(paramName);
                        if (valueStr == null) {
                            continue;
                        }
                        Object value = null;
                        Class<?> t = m.getParameterTypes()[0];
                        if (t.equals(String.class)) {
                            value = req.getParameter(paramName);
                        } else if (t.equals(Integer.class) || t.equals(int.class)) {
                            if (valueStr.length() != 0) {
                                value = Integer.parseInt(valueStr);
                            }
                        } else if (t.equals(Timestamp.class)) {
                            if (valueStr.length() != 0) {
                                value = Timestamp.parse(valueStr);
                            }
                        }
                        if (value != null) {
                            m.invoke(entity, value);
                        }
                    }
                    ps.add(entity);
                }
        }
        Object result =  method.invoke(obj,ps.toArray());
        if(result == null && method.getReturnType().getName().equals("void")){
            result = ResultMap.data("success",true);
        }
        return result;
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Object result = null;
        try{
            result = deal(servletRequest,servletResponse);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            while (ex instanceof InvocationTargetException){
                ex = (Exception) ((InvocationTargetException) ex).getTargetException();
            }
            if(ex instanceof MsgException){
                result = ResultMap.data("success",false).p("msg",ex.getLocalizedMessage());
            }else{
                result = ResultMap.data("success",false).p("msg","系统错误");
            }
        }
        finally {
            servletResponse.getWriter().write(JSONObject.toJSONString(result));
        }
    }

    @Override
    public void destroy() {

    }
}
