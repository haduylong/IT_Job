package vn.hdl.itjob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hdl.itjob.domain.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

}
