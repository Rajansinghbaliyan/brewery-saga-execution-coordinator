package io.cherrytechnologies.msscsagaexecutioncoordinator.actions;

import guru.sfg.common.models.BeerOrderDto;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderState;
import io.cherrytechnologies.msscsagaexecutioncoordinator.services.StateMachineServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class ValidationSuccessfulAction {
    public Action<BeerOrderState, BeerOrderEvent> action() {
        return context -> Optional.ofNullable(context.getMessageHeaders())
                .map(messages -> (BeerOrderDto) messages.get(StateMachineServiceImpl.BEER_ORDER))
                .ifPresent(beerOrderDto -> {
                    log.info("Running the validate successful action for beer order id: " + beerOrderDto.getId());
                    context.getStateMachine()
                            .sendEvent(
                                    MessageBuilder
                                            .withPayload(BeerOrderEvent.ALLOCATE_ORDER)
                                            .setHeader(StateMachineServiceImpl.BEER_ORDER, beerOrderDto)
                                            .build()
                            );
                });
    }
}
