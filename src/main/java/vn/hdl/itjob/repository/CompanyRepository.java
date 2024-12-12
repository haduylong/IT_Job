package vn.hdl.itjob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hdl.itjob.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}
