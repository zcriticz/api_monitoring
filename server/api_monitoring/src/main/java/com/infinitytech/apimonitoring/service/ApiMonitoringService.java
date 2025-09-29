package com.infinitytech.apimonitoring.service;

import com.infinitytech.apimonitoring.client.ExtraIdadosClient;
import com.infinitytech.apimonitoring.model.Client;
import com.infinitytech.apimonitoring.model.AiModel;
import com.infinitytech.apimonitoring.model.Incident;
import com.infinitytech.apimonitoring.repository.ClientRepository;
import com.infinitytech.apimonitoring.repository.AIModelRepository;
import com.infinitytech.apimonitoring.repository.IncidentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ApiMonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(ApiMonitoringService.class);

    // Lista de classificações válidas
    private static final List<String> VALID_DOCUMENT_CLASSIFICATIONS = Arrays.asList(
            "balanco_patrimonial_20231116", "boleto", "certidao_casamento", "certidao_nascimento",
            "certificado_ensino_medio", "cnh", "comprovante_residencia", "contrato_social_20240121",
            "cpf", "cte", "curva_abc", "declaracao_ir_2023", "diploma", "diploma_verso",
            "endividamento", "faturamento", "historico_ensino_medio", "historico_ensino_superior",
            "nfs", "recibos", "rg"
    );

    private final ExtraIdadosClient extraIdadosClient;
    private final ObjectMapper objectMapper;
    private final IncidentRepository incidentRepository;
    private final ClientRepository clientRepository;
    private final AIModelRepository aiModelRepository;

    public ApiMonitoringService(
            ExtraIdadosClient extraIdadosClient,
            ObjectMapper objectMapper,
            IncidentRepository incidentRepository,
            ClientRepository clientRepository,
            AIModelRepository aiModelRepository) {
        this.extraIdadosClient = extraIdadosClient;
        this.objectMapper = objectMapper;
        this.incidentRepository = incidentRepository;
        this.clientRepository = clientRepository;
        this.aiModelRepository = aiModelRepository;
    }

    @PostConstruct
    public void init() {
        try {
            if (clientRepository.count() == 0) {
                Client client = new Client();
                client.setName("Default Client");
                client.setContactInfo("contact@client.com");
                clientRepository.save(client);
                logger.info("Default client criado com sucesso: {}", client);
            }
            if (aiModelRepository.count() == 0) {
                AiModel aiModel = new AiModel();
                aiModel.setModelName("Vision Model");
                aiModel.setVersion("v1");
                aiModelRepository.save(aiModel);
                logger.info("Default AI model criado com sucesso: {}", aiModel);
            }
        } catch (Exception e) {
            logger.error("Erro ao inicializar dados padrão: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao inicializar dados padrão", e);
        }
    }

    // Assíncrono com Multipart
    public String startProcessing(String filePath, String documentClassification) throws IOException {
        validateInput(filePath, documentClassification);

        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            String errorMessage = "Invalid file: " + filePath;
            registerIncident(errorMessage, null);
            throw new IOException(errorMessage);
        }

        try {
            String processId = extraIdadosClient.startAsyncProcessing(filePath, documentClassification);
            registerIncident("Processing started with processId: " + processId, processId);
            logger.info("Processamento assíncrono (Multipart) iniciado com processId: {}", processId);
            return processId;
        } catch (IOException e) {
            handleProcessingError(e, null);
            throw e;
        } catch (IllegalArgumentException e) {
            handleValidationError(e, null);
            throw e;
        }
    }

    // Assíncrono com Base64
    public String startProcessingWithBase64(String base64Image, String documentClassification) throws IOException {
        validateInput(base64Image, documentClassification);

        try {
            String processId = extraIdadosClient.startAsyncProcessingWithBase64(base64Image, documentClassification);
            registerIncident("Processing started with processId: " + processId, processId);
            logger.info("Processamento assíncrono (Base64) iniciado com processId: {}", processId);
            return processId;
        } catch (IOException e) {
            handleProcessingError(e, null);
            throw e;
        } catch (IllegalArgumentException e) {
            handleValidationError(e, null);
            throw e;
        }
    }

    // Síncrono com Multipart
    public Incident processSync(String filePath, String documentClassification, String expectedDocumentType) throws IOException {
        validateInput(filePath, documentClassification);

        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            String errorMessage = "Invalid file: " + filePath;
            registerIncident(errorMessage, null);
            throw new IOException(errorMessage);
        }

        try {
            String response = extraIdadosClient.processSync(filePath, documentClassification);
            return processResult(response, null, expectedDocumentType);
        } catch (IOException e) {
            handleProcessingError(e, null);
            throw e;
        } catch (IllegalArgumentException e) {
            handleValidationError(e, null);
            throw e;
        }
    }

    // Síncrono com Base64
    public Incident processSyncWithBase64(String base64Image, String documentClassification, String expectedDocumentType) throws IOException {
        validateInput(base64Image, documentClassification);

        try {
            String response = extraIdadosClient.processSyncWithBase64(base64Image, documentClassification);
            return processResult(response, null, expectedDocumentType);
        } catch (IOException e) {
            handleProcessingError(e, null);
            throw e;
        } catch (IllegalArgumentException e) {
            handleValidationError(e, null);
            throw e;
        }
    }

    // Consulta de Resultado (Assíncrono)
    public Incident checkResult(String processId, String expectedDocumentType) throws IOException {
        if (processId == null || processId.trim().isEmpty()) {
            String errorMessage = "Invalid processId: " + processId;
            registerIncident(errorMessage, null);
            logger.error("Erro de validação: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        try {
            String response = extraIdadosClient.getProcessingResult(processId);
            return processResult(response, processId, expectedDocumentType);
        } catch (IOException e) {
            handleProcessingError(e, processId);
            throw e;
        } catch (IllegalArgumentException e) {
            handleValidationError(e, processId);
            throw e;
        }
    }

    private void validateInput(String input, String documentClassification) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input (file path or Base64 image) cannot be null or empty");
        }
        if (documentClassification == null || documentClassification.isEmpty()) {
            throw new IllegalArgumentException("Document classification cannot be null or empty");
        }
        if (!VALID_DOCUMENT_CLASSIFICATIONS.contains(documentClassification)) {
            throw new IllegalArgumentException("Invalid document classification: " + documentClassification +
                    ". Must be one of: " + VALID_DOCUMENT_CLASSIFICATIONS);
        }
    }

    private Incident processResult(String response, String processId, String expectedDocumentType) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(response);

        Incident incident = processId != null ? incidentRepository.findByProcessId(processId).orElse(new Incident()) : new Incident();
        incident.setProcessId(processId);
        incident.setTimestamp(LocalDateTime.now());

        Client client = clientRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Default client not found"));
        incident.setClient(client);

        AiModel aiModel = aiModelRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Default AI model not found"));
        incident.setAiModel(aiModel);

        JsonNode resultados = jsonNode.get("resultados");
        String detectedType = null;
        if (resultados != null && resultados.isArray() && resultados.size() > 0) {
            JsonNode camposExtraidos = resultados.get(0).get("camposExtraidos");
            if (camposExtraidos != null) {
                incident.setExtractedFields(camposExtraidos.toString());
            }
            JsonNode tipoDeDocumento = resultados.get(0).get("tipoDeDocumento");
            if (tipoDeDocumento != null && tipoDeDocumento.has("text")) {
                detectedType = tipoDeDocumento.get("text").asText();
            }
        }

        if (detectedType != null && expectedDocumentType != null && !detectedType.equalsIgnoreCase(expectedDocumentType)) {
            incident.setErrorDescription("Classificação incorreta: esperado " + expectedDocumentType + ", mas detectado " + detectedType);
            incident.setStatus("OPEN");
            logger.warn("Classificação incorreta detectada: esperado {}, detectado {}", expectedDocumentType, detectedType);
        } else {
            JsonNode erros = jsonNode.get("erros");
            if (erros != null && !erros.isEmpty()) {
                incident.setErrorDescription("API error: " + erros.toString());
                incident.setStatus("OPEN");
                logger.warn("Erro da API detectado: {}", erros.toString());
            } else if (jsonNode.has("mensagem") && !jsonNode.get("mensagem").asText().equals("Concluido sem erros.")) {
                incident.setErrorDescription("API error: " + jsonNode.get("mensagem").asText());
                incident.setStatus("OPEN");
                logger.warn("Erro da API detectado: {}", jsonNode.get("mensagem").asText());
            } else {
                incident.setErrorDescription(null);
                incident.setStatus("RESOLVED");
                logger.info("Processamento concluído com sucesso para processId: {}", processId);
            }
        }

        Incident savedIncident = incidentRepository.save(incident);
        logger.info("Incidente salvo: {}", savedIncident);
        return savedIncident;
    }

    private void handleProcessingError(IOException e, String processId) {
        String errorMessage = "Failed to process: " + e.getMessage();
        if (e.getMessage().contains("Erro 402")) {
            errorMessage = "Pagamento necessário (402): " + e.getMessage();
        } else if (e.getMessage().contains("Erro 400")) {
            errorMessage = "Bad Request (400): " + e.getMessage();
        }
        registerIncident(errorMessage, processId);
        logger.error("Erro ao processar: {}", errorMessage);
    }

    private void handleValidationError(IllegalArgumentException e, String processId) {
        String errorMessage = "Dados obrigatórios ausentes: " + e.getMessage();
        registerIncident(errorMessage, processId);
        logger.error("Erro de validação: {}", errorMessage);
    }

    private void registerIncident(String errorDescription, String processId) {
        try {
            Incident incident = new Incident();
            incident.setErrorDescription(errorDescription);
            incident.setTimestamp(LocalDateTime.now());
            incident.setStatus("OPEN");
            incident.setProcessId(processId);

            Client client = clientRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("Default client not found"));
            incident.setClient(client);

            AiModel aiModel = aiModelRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("Default AI model not found"));
            incident.setAiModel(aiModel);

            incidentRepository.save(incident);
            logger.info("Incidente registrado: {}", errorDescription);
        } catch (Exception e) {
            logger.error("Erro ao registrar incidente: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao registrar incidente", e);
        }
    }
}