package com.mayacs.experiments;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.metamodel.DataContext;
import org.apache.metamodel.util.UrlResource;
import org.apache.metamodel.csv.CsvConfiguration;
import org.apache.metamodel.csv.CsvDataContext;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.Row;

public final class Main {
    private static final String FOAF_QUERY =
        new Scanner(Main.class.getResourceAsStream("foaf.sparql.txt"), "UTF-8")
            .useDelimiter("\\A")
            .next();
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        DataContext dataContext = new CsvDataContext(new UrlResource(dbpediaUrl(FOAF_QUERY)), new CsvConfiguration());
        DataSet dataSet = dataContext.query()
            .from(dataContext.getSchemas()[1].getTable(0))
            .selectAll()
            .execute();
        for (final Row row : dataSet) {
            System.out.println(Arrays.asList(row.getValues()));
        }
    }

    private static String dbpediaUrl(final String query) throws UnsupportedEncodingException {
        return String.format(
                "http://dbpedia.org/sparql?default-graph-uri=%s&query=%s&format=%s&timeout=30000&debug=on", 
                URLEncoder.encode("http://dbpedia.org", "UTF-8"),
                URLEncoder.encode(query, "UTF-8"),
                URLEncoder.encode("text/csv", "UTF-8"));
    }
    
}
