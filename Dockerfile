# Etapa base com JRE
FROM eclipse-temurin:17-jre-alpine

# Diretório de trabalho
WORKDIR /app

# Copia o .jar já gerado (ajuste o nome se necessário)
COPY target/*.jar app.jar

# Expõe a porta (ajuste conforme o projeto)
EXPOSE 8090

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
