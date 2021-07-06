package com.mytrial.app.security;

import com.mytrial.app.models.Player;
import com.mytrial.app.models.PlayerTokenTuple;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component("tokenCodecManager")
public class ExtAuthTokenCodecManager {
    private final NoRpcSecurityConfigs noRpcSecurityConfigs;

    @Autowired
    public ExtAuthTokenCodecManager(NoRpcSecurityConfigs noRpcSecurityConfigs) {
        this.noRpcSecurityConfigs = noRpcSecurityConfigs;
    }

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        final SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(noRpcSecurityConfigs.getJasyptSymmetricSecret());
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }

    public String encrypt(final Player player) {
        final long gmtMillis = System.currentTimeMillis();
        final PlayerTokenTuple ptt = PlayerTokenTuple.builder()
                .principal(player.getUsername())
                .roles(player.getRoles())
                .createdAt(gmtMillis)
                .build();

        final String firstSerialized = ptt.toString();
        final String token = stringEncryptor().encrypt(firstSerialized);
        return token;
    }

    public PlayerTokenTuple decryptUserIdAndRoles(final String token) {
        final String firstDeserialized = stringEncryptor().decrypt(token);
        final String[] parts = firstDeserialized.split(";");
        final PlayerTokenTuple ptt = PlayerTokenTuple.builder()
                .principal(parts[0])
                .roles(Integer.valueOf(parts[1]))
                .createdAt(Long.valueOf(parts[2]))
                .build();

        return ptt;
    }
}
