package dat.entities;


import dat.dtos.SkillDTO;
import dat.enums.SkillCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name="skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="skill_id", nullable = false, unique = true)
    private Integer id;

    @Setter
    @Column(name="skill_name", nullable = false)
    private String skillName;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name="skill_category", nullable = false)
    private SkillCategory skillCategory;

    @Setter
    @Column(name="skill_description", nullable = false)
    private String skillDescription;

    @Column(name="slug", nullable = false, unique = true)
    private String slug;

    @Setter
    @Column(name="popularity_score")
    private Integer popularityScore;

    @Setter
    @Column(name="average_salary")
    private Integer averageSalary;

    @Setter
    @ManyToMany(mappedBy="skills")
    private Set<Candidate> candidates = new HashSet<>();

    public Skill(String skillName, SkillCategory skillCategory, String skillDescription, String slug, Integer popularityScore, Integer averageSalary){

        this.skillName = skillName;
        this.skillCategory = skillCategory;
        this.skillDescription = skillDescription;
        this.slug = slug;
        this.popularityScore = popularityScore;
        this.averageSalary = averageSalary;

    }

    public Skill(SkillDTO skillDTO){

        this.id = skillDTO.getId();
        this.skillName = skillDTO.getSkillName();
        this.skillCategory = skillDTO.getSkillCategory();
        this.skillDescription = skillDTO.getSkillDescription();
        this.slug = skillDTO.getSlug();

    }

}
