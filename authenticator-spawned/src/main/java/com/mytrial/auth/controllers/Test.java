package com.mytrial.auth.controllers;

import com.mytrial.app.models.Player;
import com.mytrial.app.security.GrantedRolesMapper;
import com.mytrial.app.shardingsphereds.BeanExporter;
import com.mytrial.auth.models.AuthChannelInfoRespDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class Test {
    private final BeanExporter beanExporter;

    @Autowired
    public Test(BeanExporter beanExporter) {
        this.beanExporter = beanExporter;
    }

    private Map<String, AuthChannelInfoRespDto> authChannelInfoMap;

    @PostConstruct
    void init() {
        authChannelInfoMap = new HashMap<>();
        final AuthChannelInfoRespDto credentialChannelInfo = AuthChannelInfoRespDto.builder()
                .channelName("credentials")
                .channelVersion("1.0.0")
                .channelAvailable(false)
                .build();

        final AuthChannelInfoRespDto qidaChannelInfo = AuthChannelInfoRespDto.builder()
                .channelName("qida")
                .channelVersion("1.0.0")
                .channelAvailable(true)
                .build();

        authChannelInfoMap.put(credentialChannelInfo.getChannelName(), credentialChannelInfo);
        authChannelInfoMap.put(qidaChannelInfo.getChannelName(), qidaChannelInfo);
    }

    @Secured(GrantedRolesMapper.ROLE_LEARNER_NAME)
    @GetMapping(value = "/Walled")
    public ModelAndView walled() {
        final ModelAndView mv = new ModelAndView("test");
        List<Player> players = new LinkedList<>();
        mv.addObject("players", players);
        return mv;
    }

    @GetMapping(value = "/NotWalled")
    public ModelAndView notWalled() {
        final ModelAndView mv = new ModelAndView("test");
        List<Player> players = new LinkedList<>();
        mv.addObject("players", players);
        return mv;
    }

    @Operation(operationId = "AuthChannelInfo", summary = "Info of one or more AuthChannels",
            responses = {
                @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = AuthChannelInfoRespDto.class))),
                @ApiResponse(responseCode = "404", description = "channel not found"),
            })
    @GetMapping(value = "/AuthChannelInfo")
    public ResponseEntity<AuthChannelInfoRespDto> authChannelInfo(@RequestParam final String specifiedChannelName) {
        if (null == specifiedChannelName || !authChannelInfoMap.containsKey(specifiedChannelName)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format(
                    "The specifiedChannelName \"%s\" is not found",
                    specifiedChannelName)
            );
        }
        return new ResponseEntity<>(authChannelInfoMap.get(specifiedChannelName), HttpStatus.OK);
    }
}
