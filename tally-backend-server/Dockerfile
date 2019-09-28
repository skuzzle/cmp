FROM openjdk:12
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseJVMCICompiler","-cp","app:app/lib/*","de.skuzzle.tally.TallyWebApplication", "--spring.config.location=classpath:application.properties"]
