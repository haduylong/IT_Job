package vn.hdl.itjob.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import vn.hdl.itjob.domain.response.ResultPaginationDTO;

@Service
@RequiredArgsConstructor
public class CompanyRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisMapper;

    public void saveAllCompanies(String key, ResultPaginationDTO resCompaniesDTO) throws JsonProcessingException {
        String jsonPaginationDTO = redisMapper.writeValueAsString(resCompaniesDTO);
        redisTemplate.opsForValue().set(key, jsonPaginationDTO);
    }

    public ResultPaginationDTO getAllCompanies(String key) throws JsonMappingException, JsonProcessingException {
        String jsonPaginationDTO = (String) redisTemplate.opsForValue().get(key);
        ResultPaginationDTO dto = jsonPaginationDTO != null
                ? redisMapper.readValue(jsonPaginationDTO, new TypeReference<ResultPaginationDTO>() {
                })
                : null;
        return dto;
    }

    public void clear(String pattern) {
        redisTemplate.keys(pattern).forEach(redisTemplate::delete);
    }
}
