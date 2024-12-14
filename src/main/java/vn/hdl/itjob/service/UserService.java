package vn.hdl.itjob.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hdl.itjob.domain.Company;
import vn.hdl.itjob.domain.Role;
import vn.hdl.itjob.domain.User;
import vn.hdl.itjob.domain.response.ResultPaginationDTO;
import vn.hdl.itjob.domain.response.user.RespCreateUserDTO;
import vn.hdl.itjob.domain.response.user.RespUpdateUserDTO;
import vn.hdl.itjob.domain.response.user.RespUserDTO;
import vn.hdl.itjob.repository.CompanyRepository;
import vn.hdl.itjob.repository.RoleRepository;
import vn.hdl.itjob.repository.UserRepository;
import vn.hdl.itjob.util.exception.InvalidException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
            CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
    }

    public RespCreateUserDTO handleCreateUser(User reqUser) throws InvalidException {
        // ==> check if email exist
        if (this.userRepository.existsByEmail(reqUser.getEmail())) {
            throw new InvalidException("Email " + reqUser.getEmail() + " is exist");
        }

        // ==> get role
        if (reqUser.getRole() != null) {
            Role role = this.roleRepository.findById(reqUser.getRole().getId()).orElse(null);
            reqUser.setRole(role);
        }

        // ==> get company
        if (reqUser.getCompany() != null) {
            Company company = this.companyRepository.findById(reqUser.getCompany().getId()).orElse(null);
            reqUser.setCompany(company);
        }

        // save
        User userDb = this.userRepository.save(reqUser);

        // create response
        RespCreateUserDTO dto = new RespCreateUserDTO();
        dto.setId(userDb.getId());
        dto.setName(userDb.getName());
        dto.setEmail(userDb.getEmail());
        dto.setAddress(userDb.getAddress());
        dto.setGender(userDb.getGender());
        dto.setAge(userDb.getAge());
        dto.setCreatedAt(userDb.getCreatedAt());
        dto.setUpdatedAt(userDb.getUpdatedAt());
        RespCreateUserDTO.RoleOfUser roleOfUser = new RespCreateUserDTO.RoleOfUser();
        if (userDb.getRole() != null) {
            roleOfUser.setId(userDb.getRole().getId());
            roleOfUser.setName(userDb.getRole().getName());
            dto.setRole(roleOfUser);
        }
        RespCreateUserDTO.CompanyOfUser companyOfUser = new RespCreateUserDTO.CompanyOfUser();
        if (userDb.getCompany() != null) {
            companyOfUser.setId(userDb.getCompany().getId());
            companyOfUser.setName(userDb.getCompany().getName());
            dto.setCompany(companyOfUser);
        }
        return dto;
    }

    public RespUpdateUserDTO handleUpdateUser(User reqUser) throws InvalidException {
        // ==> check if id not exist
        User userDb = this.userRepository.findById(reqUser.getId())
                .orElseThrow(() -> new InvalidException("User with id = " + reqUser.getId() + " not found"));

        // ==> get company
        if (reqUser.getCompany() != null) {
            Company company = this.companyRepository.findById(reqUser.getCompany().getId()).orElse(null);
            userDb.setCompany(company);
        }

        // ==> get role
        if (reqUser.getRole() != null) {
            Role role = this.roleRepository.findById(reqUser.getRole().getId()).orElse(null);
            reqUser.setRole(role);
        }

        userDb.setName(reqUser.getName());
        userDb.setAddress(reqUser.getAddress());
        userDb.setAge(reqUser.getAge());
        userDb.setGender(reqUser.getGender());
        // save update
        userDb = this.userRepository.save(userDb);

        // create dto
        RespUpdateUserDTO dto = new RespUpdateUserDTO();
        dto.setId(userDb.getId());
        dto.setName(userDb.getName());
        dto.setAge(userDb.getAge());
        dto.setGender(userDb.getGender());
        dto.setAddress(userDb.getAddress());
        dto.setUpdatedAt(userDb.getUpdatedAt());
        RespUpdateUserDTO.CompanyOfUser companyOfUser = new RespUpdateUserDTO.CompanyOfUser();
        if (userDb.getCompany() != null) {
            companyOfUser.setId(userDb.getCompany().getId());
            companyOfUser.setName(userDb.getCompany().getName());
            dto.setCompany(companyOfUser);
        }

        return dto;
    }

    public RespUserDTO handleGetUser(Long id) throws InvalidException {
        User userDb = this.userRepository.findById(id)
                .orElseThrow(() -> new InvalidException("User not found"));

        RespUserDTO dto = toRespUserDTO(userDb);
        return dto;
    }

    RespUserDTO toRespUserDTO(User user) {
        RespUserDTO dto = new RespUserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        RespUserDTO.CompanyOfUser companyOfUser = new RespUserDTO.CompanyOfUser();
        if (user.getCompany() != null) {
            companyOfUser.setId(user.getCompany().getId());
            companyOfUser.setName(user.getCompany().getName());
            dto.setCompany(companyOfUser);
        }
        RespUserDTO.RoleOfUser roleOfUser = new RespUserDTO.RoleOfUser();
        if (user.getRole() != null) {
            roleOfUser.setId(user.getRole().getId());
            roleOfUser.setName(user.getRole().getName());
            dto.setRole(roleOfUser);
        }

        return dto;
    }

    public ResultPaginationDTO handleGetAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);

        ResultPaginationDTO dto = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageUser.getNumber() + 1);
        meta.setPageSize(pageUser.getSize());
        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());
        dto.setMeta(meta);

        List<RespUserDTO> userDTOs = pageUser.toList().stream()
                .map(user -> toRespUserDTO(user)).toList();
        dto.setResult(userDTOs);
        return dto;
    }

    public void handleDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }
}
