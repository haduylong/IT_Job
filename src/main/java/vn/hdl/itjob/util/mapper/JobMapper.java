package vn.hdl.itjob.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import vn.hdl.itjob.domain.Job;
import vn.hdl.itjob.domain.response.job.RespCreateJobDTO;
import vn.hdl.itjob.domain.response.job.RespUpdateJobDTO;

@Mapper(componentModel = "spring")
public interface JobMapper {
    @Mapping(target = "skills", ignore = true)
    RespCreateJobDTO toRespCreateJobDTO(Job job);

    @Mapping(target = "skills", ignore = true)
    RespUpdateJobDTO toRespUpdateJobDTO(Job job);
}
