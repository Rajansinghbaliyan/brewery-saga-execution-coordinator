package io.cherrytechnologies.msscsagaexecutioncoordinator.config;

import io.cherrytechnologies.msscsagaexecutioncoordinator.actions.ValidButNoInventoryAction;
import io.cherrytechnologies.msscsagaexecutioncoordinator.actions.ValidateOrderAction;
import io.cherrytechnologies.msscsagaexecutioncoordinator.actions.ValidationSuccessfulAction;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderState;
import io.cherrytechnologies.msscsagaexecutioncoordinator.guards.BeerInventoryListGuard;
import io.cherrytechnologies.msscsagaexecutioncoordinator.guards.BeerOrderGuard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;

@Configuration
@EnableStateMachineFactory
@Slf4j
@RequiredArgsConstructor
public class StateMachineConfig extends StateMachineConfigurerAdapter<BeerOrderState, BeerOrderEvent> {

    private final ValidateOrderAction validateOrderAction;
    private final ValidationSuccessfulAction validationSuccessfulAction;
    private final ValidButNoInventoryAction validButNoInventoryAction;

    @Override
    public void configure(StateMachineTransitionConfigurer<BeerOrderState, BeerOrderEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(BeerOrderState.NEW)
                .target(BeerOrderState.NEW)
                .event(BeerOrderEvent.VALIDATE_ORDER)
                .action(validateOrderAction.action())
                .guard(BeerOrderGuard.guard())

                .and().withExternal()
                .source(BeerOrderState.NEW)
                .target(BeerOrderState.VALIDATE)
                .event(BeerOrderEvent.VALIDATION_SUCCESS)
                .action(validationSuccessfulAction.action())
                .guard(BeerOrderGuard.guard())

                .and().withExternal()
                .source(BeerOrderState.NEW)
                .target(BeerOrderState.VALIDATION_EXCEPTION)
                .event(BeerOrderEvent.VALIDATION_FAILED)

                .and().withExternal()
                .source(BeerOrderState.NEW)
                .target(BeerOrderState.PENDING_INVENTORY)
                .event(BeerOrderEvent.VALIDATED_BUT_NO_INVENTORY)
                .action(validButNoInventoryAction.action())
                .guard(BeerOrderGuard.guard())
                .guard(BeerInventoryListGuard.guard())


                .and().withExternal()
                .source(BeerOrderState.VALIDATE)
                .target(BeerOrderState.ALLOCATED)
                .event(BeerOrderEvent.ALLOCATION_SUCCESS)

                .and().withExternal()
                .source(BeerOrderState.PENDING_INVENTORY)
                .target(BeerOrderState.ALLOCATED)
                .event(BeerOrderEvent.ALLOCATION_NO_INVENTORY)

                .and().withExternal()
                .source(BeerOrderState.VALIDATE)
                .target(BeerOrderState.ALLOCATION_EXCEPTION)
                .event(BeerOrderEvent.ALLOCATION_FAILED)

                .and().withExternal()
                .source(BeerOrderState.ALLOCATED)
                .target(BeerOrderState.PICKED_UP)
                .event(BeerOrderEvent.BEER_ORDER_PICKED_UP)

                .and().withExternal()
                .source(BeerOrderState.PICKED_UP)
                .target(BeerOrderState.DELIVERED)
                .event(BeerOrderEvent.DELIVERY_SUCCESS)

                .and().withExternal()
                .source(BeerOrderState.PICKED_UP)
                .target(BeerOrderState.DELIVERY_EXCEPTION)
                .event(BeerOrderEvent.DELIVERY_FAILED);
    }

    @Override
    public void configure(StateMachineStateConfigurer<BeerOrderState, BeerOrderEvent> states) throws Exception {
        states.withStates()
                .initial(BeerOrderState.NEW)
                .states(EnumSet.allOf(BeerOrderState.class))
                .end(BeerOrderState.VALIDATION_EXCEPTION)
                .end(BeerOrderState.ALLOCATION_EXCEPTION)
                .end(BeerOrderState.DELIVERY_EXCEPTION)
                .end(BeerOrderState.DELIVERED)
                .end(BeerOrderState.CANCELLED);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<BeerOrderState, BeerOrderEvent> config) throws Exception {

        StateMachineListenerAdapter<BeerOrderState, BeerOrderEvent> adapter = new StateMachineListenerAdapter<>() {
            @Override
            public void stateMachineError(StateMachine<BeerOrderState, BeerOrderEvent> stateMachine, Exception exception) {
                log.error("Error in State Machine : " + exception.getMessage());
            }

            @Override
            public void stateChanged(State<BeerOrderState, BeerOrderEvent> from, State<BeerOrderState, BeerOrderEvent> to) {
                log.info(String.format(
                        "State changed from: %s to: %s",
                        Optional.ofNullable(from)
                                .map(State::getId)
                                .map(Objects::toString)
                                .orElse("Empty State"),

                        Optional.ofNullable(to)
                                .map(State::getId)
                                .map(Objects::toString)
                                .orElse("Empty State")
                ));
            }
        };

        config.withConfiguration().listener(adapter);
    }
}
