package com.algaworks.algasensors.device.management.domain.repository;

import com.algaworks.algasensors.device.management.domain.model.Sensor;
import com.algaworks.algasensors.device.management.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Na verdade nem precisa anotar como Repository.
//O Spring scaneia o projeto ja ve que essa classe extende JPARepository
//Com isso já sabe que ela é um repositório.
@Repository
public interface SensorRepository extends JpaRepository<Sensor, SensorId> {

}
