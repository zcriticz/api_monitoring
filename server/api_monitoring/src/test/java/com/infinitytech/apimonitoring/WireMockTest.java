package com.infinitytech.apimonitoring;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@SpringBootTest
public class WireMockTest {

private WireMockServer wireMockServer;

@BeforeEach
public void setUp() {
        wireMockServer = new WireMockServer(options().port(8089));
        wireMockServer.start();
        configureFor("localhost", 8089);

        // Cenário 1: Assíncrono com Multipart - Sucesso
        stubFor(post(urlEqualTo("/api/portalEngines-processApp/processAsync"))
                .withHeader("Authorization", equalTo("Bearer test-token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"mensagem\": \"Processamento iniciado.\", \"processId\": \"1501d432-d0f2-12ee-97f7-b23983w59597\"}")));

        // Cenário 2: Assíncrono com Base64 - Sucesso
        stubFor(post(urlEqualTo("/api/portalEngines-processApp/processWithoutFileAsync"))
                .withHeader("Authorization", equalTo("Bearer test-token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"mensagem\": \"Processamento iniciado.\", \"processId\": \"1501d432-d0f2-12ee-97f7-b23983w59597\"}")));

        // Cenário 3: Síncrono com Multipart - Sucesso
        stubFor(post(urlEqualTo("/api/portalEngines-processApp/process"))
                .withHeader("Authorization", equalTo("Bearer test-token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"mensagem\": \"Concluido sem erros.\", \"guid\": \"85-21022024-183915-6efa3d97-fad0-490a-a017-20d7660e8894\", \"erros\": {}, \"resultados\": [{\"pagina\": 1, \"qualidadeDaImagem\": {}, \"tipoDeDocumento\": {\"text\": \"rg\"}, \"camposExtraidos\": {\"nome\": [{\"probability\": 0.9693257440487545, \"text\": \"Aline Ferreira Araujo\"}]}}]}")));

        // Cenário 4: Síncrono com Base64 - Sucesso
        stubFor(post(urlEqualTo("/api/portalEngines-processApp/processWithoutFile"))
                .withHeader("Authorization", equalTo("Bearer test-token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"mensagem\": \"Concluido sem erros.\", \"guid\": \"85-21022024-183915-6efa3d97-fad0-490a-a017-20d7660e8894\", \"erros\": {}, \"resultados\": [{\"pagina\": 1, \"qualidadeDaImagem\": {}, \"tipoDeDocumento\": {\"text\": \"rg\"}, \"camposExtraidos\": {\"nome\": [{\"probability\": 0.9693257440487545, \"text\": \"Aline Ferreira Araujo\"}]}}]}")));

        // Cenário 5: Consulta de Resultado - Sucesso
        stubFor(post(urlEqualTo("/api/portalEngines-processApp/processResult"))
                .withHeader("Authorization", equalTo("Bearer test-token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"mensagem\": \"Concluido sem erros.\", \"guid\": \"85-21022024-183915-6efa3d97-fad0-490a-a017-20d7660e8894\", \"erros\": {}, \"resultados\": [{\"pagina\": 1, \"qualidadeDaImagem\": {}, \"tipoDeDocumento\": {\"text\": \"rg\"}, \"camposExtraidos\": {\"nome\": [{\"probability\": 0.9693257440487545, \"text\": \"Aline Ferreira Araujo\"}]}}]}")));

        // Cenário 6: Erro 402 (Pagamento necessário)
        stubFor(post(urlEqualTo("/api/portalEngines-processApp/processAsync"))
                .withHeader("Authorization", equalTo("Bearer invalid-token"))
                .willReturn(aResponse()
                        .withStatus(402)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"Pagamento necessário\"}")));

        // Cenário 7: Erro 400 (Bad Request)
        stubFor(post(urlEqualTo("/api/portalEngines-processApp/processAsync"))
                .withHeader("Authorization", absent())
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"Token não informado\"}")));
}

@AfterEach
public void tearDown() {
        wireMockServer.stop();
}
}