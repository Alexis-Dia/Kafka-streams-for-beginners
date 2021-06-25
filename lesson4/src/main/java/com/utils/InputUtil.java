package com.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class InputUtil {

    //Before JDK 6.0
    public static void write(String fileName, String product) {
        BufferedWriter stream = null;
        try {
             stream = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName, true), "UTF-8"));

            stream.write(product.toCharArray());
            stream.newLine();

        } catch (IOException exception) {
            //log
        } finally {
            if (stream != null) {
                try {
                    stream.flush();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    //After JDK 6.0
    public static String readText(String fileName) {
        String data = "";
        try(Reader stream = new FileReader(fileName)) {
            int temp;

            while ((temp = stream.read()) != -1) {
                char symbol = ((char) temp);
                data += String.valueOf(symbol);
                //System.out.println(temp + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String readBinary(String fileName) {
        InputStream inputStream = null;
        String data = "";
        try {
            inputStream = new FileInputStream(fileName);

            int temp;
            while ((temp = inputStream.read()) != -1) {
                char symbol = ((char) temp);
                data += String.valueOf(symbol);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    public static List<String> readUsingObjectInputStream(String fileName) {
        List<String> student = null;
        ObjectInputStream stream = null;
        try {
            stream = new ObjectInputStream(   //Allows immediately write primitives
                    new BufferedInputStream(
                            new FileInputStream(fileName)));

            student= (List<String>) stream.readObject();

        } catch (IOException | ClassNotFoundException exception) { //since JDK7.0
            System.out.println(exception);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return student;
    }
}

