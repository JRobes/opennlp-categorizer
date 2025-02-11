package org.example;

import java.io.*;

import edu.stanford.nlp.ling.CoreLabel;
import opennlp.tools.doccat.*;
import opennlp.tools.util.*;
import opennlp.tools.util.model.ModelType;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class OpenNLPCategorizer
{
    //DoccatModel model;

    public static void main(String[] args) {

        try {
            // Ruta al archivo de entrenamiento
            String rutaArchivoEntrenamiento = "C:\\Users\\jrobes\\Desktop\\AphaVantage\\OpenNLPTrain.txt";
            System.out.println("WWWWWWWWW: " + rutaArchivoEntrenamiento);
            // Crear un InputStreamFactory para leer el archivo
            InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(new File(rutaArchivoEntrenamiento));

            // Crear un ObjectStream para leer el archivo línea por línea
            ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);

            // Crear un ObjectStream de DocumentSample a partir del ObjectStream de líneas
            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

            // Parámetros de entrenamiento
            TrainingParameters params = new TrainingParameters();
            params.put(TrainingParameters.ITERATIONS_PARAM, 100); // Número de iteraciones
            params.put(TrainingParameters.CUTOFF_PARAM, 2); // Umbral de frecuencia mínima

            // Entrenar el modelo
            DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, new DoccatFactory());

            // Guardar el modelo en un archivo
            String rutaModelo = "C:\\Users\\jrobes\\Desktop\\AphaVantage\\OpenNLP_model_trained.bin";
            FileOutputStream modelOut = new FileOutputStream(rutaModelo);
            model.serialize(modelOut);
            modelOut.close();

            System.out.println("Modelo entrenado y guardado en: " + rutaModelo);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
/*
    public void trainModel() {
        InputStream dataIn = null;
        try {
            dataIn = new FileInputStream("input/tweets_output.txt");
            InputStreamFactory isf = new InputStreamFactory() {
                public InputStream createInputStream() throws IOException {
                    return new FileInputStream(new File("input/tweets.txt"));
                }
            };
            ObjectStream lineStream = new PlainTextByLineStream( isf, "UTF-8");
            ObjectStream sampleStream = new DocumentSampleStream(lineStream);
            // Specifies the minimum number of times a feature must be seen
            DoccatFactory doccatFactory = new DoccatFactory();
            TrainingParameters params = new TrainingParameters();
            //params.put(TrainingParameters.ITERATIONS_PARAM, 4000);
            //params.put(TrainingParameters.CUTOFF_PARAM, 10);
            params.put(TrainingParameters.ALGORITHM_PARAM, ModelType.MAXENT.toString());
            params.put(TrainingParameters.ITERATIONS_PARAM, "4000");
            params.put(TrainingParameters.CUTOFF_PARAM, "1");
            int cutoff = 2;
            int trainingIterations = 30;
            model = DocumentCategorizerME.train("en", sampleStream, params, doccatFactory);
            System.out.println("End of train");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dataIn != null) {
                try {
                    dataIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void classifyNewTweet(String tweet) {
            DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
            double[] outcomes = myCategorizer.categorize(new String[]{tweet});
            String category = myCategorizer.getBestCategory(outcomes);
            System.out.println("····························" + outcomes.length + "\t" +outcomes[1]);
        if (category.equalsIgnoreCase("1")) {
            System.out.println("The tweet is positive :) ");
        } else {
            System.out.println("The tweet is negative :( ");
        }
    }
    */

}
