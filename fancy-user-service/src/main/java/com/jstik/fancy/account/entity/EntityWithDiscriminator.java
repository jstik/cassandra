package com.jstik.fancy.account.entity;

public interface EntityWithDiscriminator extends Identifiable {

    String getDiscriminator();
}
