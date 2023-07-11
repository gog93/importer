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
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "disease_id", nullable = false)
    private Disease disease;

    @Column(name = "levodopa_response")
    private Integer levodopaResponse;

    @Column(name = "sex", nullable = false)
    private Integer sex;

    @Column(name = "ethnicity")
    private Integer ethnicity;

    @Column(name = "age_at_onset")
    private BigDecimal ageAtOnset;

    @Column(name = "age_at_exam")
    private BigDecimal ageAtExam;

    @Column(name = "clinical_info", nullable = false)
    private Boolean clinicalInfo;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Column(name = "individual_identity", nullable = false)
    private String individualIdentity;

    @ManyToOne
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @Column(name = "country")
    private Integer country;

    @Column(name = "index_patient", nullable = false)
    private Boolean indexPatient;

    @Column(name = "status_clinical", nullable = false)
    private Integer statusClinical;

    @Column(name = "status_paraclinical", nullable = false)
    private Integer statusParaclinical;

    @Column(name = "aao_clinical")
    private BigDecimal aaoClinical;

    @Column(name = "aao_paraclinical")
    private BigDecimal aaoParaclinical;

    @Column(name = "disease_duration")
    private BigDecimal diseaseDuration;

    @Column(name = "age_at_rem")
    private BigDecimal ageAtRem;

    @Column(name = "age_at_diagnosis")
    private BigDecimal ageAtDiagnosis;

    @Column(name = "age_at_death")
    private BigDecimal ageAtDeath;

    @Column(name = "other_pxmd")
    private Integer otherPxmd;

    @Column(name = "line_of_file", nullable = false)
    private Integer lineOfFile;

    @ManyToOne
    @JoinColumn(name = "file_upload_id", nullable = false)
    private FileUpload fileUpload;

    @Column(name = "random_index")
    private Integer randomIndex;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

    @ManyToOne
    @JoinColumn(name = "verified_by_id")
    private User verifiedBy;
}
