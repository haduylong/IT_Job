package vn.hdl.itjob.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import lombok.RequiredArgsConstructor;
import vn.hdl.itjob.domain.Company;
import vn.hdl.itjob.domain.Job;
import vn.hdl.itjob.domain.Resume;
import vn.hdl.itjob.domain.User;
import vn.hdl.itjob.domain.response.ResultPaginationDTO;
import vn.hdl.itjob.domain.response.resume.RespCreateResumeDTO;
import vn.hdl.itjob.domain.response.resume.RespFetchResumeDTO;
import vn.hdl.itjob.domain.response.resume.RespUpdateResumeDTO;
import vn.hdl.itjob.repository.JobRepository;
import vn.hdl.itjob.repository.ResumeRepository;
import vn.hdl.itjob.repository.UserRepository;
import vn.hdl.itjob.util.SecurityUtil;
import vn.hdl.itjob.util.exception.InvalidException;
import vn.hdl.itjob.util.mapper.ResumeMapper;

@RequiredArgsConstructor
// @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ResumeService {
        private final ResumeRepository resumeRepository;
        private final UserRepository userRepository;
        private final JobRepository jobRepository;
        private final ResumeMapper resumeMapper;

        private final FilterSpecificationConverter filterSpecificationConverter;
        private final FilterParser filterParser;
        // private final FilterBuilder filterBuilder;

        public RespCreateResumeDTO handleCreateResume(Resume reqResume) throws InvalidException {
                // ==> check if user and job not exist
                if (reqResume.getUser() == null || reqResume.getJob() == null) {
                        throw new InvalidException("User or Job is null");
                }

                User userDb = this.userRepository.findById(reqResume.getUser().getId())
                                .orElseThrow(() -> new InvalidException("User/Job not found"));
                reqResume.setUser(userDb);

                Job jobDb = this.jobRepository.findById(reqResume.getJob().getId())
                                .orElseThrow(() -> new InvalidException("User/Job not found"));
                reqResume.setJob(jobDb);

                // save
                Resume resume = this.resumeRepository.save(reqResume);

                return resumeMapper.toRespCreateResumeDTO(resume);
        }

        public RespUpdateResumeDTO handleUpdateResume(Resume reqResume) throws InvalidException {
                // check if resume id not exist
                Resume resumeDb = this.resumeRepository.findById(reqResume.getId())
                                .orElseThrow(() -> new InvalidException(
                                                "Resume id = " + reqResume.getId() + " not found"));

                resumeDb.setStatus(reqResume.getStatus());
                resumeDb = this.resumeRepository.save(resumeDb);

                return resumeMapper.toRespUpdateResumeDTO(resumeDb);
        }

        public ResultPaginationDTO handleGetAllResume(Specification<Resume> spec, Pageable pageable)
                        throws InvalidException {
                String email = SecurityUtil.getCurrentUserLogin()
                                .orElseThrow(() -> new InvalidException("User unauthenticated"));

                List<Long> arrJobIds = new ArrayList<>();
                // user -> company -> job -> resume of company of user
                User userDb = this.userRepository.findByEmail(email)
                                .orElseThrow(() -> new InvalidException("Email " + email + " not found"));
                Company companyOfUser = userDb.getCompany();
                if (companyOfUser != null) {
                        List<Job> jobsOfCompany = companyOfUser.getJobs();
                        if (jobsOfCompany != null && jobsOfCompany.size() > 0) {
                                arrJobIds = jobsOfCompany.stream().map(job -> job.getId()).toList();
                        }
                }

                // Specification<Resume> jobInSpec =
                // filterSpecificationConverter.convert(filterBuilder.field("job")
                // .in(filterBuilder.input(arrJobIds)).get());

                // FilterNode node = filterParser.parse("job IN " + arrJobIds);
                // FilterSpecification<Resume> jobInSpec =
                // filterSpecificationConverter.convert(node);

                final List<Long> listJobs = arrJobIds;
                Specification<Resume> jobInSpec = (root, query, criteriaBuilder) -> {
                        return criteriaBuilder.in(root.get("job").get("id")).value(listJobs);
                };

                Specification<Resume> finalSpec = jobInSpec.and(spec);
                Page<Resume> resumePage = this.resumeRepository.findAll(finalSpec, pageable);

                ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
                meta.setPage(resumePage.getNumber() + 1);
                meta.setPageSize(resumePage.getSize());
                meta.setPages(resumePage.getTotalPages());
                meta.setTotal(resumePage.getTotalElements());

                List<RespFetchResumeDTO> dtos = resumePage.getContent().stream()
                                .map(resume -> resumeMapper.toRespFetchResumeDTO(resume)).toList();
                ResultPaginationDTO res = ResultPaginationDTO.builder()
                                .meta(meta)
                                .result(dtos)
                                .build();

                return res;
        }

        public RespFetchResumeDTO handleGetResume(Long id) throws InvalidException {
                Resume resume = this.resumeRepository.findById(id)
                                .orElseThrow(() -> new InvalidException("Resume Id = " + id + " not found"));

                return resumeMapper.toRespFetchResumeDTO(resume);
        }

        public void handleDeleteResume(Long id) {
                this.resumeRepository.deleteById(id);
        }

        public ResultPaginationDTO handleGetResumeByUser(Pageable pageable) throws InvalidException {
                String email = SecurityUtil.getCurrentUserLogin()
                                .orElseThrow(() -> new InvalidException("User unauthenticated"));

                FilterNode node = filterParser.parse("email = '" + email + "'");
                FilterSpecification<Resume> emailSpec = filterSpecificationConverter.convert(node);

                Page<Resume> resumePage = this.resumeRepository.findAll(emailSpec, pageable);
                ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
                meta.setPage(resumePage.getNumber() + 1);
                meta.setPageSize(resumePage.getSize());
                meta.setPages(resumePage.getTotalPages());
                meta.setTotal(resumePage.getTotalElements());

                List<RespFetchResumeDTO> dtos = resumePage.getContent().stream()
                                .map(resume -> resumeMapper.toRespFetchResumeDTO(resume)).toList();
                ResultPaginationDTO res = ResultPaginationDTO.builder()
                                .meta(meta)
                                .result(dtos)
                                .build();

                return res;
        }

}
