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
import vn.hdl.itjob.domain.Permission;
import vn.hdl.itjob.domain.response.ApiResponse;
import vn.hdl.itjob.domain.response.ResultPaginationDTO;
import vn.hdl.itjob.service.PermissionService;
import vn.hdl.itjob.util.exception.InvalidException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping("/permissions")
    public ResponseEntity<ApiResponse<Permission>> createPermission(@Valid @RequestBody Permission reqPermission)
            throws InvalidException {
        Permission permission = this.permissionService.handleCreatePermission(reqPermission);
        ApiResponse<Permission> res = ApiResponse.<Permission>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Create permission successful")
                .data(permission)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/permissions")
    public ResponseEntity<ApiResponse<Permission>> updatePermission(@Valid @RequestBody Permission reqPermission)
            throws InvalidException {
        Permission permission = this.permissionService.handleUpdatePermission(reqPermission);
        ApiResponse<Permission> res = ApiResponse.<Permission>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Update permission successful")
                .data(permission)
                .build();
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/permissions")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllPermission(
            @Filter Specification<Permission> spec,
            Pageable pageable) {
        ResultPaginationDTO dto = this.permissionService.handleGetAllPermission(spec, pageable);
        ApiResponse<ResultPaginationDTO> res = ApiResponse.<ResultPaginationDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all permission successful")
                .data(dto)
                .build();
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable("id") Long id)
            throws InvalidException {
        this.permissionService.handleDeletePermission(id);
        ApiResponse<Void> res = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Delete permission successful")
                .build();
        return ResponseEntity.ok().body(res);
    }
}
