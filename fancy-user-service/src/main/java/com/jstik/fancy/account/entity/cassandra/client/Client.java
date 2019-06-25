package com.jstik.fancy.account.entity.cassandra.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("oauth_client_details")
@Getter
@Setter
public class Client {

    @PrimaryKey
    @Column("client_id")
    private String id;

    @Column("resource_ids")
    private String resourceIds;

    @Column("client_secret")//to store the password of clients
    private String clientSecret;

    @Column("scope")
    private String scope;

    @Column("authorized_grant_types")
    private String authorizedGrantTypes;

    @Column("web_server_redirect_uri")
    private String webServerRedirectUri;

    @Column("authorities")
    private String authorities;

    @Column("access_token_validity")
    private Integer accessTokenValidity;

    @Column("refresh_token_validity")
    private Integer refreshTokenValidity;

    @Column("additional_information")
    private String additionalInformation;

    @Column("autoapprove")
    private String autoApprove;
}
