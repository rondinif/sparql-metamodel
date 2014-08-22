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

/**
 * Experimental illustration of alternatives in accessing RDF data through
 * Apache MetaModel. This is a TOY implementation to be used in a small
 * blog post. A serious implementation would subclass QueryPostprocessDataContext
 * and use the many SQL-like features of SPARQL.
 * 
 * @author Niels Christensen
 */
public final class Main {
    //Load two SPARQL queries from resource files
    private static final String TRIPLES_QUERY = loadResource("triples.sparql.txt");
    private static final String FOAF_QUERY = loadResource("foaf.sparql.txt");
    
    /**
     * Runs two SPARQL queries on dbpedia.org, parses the results in Apache MetaModel,
     * and prints the results on stdout
     * @param args Ignore
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        printResult(TRIPLES_QUERY);
        System.out.println("================================================");
        printResult(FOAF_QUERY);
    }
    
    /**
     * Runs the given SPARQL query on dbpedia.org, parses the result in Apache MetaModel,
     * and prints the results on stdout
     * @param query A SPARQL query, e.g. "SELECT * WHERE {?subject ?relation ?object}"
     * @throws UnsupportedEncodingException
     */
    private static void printResult(final String query) throws UnsupportedEncodingException {
        DataContext dataContext = new CsvDataContext(new UrlResource(dbpediaUrl(query)), new CsvConfiguration());
        DataSet dataSet = dataContext.query()
            .from(dataContext.getSchemas()[1].getTable(0))
            .selectAll()
            .execute();
        for (final Row row : dataSet) {
            System.out.println(Arrays.asList(row.getValues()));
        }        
    }
    
    /**
     * Converts a SPARQL query into a URL string for running the query on
     * the Virtuoso server at dbpedia.org
     * @param query A SPARQL query, e.g. "SELECT * WHERE {?subject ?relation ?object}"
     * @return A dbpedia.org-URL that should return CSV-encoded results of the query
     * @throws UnsupportedEncodingException
     */
    private static String dbpediaUrl(final String query) throws UnsupportedEncodingException {
        return String.format(
                "http://dbpedia.org/sparql?default-graph-uri=%s&query=%s&format=%s&timeout=30000", 
                URLEncoder.encode("http://dbpedia.org", "UTF-8"),
                URLEncoder.encode(query, "UTF-8"),
                URLEncoder.encode("text/csv", "UTF-8"));
    }
    
    /**
     * Loads a Java class resource into a String using https://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner.html
     * This method assumes UTF-8 encoding. 
     * @param name The name of the resource, e.g. "foaf.sparql.txt"
     * @return A String containing the contents of the resource
     */
    private static String loadResource(final String name) {
        final Scanner scan = new Scanner(Main.class.getResourceAsStream(name), "UTF-8");
        final String result = scan.useDelimiter("\\A").next();
        scan.close();
        return result;
    }
}
