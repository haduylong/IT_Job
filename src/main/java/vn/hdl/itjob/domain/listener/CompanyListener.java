package vn.hdl.itjob.domain.listener;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import vn.hdl.itjob.domain.Company;
import vn.hdl.itjob.service.CompanyRedisService;

@Log4j2
@RequiredArgsConstructor
public class CompanyListener {
    private final CompanyRedisService companyRedisService;

    @PostPersist
    public void handleCacheAfterCreate(Company company) {
        log.info("post persist");
        companyRedisService.clear("companies:*"); // delete keys start with companies
    }

    @PostUpdate
    public void handleCacheAfterUpdate(Company company) {
        log.info("post update");
        companyRedisService.clear("companies:*"); // delete keys start with companies
    }
}
