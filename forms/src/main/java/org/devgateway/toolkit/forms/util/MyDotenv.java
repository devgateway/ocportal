package org.devgateway.toolkit.forms.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MyDotenv {
    private MyDotenv(){
        // Private constructor to prevent instantiation
    }


    /**
     * Retrieves the value of a specified environment variable from a given file.
     * If the variable is not found, returns a default value.
     *
     * @param fileName the name of the file containing environment variables
     * @param variableName the name of the variable to retrieve
     * @param defaultValue the default value to return if the variable is not found
     * @return the value of the specified variable, or the default value if not found
     */
    public static String getVariable(String fileName, String variableName, String defaultValue) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2 && (parts[0].equals(variableName))) {
                        return parts[1].trim();

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}
