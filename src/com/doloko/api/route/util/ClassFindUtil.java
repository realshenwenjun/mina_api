package com.doloko.api.route.util;

import com.doloko.api.route.annotation.Mapping;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/11/20.
 */
public class ClassFindUtil {
    private static List<String> classes = new ArrayList<String>();
    public static void main(String[] args) throws Exception {
        String packageName = "";
        File root = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath());
        System.out.println(root);
        List<String> classes = loop(root, packageName);
    }

    public static  List<String> loop(File folder, String packageName) throws Exception {
        File[] files = folder.listFiles();
        for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
            File file = files[fileIndex];
            if (file.isDirectory()) {
                loop(file, packageName + file.getName() + ".");
            } else {
                classes.addAll(listMethodNames(file.getName(), packageName));
            }
        }
        return classes;
    }

    public static List<String> listMethodNames(String filename, String packageName) {
        List<String> classes = new ArrayList<String>();
        try {
            if (filename.endsWith(".class")) {
                String name = filename.substring(0, filename.length() - 6);
                Class c = Class.forName(packageName + name);
                if (c.isAnnotationPresent(Mapping.class)) {
                    classes.add(name);
                }
            }
        } catch (Exception e) {
        }
        return classes;
    }
}
