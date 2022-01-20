package io.cherrytechnologies.msscsagaexecutioncoordinator.listener;

import io.cherrytechnologies.msscsagaexecutioncoordinator.config.JmsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BeerOrderPickupListener {
    @JmsListener(destination = JmsConfig.BEER_PICKUP_QUEUE)
    public void listener(){

    }
}
