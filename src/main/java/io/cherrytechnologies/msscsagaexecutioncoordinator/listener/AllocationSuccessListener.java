package io.cherrytechnologies.msscsagaexecutioncoordinator.listener;

import io.cherrytechnologies.msscsagaexecutioncoordinator.config.JmsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AllocationSuccessListener {
    @JmsListener(destination = JmsConfig.ALLOCATION_SUCCESS_QUEUE)
    public void listener(){

    }
}
