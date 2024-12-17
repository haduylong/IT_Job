package vn.hdl.itjob.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import vn.hdl.itjob.domain.Resume;
import vn.hdl.itjob.domain.response.resume.RespCreateResumeDTO;
import vn.hdl.itjob.domain.response.resume.RespFetchResumeDTO;
import vn.hdl.itjob.domain.response.resume.RespUpdateResumeDTO;

@Mapper(componentModel = "spring")
public interface ResumeMapper {
    RespCreateResumeDTO toRespCreateResumeDTO(Resume resume);

    RespUpdateResumeDTO toRespUpdateResumeDTO(Resume resume);

    @Mapping(target = "companyName", source = "resume.job.company.name")
    RespFetchResumeDTO toRespFetchResumeDTO(Resume resume);
}
