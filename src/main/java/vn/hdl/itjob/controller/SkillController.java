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
import vn.hdl.itjob.domain.Skill;
import vn.hdl.itjob.domain.response.ApiResponse;
import vn.hdl.itjob.domain.response.ResultPaginationDTO;
import vn.hdl.itjob.service.SkillService;
import vn.hdl.itjob.util.exception.InvalidException;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    public ResponseEntity<ApiResponse<Skill>> createSkill(@Valid @RequestBody Skill reqSkill) throws InvalidException {
        Skill skill = this.skillService.handleCreateSkill(reqSkill);
        ApiResponse<Skill> res = ApiResponse.<Skill>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Create skill successful")
                .data(skill)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/skills")
    public ResponseEntity<ApiResponse<Skill>> updateSkill(@Valid @RequestBody Skill reqSkill) throws InvalidException {
        Skill skill = this.skillService.handleUpdateSkill(reqSkill);
        ApiResponse<Skill> res = ApiResponse.<Skill>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Update skill successful")
                .data(skill)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllSkill(
            @Filter Specification<Skill> spec,
            Pageable pageable) {
        ResultPaginationDTO dto = this.skillService.handleGetAllSkill(spec, pageable);
        ApiResponse<ResultPaginationDTO> res = ApiResponse.<ResultPaginationDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all skill successful")
                .data(dto)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/skills/{id}")
    public ResponseEntity<ApiResponse<Skill>> getSkill(@PathVariable("id") Long id) throws InvalidException {
        Skill skill = this.skillService.handleGetSkill(id);
        ApiResponse<Skill> res = ApiResponse.<Skill>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get skill successful")
                .data(skill)
                .build();
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSkill(@PathVariable("id") Long id) throws InvalidException {
        this.skillService.handleDeleteSkill(id);
        ApiResponse<Void> res = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Delete skill successful")
                .build();
        return ResponseEntity.ok().body(res);
    }
}
