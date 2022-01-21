package io.cherrytechnologies.msscsagaexecutioncoordinator.services;

import guru.sfg.common.events.NoInventoryEvent;
import guru.sfg.common.events.PendingToValidateEvent;
import guru.sfg.common.models.BeerOrderDto;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderState;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StateMachineServiceImpl implements StateMachineService {
    public static final String BEER_ORDER_ID = "beer_order_id_header";
    public static final String BEER_ORDER = "beer_order_header";
    public static final String NO_INVENTORY_BEER_LIST = "no_inventory_beer_list";

    private final StateMachineFactory<BeerOrderState, BeerOrderEvent> factory;
    private final StateChangeInterceptor interceptor;

    @Override
    public StateMachine<BeerOrderState, BeerOrderEvent> newOrderService(BeerOrderDto beerOrderDto) {
        beerOrderDto.setStatus(BeerOrderState.NEW);
        StateMachine<BeerOrderState, BeerOrderEvent> stateMachine = build(beerOrderDto.getId(), beerOrderDto.getStatus());

        stateMachine.sendEvent(MessageBuilder
                .withPayload(BeerOrderEvent.VALIDATE_ORDER)
                .setHeader(BEER_ORDER, beerOrderDto)
                .build()
        );

        return stateMachine;
    }

    @Override
    public StateMachine<BeerOrderState, BeerOrderEvent> validateOrderService(BeerOrderDto beerOrderDto) {
        UUID beerOrderId = beerOrderDto.getId();
        BeerOrderState state = beerOrderDto.getStatus();
        StateMachine<BeerOrderState, BeerOrderEvent> stateMachine = build(beerOrderId, state);
        sendEvent(stateMachine, beerOrderId, BeerOrderEvent.VALIDATE_ORDER);
        return stateMachine;
    }

    @Override
    public StateMachine<BeerOrderState, BeerOrderEvent> validateSuccessService(BeerOrderDto beerOrderDto) {
        StateMachine<BeerOrderState, BeerOrderEvent> stateMachine = build(beerOrderDto.getId(), beerOrderDto.getStatus());
        stateMachine.sendEvent(
                MessageBuilder.withPayload(BeerOrderEvent.VALIDATION_SUCCESS)
                .setHeader(BEER_ORDER,beerOrderDto)
                .build()
        );
        return stateMachine;
    }

    @Override
    public StateMachine<BeerOrderState, BeerOrderEvent> validateButNoInventoryService(NoInventoryEvent event) {
        BeerOrderDto beerOrderDto = event.getBeerOrderDto();
        StateMachine<BeerOrderState, BeerOrderEvent> stateMachine = build(beerOrderDto.getId(), beerOrderDto.getStatus());
        stateMachine.sendEvent(
                MessageBuilder.withPayload(BeerOrderEvent.VALIDATED_BUT_NO_INVENTORY)
                        .setHeader(BEER_ORDER,beerOrderDto)
                        .setHeader(NO_INVENTORY_BEER_LIST,event.getBeersWithLessInventory())
                        .build()
        );
        return stateMachine;
    }

    @Override
    public StateMachine<BeerOrderState, BeerOrderEvent> pendingToValidateService(PendingToValidateEvent event) {
        BeerOrderDto beerOrderDto = event.getBeerOrderDto();
        StateMachine<BeerOrderState, BeerOrderEvent> stateMachine = build(beerOrderDto.getId(), beerOrderDto.getStatus());
        stateMachine.sendEvent(
                MessageBuilder.withPayload(BeerOrderEvent.PENDING_TO_VALIDATED)
                        .setHeader(BEER_ORDER,beerOrderDto)
                        .build()
        );
        return stateMachine;
    }

    private StateMachine<BeerOrderState, BeerOrderEvent> build(UUID beerOrderId, BeerOrderState state) {
        StateMachine<BeerOrderState, BeerOrderEvent> stateMachine = factory.getStateMachine(beerOrderId);
        stateMachine.start();

        stateMachine.start();

        stateMachine.getStateMachineAccessor().doWithAllRegions(sma -> {
            sma.addStateMachineInterceptor(interceptor);
            sma.resetStateMachine(
                    new DefaultStateMachineContext<>(
                            state,
                            null,
                            null,
                            null
                    )
            );
        });

        stateMachine.start();

        return stateMachine;
    }

    private void sendEvent(StateMachine<BeerOrderState, BeerOrderEvent> stateMachine, UUID beerOrderId,
                           BeerOrderEvent event) {
        stateMachine.sendEvent(
                MessageBuilder
                        .withPayload(event)
                        .setHeader(BEER_ORDER_ID, beerOrderId)
                        .build()
        );
    }
}
