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

        String testText = "On Monday, Apple Inc. AAPL rolled out iOS 18.3.1 and iPadOS 18.3.1 updates. What Happened: The updates address a vulnerability that could disable USB Restricted Mode on locked devices, said Apple. The flaw, which required physical access to the device, was reportedly exploited in sophisticated";
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(testText);
        double[] outcomes = categorizer.categorize(tokens);
        String category = categorizer.getBestCategory(outcomes);

        System.out.println("La categoría del texto es: " + category);
    }
}
