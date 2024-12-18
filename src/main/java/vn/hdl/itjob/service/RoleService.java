package vn.hdl.itjob.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hdl.itjob.domain.Permission;
import vn.hdl.itjob.domain.Role;
import vn.hdl.itjob.domain.response.ResultPaginationDTO;
import vn.hdl.itjob.repository.PermissionRepository;
import vn.hdl.itjob.repository.RoleRepository;
import vn.hdl.itjob.util.exception.InvalidException;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public Role handleCreateRole(Role reqRole) throws InvalidException {
        // ==> check if role name exist
        if (this.roleRepository.existsByName(reqRole.getName())) {
            throw new InvalidException("Role " + reqRole.getName() + " already existed");
        }

        // ==> get permission
        if (reqRole.getPermissions() != null) {
            List<Long> permissionIds = reqRole.getPermissions().stream()
                    .map((permission) -> permission.getId()).toList();
            List<Permission> permissions = this.permissionRepository.findAllById(permissionIds);
            reqRole.setPermissions(permissions);
        }

        return this.roleRepository.save(reqRole);
    }

    public Role handleUpdateRole(Role reqRole) throws InvalidException {
        // ==> check if role id not exist
        Role roleDb = this.roleRepository.findById(reqRole.getId())
                .orElseThrow(() -> new InvalidException("Role id = " + reqRole.getId() + " not exist"));

        // ==> get permission
        if (reqRole.getPermissions() != null) {
            List<Long> permissionIds = reqRole.getPermissions().stream()
                    .map((permission) -> permission.getId()).toList();
            List<Permission> permissions = this.permissionRepository.findAllById(permissionIds);
            roleDb.setPermissions(permissions);
        }

        roleDb.setName(reqRole.getName());
        roleDb.setActive(reqRole.isActive());
        roleDb.setDescription(reqRole.getDescription());

        return this.roleRepository.save(roleDb);
    }

    public ResultPaginationDTO handleGetAllRole(Specification<Role> spec, Pageable pageable) {
        Page<Role> rolePage = this.roleRepository.findAll(spec, pageable);

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(rolePage.getNumber() + 1);
        meta.setPageSize(rolePage.getSize());
        meta.setPages(rolePage.getTotalPages());
        meta.setTotal(rolePage.getTotalElements());
        ResultPaginationDTO dto = ResultPaginationDTO.builder()
                .meta(meta)
                .result(rolePage.getContent())
                .build();

        return dto;
    }

    public Role handleGetRole(Long id) throws InvalidException {
        return this.roleRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Role id = " + id + " not exist"));
    }

    public void handleDeleteRole(Long id) {
        this.roleRepository.deleteById(id);
    }
}
