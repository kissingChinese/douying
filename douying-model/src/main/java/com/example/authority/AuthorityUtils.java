package com.example.authority;

import org.springframework.util.ObjectUtils;

import java.util.*;


public class AuthorityUtils {


    /**权限集合*/
    private static Map<Long, Collection<String>> map = new HashMap<>();

    /**过滤权限集合*/
    private static Set<String> filterPermission = new HashSet<>();

    /**全局权限校验类*/
    private static Class c;

    /**
     * 是否开启全局校验 默认为false不开启
     */
    private static Boolean globalVerify = false;
    /**
     * 是否开启 @PostMapping 全局校验 默认为false不开启
     */
    private static Boolean postAuthority = false;


    /**
     * 设置是否开启 @PostMapping 全局校验
     * @param state
     */
    public static void setPostAuthority(Boolean state,Class z){
        c = z;
        postAuthority = state;
    }

    /**
     * 获取 postAuthority 状态
     * @return
     */
    public static Boolean getPostAuthority(){
        return postAuthority;
    }

    /**
     * 重新初始化全局校验类Class
     */
    public static void cleanVerifyClass(){
        c = null;
    }


    /**
     * 获取全局权限校验类
     * @return
     */
    public static Class getGlobalVerify(){
        return c;
    }

    /**
     * 开启全局校验
     * @param state
     * @param o
     */
    public static void setGlobalVerify(Boolean state,Object o){
        if (o == null){
            throw new NullPointerException();
        }else if (!(o instanceof AuthorityVerify)){
            throw new ClassCastException(o.getClass()+ "类型不是 AuthorityVerify 实现类");
        }

        c = o.getClass();
        globalVerify = state;
    }

    /**
     * 添加权限
     * @param uId 用户id
     * @param authority 权限集合
     */
    public static void setAuthority(Long uId,Collection<String> authority){
        map.put(uId,authority);
    }

    /**
     * 校验权限
     * @param uId
     * @param authority
     * @return
     */
    public static Boolean verify(Long uId,String authority){
        if (isEmpty(uId)) {
            return false;
        }
        return map.get(uId).contains(authority);
    }

    /**
     * 排除权限
     * @param permissions
     */
    public static void exclude(String... permissions){
        filterPermission.addAll(Arrays.asList(permissions));
    }

    /**
     * 是否有过滤权限
     * @param permission
     * @return
     */
    public static Boolean filterPermission(String permission){
        return filterPermission.contains(permission);
    }

    /**
     * 判空
     * @param uId
     * @return
     */
    public static Boolean isEmpty(Long uId){
        return ObjectUtils.isEmpty(map.get(uId));
    }
}
