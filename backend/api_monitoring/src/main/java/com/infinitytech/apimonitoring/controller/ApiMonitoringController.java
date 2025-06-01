package com.infinitytech.apimonitoring.controller;

import com.infinitytech.apimonitoring.model.Incident;
import com.infinitytech.apimonitoring.service.ApiMonitoringService;
import com.infinitytech.apimonitoring.repository.IncidentRepository;
import com.opencsv.CSVWriter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/monitor")
public class ApiMonitoringController {

  private final ApiMonitoringService service;
  private final IncidentRepository incidentRepository;

  public ApiMonitoringController(ApiMonitoringService service, IncidentRepository incidentRepository) {
    this.service = service;
    this.incidentRepository = incidentRepository;
  }

  // Assíncrono com Multipart
  @PostMapping("/process/async/multipart")
  @Operation(summary = "Inicia o processamento assíncrono de um arquivo (Multipart)", description = "Envia um arquivo para a API ExtrAI Dados e retorna o processId")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Processamento iniciado com sucesso"),
          @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
          @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public ResponseEntity<String> startProcessingAsyncMultipart(
          @RequestParam("file") MultipartFile file,
          @RequestParam String documentClassification,
          @RequestParam(required = false) String expectedDocumentType) {
    try {
      if (file == null || file.isEmpty() || documentClassification == null || documentClassification.trim().isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Arquivo e classificação são obrigatórios");
      }
      // Aqui você pode salvar o arquivo ou processar conforme sua lógica
      // Exemplo: salvar em disco temporariamente
      String filePath = "uploads/" + file.getOriginalFilename();
      file.transferTo(new java.io.File(filePath));

      String processId = service.startProcessing(filePath, documentClassification);
      return ResponseEntity.ok(processId);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Erro ao iniciar processamento: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("Parâmetros inválidos: " + e.getMessage());
    }
  }

  // Assíncrono com Base64
  @PostMapping("/process/async/base64")
  @Operation(summary = "Inicia o processamento assíncrono de uma imagem em Base64", description = "Envia uma imagem em Base64 para a API ExtrAI Dados e retorna o processId")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Processamento iniciado com sucesso"),
          @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
          @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public ResponseEntity<String> startProcessingAsyncBase64(
          @RequestBody String base64Image,
          @RequestParam String documentClassification,
          @RequestParam(required = false) String expectedDocumentType) {
    try {
      if (base64Image == null || base64Image.trim().isEmpty() || documentClassification == null || documentClassification.trim().isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parâmetros obrigatórios não podem ser vazios");
      }
      String processId = service.startProcessingWithBase64(base64Image, documentClassification);
      return ResponseEntity.ok(processId);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Erro ao iniciar processamento: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("Parâmetros inválidos: " + e.getMessage());
    }
  }

