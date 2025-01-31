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

public class OpenNLPCategorizer
{
    DoccatModel model;

    public static void main(String[] args) {
        OpenNLPCategorizer twitterCategorizer = new OpenNLPCategorizer();
        twitterCategorizer.trainModel();
        twitterCategorizer.classifyNewTweet("Ugly hat");
    }

    public void trainModel() {
        InputStream dataIn = null;
        try {
            dataIn = new FileInputStream("input/tweets.txt");
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
            params.put(TrainingParameters.ITERATIONS_PARAM, 100);
            params.put(TrainingParameters.CUTOFF_PARAM, 2);
            int cutoff = 2;
            int trainingIterations = 30;
            model = DocumentCategorizerME.train("en", sampleStream, params, doccatFactory);
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
        System.out.println("································· " + outcomes.length + "\t" +outcomes[1]);
        if (category.equalsIgnoreCase("1")) {
            System.out.println("The tweet is positive :) ");
        } else {
            System.out.println("The tweet is negative :( ");
        }
    }
}
