package com.infinitytech.apimonitoring.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class ExtraIdadosClient {

  private static final Logger logger = LoggerFactory.getLogger(ExtraIdadosClient.class);

  private final CloseableHttpClient httpClient;
  private final String baseUrl;
  private final String token;
  private final String userId;

  public ExtraIdadosClient(CloseableHttpClient httpClient, @Value("${extraidados.api.url}") String baseUrl, @Value("${extraidados.api.token}") String token, @Value("${extraidados.api.userId}") String userId) {
    this.httpClient = httpClient;
    this.baseUrl = baseUrl;
    this.token = token;
    this.userId = userId;
  }

  // Assíncrono com Multipart
  public String startAsyncProcessing(String filePath, String documentClassification) throws IOException {
    validateRequiredFields(token, userId, filePath, documentClassification);

    HttpPost httpPost = new HttpPost(baseUrl + "/processAsync");
    httpPost.setHeader("Authorization", "Bearer " + token);

    File file = new File(filePath);
    var builder = MultipartEntityBuilder.create();
    builder.addTextBody("userId", userId, ContentType.TEXT_PLAIN);
    builder.addTextBody("documentClassification", documentClassification, ContentType.TEXT_PLAIN);
    builder.addBinaryBody("image", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());

    HttpEntity multipart = builder.build();
    httpPost.setEntity(multipart);

    return executePostRequest(httpPost, "startAsyncProcessing");
  }

  // Assíncrono com Base64
  public String startAsyncProcessingWithBase64(String base64Image, String documentClassification) throws IOException {
    validateRequiredFields(token, userId, base64Image, documentClassification);

    HttpPost httpPost = new HttpPost(baseUrl + "/processWithoutFileAsync");
    httpPost.setHeader("Authorization", "Bearer " + token);

    String body = String.format("{\"image\": \"%s\", \"userId\": \"%s\", \"documentClassification\": \"%s\"}", base64Image, userId, documentClassification);
    httpPost.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));

    return executePostRequest(httpPost, "startAsyncProcessingWithBase64");
  }

  // Síncrono com Multipart
  public String processSync(String filePath, String documentClassification) throws IOException {
    validateRequiredFields(token, userId, filePath, documentClassification);

    HttpPost httpPost = new HttpPost(baseUrl + "/process");
    httpPost.setHeader("Authorization", "Bearer " + token);

    File file = new File(filePath);
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.addTextBody("userId", userId, ContentType.TEXT_PLAIN);
    builder.addTextBody("documentClassification", documentClassification, ContentType.TEXT_PLAIN);
    builder.addBinaryBody("image", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());

    HttpEntity multipart = builder.build();
    httpPost.setEntity(multipart);

    return executePostRequest(httpPost, "processSync");
  }

  // Síncrono com Base64
  public String processSyncWithBase64(String base64Image, String documentClassification) throws IOException {
    validateRequiredFields(token, userId, base64Image, documentClassification);

    HttpPost httpPost = new HttpPost(baseUrl + "/processWithoutFile");
    httpPost.setHeader("Authorization", "Bearer " + token);

    String body = String.format("{\"image\": \"%s\", \"userId\": \"%s\", \"documentClassification\": \"%s\"}", base64Image, userId, documentClassification);
    httpPost.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));

    return executePostRequest(httpPost, "processSyncWithBase64");
  }

  // Consulta de Resultado (Assíncrono)
  public String getProcessingResult(String processId) throws IOException {
    validateRequiredFields(token, userId, processId);

    HttpPost httpPost = new HttpPost(baseUrl + "/processResult");
    httpPost.setHeader("Authorization", "Bearer " + token);

    String body = String.format("{\"userId\": \"%s\", \"processId\": \"%s\"}", userId, processId);
    httpPost.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));

    return executePostRequest(httpPost, "getProcessingResult");
  }

  private void validateRequiredFields(String... fields) {
    for (String field : fields) {
      if (field == null || field.trim().isEmpty()) {
        throw new IllegalArgumentException("Campo obrigatório não informado: " + field);
      }
    }
  }

  private String executePostRequest(HttpPost httpPost, String methodName) throws IOException {
    try {
      logger.info("Executando {} na URL: {}", methodName, httpPost.getURI());
      HttpResponse response = httpClient.execute(httpPost);
      int statusCode = response.getStatusLine().getStatusCode();
      String responseBody = EntityUtils.toString(response.getEntity());

      if (statusCode >= 200 && statusCode <= 299) {
        logger.info("Requisição {} bem-sucedida: {}", methodName, responseBody);
        return responseBody;
      } else if (statusCode == 400) {
        throw new IOException("Erro 400 (Bad Request): " + responseBody);
      } else if (statusCode == 402) {
        throw new IOException("Erro 402 (Pagamento necessário): " + responseBody);
      } else if (statusCode == 500) {
        throw new IOException("Erro 500 (Internal Server Error): " + responseBody);
      } else {
        throw new IOException("Falha na requisição (" + statusCode + "): " + responseBody);
      }
    } finally {
      httpPost.releaseConnection();
    }
  }
}