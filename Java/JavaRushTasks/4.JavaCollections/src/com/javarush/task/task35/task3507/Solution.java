package com.javarush.task.task35.task3507;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
ClassLoader - что это такое?
*/
public class Solution {


    public static void main(String[] args) {
        Set<? extends Animal> allAnimals = getAllAnimals(Solution.class.getProtectionDomain().getCodeSource().getLocation().getPath() + Solution.class.getPackage().getName().replaceAll("[.]", "/") + "/data");
        System.out.println(allAnimals);

    }

    public static Set<? extends Animal> getAllAnimals(String pathToAnimals) {

        Set<Animal> animals = new HashSet<>();

        try {

         //   while (pathToAnimals.length() > 1 && pathToAnimals.startsWith("/"))
                pathToAnimals = pathToAnimals.substring(1, pathToAnimals.length());

            List<Path> files = Files.list(Paths.get(URLDecoder.decode(pathToAnimals, "UTF-8")))  //берем путь UTF-8 его при этом
                    .filter(Files::isRegularFile)                                                      //фильтр это файл?
                    .filter(path -> path.toString().toLowerCase().endsWith(".class"))
                    .collect(Collectors.toList());
            for (Path file : files) {
                try {
                    MyLoader myLoader = new MyLoader();
                    Class clazz = myLoader.findClass(file.toString());
                    Constructor[] constructors = clazz.getConstructors();

                    boolean hasDefaultConstructor = false;
                    for (Constructor c : constructors) {
                        hasDefaultConstructor = (c.getParameterTypes().length == 0);
                        c.setAccessible(true);
                        if (Animal.class.isAssignableFrom(clazz) && hasDefaultConstructor)
                            animals.add((Animal) c.newInstance());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return animals;
    }

    public static class MyLoader extends ClassLoader {

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {

            try {
                Path file = Paths.get(name);
                byte[] bytes = Files.readAllBytes(file);
                return defineClass(null, bytes, 0, bytes.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return super.findClass(name);
        }
    }
}
