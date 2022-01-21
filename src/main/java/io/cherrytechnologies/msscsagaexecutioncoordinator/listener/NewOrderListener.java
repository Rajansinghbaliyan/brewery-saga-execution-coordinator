package io.cherrytechnologies.msscsagaexecutioncoordinator.listener;

import guru.sfg.common.events.BeerOrderEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.config.JmsConfig;
import io.cherrytechnologies.msscsagaexecutioncoordinator.services.StateMachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewOrderListener {

    private final StateMachineService stateMachineService;

    @JmsListener(destination = JmsConfig.NEW_ORDER_QUEUE)
    public void listener(BeerOrderEvent event) {
        log.info("New Order Listener for Beer Order ID: " + event.getBeerOrderDto().getId());
        stateMachineService.newOrderService(event.getBeerOrderDto());
    }
}
