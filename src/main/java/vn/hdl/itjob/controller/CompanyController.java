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
import lombok.extern.slf4j.Slf4j;
import vn.hdl.itjob.domain.Company;
import vn.hdl.itjob.domain.response.ApiResponse;
import vn.hdl.itjob.domain.response.ResultPaginationDTO;
import vn.hdl.itjob.service.CompanyService;
import vn.hdl.itjob.util.exception.InvalidException;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class CompanyController {
    // private final Logger log = LoggerFactory.getLogger(CompanyController.class);

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<ApiResponse<Company>> createCompany(@Valid @RequestBody Company reqCompany) {
        log.info("Create company: {}", reqCompany);
        Company company = this.companyService.handleCreateCompany(reqCompany);
        ApiResponse<Company> res = ApiResponse.<Company>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Create company successful")
                .data(company)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/companies")
    public ResponseEntity<ApiResponse<Company>> updateCompany(@Valid @RequestBody Company reqCompany)
            throws InvalidException {
        log.info("Create company: {}", reqCompany);
        Company company = this.companyService.handleUpdateCompany(reqCompany);
        ApiResponse<Company> res = ApiResponse.<Company>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Update company successful")
                .data(company)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<ApiResponse<Company>> getCompany(@PathVariable("id") Long id) throws InvalidException {
        Company company = this.companyService.handleGetCompany(id);
        ApiResponse<Company> res = ApiResponse.<Company>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get company successful")
                .data(company)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllCompany(
            @Filter Specification<Company> spec,
            Pageable pageable) {
        ResultPaginationDTO dto = this.companyService.handleGetAllCompany(spec, pageable);
        ApiResponse<ResultPaginationDTO> res = ApiResponse.<ResultPaginationDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all company successful")
                .data(dto)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable("id") Long id) {
        this.companyService.handleDeleteCompany(id);
        ApiResponse<Void> res = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Delete company successful")
                .build();
        return ResponseEntity.ok().body(res);
    }
}
