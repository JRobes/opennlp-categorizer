package org.example;

import java.io.*;
import org.json.JSONObject;

public class ParseJson2NLPTrain {
    public static void main(String[] args) {
        // Rutas de los archivos de entrada y salida
        String inputFilePath = "negative_tweets.json"; // Archivo de entrada1
        String inputFilePath2 = "positive_tweets.json"; // Archivo de entrada2

        String outputFilePath = "tweets_output.txt"; // Archivo de salida

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
                BufferedReader reader2 = new BufferedReader(new FileReader(inputFilePath2));
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))
        ) {
            String line, line2;
            while ((line = reader.readLine()) != null) {
                if((line2 = reader2.readLine()) == null){
                    line2 = "";
                }

                // Parsear la línea como un objeto JSON
                JSONObject jsonObject = new JSONObject(line);
                JSONObject jsonObject2 = new JSONObject(line2);

                // Extraer el valor del parámetro "text"
                if (jsonObject.has("text")) {
                    String textValue = jsonObject.getString("text");
                    textValue = removeUnwantedContent(textValue);
                    if(textValue.length()!=0){
                        // Escribir el valor en el archivo de salida
                        writer.write("0 " + textValue);
                        writer.newLine(); // Nueva línea para el siguiente valor
                    }

                }
                // Extraer el valor del parámetro "text"
                if (jsonObject2.has("text")) {
                    String textValue2 = jsonObject2.getString("text");
                    textValue2 = removeUnwantedContent(textValue2);
                    if(textValue2.length()!=0){
                        // Escribir el valor en el archivo de salida
                        writer.write("1 " + textValue2);
                        writer.newLine(); // Nueva línea para el siguiente valor
                    }

                }
            }
            System.out.println("Proceso completado. Los valores de 'text' se han escrito en " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error al leer o escribir el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al procesar el JSON: " + e.getMessage());
        }
    }

    // Método para eliminar contenido no deseado
    private static String removeUnwantedContent(String text) {
        // Eliminar palabras que comienzan con @
        text = text.replaceAll("@\\w+", "");
        text = text.replaceAll("#\\w+", "");
        text = text.replaceAll("https?://[^\\\\s]+", "");
        //text = text.replaceAll("https//\\S+", "");
        text = text.replaceAll(":\\-\\(", ""); // eliminar :-(
        text = text.replaceAll("\\(+", "");
        text = text.replaceAll("[\\?:!\\.]", "");
        text = text.replaceAll("[\"]", "");
        text = text.replaceAll("[\\*]", "");
        text = text.replaceAll("[\\+]", "");
        text = text.replaceAll("[-;',#]", "");
        text = text.replaceAll("/", " ");
        text = text.replaceAll(".*\\d+.*", "");


        // Eliminar el carácter @ si aparece solo
        text = text.replaceAll("\\s@\\s", " "); // Espacios alrededor de @
        text = text.replaceAll("^@|@$", ""); // @ al inicio o final

        // Eliminar cadenas que comienzan con http

        text = text.replaceAll("&", "");
        // Eliminar caracteres que no son UTF-8
        text = text.replaceAll("[^\\x00-\\x7F]", "");
        //Saltos de linea
        text = text.replaceAll("\\n+", "");
        // Eliminar la secuencia :(
        text = text.replaceAll(":\\(", "");
        text = text.replaceAll("\\)", "");

        text = text.toLowerCase();

        // Eliminar espacios múltiples y trimear el texto
        text = text.replaceAll("\\s+", " ").trim();


        return text;
    }
}
