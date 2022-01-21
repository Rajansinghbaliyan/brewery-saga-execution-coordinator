package io.cherrytechnologies.msscsagaexecutioncoordinator.listener;

import guru.sfg.common.events.NoInventoryEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.config.JmsConfig;
import io.cherrytechnologies.msscsagaexecutioncoordinator.services.StateMachineService;
import io.cherrytechnologies.msscsagaexecutioncoordinator.services.StateMachineServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidateButNoInventoryListener {
    private final StateMachineService stateMachineService;
    @JmsListener(destination = JmsConfig.VALIDATION_BUT_NO_INVENTORY_QUEUE)
    public void listener(NoInventoryEvent event){
        log.info("Validate but no inventory event for beer order id: "+ event.getBeerOrderDto().getId());
        stateMachineService.validateButNoInventoryService(event);
    }
}
