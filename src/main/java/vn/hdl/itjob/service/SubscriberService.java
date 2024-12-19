package vn.hdl.itjob.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hdl.itjob.domain.Skill;
import vn.hdl.itjob.domain.Subscriber;
import vn.hdl.itjob.repository.SkillRepository;
import vn.hdl.itjob.repository.SubscriberRepository;
import vn.hdl.itjob.util.exception.InvalidException;

@Service
@RequiredArgsConstructor
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public Subscriber handleCreateSubscriber(Subscriber reqSubscriber) throws InvalidException {
        // ==> check if subscriber email existed
        if (this.subscriberRepository.existsByEmail(reqSubscriber.getEmail())) {
            throw new InvalidException("Subscriber email " + reqSubscriber.getEmail() + " already existed");
        }

        // get skills
        if (reqSubscriber.getSkills() != null) {
            List<Long> skillIds = reqSubscriber.getSkills().stream()
                    .map(skill -> skill.getId()).toList();
            List<Skill> skills = this.skillRepository.findAllById(skillIds);
            reqSubscriber.setSkills(skills);
        }

        return this.subscriberRepository.save(reqSubscriber);
    }

    public Subscriber handleUpdateSubscriber(Subscriber reqSubscriber) throws InvalidException {
        Subscriber subscriberDb = this.subscriberRepository.findById(reqSubscriber.getId())
                .orElseThrow(() -> new InvalidException("Subscriber id = " + reqSubscriber.getId() + " not exist"));

        // get skills
        if (reqSubscriber.getSkills() != null) {
            List<Long> skillIds = reqSubscriber.getSkills().stream()
                    .map(skill -> skill.getId()).toList();
            List<Skill> skills = this.skillRepository.findAllById(skillIds);
            subscriberDb.setSkills(skills);
        }

        return this.subscriberRepository.save(subscriberDb);
    }
}
