package io.cherrytechnologies.msscsagaexecutioncoordinator.actions;

import guru.sfg.common.events.BrewMoreBeerEvent;
import guru.sfg.common.events.NoInventoryEvent;
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

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidButNoInventoryAction {

    private final JmsTemplate jmsTemplate;

    public Action<BeerOrderState, BeerOrderEvent> action() {
        return context -> Optional.ofNullable(context.getMessageHeaders())
                .map(messages -> NoInventoryEvent
                        .builder()
                        .beerOrderDto((BeerOrderDto) messages.get(StateMachineServiceImpl.BEER_ORDER))
                        .beersWithLessInventory((ArrayList<UUID>) messages.get(StateMachineServiceImpl.NO_INVENTORY_BEER_LIST))
                        .build()
                )
                .ifPresent(event -> {
                    log.info("Running the validate successful action for beer order id: " + event.getBeerOrderDto().getId());
                    event.getBeerOrderDto().setStatus(BeerOrderState.PENDING_INVENTORY);
                    jmsTemplate.convertAndSend(
                            JmsConfig.BREW_MORE_BEER_QUEUE,
                            BrewMoreBeerEvent.builder()
                                    .beerOrderDto(event.getBeerOrderDto())
                                    .beersWithLessInventory(event.getBeersWithLessInventory())
                                    .build()
                    );
                });
    }
}
