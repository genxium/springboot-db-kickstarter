package com.mytrial.auth.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class AuthChannelInfoRespDto {
    private final String channelName;
    private final String channelVersion;
    private final boolean channelAvailable;
}
