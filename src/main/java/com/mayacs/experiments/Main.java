package com.mayacs.experiments;

import org.apache.metamodel.DataContext;
import org.apache.metamodel.schema.Schema;
import org.apache.metamodel.schema.Table;
import org.apache.metamodel.schema.Column;
import org.apache.metamodel.util.UrlResource;
import org.apache.metamodel.csv.CsvConfiguration;
import org.apache.metamodel.csv.CsvDataContext;

public final class Main {

    public static void main(String[] args) {
        DataContext dataContext = new CsvDataContext(new UrlResource("http://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=SELECT+*+WHERE+%7B%0D%0A%3Fid+a+foaf%3APerson+%3B%0D%0A++++foaf%3Aname+%3Fname+%3B%0D%0A++++foaf%3Adepiction+%3Fimgurl%0D%0A%7D%0D%0ALIMIT+100%0D%0AOFFSET+100&format=text%2Fcsv&timeout=30000&debug=on"), new CsvConfiguration());
        Schema[] schemas = dataContext.getSchemas();  
        log("Schemas");
        for (final Schema s : schemas) {
            log(s);
        }
        Table[] tables = schemas[1].getTables();  
        log("Tables");
        for (final Table s : tables) {
            log(s);
        }
        Column[] columns = tables[0].getColumns();
        log("Columns");
        for (final Column s : columns) {
            log(s);
        }
    }

    private static void log(final Object o) {
        System.out.println(o.toString());
    }
}
