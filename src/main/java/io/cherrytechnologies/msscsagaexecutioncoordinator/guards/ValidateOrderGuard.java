package io.cherrytechnologies.msscsagaexecutioncoordinator.guards;

import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderState;
import io.cherrytechnologies.msscsagaexecutioncoordinator.services.StateMachineServiceImpl;
import org.springframework.statemachine.guard.Guard;

import java.util.Optional;

public class ValidateOrderGuard {
    public static Guard<BeerOrderState, BeerOrderEvent> beerOrderDtoGuard() {
        return context -> {
            return Optional.ofNullable(context.getMessageHeaders())
                    .map(messages -> messages.get(StateMachineServiceImpl.BEER_ORDER))
                    .isPresent();
        };
    }
}
