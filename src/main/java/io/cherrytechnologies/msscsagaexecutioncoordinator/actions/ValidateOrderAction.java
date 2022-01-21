package io.cherrytechnologies.msscsagaexecutioncoordinator.actions;

import guru.sfg.common.events.ValidateOrderEvent;
import guru.sfg.common.models.BeerOrderDto;
import io.cherrytechnologies.msscsagaexecutioncoordinator.config.JmsConfig;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderState;
import io.cherrytechnologies.msscsagaexecutioncoordinator.services.StateMachineServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.action.Action;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ValidateOrderAction {

    private final JmsTemplate jmsTemplate;

    public Action<BeerOrderState, BeerOrderEvent> action() {
        return context -> {
            Optional.ofNullable(context.getMessageHeaders())
                    .map(messages -> (BeerOrderDto) messages.get(StateMachineServiceImpl.BEER_ORDER))
                    .ifPresent(beerOrderDto -> {
                        log.info("Running the validate order action for beer order id: " + beerOrderDto.getId());
                        jmsTemplate.convertAndSend(
                                JmsConfig.VALIDATE_QUEUE,
                                ValidateOrderEvent
                                        .builder()
                                        .beerOrderDto(beerOrderDto)
                                        .build()
                        );
                    });
        };
    }
}
