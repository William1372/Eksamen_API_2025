package dat.dtos;

import dat.entities.Candidate;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CandidateDTO {

    private Integer id;
    private String candidateName;
    private String candidatePhoneNumber;
    private String candidateEducation;
    private List<SkillDTO> skills;

    public CandidateDTO(Candidate candidate){

        this.id = candidate.getId();
        this.candidateName = candidate.getCandidateName();
        this.candidatePhoneNumber = candidate.getCandidatePhoneNumber();
        this.candidateEducation = candidate.getCandidateEducation();
        this.skills = candidate.getSkills().stream()
                .map(SkillDTO::new)
                .collect(Collectors.toList());

    }

    public CandidateDTO(String candidateName, String candidatePhoneNumber, String candidateEducation){

        this.candidateName = candidateName;
        this.candidatePhoneNumber = candidatePhoneNumber;
        this.candidateEducation = candidateEducation;

    }

    public static List<CandidateDTO> toCandidateDTOList (List<Candidate> candidates) {

        return candidates.stream().map(CandidateDTO::new).collect(Collectors.toList());

    }

}
