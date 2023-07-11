package org.ensembl.importer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "studies")
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pubmed_id", unique = true, nullable = false)
    private String pubmedId;//

    @Column(nullable = false)
    private Integer design;

    @Column(name = "lower_age_limit")
    private Integer lowerAgeLimit;

    @Column(name = "upper_age_limit")
    private Integer upperAgeLimit;

    @Column(nullable = false)
    private String title;

    @Column(name = "abstract")
    private String abstractText;

    @Column(name = "journal_title", nullable = false)
    private String journalTitle;

    @Column(name = "journal_abbreviation", nullable = false)
    private String journalAbbreviation;

    @Column(name = "issue_year", nullable = false)
    private Integer issueYear;

    @Column(name = "issue_month")
    private Integer issueMonth;

    @Column(name = "journal_volume")
    private Integer journalVolume;

    @Column(name = "journal_issue")
    private Integer journalIssue;

    private String pagination;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "genetic_methods")
    private String geneticMethods;

    @Column(name = "random_index")
    private Integer randomIndex;

    @Column(length = 10000)
    private String authors;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;


}
