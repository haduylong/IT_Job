package vn.hdl.itjob.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.hdl.itjob.domain.Subscriber;
import vn.hdl.itjob.domain.response.ApiResponse;
import vn.hdl.itjob.service.SubscriberService;
import vn.hdl.itjob.util.SecurityUtil;
import vn.hdl.itjob.util.exception.InvalidException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SubscriberController {
    private final SubscriberService subscriberService;

    @PostMapping("/subscribers")
    public ResponseEntity<ApiResponse<Subscriber>> createSubscriber(@Valid @RequestBody Subscriber reqSubscriber)
            throws InvalidException {
        Subscriber subscriber = this.subscriberService.handleCreateSubscriber(reqSubscriber);
        ApiResponse<Subscriber> res = ApiResponse.<Subscriber>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Create subscriber successful")
                .data(subscriber)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/subscribers")
    public ResponseEntity<ApiResponse<Subscriber>> updateSubscriber(@RequestBody Subscriber reqSubscriber)
            throws InvalidException {
        Subscriber subscriber = this.subscriberService.handleUpdateSubscriber(reqSubscriber);
        ApiResponse<Subscriber> res = ApiResponse.<Subscriber>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Update subscriber successful")
                .data(subscriber)
                .build();
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/subscribers/skills")
    public ResponseEntity<ApiResponse<Subscriber>> getSubscriberSkills() throws InvalidException {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new InvalidException("User unauthenticated"));

        Subscriber subscriber = this.subscriberService.handleGetSubscriber(email);
        ApiResponse<Subscriber> res = ApiResponse.<Subscriber>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get subscriber's skills successful")
                .data(subscriber)
                .build();
        return ResponseEntity.ok().body(res);
    }
}
