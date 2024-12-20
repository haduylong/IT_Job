package vn.hdl.itjob.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.hdl.itjob.service.SubscriberService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class EmailController {
    private final SubscriberService subscriberService;

    @GetMapping("/emails")
    @Scheduled(cron = "0 0 0 * * *") // <=> @Scheduled(cron = "@daily")
    @Transactional
    public String sendMail() {
        log.info(">>> SEND MAIL SCHEDULED");
        // this.subscriberService.sendMailSubscriber();
        this.subscriberService.sendMailSubscriberPaged();
        return "ok";
    }
}
