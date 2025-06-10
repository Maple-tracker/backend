package com.mmt.tracker.market.domain;

import com.mmt.tracker.market.repository.AdditionalPotentialOptionRepository;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class AdditionalPotentialOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PotentialGrade grade;

    @Column(nullable = false)
    private Short optionLines;

    @Column(nullable = false)
    private Short percentLines;

    public AdditionalPotentialOption(PotentialGrade grade, Short lines, Short percentLines) {
        this.grade = grade;
        this.optionLines = lines;
        this.percentLines = percentLines;
    }

    public String toInfo() {
        return grade.getValue() + " " + optionLines + " " + percentLines;
    }

    public static AdditionalPotentialOption findOrCreate(
            String grade, 
            Short lines, 
            Short percentLines, 
            AdditionalPotentialOptionRepository repository) {

        PotentialGrade additionalPotentialGrade = PotentialGrade.fromString(grade);
        return repository.findByGradeAndOptionLinesAndPercentLines(
                additionalPotentialGrade,
                lines,
                percentLines
        ).orElseGet(() -> {
            AdditionalPotentialOption newOption = new AdditionalPotentialOption(
                    PotentialGrade.fromString(grade),
                    lines,
                    percentLines
            );
            return repository.save(newOption);
        });
    }
}
