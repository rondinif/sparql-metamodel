# Logging notes
## No Logging Issue

```
warning: [options] bootstrap class path not set in conjunction with -source 1.7
1 warning
:processResources
:classes
:run
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
```

### SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
DOCUMENTATION:
  - http://www.slf4j.org/codes.html#StaticLoggerBinder

#### FIX: 
  - file: log 

    ADDED: 
    -  under: ```dependencies {```:
```  runtime 'ch.qos.logback:logback-classic:1.1.2' ```
    -  then:

``` groovy
run {    
    systemProperties System.getProperties()
}
```  

rationale: 
This is a simplified solution derived from reading the following question abaut Gradle:
[problems passing system properties ...](http://stackoverflow.com/questions/23689054/problems-passing-system-properties-and-parameters-when-running-java-class-via-gr)
and [this answer](http://stackoverflow.com/a/23689696/1657028) 
The adopted solution is based based o **Application Plugin Approach**
that should allow to launch program ececution this way:   
```gradle run -Dmyvariable=myvalue -Dexec.args="arg1 arg2 arg3"``` 

by including in ```build.gradle``` the code:

``` groovy
run {    
    /* Can pass all the properties: */
    systemProperties System.getProperties()

    /* Or just each by name: */
    systemProperty "myvariable", System.getProperty("myvariable")

    /* Need to split the space-delimited value in the exec.args */
    args System.getProperty("exec.args").split()    
}
```


#### HOW TO:
to run the project with a specific logging configuration: 
``` bash  
 $ cd ~/path/to/this/projects/parent-folder/sparql-metamodel  
 
 # should use  ./src/main/resources/logback-file.xml specific configuration:
 # and produce ./degug-file.log
 $ ./gradlew run -Dlogback.configurationFile=./src/main/resources/logback-file.xml


 # this require groovy 
 # should use ./src/main/resources/logback.groovy configuration:
 $ ./gradlew run 
```

#### other ways to configure logging 
Even if we prefer to specify the location of the
 configuration file with a system property named "logback.configurationFile", 
consider there are other ways to configure logging:
with reference to: [logback configuration manual](http://logback.qos.ch/manual/configuration.html):
- Logback tries to find a file called ```logback.groovy``` in the classpath.
- If no such file is found, logback tries to find a file called ```logback-test.xml``` in the classpath.
- If no such file is found, it checks for the file ```logback.xml`` in the classpath..
- If no such file is found, **service-provider loading facility** (introduced in JDK 1.6) is used to resolve 
  the implementation of ```com.qos.logback.classic.spi.Configurator``` interface 
  by looking up the file ```META-INF\services\ch.qos.logback.classic.spi.Configurator``` in the class path.
  Its contents should specify the fully qualified class name of the desired Configurator implementation.
- If none of the above succeeds, logback configures itself automatically using the BasicConfigurator which will cause logging output to be directed to the console.
