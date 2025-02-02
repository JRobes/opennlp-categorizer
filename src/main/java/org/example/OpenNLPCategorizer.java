package org.example;

import java.io.*;

import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.model.ModelType;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.Properties;

public class OpenNLPCategorizer
{
    DoccatModel model;

    public static void main(String[] args) {

        // Configurar las propiedades del pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, parse, sentiment");
        props.setProperty("coref.algorithm", "neural");

        // Crear el pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // Texto de ejemplo
        String text = "Banco Sabadell and three other European titles with a clear graphic appeal";

        // Crear un objeto Annotation con el texto
        Annotation annotation = new Annotation(text);

        // Anotar el texto
        pipeline.annotate(annotation);

        // Obtener las frases y sus sentimientos
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            // Obtener el sentimiento de la frase
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            System.out.println("Sentence: " + sentence);
            System.out.println("Sentiment: " + sentiment);
            System.out.println("----------");
        }





        //OpenNLPCategorizer twitterCategorizer = new OpenNLPCategorizer();
        //twitterCategorizer.trainModel();
        //twitterCategorizer.classifyNewTweet("I love this film");


    }

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
}
