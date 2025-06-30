package com.algaworks.algasensors.device.management;

import io.hypersistence.tsid.TSID;

public class IdGenerator {

    private static final TSID.Factory tsidFactory = TSID.Factory.builder().build();

    //Tambem funciona sem setar essas variaveis no sistema sem setar o bloco estatico abaixo.
    //Basta colocar esssas configuraões nas variaveis de sistema
    //classe que vai rodar > Edit Configurations... > Environmate Variables.
    //TSID_NODE=7;TSID_NODE_COUNT=64
//    static {
//        //Verifica se as variaveis de sistema para o TSID foram setadas, se não, pega a padrão
//        Optional.ofNullable(System.getenv("tsid.node"))
//                        .ifPresent(tsiNode -> System.setProperty("tsid.node", tsiNode));
//
//        Optional.ofNullable(System.getenv("tsid.node.count"))
//                .ifPresent(tsiNodeCount -> System.setProperty("tsid.node.count", tsiNodeCount));
//
//        tsidFactory = TSID.Factory.builder().build();
//    }

    private IdGenerator() {
    }

    public static TSID generateTSID() {
        return tsidFactory.generate();
    }
}
