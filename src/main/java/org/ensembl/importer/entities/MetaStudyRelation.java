package org.ensembl.importer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "meta_study_relations")
public class MetaStudyRelation {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meta_analysis_id", nullable = false)
    private MetaAnalysis metaAnalysis;

    @ManyToOne
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;


}
