package vn.hdl.itjob.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hdl.itjob.domain.Skill;
import vn.hdl.itjob.domain.response.ResultPaginationDTO;
import vn.hdl.itjob.repository.SkillRepository;
import vn.hdl.itjob.util.exception.InvalidException;

@Service
public class SkillService {
    private SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill handleCreateSkill(Skill reqSkill) throws InvalidException {
        // ==> check if skill's name already exist
        if (this.skillRepository.existsByNameIgnoreCase(reqSkill.getName())) {
            throw new InvalidException("skill's name already exist");
        }

        return this.skillRepository.save(reqSkill);
    }

    public Skill handleUpdateSkill(Skill reqSkill) throws InvalidException {
        // ==> check if skill's id not exist
        Skill skill = this.skillRepository.findById(reqSkill.getId())
                .orElseThrow(() -> new InvalidException("Skill id = " + reqSkill.getId() + " not found"));

        // ==> check if skill's name already exist
        if (this.skillRepository.existsByNameIgnoreCase(reqSkill.getName())) {
            throw new InvalidException("skill's name already exist");
        }

        skill.setName(reqSkill.getName());
        return this.skillRepository.save(skill);
    }

    public ResultPaginationDTO handleGetAllSkill(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> skillPage = this.skillRepository.findAll(spec, pageable);

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(skillPage.getNumber() + 1);
        meta.setPageSize(skillPage.getSize());
        meta.setPages(skillPage.getTotalPages());
        meta.setTotal(skillPage.getTotalElements());
        ResultPaginationDTO dto = ResultPaginationDTO.builder()
                .meta(meta)
                .result(skillPage.getContent())
                .build();

        return dto;
    }

    public Skill handleGetSkill(Long id) throws InvalidException {
        return this.skillRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Skill not found"));
    }

    public void handleDeleteSkill(Long id) throws InvalidException {
        Skill skill = this.skillRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Skill id = " + id + " not exist"));
        // delete skill in each job
        skill.getJobs().forEach(job -> job.getSkills().remove(skill));
        // delete skill in each subscriber
        skill.getSubscribers().forEach((subscriber) -> subscriber.getSkills().remove(skill));
        // delete skill
        this.skillRepository.delete(skill);
    }
}
