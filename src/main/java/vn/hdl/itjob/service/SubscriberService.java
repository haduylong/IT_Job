package vn.hdl.itjob.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hdl.itjob.domain.Job;
import vn.hdl.itjob.domain.Skill;
import vn.hdl.itjob.domain.Subscriber;
import vn.hdl.itjob.domain.response.email.RespEmailJob;
import vn.hdl.itjob.repository.JobRepository;
import vn.hdl.itjob.repository.SkillRepository;
import vn.hdl.itjob.repository.SubscriberRepository;
import vn.hdl.itjob.util.exception.InvalidException;
import vn.hdl.itjob.util.mapper.EmailMapper;

@Service
@RequiredArgsConstructor
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;
    private final EmailMapper emailMapper;

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

    public void sendMailSubscriber() {
        // subscribers -> skills -> jobs in skills
        List<Subscriber> subscribers = this.subscriberRepository.findAll();
        if (subscribers != null && subscribers.size() > 0) {
            for (Subscriber subscriber : subscribers) {
                List<Skill> skills = subscriber.getSkills();
                if (skills != null && skills.size() > 0) {
                    List<Job> jobs = this.jobRepository.findBySkillsIn(skills);
                    List<RespEmailJob> emailJobs = jobs.stream()
                            .map(job -> emailMapper.toRespEmailJob(job)).toList();

                    if (jobs != null && jobs.size() > 0) {
                        this.emailService.sendEmailFromTemplateSync(subscriber.getEmail(), "Available jobs",
                                "job", subscriber.getName(), emailJobs);
                    }
                }

            }
        }
    }

    // #region Send mail pagination
    public void sendMailSubscriberPaged() {
        int pageSize = 1; // The number of subscriber per page
        int currentPage = 0; // Starting page
        Page<Subscriber> subscriberPage;

        do {
            // Get the list of subscriber by page
            subscriberPage = this.subscriberRepository.findAll(PageRequest.of(currentPage, pageSize));
            List<Subscriber> subscribers = subscriberPage.getContent();

            if (subscribers != null && !subscribers.isEmpty()) {
                for (Subscriber subscriber : subscribers) {
                    List<Skill> skills = subscriber.getSkills();
                    if (skills != null && !skills.isEmpty()) {
                        List<Job> jobs = this.jobRepository.findBySkillsIn(skills);
                        List<RespEmailJob> emailJobs = jobs.stream()
                                .map(job -> emailMapper.toRespEmailJob(job)).toList();

                        if (jobs != null && !jobs.isEmpty()) {
                            this.emailService.sendEmailFromTemplateSync(subscriber.getEmail(), "Available jobs",
                                    "job", subscriber.getName(), emailJobs);
                        }
                    }
                }
            }

            currentPage++; // Go to the next page
        } while (subscriberPage.hasNext()); // Continue if there are more pages
    }
    // #endregion

    public Subscriber handleGetSubscriber(String email) throws InvalidException {
        return this.subscriberRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidException("Subscriber email " + email + " not fonud"));
    }
}
