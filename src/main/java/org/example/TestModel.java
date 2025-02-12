package org.example;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.tokenize.SimpleTokenizer;

import java.io.File;
import java.io.IOException;

public class TestModel {

    public static void main(String[] args) throws IOException {
        File modelFile = new File("C:\\Users\\jrobes\\Desktop\\AphaVantage\\OpenNLP_model_trained.bin");
        DoccatModel model = new DoccatModel(modelFile);
        DocumentCategorizerME categorizer = new DocumentCategorizerME(model);

        String testText = "the crypto market lost more than half its value in the latest period.";
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(testText);
        double[] outcomes = categorizer.categorize(tokens);
        String category = categorizer.getBestCategory(outcomes);

        System.out.println("La categoría del texto es: " + category);
    }
}
