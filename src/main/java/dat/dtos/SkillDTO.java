package dat.dtos;

import dat.entities.Candidate;
import dat.entities.Skill;
import dat.enums.SkillCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class SkillDTO {

    private Integer id;
    private String skillName;
    private SkillCategory skillCategory;
    private String skillDescription;
    private String slug;

    private Integer popularityScore;
    private Integer averageSalary;

    public SkillDTO(Skill skill){

        this.id = skill.getId();
        this.skillName = skill.getSkillName();
        this.skillCategory = skill.getSkillCategory();
        this.skillDescription = skill.getSkillDescription();
        this.slug = skill.getSlug();
        this.popularityScore = skill.getPopularityScore();
        this.averageSalary = skill.getAverageSalary();

    }
    public static List<SkillDTO> toSkillDTOList (List<Skill> skills) {

        return List.of(skills.stream().map(SkillDTO::new).toArray(SkillDTO[]::new));

    }

}
