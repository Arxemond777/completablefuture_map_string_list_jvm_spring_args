```bash
 export MAVEN_OPTS="-Xmx3g";
 export JAVA_OPTS="-Xms756m -Xmx756m -Xss128m -Xmn512m";
 export JAVA_OPTS="-Xms756m -Xmx3g -Xss128m -Xmn512m";
 
 
 
 mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx1024m"


```