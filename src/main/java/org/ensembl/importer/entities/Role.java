package org.ensembl.importer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role {
    @Id
    private Integer id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "can_create_categories")
    private Boolean canCreateCategories;

    @Column(name = "can_create_diseases")
    private Boolean canCreateDiseases;

    @Column(name = "can_create_genes")
    private Boolean canCreateGenes;

    @Column(name = "can_create_meta")
    private Boolean canCreateMeta;

    @Column(name = "can_create_patients")
    private Boolean canCreatePatients;

    @Column(name = "can_create_roles")
    private Boolean canCreateRoles;

    @Column(name = "can_create_study")
    private Boolean canCreateStudy;

    @Column(name = "can_create_symptoms")
    private Boolean canCreateSymptoms;

    @Column(name = "can_create_users")
    private Boolean canCreateUsers;

    @Column(name = "can_delete_categories")
    private Boolean canDeleteCategories;

    @Column(name = "can_delete_diseases")
    private Boolean canDeleteDiseases;

    @Column(name = "can_delete_genes")
    private Boolean canDeleteGenes;

    @Column(name = "can_delete_meta")
    private Boolean canDeleteMeta;

    @Column(name = "can_delete_patients")
    private Boolean canDeletePatients;

    @Column(name = "can_delete_roles")
    private Boolean canDeleteRoles;

    @Column(name = "can_delete_study")
    private Boolean canDeleteStudy;

    @Column(name = "can_delete_symptoms")
    private Boolean canDeleteSymptoms;

    @Column(name = "can_edit_categories")
    private Boolean canEditCategories;

    @Column(name = "can_edit_diseases")
    private Boolean canEditDiseases;

    @Column(name = "can_edit_genes")
    private Boolean canEditGenes;

    @Column(name = "can_edit_meta")
    private Boolean canEditMeta;

    @Column(name = "can_edit_patients")
    private Boolean canEditPatients;

    @Column(name = "can_edit_roles")
    private Boolean canEditRoles;

    @Column(name = "can_edit_study")
    private Boolean canEditStudy;

    @Column(name = "can_edit_symptoms")
    private Boolean canEditSymptoms;

    @Column(name = "can_edit_users")
    private Boolean canEditUsers;

    @Column(name = "can_enter_charts")
    private Boolean canEnterCharts;

    @Column(name = "can_enter_meta_files")
    private Boolean canEnterMetaFiles;

    @Column(name = "can_enter_symptom_category")
    private Boolean canEnterSymptomCategory;

    @Column(name = "can_merge_symptoms")
    private Boolean canMergeSymptoms;

    @Column(name = "can_create_family")
    private Boolean canCreateFamily;

    @Column(name = "can_delete_family")
    private Boolean canDeleteFamily;

    @Column(name = "can_edit_family")
    private Boolean canEditFamily;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;
}
