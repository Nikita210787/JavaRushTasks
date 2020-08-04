package com.javarush.task.task36.task3605;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

/* 
Использование TreeSet
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        File file=new File(args[0]);
        FileReader fr=new FileReader(file);
        TreeSet<Character> treeSet=new TreeSet<Character>();
        while (fr.ready()) {
            treeSet.add((char) String.valueOf(
                    (char)fr.read()).toLowerCase().toCharArray()[0]);
        }

        treeSet.stream()
                .filter(Character::isLetter)
                .limit(5)
                .forEach(System.out::print);
        //System.out.println();
       /* for (Character ch:treeSet) {
            //ch=ch.toString().toLowerCase().toCharArray()[0];
           // System.out.print(ch);
            if(ch>='a' && ch<='z' )
            System.out.print(ch.toString().toLowerCase());
        }*/
        fr.close();

    }
}
