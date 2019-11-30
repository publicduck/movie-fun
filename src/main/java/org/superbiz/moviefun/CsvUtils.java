package org.superbiz.moviefun;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CsvUtils {
//    Move both .csv files to the classpath by putting them into the resources folder. DONE
//
//    Update the readFile function in CsvUtils to load files from the classpath:
//
//    Obtain an instance of ClassLoader via the getClassLoader function.
//    Update the scanner so it takes an instance of InputStream obtained from ClassLoader.getResourceAsStream instead of a new File.

    public static String readFile(String path) {
        ClassLoader classloader = CsvUtils.class.getClassLoader();


        //Scanner scanner = new Scanner(new File(path)).useDelimiter("\\A");
        InputStream input = classloader.getResourceAsStream(path);
        Scanner scanner = new Scanner(input).useDelimiter("\\A");

        if (scanner.hasNext()) {
            return scanner.next();
        } else {
            return "";
        }

    }

    public static <T> List<T> readFromCsv(ObjectReader objectReader, String path) {
        try {
            List<T> results = new ArrayList<>();

            MappingIterator<T> iterator = objectReader.readValues(readFile(path));

            while (iterator.hasNext()) {
                results.add(iterator.nextValue());
            }

            return results;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
