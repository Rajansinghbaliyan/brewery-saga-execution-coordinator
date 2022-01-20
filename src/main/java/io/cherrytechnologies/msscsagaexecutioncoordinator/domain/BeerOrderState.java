package io.cherrytechnologies.msscsagaexecutioncoordinator.domain;

public enum BeerOrderState {
    NEW, VALIDATE, VALIDATION_EXCEPTION, ALLOCATED, ALLOCATION_EXCEPTION,
    PENDING_INVENTORY, PICKED_UP, DELIVERED, DELIVERY_EXCEPTION, CANCELLED
}
