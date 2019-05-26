package com.jstik.fancy.chat.dao.config;

public interface ICassandraConfigProperties {

    String getKeyspace();

    String getPoints();

    Integer getPort();
}
