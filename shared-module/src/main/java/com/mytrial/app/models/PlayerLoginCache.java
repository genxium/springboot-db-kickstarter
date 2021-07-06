package com.mytrial.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;

@Data
@AllArgsConstructor
public class PlayerLoginCache {
    private String series;
    @Column(name = "player_principal")
    private String playerPrincipal;
    @Column(name = "from_public_ip")
    private String fromPublicIp;
    @Column(name = "int_auth_token")
    private String intAuthToken;
    @Column(name = "meta_data")
    private String metaData;
    @Column(name = "created_at")
    private Long createdAt;
    @Column(name = "updated_at")
    private Long updatedAt;
    @Column(name = "deleted_at")
    private Long deletedAt;
}