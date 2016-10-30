//
// Built on Sun Oct 30 16:08:46 UTC 2016 by logback-translator
// For more information on configuration files in Groovy
// please see http://logback.qos.ch/manual/groovy.html

// For assistance related to this tool or configuration files
// in general, please contact the logback user mailing list at
//    http://qos.ch/mailman/listinfo/logback-user

// For professional support please see
//   http://www.qos.ch/shop/products/professionalSupport

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.FileAppender

import static ch.qos.logback.classic.Level.DEBUG

appender("FILE", FileAppender) {
  file = "debug-file.log"
  append = true
  encoder(PatternLayoutEncoder) {
    pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"  
    // pattern = "%-4relative [%thread] %-5level %logger{35} - %msg%n"
  }
}
root(DEBUG, ["FILE"])