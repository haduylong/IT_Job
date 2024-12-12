package vn.hdl.itjob.service;

import org.springframework.stereotype.Service;

import vn.hdl.itjob.domain.Company;
import vn.hdl.itjob.domain.Role;
import vn.hdl.itjob.domain.User;
import vn.hdl.itjob.domain.response.user.RespCreateUserDTO;
import vn.hdl.itjob.repository.CompanyRepository;
import vn.hdl.itjob.repository.RoleRepository;
import vn.hdl.itjob.repository.UserRepository;
import vn.hdl.itjob.util.exception.AppException;

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

    public RespCreateUserDTO handleCreateUser(User reqUser) throws AppException {
        // ==> check if email exist
        if (this.userRepository.existsByEmail(reqUser.getEmail())) {
            throw new AppException("Email " + reqUser.getEmail() + " is exist");
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

}