  // Síncrono com Multipart
  @PostMapping("/process/sync/multipart")
  @Operation(summary = "Processa um arquivo de forma síncrona (Multipart)", description = "Envia um arquivo para a API ExtrAI Dados e retorna o resultado imediatamente")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Processamento concluído com sucesso"),
          @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
          @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public ResponseEntity<Incident> processSyncMultipart(
          @RequestParam String filePath,
          @RequestParam String documentClassification,
          @RequestParam(required = false) String expectedDocumentType) {
    try {
      if (filePath == null || filePath.trim().isEmpty() || documentClassification == null || documentClassification.trim().isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
      Incident incident = service.processSync(filePath, documentClassification, expectedDocumentType);
      return ResponseEntity.ok(incident);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(null);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(null);
    }
  }

  // Síncrono com Base64
  @PostMapping("/process/sync/base64")
  @Operation(summary = "Processa uma imagem em Base64 de forma síncrona", description = "Envia uma imagem em Base64 para a API ExtrAI Dados e retorna o resultado imediatamente")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Processamento concluído com sucesso"),
          @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
          @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public ResponseEntity<Incident> processSyncBase64(
          @RequestBody String base64Image,
          @RequestParam String documentClassification,
          @RequestParam(required = false) String expectedDocumentType) {
    try {
      if (base64Image == null || base64Image.trim().isEmpty() || documentClassification == null || documentClassification.trim().isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
      Incident incident = service.processSyncWithBase64(base64Image, documentClassification, expectedDocumentType);
      return ResponseEntity.ok(incident);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(null);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(null);
    }
  }

  // Consulta de Resultado (Assíncrono)
  @PostMapping("/result/{processId}")
  @Operation(summary = "Consulta o resultado do processamento assíncrono", description = "Retorna o incidente associado ao processId")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Resultado encontrado"),
          @ApiResponse(responseCode = "404", description = "ProcessId não encontrado"),
          @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
          @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public ResponseEntity<Incident> checkResult(
          @PathVariable String processId,
          @RequestParam(required = false) String expectedDocumentType) {
    try {
      if (processId == null || processId.trim().isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
      Incident incident = service.checkResult(processId, expectedDocumentType);
      if (incident == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
      return ResponseEntity.ok(incident);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(null);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(null);
    }
  }

  @GetMapping("/incidents")
  @Operation(summary = "Lista todos os incidentes", description = "Retorna uma lista de todos os incidentes registrados")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
          @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public ResponseEntity<List<Incident>> listIncidents() {
    try {
      List<Incident> incidents = incidentRepository.findAll();
      return ResponseEntity.ok(incidents);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(null);
    }
  }

  @GetMapping("/incidents/by-client/{clientId}")
  @Operation(summary = "Lista incidentes por cliente", description = "Retorna uma lista de incidentes para o clientId especificado")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
          @ApiResponse(responseCode = "400", description = "clientId inválido"),
          @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public ResponseEntity<List<Incident>> listIncidentsByClient(@PathVariable Long clientId) {
    try {
      List<Incident> incidents = incidentRepository.findByClientId(clientId);
      return ResponseEntity.ok(incidents);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(null);
    }
  }

  @GetMapping("/incidents/by-date")
  @Operation(summary = "Lista incidentes por intervalo de datas", description = "Retorna uma lista de incidentes entre as datas especificadas")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
          @ApiResponse(responseCode = "400", description = "Formato de data inválido"),
          @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public ResponseEntity<List<Incident>> listIncidentsByDate(
          @RequestParam String start,
          @RequestParam String end) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
      LocalDateTime startDate = LocalDateTime.parse(start, formatter);
      LocalDateTime endDate = LocalDateTime.parse(end, formatter);
      List<Incident> incidents = incidentRepository.findByTimestampBetween(startDate, endDate);
      return ResponseEntity.ok(incidents);
    } catch (DateTimeParseException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(null);
    }
  }

  @GetMapping("/incidents/errors")
  @Operation(summary = "Lista incidentes com erros", description = "Retorna uma lista de incidentes com mensagens de erro não nulas")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
          @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public ResponseEntity<List<Incident>> listIncidentsWithErrors() {
    try {
      List<Incident> incidents = incidentRepository.findByErrorDescriptionIsNotNull();
      return ResponseEntity.ok(incidents);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(null);
    }
  }

  @GetMapping(value = "/incidents/export/csv", produces = MediaType.TEXT_PLAIN_VALUE)
  @Operation(summary = "Exporta incidentes para CSV", description = "Gera um arquivo CSV com incidentes filtrados")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "CSV gerado com sucesso"),
          @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
          @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public ResponseEntity<String> exportIncidentsToCsv(
          @RequestParam(required = false) Long clientId,
          @RequestParam(required = false) Long aiModelId,
          @RequestParam(required = false) String start,
          @RequestParam(required = false) String end) {
    try {
      List<Incident> incidents;
      if (clientId != null && aiModelId != null && start != null && end != null) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        incidents = incidentRepository.findByClientIdAndAiModelIdAndTimestampBetween(clientId, aiModelId, startDate, endDate);
      } else if (clientId != null && start != null && end != null) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        incidents = incidentRepository.findByClientIdAndTimestampBetween(clientId, startDate, endDate);
      } else if (aiModelId != null && start != null && end != null) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        incidents = incidentRepository.findByAiModelIdAndTimestampBetween(aiModelId, startDate, endDate);
      } else if (clientId != null) {
        incidents = incidentRepository.findByClientId(clientId);
      } else if (aiModelId != null) {
        incidents = incidentRepository.findByAiModelId(aiModelId);
      } else if (start != null && end != null) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        incidents = incidentRepository.findByTimestampBetween(startDate, endDate);
      } else {
        incidents = incidentRepository.findAll();
      }

      StringWriter stringWriter = new StringWriter();
      try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
        String[] header = {"ID", "Error Description", "Timestamp", "Client ID", "AI Model ID", "Status", "Process ID", "Extracted Fields"};
        csvWriter.writeNext(header);

        for (Incident incident : incidents) {
          csvWriter.writeNext(new String[]{
                  String.valueOf(incident.getId()),
                  incident.getErrorDescription() != null ? incident.getErrorDescription().replace(",", ";") : "",
                  incident.getTimestamp() != null ? incident.getTimestamp().toString() : "",
                  (incident.getClient() != null && incident.getClient().getId() != null) ? String.valueOf(incident.getClient().getId()) : "",
                  (incident.getAiModel() != null && incident.getAiModel().getId() != null) ? String.valueOf(incident.getAiModel().getId()) : "",
                  incident.getStatus() != null ? incident.getStatus() : "",
                  incident.getProcessId() != null ? incident.getProcessId() : "",
                  incident.getExtractedFields() != null ? incident.getExtractedFields().replace(",", ";") : ""
          });
        }
      }

      return ResponseEntity.ok()
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=incidents.csv")
              .contentType(MediaType.parseMediaType("text/csv"))
              .body(stringWriter.toString());
    } catch (DateTimeParseException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("Formato de data inválido: " + e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Erro ao exportar CSV: " + e.getMessage());
    }
  }
}