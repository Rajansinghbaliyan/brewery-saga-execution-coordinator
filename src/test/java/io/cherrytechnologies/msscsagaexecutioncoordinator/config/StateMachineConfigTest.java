package io.cherrytechnologies.msscsagaexecutioncoordinator.config;

import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderEvent;
import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StateMachineConfigTest {

    @Autowired
    private StateMachineFactory<BeerOrderState, BeerOrderEvent> factory;

    private StateMachine<BeerOrderState, BeerOrderEvent> stateMachine;

    @BeforeEach
    void setUp() {
        stateMachine = factory.getStateMachine(UUID.randomUUID());
        stateMachine.start();
    }

    @AfterEach
    void tearDown() {
        stateMachine.stop();
    }

    private StateMachine<BeerOrderState, BeerOrderEvent> build(StateMachine<BeerOrderState, BeerOrderEvent> sm,
                                                               BeerOrderState state) {
        sm.stop();

        sm.getStateMachineAccessor().doWithAllRegions(sma -> {
            sma.resetStateMachine(new DefaultStateMachineContext<BeerOrderState, BeerOrderEvent>(
                    state,
                    null,
                    null,
                    null
            ));
        });

        sm.start();

        return sm;
    }

    @Test()
    @DisplayName("Test Validate Order Event")
    void testEventValidateOrder() {

        stateMachine.sendEvent(BeerOrderEvent.VALIDATE_ORDER);

        //Then BeerOrderState Should be NEW
        assertEquals(BeerOrderState.NEW, stateMachine.getState().getId());
    }

    @Test()
    @DisplayName("Test Validation Success Event")
    void testValidationPassed() {
        stateMachine.sendEvent(BeerOrderEvent.VALIDATION_SUCCESS);

        assertEquals(BeerOrderState.VALIDATE, stateMachine.getState().getId());
    }

    @Test()
    @DisplayName("Test Validation Failed Event")
    void testValidationFailed() {
        stateMachine.sendEvent(BeerOrderEvent.VALIDATION_FAILED);

        assertEquals(BeerOrderState.VALIDATION_EXCEPTION, stateMachine.getState().getId());
    }

    @Test()
    @DisplayName("Test Allocation No Inventory Event")
    void testAllocationNoInventoryEvent() {
        //given
        build(stateMachine, BeerOrderState.VALIDATE)
                .sendEvent(BeerOrderEvent.ALLOCATION_NO_INVENTORY);

        assertEquals(BeerOrderState.PENDING_INVENTORY, stateMachine.getState().getId());
    }

    @Test()
    @DisplayName("Test Allocation Success form validate")
    void testAllocationSuccessEventFromValidate() {
        // given
        build(stateMachine, BeerOrderState.VALIDATE)
                .sendEvent(BeerOrderEvent.ALLOCATION_SUCCESS);

        assertEquals(BeerOrderState.ALLOCATED, stateMachine.getState().getId());
    }

    @Test
    @DisplayName("Test Allocation Success from pending")
    void testAllocationSuccessEventFromPending() {
        // given
        build(stateMachine, BeerOrderState.PENDING_INVENTORY)
                .sendEvent(BeerOrderEvent.ALLOCATION_SUCCESS);

        assertEquals(BeerOrderState.ALLOCATED, stateMachine.getState().getId());
    }

    @Test
    @DisplayName("Test Allocation Error Event")
    void testAllocationErrorEvent() {
        //given
        build(stateMachine, BeerOrderState.VALIDATE)
                .sendEvent(BeerOrderEvent.ALLOCATION_FAILED);


        assertEquals(BeerOrderState.ALLOCATION_EXCEPTION, stateMachine.getState().getId());
    }

    @Test
    @DisplayName("Test Beer Pickup Event")
    void testBeerOderPickedUpEvent() {
        //given
        build(stateMachine, BeerOrderState.ALLOCATED)
                .sendEvent(BeerOrderEvent.BEER_ORDER_PICKED_UP);

        assertEquals(BeerOrderState.PICKED_UP, stateMachine.getState().getId());
    }

    @Test
    @DisplayName("Test Delivery Success Event")
    void testDeliverySuccessEvent() {
        //given
        build(stateMachine, BeerOrderState.PICKED_UP)
                .sendEvent(BeerOrderEvent.DELIVERY_SUCCESS);

        assertEquals(BeerOrderState.DELIVERED, stateMachine.getState().getId());
    }

    @Test
    @DisplayName("Test Delivery Failed Event")
    void testDeliveryFailedEvent() {
        //given
        build(stateMachine, BeerOrderState.PICKED_UP)
                .sendEvent(BeerOrderEvent.DELIVERY_FAILED);

        assertEquals(BeerOrderState.DELIVERY_EXCEPTION, stateMachine.getState().getId());
    }
}