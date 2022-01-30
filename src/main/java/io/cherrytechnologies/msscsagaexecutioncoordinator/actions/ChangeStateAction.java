package io.cherrytechnologies.msscsagaexecutioncoordinator.actions;

import guru.sfg.common.events.ChangeStateEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.config.JmsConfig;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;

import java.util.UUID;

@Slf4j
public class ChangeStateAction {
    public static void changeState(BeerOrderState state, UUID beerOrderId, JmsTemplate jmsTemplate) {
        jmsTemplate.convertAndSend(
                JmsConfig.CHANGE_STATE_QUEUE,
                ChangeStateEvent.builder()
                        .beerOrderId(beerOrderId)
                        .beerOrderState(state)
                        .build()
        );
    }
};
