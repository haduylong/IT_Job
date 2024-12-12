package vn.hdl.itjob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hdl.itjob.domain.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

}
