package io.cherrytechnologies.msscsagaexecutioncoordinator.actions;

import guru.sfg.common.models.BeerOrderDto;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderState;
import io.cherrytechnologies.msscsagaexecutioncoordinator.services.StateMachineServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class PendingToValidateAction {
    private final JmsTemplate jmsTemplate;

    public Action<BeerOrderState, BeerOrderEvent> action() {
        return context -> Optional.ofNullable(context.getMessageHeaders())
                .map(messages -> (BeerOrderDto) messages.get(StateMachineServiceImpl.BEER_ORDER))
                .ifPresent(beerOrderDto -> {
                    log.info("Running the pending to validate order action for beer order id: " + beerOrderDto.getId());
                });
    }
}
