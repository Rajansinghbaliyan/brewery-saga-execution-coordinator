package io.cherrytechnologies.msscsagaexecutioncoordinator.guards;

import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderState;
import io.cherrytechnologies.msscsagaexecutioncoordinator.services.StateMachineServiceImpl;
import org.springframework.statemachine.guard.Guard;

import java.util.Optional;

public class BeerInventoryListGuard {
    public static Guard<BeerOrderState, BeerOrderEvent> guard() {
        return context -> Optional.ofNullable(context.getMessageHeaders())
                .map(messages -> messages.get(StateMachineServiceImpl.NO_INVENTORY_BEER_LIST))
                .isPresent();
    }
}
