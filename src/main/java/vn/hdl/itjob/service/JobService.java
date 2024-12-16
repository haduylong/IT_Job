package vn.hdl.itjob.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hdl.itjob.domain.Company;
import vn.hdl.itjob.domain.Job;
import vn.hdl.itjob.domain.Skill;
import vn.hdl.itjob.domain.response.ResultPaginationDTO;
import vn.hdl.itjob.domain.response.job.RespCreateJobDTO;
import vn.hdl.itjob.domain.response.job.RespUpdateJobDTO;
import vn.hdl.itjob.repository.CompanyRepository;
import vn.hdl.itjob.repository.JobRepository;
import vn.hdl.itjob.repository.SkillRepository;
import vn.hdl.itjob.util.exception.InvalidException;
import vn.hdl.itjob.util.mapper.JobMapper;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;
    private final JobMapper jobMapper;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository,
            CompanyRepository companyRepository, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
        this.jobMapper = jobMapper;
    }

    public RespCreateJobDTO handleCreateJob(Job reqJob) {
        // check skills
        if (reqJob.getSkills() != null) {
            List<Long> skillIds = reqJob.getSkills().stream()
                    .map(skill -> skill.getId()).toList();
            List<Skill> skills = this.skillRepository.findAllById(skillIds);
            reqJob.setSkills(skills);
        }

        // check company
        if (reqJob.getCompany() != null) {
            Company company = this.companyRepository.findById(reqJob.getCompany().getId())
                    .orElse(null);
            reqJob.setCompany(company);
        }
        // save
        Job jobDb = this.jobRepository.save(reqJob);

        RespCreateJobDTO dto = jobMapper.toRespCreateJobDTO(jobDb);
        List<String> skillNames = jobDb.getSkills().stream()
                .map(skill -> skill.getName()).toList();
        dto.setSkills(skillNames);

        return dto;
    }

    public RespUpdateJobDTO handleUpdateJob(Job reqJob) throws InvalidException {
        // check if job id not exist
        Job jobDb = this.jobRepository.findById(reqJob.getId())
                .orElseThrow(() -> new InvalidException("Job with id = " + reqJob.getId() + "not found"));
        // check skills
        if (reqJob.getSkills() != null) {
            List<Long> skillIds = reqJob.getSkills().stream()
                    .map(skill -> skill.getId()).toList();
            List<Skill> skills = this.skillRepository.findAllById(skillIds);
            reqJob.setSkills(skills);
        }

        // check company
        if (reqJob.getCompany() != null) {
            Company company = this.companyRepository.findById(reqJob.getCompany().getId())
                    .orElse(null);
            reqJob.setCompany(company);
        }

        reqJob.setCreatedAt(jobDb.getCreatedAt());
        reqJob.setCreatedBy(jobDb.getCreatedBy());
        jobDb = this.jobRepository.save(reqJob);

        RespUpdateJobDTO dto = jobMapper.toRespUpdateJobDTO(jobDb);
        List<String> skillNames = jobDb.getSkills().stream()
                .map(skill -> skill.getName()).toList();
        dto.setSkills(skillNames);

        return dto;
    }

    public ResultPaginationDTO handleGetAllJob(Specification<Job> spec, Pageable pageable) {
        Page<Job> jobPage = this.jobRepository.findAll(spec, pageable);

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(jobPage.getNumber() + 1);
        meta.setPageSize(jobPage.getSize());
        meta.setPages(jobPage.getTotalPages());
        meta.setTotal(jobPage.getTotalElements());
        ResultPaginationDTO dto = ResultPaginationDTO.builder()
                .meta(meta)
                .result(jobPage.getContent())
                .build();

        return dto;
    }

    public Job handleGetJob(Long id) throws InvalidException {
        return this.jobRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Job with id = " + id + " not found"));
    }

    public void handleDeleteJob(Long id) {
        this.jobRepository.deleteById(id);
    }
}
