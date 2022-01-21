package io.cherrytechnologies.msscsagaexecutioncoordinator.listener;

import guru.sfg.common.events.ValidationFailedEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.config.JmsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ValidationFailedListener {
    @JmsListener(destination = JmsConfig.VALIDATION_FAILED_QUEUE)
    public void listener(ValidationFailedEvent event){
        log.info("validation failed for beer order id: "+ event.getBeerOrderDto().getId());
    }
}
