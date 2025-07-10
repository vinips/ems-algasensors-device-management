package com.algaworks.algasensors.device.management.api.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public SensorMonitoringClient sensorMonitoringClient(RestClientFactory factory) {
        RestClient restClient = factory.temperatureMonitoringRestClient();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();

        //O ProxyFactory, com as informações passadas pelo RestClient e pelo Adapter,
        //consegue criar de forma dinâmica um Bean da interface passada, com base nas anotações da mesma
        return proxyFactory.createClient(SensorMonitoringClient.class);
    }
}
