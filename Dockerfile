FROM eclipse-temurin:21-jre-jammy
RUN groupadd -r appuser && useradd -r -g appuser -m appuser
WORKDIR /app
COPY consent-query-service/consent_query_service/target/*.jar app.jar
COPY consent-query-service/opa/policies /opa/policies
COPY consent-query-service/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
RUN chown appuser:appuser app.jar /entrypoint.sh
EXPOSE 8080
ENV JAVA_OPTS=
ENTRYPOINT ["/entrypoint.sh"]
