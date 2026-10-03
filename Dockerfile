# --- Build do React ---

# Imagem do Node e etapa atual 'frontend-build'
FROM node:22 AS frontend-build

# Local de trabalho dentro do container
WORKDIR /app/frontend

# Copia os arquivos de dependências
COPY frontend/package*.json ./

# Instala as dependências registradas no package-lock.json
RUN npm ci

# Copia o frontend para dentro do container
COPY frontend/ ./

# Realiza o build do React dentro do container
RUN npm run build



# --- Build do Spring Boot ---

# Imagem do Java e etapa atual 'backend-build'
FROM eclipse-temurin:17-jdk AS backend-build

# Local de trabalho dentro do container
WORKDIR /app

# Copia o projeto completo para dentro do container
COPY . .

# Copia o dist gerado na primeira etapa para o static do Spring
COPY --from=frontend-build /app/frontend/dist/ src/main/resources/static/

# Compila e empacota o Spring Boot
RUN ./mvnw clean package -DskipTests



# --- Imagem final ---

FROM eclipse-temurin:17-jre

# Local de trabalho dentro do container
WORKDIR /app

# Copia o JAR gerado na etapa anterior
COPY --from=backend-build /app/target/purchase_invoices-0.0.1-SNAPSHOT.jar app.jar

# Porta da aplicação
EXPOSE 8080

# Executa a aplicação
CMD ["java", "-jar", "app.jar"]