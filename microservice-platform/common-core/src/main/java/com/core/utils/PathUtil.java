package com.core.utils;

import java.io.File;

/**
 * @Description path 工具类
 * @Author linyf
 * @Date 2022-06-24 16:37
 */
public class PathUtil {
    private static String webRootPath;
    private static String rootClassPath;

    public static String getPath(Class clazz){
        String path = clazz.getResource("").getPath();
        return new File(path).getAbsolutePath();
    }

    public static String getPath(Object object){
        String path = object.getClass().getResource("").getPath();
        return new File(path).getAbsolutePath();
    }

    public static String getRootClassPath(){
        if(rootClassPath == null){
            try{
                String path = PathUtil.class.getClassLoader().getResource("").toURI().getPath();
                rootClassPath = new File(path).getAbsolutePath();
            }catch (Exception e){
                String path = PathUtil.class.getClassLoader().getResource("").getPath();
                rootClassPath = new File(path).getAbsolutePath();
            }
        }
        return rootClassPath;
    }

    public static String getPackagePath(Object object){
        Package p = object.getClass().getPackage();
        return p != null ? p.getName().replaceAll("\\.", "/") : "";
    }

    public static File getFileFromJar(String file){
        throw new RuntimeException("Not finish, do not use this method.");
    }

    public static String getWebRootPath(){
        if(webRootPath == null)
            webRootPath = detectWebRootPath();
        return webRootPath;
    }

    public static void setWebRootPath(String webRootPath){
        if(webRootPath == null)
            return;
        if(webRootPath.endsWith(File.separator))
            webRootPath = webRootPath.substring(0, webRootPath.length() - 1);
        PathUtil.webRootPath = webRootPath;
    }

    public static String detectWebRootPath(){
        try{
            String path = PathUtil.class.getResource("/").toURI().getPath();
            return new File(path).getParentFile().getParentFile().getCanonicalPath();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
