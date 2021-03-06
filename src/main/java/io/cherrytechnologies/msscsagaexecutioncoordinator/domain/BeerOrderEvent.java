package io.cherrytechnologies.msscsagaexecutioncoordinator.domain;

public enum BeerOrderEvent {
    VALIDATE_ORDER, VALIDATION_SUCCESS, VALIDATION_FAILED, VALIDATED_BUT_NO_INVENTORY,
    PENDING_TO_VALIDATED , ALLOCATE_ORDER, ALLOCATION_SUCCESS, ALLOCATION_FAILED, ALLOCATION_NO_INVENTORY,
    BEER_ORDER_PICKED_UP, DELIVERY_SUCCESS, DELIVERY_FAILED, CANCEL_ORDER
}
