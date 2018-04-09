package com.flitetrakr;

import com.flitetrakr.model.Airport;
import com.flitetrakr.model.ConnectionGraph;
import com.flitetrakr.model.FlightSegment;
import com.flitetrakr.question.Question;
import com.flitetrakr.question.QuestionFactory;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private final static String ConnectionsPrefix = "Connections:";

    public static void main(final String[] args) {
        // Check if input file was passed
       if (args.length != 1) {
            System.out.println("Usage: java flitetrakr <input>");
            System.out.println("   where <input> follows the specifications in the README file");
            System.exit(0);
        }

        // check if argument passed is a file
        final File inputFile = new File(args[0]);
        if (!inputFile.exists() || !inputFile.isFile()) {
            System.out.println(inputFile.getName() + " is not a valid file");
            System.exit(0);
        }

        processInputFile(inputFile);


    }

    /**
     * creates a new connection graph
     * @param connections a string with connections, of the form NUE-FRA-43, NUE-AMS-67, FRA-AMS-17, FRA-LHR-27, LHR-NUE-23
     */
    private static void populateConnectionGraph(@NotNull final ConnectionGraph graph,
                                                @NotNull final String connections) {
        final Pattern pattern = Pattern.compile("([A-Z]{3})-([A-Z]{3})-(\\d+)");

        for (final String connection : connections.split(",")) {
            final Matcher m = pattern.matcher(connection.trim());
            if (m.find()) {
                try {
                    final Airport src = Airport.valueOf(m.group(1));
                    final Airport dst = Airport.valueOf(m.group(2));
                    // addEdge adds both nodes and edges.
                    graph.addEdge(new FlightSegment(src, dst, Integer.valueOf(m.group(3))));
                } catch (IllegalArgumentException ex) {
                    System.out.println("Wrong input " + m.group(0) + ": " + ex.getMessage());
                }
            } else {
                System.out.println(String.format("String %s should be a string separated concatenation of connections as follow XXX-YYY-99", connections));
            }
        }
    }


    /**
     * Process all lines in an input file and then runs all questions in order.
     * @param file a file containing a graph with connections and one or more questions on the graph
     */
    private static void processInputFile(@NotNull final File file) {

        final ConnectionGraph connections = new ConnectionGraph();
        final List<Question> questions = new ArrayList<>();
        try {
            final FileReader fileReader = new FileReader(file);
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("Processing line " + line);
                // check if line is a connection
                if (line.startsWith(ConnectionsPrefix)) {
                    populateConnectionGraph(connections, line.substring(ConnectionsPrefix.length()+1).trim());
                } else {
                    // assume line is a question. If not a question, then it will be discarded
                    final Question question = QuestionFactory.getQuestion(line);
                    if (question != null) {
                        questions.add(question);
                    } else {
                        // no valid question, print error and ignore line
                        System.out.println(line + " is not a valid question");
                    }
                }
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (connections.getNodes().isEmpty()) {
            // there was no connection in the input File
            System.out.println("Missing connections line in input file");
            System.exit(0);
        }
        for (final Question question : questions) {
            System.out.println(question.toString(question.processQuestion(connections)));
        }
    }

}
