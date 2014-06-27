${artifactId}
=========================

${artifactId} rest frontend.

Please run:

Development embeded database:

    jetty:run-war -DskipTests -Pdevelopment

Development mysql database:

    jetty:run-war -DskipTests -Pintegrations

Open browser on:

http://localhost:8080/${artifactId}-backend
