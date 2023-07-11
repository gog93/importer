package org.ensembl.importer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "sequence_variations")
public class SequenceVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alias")
    private String alias;

    @Column(name = "cadd_score")
    private BigDecimal caddScore;

    @Column(name = "cdna_related")
    private String cdnaRelated;

    @Column(name = "chromosome")
    private Integer chromosome;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "exac_link", nullable = false)
    private Boolean exacLink;

    @Column(name = "gdna_related")
    private String gdnaRelated;

    @Column(name = "genome_build")
    private String genomeBuild;

    @Column(name = "genotype")
    private Integer genotype;

    @Column(name = "hgrb")
    private Integer hgrb;

    @Column(name = "impact", nullable = false)
    private Integer impact;

    @Column(name = "negative_evidence")
    private String negativeEvidence;

    @Column(name = "num_within_spg", nullable = false)
    private Integer numWithinSpg;

    @Column(name = "observed")
    private String observed;

    @Column(name = "pathogenicity", nullable = false)
    private Integer pathogenicity;

    @Column(name = "position")
    private Integer position;

    @Column(name = "positive_evidence")
    private String positiveEvidence;

    @Column(name = "protein_related")
    private String proteinRelated;

    @Column(name = "reference")
    private String reference;

    @Column(name = "sporadic")
    private Boolean sporadic;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "gene_id", nullable = false)
    private Gene gene;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "gnom_ad")
    private String gnomAd;

    @Column(name = "mut_de_novo")
    private Integer mutDeNovo;

    @Column(name = "random_index")
    private Integer randomIndex;

    @Column(name = "transcript_id_enst")
    private String transcriptIdEnst;

    @Column(name = "transcript_id_ncbi")
    private String transcriptIdNcbi;


}
