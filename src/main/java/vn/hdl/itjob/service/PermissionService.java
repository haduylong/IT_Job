package vn.hdl.itjob.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hdl.itjob.domain.Permission;
import vn.hdl.itjob.domain.response.ResultPaginationDTO;
import vn.hdl.itjob.repository.PermissionRepository;
import vn.hdl.itjob.util.exception.InvalidException;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public Permission handleCreatePermission(Permission reqPermission) throws InvalidException {
        // ==> check if permission exist
        if (this.permissionRepository.existsByModuleAndMethodAndApiPath(
                reqPermission.getModule(), reqPermission.getMethod(), reqPermission.getApiPath())) {
            throw new InvalidException("Permission already existed");
        }

        Permission permissionDb = this.permissionRepository.save(reqPermission);
        return permissionDb;
    }

    public Permission handleUpdatePermission(Permission reqPermission) throws InvalidException {
        // ==> check if permission id not exist
        Permission permissionDb = this.permissionRepository.findById(reqPermission.getId())
                .orElseThrow(() -> new InvalidException("Permission not found"));

        permissionDb.setName(reqPermission.getName());
        permissionDb.setModule(reqPermission.getModule());
        permissionDb.setApiPath(reqPermission.getApiPath());
        permissionDb.setMethod(reqPermission.getMethod());

        return this.permissionRepository.save(permissionDb);
    }

    public ResultPaginationDTO handleGetAllPermission(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> permissionPage = this.permissionRepository.findAll(spec, pageable);

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(permissionPage.getNumber() + 1);
        meta.setPageSize(permissionPage.getSize());
        meta.setPages(permissionPage.getTotalPages());
        meta.setTotal(permissionPage.getTotalElements());
        ResultPaginationDTO dto = ResultPaginationDTO.builder()
                .meta(meta)
                .result(permissionPage.getContent())
                .build();

        return dto;
    }

    public void handleDeletePermission(Long id) throws InvalidException {
        // ==> check if permission not exist
        Permission permissionDb = this.permissionRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Permission id = " + id + " not found"));

        // delete permission in each role
        permissionDb.getRoles().forEach(role -> role.getPermissions().remove(permissionDb));

        // remove permission
        this.permissionRepository.delete(permissionDb);
    }

}
