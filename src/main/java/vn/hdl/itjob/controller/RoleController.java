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
import lombok.RequiredArgsConstructor;
import vn.hdl.itjob.domain.Role;
import vn.hdl.itjob.domain.response.ApiResponse;
import vn.hdl.itjob.domain.response.ResultPaginationDTO;
import vn.hdl.itjob.service.RoleService;
import vn.hdl.itjob.util.exception.InvalidException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("roles")
    public ResponseEntity<ApiResponse<Role>> createRole(@Valid @RequestBody Role reqRole)
            throws InvalidException {
        Role role = this.roleService.handleCreateRole(reqRole);
        ApiResponse<Role> res = ApiResponse.<Role>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Create role successful")
                .data(role)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/roles")
    public ResponseEntity<ApiResponse<Role>> updateRole(@Valid @RequestBody Role reqRole)
            throws InvalidException {
        Role role = this.roleService.handleUpdateRole(reqRole);
        ApiResponse<Role> res = ApiResponse.<Role>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Update role successful")
                .data(role)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllRole(
            @Filter Specification<Role> spec,
            Pageable pageable) {
        ResultPaginationDTO dto = this.roleService.handleGetAllRole(spec, pageable);
        ApiResponse<ResultPaginationDTO> res = ApiResponse.<ResultPaginationDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all role successful")
                .data(dto)
                .build();
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<ApiResponse<Role>> getRole(@PathVariable("id") Long id) throws InvalidException {
        Role role = this.roleService.handleGetRole(id);
        ApiResponse<Role> res = ApiResponse.<Role>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get role successful")
                .data(role)
                .build();
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable("id") Long id) {
        this.roleService.handleDeleteRole(id);
        ApiResponse<Void> res = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Delete role successful")
                .build();
        return ResponseEntity.ok(res);
    }
}
