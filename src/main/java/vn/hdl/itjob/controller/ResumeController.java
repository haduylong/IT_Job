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
import vn.hdl.itjob.domain.Resume;
import vn.hdl.itjob.domain.response.ApiResponse;
import vn.hdl.itjob.domain.response.ResultPaginationDTO;
import vn.hdl.itjob.domain.response.resume.RespCreateResumeDTO;
import vn.hdl.itjob.domain.response.resume.RespFetchResumeDTO;
import vn.hdl.itjob.domain.response.resume.RespUpdateResumeDTO;
import vn.hdl.itjob.service.ResumeService;
import vn.hdl.itjob.service.UserService;
import vn.hdl.itjob.util.exception.InvalidException;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService, UserService userService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    public ResponseEntity<ApiResponse<RespCreateResumeDTO>> createResume(@Valid @RequestBody Resume reqResume)
            throws InvalidException {
        RespCreateResumeDTO dto = this.resumeService.handleCreateResume(reqResume);
        ApiResponse<RespCreateResumeDTO> res = ApiResponse.<RespCreateResumeDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Create resume successful")
                .data(dto)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/resumes")
    public ResponseEntity<ApiResponse<RespUpdateResumeDTO>> updateResume(@RequestBody Resume reqResume)
            throws InvalidException {
        RespUpdateResumeDTO dto = this.resumeService.handleUpdateResume(reqResume);
        ApiResponse<RespUpdateResumeDTO> res = ApiResponse.<RespUpdateResumeDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Update resume successful")
                .data(dto)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    /*
     * only company employees can view resumes for company jobs
     */
    @GetMapping("/resumes")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllResume(
            @Filter Specification<Resume> spec,
            Pageable pageable) throws InvalidException {
        ResultPaginationDTO dto = this.resumeService.handleGetAllResume(spec, pageable);

        ApiResponse<ResultPaginationDTO> res = ApiResponse.<ResultPaginationDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all resume successful")
                .data(dto)
                .build();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/resumes/{id}")
    public ResponseEntity<ApiResponse<RespFetchResumeDTO>> getResume(@PathVariable("id") Long id)
            throws InvalidException {
        RespFetchResumeDTO dto = this.resumeService.handleGetResume(id);
        ApiResponse<RespFetchResumeDTO> res = ApiResponse.<RespFetchResumeDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get resume successful")
                .data(dto)
                .build();
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(@PathVariable("id") Long id)
            throws InvalidException {
        this.resumeService.handleDeleteResume(id);
        ApiResponse<Void> res = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Delete resume successful")
                .build();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/resumes/by-user")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getResumeByUser(Pageable pageable)
            throws InvalidException {
        ResultPaginationDTO dto = this.resumeService.handleGetResumeByUser(pageable);
        ApiResponse<ResultPaginationDTO> res = ApiResponse.<ResultPaginationDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get resume by user successful")
                .data(dto)
                .build();
        return ResponseEntity.ok(res);
    }
}
