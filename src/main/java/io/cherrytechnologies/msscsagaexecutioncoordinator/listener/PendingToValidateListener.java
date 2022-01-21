package io.cherrytechnologies.msscsagaexecutioncoordinator.listener;

import guru.sfg.common.events.PendingToValidateEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.config.JmsConfig;
import io.cherrytechnologies.msscsagaexecutioncoordinator.services.StateMachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PendingToValidateListener {
    private final StateMachineService stateMachineService;

    @JmsListener(destination = JmsConfig.PENDING_TO_VALIDATE_QUEUE)
    public void listener(PendingToValidateEvent event){
        log.info("Pending to validate listener for order id: "+ event.getBeerOrderDto().getId());
        stateMachineService.pendingToValidateService(event);
    }
}
