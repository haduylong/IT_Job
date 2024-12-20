package vn.hdl.itjob.util.mapper;

import org.mapstruct.Mapper;

import vn.hdl.itjob.domain.Job;
import vn.hdl.itjob.domain.response.RespEmailJob;

@Mapper(componentModel = "spring")
public interface EmailMapper {

    RespEmailJob toRespEmailJob(Job job);
}
