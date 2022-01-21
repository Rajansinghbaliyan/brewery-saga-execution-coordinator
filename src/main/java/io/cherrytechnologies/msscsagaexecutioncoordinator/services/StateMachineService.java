package io.cherrytechnologies.msscsagaexecutioncoordinator.services;

import guru.sfg.common.models.BeerOrderDto;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderState;
import org.springframework.statemachine.StateMachine;

import java.util.UUID;

public interface StateMachineService {

    StateMachine<BeerOrderState, BeerOrderEvent> newOrderService(BeerOrderDto beerOrderDto);
    StateMachine<BeerOrderState, BeerOrderEvent> validateOrderService(UUID beerOrderId, BeerOrderState state);
    StateMachine<BeerOrderState, BeerOrderEvent> validateSuccessService(UUID beerOrderId, BeerOrderState state);
}
