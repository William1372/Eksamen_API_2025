package dat.entities;

import dat.dtos.CandidateDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name="candidate")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="candidate_id", nullable = false, unique = true)
    private Integer id;

    @Setter
    @Column(name="candidate_name", nullable = false)
    private String candidateName;

    @Setter
    @Column(name="candidate_phonenumber", nullable = false, unique = true)
    private String candidatePhoneNumber;

    @Setter
    @Column(name="candidate_education")
    private String candidateEducation;

    @Setter
    @ManyToMany
    @JoinTable(
            name="candidate_skills",
            joinColumns = @JoinColumn(name="candidate_id"),
            inverseJoinColumns = @JoinColumn(name="skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    public Candidate(String candidateName, String candidatePhoneNumber, String candidateEducation, Set<Skill> skills){

        this.candidateName = candidateName;
        this.candidatePhoneNumber = candidatePhoneNumber;
        this.candidateEducation = candidateEducation;
        this.skills = skills != null ? skills : new HashSet<>();

    }

    public Candidate(CandidateDTO candidateDTO){

        this.id = candidateDTO.getId();
        this.candidateName = candidateDTO.getCandidateName();
        this.candidatePhoneNumber = candidateDTO.getCandidatePhoneNumber();
        this.candidateEducation = candidateDTO.getCandidateEducation();

    }

    public void addSkill(Skill skill) {
        this.skills.add(skill);
        skill.getCandidates().add(this);
    }

    public void removeSkill(Skill skill) {
        this.skills.remove(skill);
        skill.getCandidates().remove(this);
    }

}
