package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TestHelper {
    public User user() {
        User user;
        return user = User.builder()
                .id(1L)
                .active(true)
                .firstname("user")
                .lastname("user")
                .createdAt(Timestamp.from(Instant.now()))
                .currentSignInAt(Timestamp.from(Instant.now()))
                .lastSignInAt(Timestamp.from(Instant.now()))
                .password("user")
                .passwordChanged(true)
                .rememberCreatedAt(Timestamp.from(Instant.now()))
                .resetPasswordSentAt(Timestamp.from(Instant.now()))
                .resetPasswordToken("user")
                .role(role())
                .signInCount(1)
                .updatedAt(Timestamp.from(Instant.now()))
                .username("user")
                .build();
    }

    public Role role() {
        Role role;
        return role = Role.builder()
                .id(1)
                .roleName("Admin")
                .canCreateCategories(true)
                .canCreateDiseases(true)
                .canCreateGenes(true)
                .canCreateMeta(true)
                .canCreatePatients(true)
                .canCreateRoles(true)
                .canCreateStudy(true)
                .canCreateSymptoms(true)
                .canCreateUsers(true)
                .canDeleteCategories(true)
                .canDeleteDiseases(true)
                .canDeleteGenes(true)
                .canDeleteMeta(true)
                .canDeletePatients(true)
                .canDeleteRoles(true)
                .canDeleteStudy(true)
                .canDeleteSymptoms(true)
                .canEditCategories(true)
                .canEditDiseases(true)
                .canEditGenes(true)
                .canEditMeta(true)
                .canEditPatients(true)
                .canEditRoles(true)
                .canEditStudy(true)
                .canEditSymptoms(true)
                .canEditUsers(true)
                .canEnterCharts(true)
                .canEnterMetaFiles(true)
                .canEnterSymptomCategory(true)
                .canMergeSymptoms(true)
                .canCreateFamily(true)
                .canDeleteFamily(true)
                .canEditFamily(true)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();

    }

    public Symptom symptom() {
        Symptom symptom;
        return symptom = Symptom.builder()
                .id(1L)
                .category(123)
                .createdAt(LocalDateTime.now())
                .definition("Symptom definition")
                .importHeader("Header")
                .name("Symptom Name")
                .name1("Alternate Name")
                .updatedAt(LocalDateTime.now())
                .signName("Sign Name")
                .build();

    }

    public Study study() {
        Study study;
        return study = Study.builder()
                .id(1L)
                .pubmedId("123456")
                .design(1)
                .lowerAgeLimit(18)
                .upperAgeLimit(65)
                .title("Study Title")
                .abstractText("Study Abstract")
                .journalTitle("Journal Title")
                .journalAbbreviation("Journal Abbreviation")
                .issueYear(2023)
                .issueMonth(7)
                .journalVolume(10)
                .journalIssue(3)
                .pagination("123-145")
                .comment("Study Comment")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .geneticMethods("Genetic Methods")
                .randomIndex(123)
                .authors("Author 1, Author 2")
                .createdBy(user())
                .updatedBy(user())
                .build();

    }
    public Study study2() {
        Study study;
        return study = Study.builder()
                .id(1L)
                .pubmedId("123456")
                .design(1)
                .lowerAgeLimit(18)
                .upperAgeLimit(65)
                .title("Study Title2")
                .abstractText("Study Abstract2")
                .journalTitle("Journal Title2")
                .journalAbbreviation("Journal Abbreviation2")
                .issueYear(2023)
                .issueMonth(7)
                .journalVolume(10)
                .journalIssue(3)
                .pagination("123-145")
                .comment("Study Comment")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .geneticMethods("Genetic Methods")
                .randomIndex(123)
                .authors("Author 1, Author 2")
                .createdBy(user())
                .updatedBy(user())
                .build();

    }

    public SequenceVariation sequenceVariation() {
        SequenceVariation sequenceVariation;
        return sequenceVariation = SequenceVariation.builder()
                .id(1L)
                .alias("Alias")
                .caddScore(BigDecimal.valueOf(5.67))
                .cdnaRelated("cDNA Related")
                .chromosome(1)
                .createdAt(LocalDateTime.now())
                .exacLink(true)
                .gdnaRelated("gDNA Related")
                .genomeBuild("Genome Build")
                .genotype(2)
                .hgrb(3)
                .impact(4)
                .negativeEvidence("Negative Evidence")
                .numWithinSpg(5)
                .observed("Observed")
                .pathogenicity(6)
                .position(7)
                .positiveEvidence("Positive Evidence")
                .proteinRelated("Protein Related")
                .reference("Reference")
                .sporadic(true)
                .updatedAt(LocalDateTime.now())
                .gene(gene())
                .patient(patient())
                .gnomAd("gnomAD")
                .mutDeNovo(8)
                .randomIndex(9)
                .transcriptIdEnst("ENST123")
                .transcriptIdNcbi("NCBI123")
                .build();

    }

    public Gene gene() {
        Gene gene;
        return  gene = Gene.builder()
                .id(1)
                .name("Example Gene")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .name1("Example Name 1")
                .build();

    }

    public Patient patient() {
        Patient patient;
        return patient = Patient.builder()
                .id(1L)
                .disease(disease())
                .levodopaResponse(1)
                .sex(1)
                .ethnicity(1)
                .ageAtOnset(BigDecimal.valueOf(30))
                .ageAtExam(BigDecimal.valueOf(40))
                .clinicalInfo(true)
                .comment("Some comment")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .study(study())
                .individualIdentity("ABC123")
                .family(family())
                .country(1)
                .indexPatient(true)
                .statusClinical(1)
                .statusParaclinical(1)
                .aaoClinical(BigDecimal.valueOf(35))
                .aaoParaclinical(BigDecimal.valueOf(38))
                .diseaseDuration(BigDecimal.valueOf(10))
                .ageAtRem(BigDecimal.valueOf(50))
                .ageAtDiagnosis(BigDecimal.valueOf(32))
                .ageAtDeath(BigDecimal.valueOf(70))
                .otherPxmd(1)
                .lineOfFile(1)
                .fileUpload(fileUpload())
                .randomIndex(1)
                .verifiedAt(LocalDateTime.now())
                .createdBy(user())
                .updatedBy(user())
                .verifiedBy(user())
                .build();

    }
    public Patient patient2() {
        Patient patient;
        return patient = Patient.builder()
                .id(2L)
                .disease(disease())
                .levodopaResponse(1)
                .sex(1)
                .ethnicity(1)
                .ageAtOnset(BigDecimal.valueOf(30))
                .ageAtExam(BigDecimal.valueOf(40))
                .clinicalInfo(true)
                .comment("Some comment2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .study(study())
                .individualIdentity("ABC1232")
                .family(family())
                .country(1)
                .indexPatient(true)
                .statusClinical(1)
                .statusParaclinical(1)
                .aaoClinical(BigDecimal.valueOf(35))
                .aaoParaclinical(BigDecimal.valueOf(38))
                .diseaseDuration(BigDecimal.valueOf(10))
                .ageAtRem(BigDecimal.valueOf(50))
                .ageAtDiagnosis(BigDecimal.valueOf(32))
                .ageAtDeath(BigDecimal.valueOf(70))
                .otherPxmd(1)
                .lineOfFile(1)
                .fileUpload(fileUpload())
                .randomIndex(1)
                .verifiedAt(LocalDateTime.now())
                .createdBy(user())
                .updatedBy(user())
                .verifiedBy(user())
                .build();

    }

    public Disease disease3() {
        Disease disease;
        return  disease = Disease.builder()
                .id(1L)
                .name("Example Disease")
                .abbreviation("ED")
                .launched(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .parent(null) // Set the parent disease object
                .build();

    } public Disease disease() {
        Disease disease;
        return  disease = Disease.builder()
                .id(3L)
                .name("Example Disease")
                .abbreviation("ED")
                .launched(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .parent(disease3()) // Set the parent disease object
                .build();

    }
    public Disease disease2() {
        Disease disease;
        return  disease = Disease.builder()
                .id(2L)
                .name("Example Disease2")
                .abbreviation("ED")
                .launched(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .parent(disease3()) // Set the parent disease object
                .build();

    }

    public Family family() {
        Family family;
        return  family = Family.builder()
                .id(1L)
                .name("Example Family")
                .history(true)
                .numberOfHeterozygousMutationsAffected(2)
                .numberOfHomozygousMutationsAffected(1)
                .numberOfHeterozygousMutationsUnaffected(1)
                .numberOfHomozygousMutationsUnaffected(0)
                .numberOfWildtypeAffected(3)
                .numberOfWildtypeUnaffected(2)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .consanguinity(false)
                .build();

    }

    public PatientsSymptoms patientsSymptoms() {
        PatientsSymptoms patientsSymptoms;
        return patientsSymptoms = PatientsSymptoms.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .initial(true)
                .present(true)
                .updatedAt(LocalDateTime.now())
                .patient(patient())
                .symptom(symptom())
                .build();

    }

    public MetaStudyRelation metaStudyRelation() {
        MetaStudyRelation metaStudyRelation;
        return metaStudyRelation = MetaStudyRelation.builder()
                .id(1L)
                .metaAnalysis(metaAnalysis())
                .study(study())
                .build();

    }

    public MetaAnalysis metaAnalysis() {
        MetaAnalysis metaAnalysis;
        return metaAnalysis = MetaAnalysis.builder()
                .comment("Example comment")
                .createdAt(LocalDateTime.now())
                .metaName("Example meta name")
                .searchPerformedBy("John Doe")
                .searchTerm("Example search term")
                .searchedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy(user())
                .updatedBy(user())
                .build();

    }
    public FileUpload fileUpload(){
        FileUpload fileUpload;
        return  fileUpload = FileUpload.builder()
                .id(1L)
                .tsvFileFileName("example.tsv")
                .tsvFileContentType("text/tab-separated-values")
                .tsvFileFileSize(1024)
                .tsvFileUpdatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .description("Example file upload")
                .study(study()) // Replace 'study' with an actual Study object
                .build();

    }
    public Map<Long, Long> createPatientsMap() {
        Map<Long, Long> patientsMap = new HashMap<>();
        patientsMap.put(1L, 1L);
        return patientsMap;
    }
    public Map<Long, Long> createStudyMap() {
        Map<Long, Long> studyMap = new HashMap<>();
        studyMap.put(1L, 1L);
        studyMap.put(2L, 2L);
        return studyMap;
    }
    public Map<Long, Long> createStudiesMap() {
        Map<Long, Long> studiesMap = new HashMap<>();
        studiesMap.put(1L, 1L);
        return studiesMap;
    }
    public Map<Long, Long> createDiseaseMap() {
        Map<Long, Long> diseaseMap = new HashMap<>();
        diseaseMap.put(1L, 1L);
        diseaseMap.put(2L, 2L);
        return diseaseMap;
    }

    public Map<Long, Long> createFileUploadMap() {
        Map<Long, Long> fileUploadMap = new HashMap<>();
        fileUploadMap.put(1L, 1L);
        fileUploadMap.put(2L, 2L);
        return fileUploadMap;
    }
    public Map<Long, Long> createUserMap() {
        Map<Long, Long> userMap = new HashMap<>();
        userMap.put(1L, 1L);
        userMap.put(2L, 2L);
        return userMap;
    }

    public Map<Long, Long> createFamilyMap() {
        Map<Long, Long> familyMap = new HashMap<>();
        familyMap.put(1L, 1L);
        familyMap.put(2L, 2L);
        return familyMap;
    }
}
