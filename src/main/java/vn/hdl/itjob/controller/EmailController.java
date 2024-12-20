package vn.hdl.itjob.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.hdl.itjob.service.SubscriberService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EmailController {
    private final SubscriberService subscriberService;

    @GetMapping("/emails")
    public String sendMail() {
        // this.subscriberService.sendMailSubscriber();
        this.subscriberService.sendMailSubscriberPaged();
        return "ok";
    }
}
