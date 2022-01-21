package io.cherrytechnologies.msscsagaexecutioncoordinator.listener;

import guru.sfg.common.events.ValidateOrderEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.config.JmsConfig;
import io.cherrytechnologies.msscsagaexecutioncoordinator.services.StateMachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidateListener {

    private final StateMachineService stateMachineService;

    @JmsListener(destination = JmsConfig.VALIDATE_QUEUE)
    public void listener(ValidateOrderEvent event){

    }
}
