FROM openjdk:11
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java", "-noverify", "-XX:TieredStopAtLevel=1","-cp","app:app/lib/*","de.skuzzle.tally.TallyWebApplication", "--spring.config.location=classpath:application.properties"]
