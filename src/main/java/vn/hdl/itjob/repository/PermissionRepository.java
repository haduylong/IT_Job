package vn.hdl.itjob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hdl.itjob.domain.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
