package org.example;

import java.io.*;
import org.json.JSONObject;

public class ParseJson2NLPTrain {
    public static void main(String[] args) {
        // Rutas de los archivos de entrada y salida
        String inputFilePath = "negative_tweets.json"; // Archivo de entrada
        String outputFilePath = "negative_tweets_output.txt"; // Archivo de salida

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parsear la línea como un objeto JSON
                JSONObject jsonObject = new JSONObject(line);

                // Extraer el valor del parámetro "text"
                if (jsonObject.has("text")) {
                    String textValue = jsonObject.getString("text");
                    textValue = removeUnwantedContent(textValue);
                    // Escribir el valor en el archivo de salida
                    writer.write("0 " + textValue);
                    writer.newLine(); // Nueva línea para el siguiente valor
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
        text = text.replaceAll(":\\-\\(", ""); // eliminar :-(
        text = text.replaceAll("\\(+", "");

        // Eliminar el carácter @ si aparece solo
        text = text.replaceAll("\\s@\\s", " "); // Espacios alrededor de @
        text = text.replaceAll("^@|@$", ""); // @ al inicio o final

        // Eliminar cadenas que comienzan con http
        text = text.replaceAll("https?://\\S+", "");

        // Eliminar caracteres que no son UTF-8
        text = text.replaceAll("[^\\x00-\\x7F]", "");
        //Saltos de linea
        text = text.replaceAll("\\n+", "");
        // Eliminar la secuencia :(
        text = text.replaceAll(":\\(", "");

        // Eliminar espacios múltiples y trimear el texto
        text = text.replaceAll("\\s+", " ").trim();

        return text;
    }
}
