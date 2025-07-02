package com.algaworks.algasensors.device.management.api.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SensorInput {

    @NotNull
    private String name;

    @NotNull
    private String ip;

    @NotNull
    private String location;

    @NotNull
    private String protocol;

    @NotNull
    private String model;

}
