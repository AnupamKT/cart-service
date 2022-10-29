package com.example.cartservice.scheduler;

import com.example.cartservice.service.CartSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobScheduler {

    @Autowired
    private CartSchedulerService service;


    @Scheduled(cron = "0 0/2 * * * ?")
    public void scheduleCartService(){
        log.info("start of scheduleCartService");
        service.checkCartEntriesWithInventory();
        log.info("End of scheduleCartService");
    }

}
