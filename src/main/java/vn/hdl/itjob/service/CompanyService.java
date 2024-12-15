package vn.hdl.itjob.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hdl.itjob.domain.Company;
import vn.hdl.itjob.domain.response.ResultPaginationDTO;
import vn.hdl.itjob.repository.CompanyRepository;
import vn.hdl.itjob.repository.UserRepository;
import vn.hdl.itjob.util.exception.InvalidException;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;
    private UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company handleCreateCompany(Company reqCompany) {
        return this.companyRepository.save(reqCompany);
    }

    public Company handleUpdateCompany(Company reqCompany) throws InvalidException {
        // ==> check if id not exist
        Company companyDb = this.companyRepository.findById(reqCompany.getId())
                .orElseThrow(() -> new InvalidException("Company with id =" + reqCompany.getId() + " not found"));

        companyDb.setName(reqCompany.getName());
        companyDb.setAddress(reqCompany.getAddress());
        companyDb.setLogo(reqCompany.getLogo());
        companyDb.setAddress(reqCompany.getAddress());

        return this.companyRepository.save(companyDb);
    }

    public Company handleGetCompany(Long id) throws InvalidException {
        return this.companyRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Company not found"));
    }

    public ResultPaginationDTO handleGetAllCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> companyPage = this.companyRepository.findAll(spec, pageable);

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(companyPage.getNumber() + 1);
        meta.setPageSize(companyPage.getSize());
        meta.setPages(companyPage.getTotalPages());
        meta.setTotal(companyPage.getTotalElements());
        ResultPaginationDTO dto = ResultPaginationDTO.builder()
                .meta(meta)
                .result(companyPage.getContent())
                .build();
        return dto;
    }

    public void handleDeleteCompany(Long id) throws InvalidException {
        Company company = this.companyRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Company not found"));
        this.userRepository.deleteAll(company.getUsers());
        this.companyRepository.deleteById(id);
    }
}
