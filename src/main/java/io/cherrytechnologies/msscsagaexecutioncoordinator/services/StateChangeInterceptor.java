package io.cherrytechnologies.msscsagaexecutioncoordinator.services;

import guru.sfg.common.models.BaseItem;
import guru.sfg.common.models.BeerOrderDto;
import io.cherrytechnologies.msscsagaexecutioncoordinator.actions.ChangeStateAction;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderState;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StateChangeInterceptor extends StateMachineInterceptorAdapter<BeerOrderState, BeerOrderEvent> {
    private final JmsTemplate jmsTemplate;

    @Override
    public void preStateChange(State<BeerOrderState, BeerOrderEvent> state, Message<BeerOrderEvent> message, Transition<BeerOrderState, BeerOrderEvent> transition, StateMachine<BeerOrderState, BeerOrderEvent> stateMachine) {
        Optional.ofNullable(message)
                .map(Message::getHeaders)
                .map(headers -> (BeerOrderDto) headers.get(StateMachineServiceImpl.BEER_ORDER))
                .ifPresent(beerOrderDto -> {
                    UUID beerOrderId = beerOrderDto.getId();
                    beerOrderDto.setStatus(state.getId());
                    ChangeStateAction.changeState(
                            state.getId(),
                            beerOrderId,
                            jmsTemplate
                    );
                });
    }
}
