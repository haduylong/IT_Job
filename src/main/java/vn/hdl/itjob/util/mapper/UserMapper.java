package vn.hdl.itjob.util.mapper;

import org.mapstruct.Mapper;

import vn.hdl.itjob.domain.User;
import vn.hdl.itjob.domain.response.user.RespUserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    RespUserDTO toRespUserDTO(User user);
}
