package io.cherrytechnologies.msscsagaexecutioncoordinator.services;

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

    private final StateMachineFactory<BeerOrderState, BeerOrderEvent> factory;

    @Override
    public StateMachine<BeerOrderState, BeerOrderEvent> newOrderService(BeerOrderDto beerOrderDto) {
        StateMachine<BeerOrderState, BeerOrderEvent> stateMachine = build(beerOrderDto.getId(), beerOrderDto.getStatus());

        stateMachine.sendEvent(MessageBuilder
                .withPayload(BeerOrderEvent.VALIDATE_ORDER)
                .setHeader(BEER_ORDER, beerOrderDto)
                .build()
        );

        return stateMachine;
    }

    @Override
    public StateMachine<BeerOrderState, BeerOrderEvent> validateOrderService(UUID beerOrderId, BeerOrderState state) {
        StateMachine<BeerOrderState, BeerOrderEvent> stateMachine = build(beerOrderId, state);
        sendEvent(stateMachine, beerOrderId, BeerOrderEvent.VALIDATE_ORDER);
        return stateMachine;
    }

    @Override
    public StateMachine<BeerOrderState, BeerOrderEvent> validateSuccessService(UUID beerOrderId, BeerOrderState state) {
        StateMachine<BeerOrderState, BeerOrderEvent> stateMachine = build(beerOrderId, state);
        sendEvent(stateMachine, beerOrderId, BeerOrderEvent.VALIDATION_SUCCESS);
        return null;
    }

    private StateMachine<BeerOrderState, BeerOrderEvent> build(UUID beerOrderId, BeerOrderState state) {
        StateMachine<BeerOrderState, BeerOrderEvent> stateMachine = factory.getStateMachine(beerOrderId);
        stateMachine.start();

        stateMachine.start();

        stateMachine.getStateMachineAccessor().doWithAllRegions(sma -> {
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
