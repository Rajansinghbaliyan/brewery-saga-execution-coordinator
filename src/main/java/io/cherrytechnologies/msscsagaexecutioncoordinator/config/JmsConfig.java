package io.cherrytechnologies.msscsagaexecutioncoordinator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.net.PortUnreachableException;

@Configuration
public class JmsConfig {

    public static final String NEW_ORDER_QUEUE = "new_order_queue";
    public static final String VALIDATE_QUEUE = "validate_queue";
    public static final String VALIDATION_SUCCESS_QUEUE = "validation_success_queue";
    public static final String VALIDATION_FAILED_QUEUE = "validation_failed_queue";
    public static final String VALIDATION_BUT_NO_INVENTORY_QUEUE     = "validation_but_no_inventory_queue";
    public static final String ALLOCATION_SUCCESS_QUEUE = "allocation_success_queue";
    public static final String ALLOCATION_NO_INVENTORY_QUEUE = "allocation_no_inventory_queue";
    public static final String ALLOCATION_FAILED_QUEUE = "allocation_failed_queue";
    public static final String BEER_PICKUP_QUEUE = "beer_pickup_queue";
    public static final String DELIVERY_SUCCESS_QUEUE = "delivery_success_queue";
    public static final String DELIVERY_FAILED_QUEUE = "delivery_failed_queue";
    public static final String CHANGE_STATE_QUEUE = "change_state_queue";
    public static final String BREW_MORE_BEER_QUEUE = "brew_more_beer_queue";
    public static final String PENDING_TO_VALIDATE_QUEUE = "pending_to_validate_queue";
    public static final String ALLOCATE_ORDER_QUEUE = "allocate_order_queue";

    @Bean
    public MessageConverter converter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(objectMapper);

        return converter;
    }
}
