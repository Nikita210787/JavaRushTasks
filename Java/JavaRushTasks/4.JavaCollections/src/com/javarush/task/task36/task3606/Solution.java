package com.javarush.task.task36.task3606;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/* 
Осваиваем ClassLoader и Reflection
*/
public class Solution {
    private List<Class> hiddenClasses = new ArrayList<>();
    private String packageName;

    public Solution(String packageName) {
        this.packageName = packageName;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Solution solution = new Solution(Solution.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "com/javarush/task/task36/task3606/data/second");
        solution.scanFileSystem();
        System.out.println(solution.getHiddenClassObjectByKey("secondhiddenclassimpl"));
        System.out.println(solution.getHiddenClassObjectByKey("firsthiddenclassimpl"));
        System.out.println(solution.getHiddenClassObjectByKey("packa"));
    }

    public void scanFileSystem() throws ClassNotFoundException {
        //проверяем на наличие разделителя в пути к пакету и добавляем, если надо

        String sep = System.getProperty("file.separator");
        String pathName = packageName.replace("%20"," ");
        if(!(packageName.endsWith(sep))){
            pathName = pathName.concat(sep);
        }
        // получаем массив  name классов в пакете
        File folder = new File(pathName);
        String[] files = folder.list(new FilenameFilter() {
            @Override
            public boolean accept(File folder, String name) {
                return name.endsWith(".class");
            }
        });
        // загружаем классы
        MyLoader myLoader = new MyLoader(ClassLoader.getSystemClassLoader(),pathName);

        for (String f:files
        ) {
            hiddenClasses.add(myLoader.findClass(f.replace(".class","")));
        }

    }

    public HiddenClass getHiddenClassObjectByKey(String key) {
        Class result = null;
        try{
            for (Class cl: hiddenClasses
            ) {
                String classNames = cl.getSimpleName().toLowerCase();
                if(classNames.startsWith(key.toLowerCase())){
                    result=cl;
                    break;
                }
            }
            Constructor[] constructors = result.getDeclaredConstructors();
            for (Constructor constr: constructors) {
                if(constr.getParameterTypes().length == 0){
            constr.setAccessible(true);
            return (HiddenClass)constr.newInstance();}

            }
        }
        catch (InstantiationException e) {e.printStackTrace();}
        catch (IllegalAccessException e) {e.printStackTrace(); }
        catch (SecurityException e) {e.printStackTrace();}
        catch (InvocationTargetException e) {e.printStackTrace();}
        return null;
    }

    public class MyLoader extends ClassLoader {
        private String pathAn;

        public MyLoader(ClassLoader parent, String pathAn) {
            super(parent);
            this.pathAn = pathAn;
        }

        private byte[] loadClassDate(String pathAn) throws IOException {
            FileInputStream in = new FileInputStream(new File(pathAn));
            byte[]b = new byte[(int)new File(pathAn).length()];
            in.read(b);
            in.close();
            return b;
        }

        public  Class<?> findClass(String className)throws ClassNotFoundException {
            byte[] bytes=null;
            try {
                bytes = loadClassDate(pathAn+className+".class");
                return defineClass(null,bytes,0,bytes.length);
            }
            catch (IOException e){e.getMessage();
                return super.findClass(className);}

        }
    }
}

