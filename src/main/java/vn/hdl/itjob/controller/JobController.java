package vn.hdl.itjob.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hdl.itjob.domain.Job;
import vn.hdl.itjob.domain.response.ApiResponse;
import vn.hdl.itjob.domain.response.ResultPaginationDTO;
import vn.hdl.itjob.domain.response.job.RespCreateJobDTO;
import vn.hdl.itjob.domain.response.job.RespUpdateJobDTO;
import vn.hdl.itjob.service.JobService;
import vn.hdl.itjob.util.exception.InvalidException;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    public ResponseEntity<ApiResponse<RespCreateJobDTO>> createJob(@Valid @RequestBody Job reqJob) {
        RespCreateJobDTO dto = this.jobService.handleCreateJob(reqJob);
        ApiResponse<RespCreateJobDTO> res = ApiResponse.<RespCreateJobDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Create job successful")
                .data(dto)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/jobs")
    public ResponseEntity<ApiResponse<RespUpdateJobDTO>> updateJob(@Valid @RequestBody Job reqJob)
            throws InvalidException {
        RespUpdateJobDTO dto = this.jobService.handleUpdateJob(reqJob);
        ApiResponse<RespUpdateJobDTO> res = ApiResponse.<RespUpdateJobDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Update job successful")
                .data(dto)
                .build();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllJob(
            @Filter Specification<Job> spec,
            Pageable pageable) {
        ResultPaginationDTO dto = this.jobService.handleGetAllJob(spec, pageable);
        ApiResponse<ResultPaginationDTO> res = ApiResponse.<ResultPaginationDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all job successful")
                .data(dto)
                .build();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<Job>> getJob(@PathVariable("id") Long id) throws InvalidException {
        Job job = this.jobService.handleGetJob(id);
        ApiResponse<Job> res = ApiResponse.<Job>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get job successful")
                .data(job)
                .build();
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable("id") Long id) {
        this.jobService.handleDeleteJob(id);
        ApiResponse<Void> res = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Delete job successful")
                .build();
        return ResponseEntity.ok(res);
    }
}
