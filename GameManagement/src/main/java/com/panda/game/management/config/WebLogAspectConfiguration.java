/***
 * @pName proback
 * @name FinanceLogAspect
 * @user HongWei
 * @date 2018/8/5
 * @desc
 */
package com.panda.game.management.config;

import com.alibaba.druid.support.json.JSONUtils;
import com.panda.game.management.annotaion.AspectLog;
import com.panda.game.management.biz.ExceptionService;
import com.panda.game.management.entity.db.Exceptions;
import com.panda.game.management.utils.RequestUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 全站日志切面配置类
 */
@Aspect
public class WebLogAspectConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private long startTimeMillis = 0;
    private long endTimeMillis = 0;
    private HttpServletRequest request = null;

    @Autowired
    private ExceptionService exceptionService;

    /**
     * 控制是否上报数据库
     */
    private final Boolean isUpload;

    public WebLogAspectConfiguration(Boolean isUpload) {
        this.isUpload = isUpload;
    }


    @Pointcut(value = "execution(* com.panda.game.management.biz.impl.*.*(..))")
    public boolean point() { return false; }

    @Before("point()")
    public boolean before(JoinPoint joinPoint){
        if(!isLog(joinPoint)) return false;

        request = getHttpServletRequest();
        startTimeMillis = System.currentTimeMillis(); //记录方法开始执行的时间
        return true;
    }

    @After("point()")
    public boolean after(JoinPoint joinPoint){
        if(!isLog(joinPoint)) return false;

        request = getHttpServletRequest();
        JsonView jsonView = getJsonView(joinPoint);
        jsonView.setType("after");

        endTimeMillis = System.currentTimeMillis();
        jsonView.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTimeMillis));
        jsonView.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTimeMillis));

        String body = JSONUtils.toJSONString(jsonView);

        logger.info(body);
        return true;
    }

    @AfterReturning(pointcut = "point()",returning = "rvt")
    public boolean afterReturning(JoinPoint joinPoint, Object rvt){
        if(!isLog(joinPoint)) return false;

        JsonView jsonView = getJsonView(joinPoint);
        jsonView.setType("afterReturning");
        jsonView.setRet(rvt);
        if(rvt == null) jsonView.setRet("void");

        String body = JSONUtils.toJSONString(jsonView);
        logger.info(body);
        return true;
    }

    @AfterThrowing(pointcut = "point()", throwing = "ex")
    public boolean afterThrowing(JoinPoint joinPoint, Throwable ex){
        if(!isLog(joinPoint)) return false;

        JsonView jsonView = getJsonView(joinPoint);
        jsonView.setType("afterThrowing");
        jsonView.setThrowable(ex);
        if(ex == null) jsonView.setThrowable(new Throwable());

        String body = JSONUtils.toJSONString(jsonView);
        logger.error(body);

        if(isUpload){
            exceptionService.asyncInsert(new Exceptions(null, 0, body, new Date()));
        }
        return true;
    }

    /**
     * 获取request对象
     * @return
     */
    public HttpServletRequest getHttpServletRequest(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();
        return request;
    }

    /**
     * 获取方法参数
     * @param joinPoint
     * @return
     */
    private JsonView getJsonView(JoinPoint joinPoint) {
        String url = RequestUtil.getParameters(request);
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = null;
        try {
            targetClass = Class.forName(targetName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 获取方法描述
        Method[] methods = targetClass.getMethods();
        String desc = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs!=null&&clazzs.length == arguments.length&&method.getAnnotation(AspectLog.class)!=null) {
                    desc = method.getAnnotation(AspectLog.class).description();
                    break;
                }
            }
        }
        JsonView view = new JsonView();
        view.setTargetName(targetName);
        view.setMethodName(methodName);
        view.setArguments(arguments);
        view.setDescription(desc);
        return view;
    }

    private Boolean isLog(JoinPoint joinPoint){
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = null;
        try {
            targetClass = Class.forName(targetName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 获取方法描述
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs!=null&&clazzs.length == arguments.length&&method.getAnnotation(AspectLog.class)!=null) {
                   return true;
                }
            }
        }
        return false;
    }

    class JsonView{
        private String type;
        private String url;
        private String targetName;
        private String methodName;
        private Object[] arguments;
        private String description;
        private String startTime;
        private String endTime;
        private Object ret;
        private Throwable throwable;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTargetName() {
            return targetName;
        }

        public void setTargetName(String targetName) {
            this.targetName = targetName;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public Object[] getArguments() {
            return arguments;
        }

        public void setArguments(Object[] arguments) {
            this.arguments = arguments;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public Object getRet() {
            return ret;
        }

        public void setRet(Object ret) {
            this.ret = ret;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public void setThrowable(Throwable throwable) {
            this.throwable = throwable;
        }

        public JsonView() {

        }

        public JsonView(String type, String url, String targetName, String methodName, Object[] arguments, String description, String startTime, String endTime, Object ret, Throwable throwable) {

            this.type = type;
            this.url = url;
            this.targetName = targetName;
            this.methodName = methodName;
            this.arguments = arguments;
            this.description = description;
            this.startTime = startTime;
            this.endTime = endTime;
            this.ret = ret;
            this.throwable = throwable;
        }
    }
}
