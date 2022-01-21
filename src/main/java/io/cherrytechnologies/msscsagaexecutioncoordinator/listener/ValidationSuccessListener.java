package io.cherrytechnologies.msscsagaexecutioncoordinator.listener;

import guru.sfg.common.events.ValidationSuccessfulEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.config.JmsConfig;
import io.cherrytechnologies.msscsagaexecutioncoordinator.services.StateMachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidationSuccessListener {
    private StateMachineService stateMachineService;
    @JmsListener(destination = JmsConfig.VALIDATION_SUCCESS_QUEUE)
    public void listener(ValidationSuccessfulEvent event){
        log.info("Validation successful for beer order id: "+ event.getBeerOrderDto().getId());
        stateMachineService.validateSuccessService(event.getBeerOrderDto());
    }
}
