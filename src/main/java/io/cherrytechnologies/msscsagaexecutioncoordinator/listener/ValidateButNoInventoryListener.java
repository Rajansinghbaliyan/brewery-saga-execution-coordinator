package io.cherrytechnologies.msscsagaexecutioncoordinator.listener;

import guru.sfg.common.events.NoInventoryEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.config.JmsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ValidateButNoInventoryListener {
    @JmsListener(destination = JmsConfig.VALIDATION_BUT_NO_INVENTORY_QUEUE)
    public void listener(NoInventoryEvent event){
        log.info("Validate but no inventory event for beer order id: "+ event.getBeerOrderDto().getId());
    }
}
