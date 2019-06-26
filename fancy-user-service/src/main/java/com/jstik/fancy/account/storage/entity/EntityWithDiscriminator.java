package com.jstik.fancy.account.storage.entity;

public interface EntityWithDiscriminator extends Identifiable {

    String getDiscriminator();
}
