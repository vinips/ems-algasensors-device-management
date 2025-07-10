package com.algaworks.algasensors.device.management.api.client.impl;

import com.algaworks.algasensors.device.management.api.client.RestClientFactory;
import com.algaworks.algasensors.device.management.api.client.SensorMonitoringClient;
import com.algaworks.algasensors.device.management.api.model.SensorMonitoringOutput;
import io.hypersistence.tsid.TSID;
import org.springframework.web.client.RestClient;


/** Classe descontinuada na aula 11.7. Foi substituida pela RestClientConfig, que faz a mesma coisa com menos código
 *  utilizando as anotações na Interface SensorMonitoringClient **/

//@Component
public class SensorMonitoringClientImpl implements SensorMonitoringClient {

    private final RestClient restClient;

    @SuppressWarnings("squid:S125")
    //Melhor fazer a configuração do RestClient assim, via Builder.
    //Se instanciar direto com o RestClient.create(http), ele vem sem nenhuma outra configuração base.
    //Por exemplo os módulos do Jackson não sou chamados, fazendo com que as classes
    //TSIDToStringSerializer e StringToTSIDDeserializer funcionem.
    /*
    public SensorMonitoringClientImpl(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://localhost:8082")
                .requestFactory(generateClientHttpRequestFactory())
                //Com essa configuração, qualquer erro (isError) que chegar, vai ser tratado como SensorMonitoringClientBadGatewayException
                //Configuramos um ApiExceptionHandler para juntamente com essa configuração aqui, enviar a msg de erro corretamente.
                .defaultStatusHandler(HttpStatusCode::isError, ((request, response) -> {
                    throw new SensorMonitoringClientBadGatewayException();
                }))
                .build();
    }*/

    public SensorMonitoringClientImpl(RestClientFactory factory) {
        this.restClient = factory.temperatureMonitoringRestClient();
    }

    @Override
    public void enableMonitoring(TSID sensorId) {
        restClient.put()
                .uri("/api/sensors/{sensorId}/monitoring/enable", sensorId)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void disableMonitoring(TSID sensorId) {
        restClient.delete()
                .uri("/api/sensors/{sensorId}/monitoring/disable", sensorId)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public SensorMonitoringOutput getDetail(TSID sensorId) {
        return restClient.get()
                .uri("/api/sensors/{sensorId}/monitoring", sensorId)
                .retrieve()
                .body(SensorMonitoringOutput.class);
    }
}
