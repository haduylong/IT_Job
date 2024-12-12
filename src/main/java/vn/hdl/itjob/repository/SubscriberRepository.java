package vn.hdl.itjob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hdl.itjob.domain.Subscriber;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

}
