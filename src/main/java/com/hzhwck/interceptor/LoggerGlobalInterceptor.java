package com.hzhwck.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import org.apache.log4j.Logger;


/**
 * Created by 陈忠意 on 2017/6/2.
 */
public class LoggerGlobalInterceptor implements Interceptor{
    private static Logger log = Logger.getLogger(LoggerGlobalInterceptor.class);
    private static final int N = 1004;

    public void intercept(Invocation inv){
        Controller c = inv.getController();
        log.info("Controller --> " + inv.getController().toString());
        log.info("ActionKey --> " + inv.getActionKey());
        log.info("Method --> " + inv.getMethodName());
        /*c.getRequest().getParameterMap();
        Map<String, String[]> map = c.getParaMap();
        StringBuffer sb = new StringBuffer("Parameter --> ");
        for(String temp : map.keySet()){
            sb.append(temp + "=" + Arrays.toString(map.get(temp)) + " ");
        }
        log.info(sb.toString());*/
        inv.invoke();
    }
}
