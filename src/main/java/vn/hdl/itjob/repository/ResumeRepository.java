package vn.hdl.itjob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hdl.itjob.domain.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

}
