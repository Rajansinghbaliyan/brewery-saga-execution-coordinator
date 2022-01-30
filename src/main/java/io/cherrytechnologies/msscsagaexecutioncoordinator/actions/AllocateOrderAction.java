package io.cherrytechnologies.msscsagaexecutioncoordinator.actions;

import guru.sfg.common.events.AllocateOrderEvent;
import guru.sfg.common.models.BeerOrderDto;
import io.cherrytechnologies.msscsagaexecutioncoordinator.config.JmsConfig;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderState;
import io.cherrytechnologies.msscsagaexecutioncoordinator.services.StateMachineServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AllocateOrderAction {
    private final JmsTemplate jmsTemplate;

    public Action<BeerOrderState, BeerOrderEvent> action() {
        return context -> Optional.ofNullable(context.getMessageHeaders())
                .map(messages -> (BeerOrderDto) messages.get(StateMachineServiceImpl.BEER_ORDER))
                .ifPresent(beerOrderDto -> {
                    log.info("Running the allocate order action for beer order id: " + beerOrderDto.getId());
                    jmsTemplate.convertAndSend(
                            JmsConfig.ALLOCATE_ORDER_QUEUE,
                            AllocateOrderEvent.builder()
                                    .beerOrderDto(beerOrderDto)
                                    .build()
                    );
                });
    }
}
