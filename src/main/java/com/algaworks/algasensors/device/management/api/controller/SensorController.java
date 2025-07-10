package com.algaworks.algasensors.device.management.api.controller;

import com.algaworks.algasensors.device.management.api.client.SensorMonitoringClient;
import com.algaworks.algasensors.device.management.api.model.SensorDetailOutput;
import com.algaworks.algasensors.device.management.api.model.SensorInput;
import com.algaworks.algasensors.device.management.api.model.SensorMonitoringOutput;
import com.algaworks.algasensors.device.management.api.model.SensorOutput;
import com.algaworks.algasensors.device.management.common.IdGenerator;
import com.algaworks.algasensors.device.management.domain.model.Sensor;
import com.algaworks.algasensors.device.management.domain.model.SensorId;
import com.algaworks.algasensors.device.management.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/sensors")
@RequiredArgsConstructor
public class SensorController {

    //Injeção via Construtor é mais correta que o @Autowired.
    //Como estamos utilizando lombok e a palavra final.
    //Ao anotar o @RequiredArgsConstructor ele ja cria o construtor com a depencendia para a gente.
    private final SensorRepository sensorRepository;

    private final SensorMonitoringClient sensorMonitoringClient;

    @GetMapping
    public Page<SensorOutput> findAll(@PageableDefault Pageable pageable) {
        Page<Sensor> sensors = sensorRepository.findAll(pageable);

        return sensors.map(this::convertToOutputModel);
    }

    //Utiliza o conversor StringToTSIDWebConverter para que o Spring converta o ID String em TSID.
    @GetMapping("{sensorId}")
    public SensorOutput findOne(@PathVariable TSID sensorId){
        Sensor sensor = findByIdOrFail(sensorId);

        return convertToOutputModel(sensor);
    }

    @GetMapping("{sensorId}/detail")
    public SensorDetailOutput findOneWithDetail(@PathVariable TSID sensorId){
        Sensor sensor = findByIdOrFail(sensorId);

        SensorMonitoringOutput sensorMonitoringOutput = sensorMonitoringClient.getDetail(sensorId);
        SensorOutput sensorOutput = convertToOutputModel(sensor);

        return SensorDetailOutput.builder()
                .sensor(sensorOutput)
                .monitoring(sensorMonitoringOutput)
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SensorOutput create(@RequestBody SensorInput sensorInput) {
        Sensor sensor = Sensor.builder()
                .id(new SensorId(IdGenerator.generateTSID()))
                .name(sensorInput.getName())
                .ip(sensorInput.getIp())
                .location(sensorInput.getLocation())
                .protocol(sensorInput.getLocation())
                .model(sensorInput.getModel())
                .enabled(false)
                .build();

        sensorRepository.saveAndFlush(sensor);

        return convertToOutputModel(sensor);
    }

    @PutMapping("{sensorId}")
    public SensorOutput update(@RequestBody @Valid SensorInput sensorInput, @PathVariable TSID sensorId) {
        Sensor sensor = findByIdOrFail(sensorId);

        BeanUtils.copyProperties(sensorInput, sensor, "id");
        sensorRepository.saveAndFlush(sensor);

        return convertToOutputModel(sensor);
    }

    @DeleteMapping("{sensorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable TSID sensorId) {
        Sensor sensor = findByIdOrFail(sensorId);

        sensorRepository.delete(sensor);

        sensorMonitoringClient.disableMonitoring(sensorId);
    }

    @PutMapping("{sensorId}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable TSID sensorId) {
        Sensor sensor = findByIdOrFail(sensorId);
        sensor.setEnabled(true);

        sensorRepository.saveAndFlush(sensor);

        sensorMonitoringClient.enableMonitoring(sensorId);
    }

    @DeleteMapping("{sensorId}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable TSID sensorId) {
        Sensor sensor = findByIdOrFail(sensorId);
        sensor.setEnabled(false);

        sensorRepository.saveAndFlush(sensor);

        sensorMonitoringClient.disableMonitoring(sensorId);
    }

    private SensorOutput convertToOutputModel(Sensor sensor) {
        return SensorOutput.builder()
                .id(sensor.getId().getValue())
                .name(sensor.getName())
                .ip(sensor.getIp())
                .location(sensor.getLocation())
                .protocol(sensor.getLocation())
                .model(sensor.getModel())
                .enabled(sensor.getEnabled())
                .build();
    }

    private Sensor findByIdOrFail(TSID sensorId) {
        return sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


}
