package org.ensembl.importer.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/copyTables")
public class CopyController {
    private final DiseaseController diseaseController;
    private final FamilyController familyController;
    private final FileUploadController fileUploadController;
    private final MetaAnalysisController metaAnalysisController;
    private final MetaStudyRelationController metaStudyRelationController;
    private final GeneController geneController;
    private final PatientController patientController;
    private final PatientsSymptomsController patientsSymptomsController;
    private final SequenceVariationController sequenceVariationController;
    private final RoleController roleController;
    private final StudyController studyController;
    private final SymptomController symptomController;
    private final UserController userController;
    private Map<Long, Long> diseaseMap = new HashMap<>();
    private Map<Long, Long> userMap = new HashMap<>();
    private Map<Long, Long> symptomMap = new HashMap<>();
    private Map<Long, Long> studyMap = new HashMap<>();
    private Map<Integer, Integer> roleMap = new HashMap<>();
    private Map<Long, Long>  patientsSymptomsMap = new HashMap<>();
    private Map<Long, Long> patientMap = new HashMap<>();
    private Map<Long, Long> metaStudyRelation = new HashMap<>();
    private Map<Long, Long> metaAnalysesMap = new HashMap<>();
    private Map<Integer, Integer> geneMap = new HashMap<>();
    private Map<Long, Long> fileUploadMap = new HashMap<>();
    private Map<Long, Long> familyMap = new HashMap<>();
    private Map<Long, Long> sequenceVariationsMap = new HashMap<>();


    @GetMapping("/sequenceVariations")
    private String copySequenceVariations(Model model) {
        Map<Long, Long> map = sequenceVariationController.processSequenceVariationChanges();
        if (!sequenceVariationsMap.equals(map)) {
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                Long key = entry.getKey();
                Long value = entry.getValue();

                if (!sequenceVariationsMap.containsValue(value)) {
                    sequenceVariationsMap.put(key, value);
                    // Key does not exist in diseaseMap
                    System.out.println("Key " + key + " does not exist in diseaseMap");
                }

            }
            return "copyTables";
        }
        model.addAttribute("checkedSequenceVariations", "There is nothing to change");
        return "copyTables";
    }
    @GetMapping("/user")
    private String copyUser(Model model) {
        Map<Long, Long> map = userController.processUsersChanges();
        if (!userMap.equals(map)) {
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                Long key = entry.getKey();
                Long value = entry.getValue();

                if (!userMap.containsValue(value)) {
                    userMap.put(key, value);
                    // Key does not exist in diseaseMap
                    System.out.println("Key " + key + " does not exist in diseaseMap");
                }

            }
            return "copyTables";
        }
        model.addAttribute("checkedUser", "There is nothing to change");
        return "copyTables";
    }
    @GetMapping("/symptom")
    private String copySymptom(Model model) {
        Map<Long, Long> map = symptomController.processSymptomChanges();
        if (!symptomMap.equals(map)) {
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                Long key = entry.getKey();
                Long value = entry.getValue();

                if (!symptomMap.containsValue(value)) {
                    symptomMap.put(key, value);
                    // Key does not exist in diseaseMap
                    System.out.println("Key " + key + " does not exist in diseaseMap");
                }

            }
            return "copyTables";
        }
        model.addAttribute("checkedSymptom", "There is nothing to change");
        return "copyTables";
    }
    @GetMapping("/study")
    private String copyStudy(Model model) {
        Map<Long, Long> map = studyController.processStudyChanges();
        if (!studyMap.equals(map)) {
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                Long key = entry.getKey();
                Long value = entry.getValue();

                if (!studyMap.containsValue(value)) {
                    studyMap.put(key, value);
                    // Key does not exist in diseaseMap
                    System.out.println("Key " + key + " does not exist in diseaseMap");
                }

            }
            return "copyTables";
        }
        model.addAttribute("checkedSymptom", "There is nothing to change");
        return "copyTables";
    }
    @GetMapping("/disease")
    private String copyDisease(Model model) {
        Map<Long, Long> map = diseaseController.processDiseaseChanges();
        if (!diseaseMap.equals(map)) {
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                Long key = entry.getKey();
                Long value = entry.getValue();

                if (!diseaseMap.containsValue(value)) {
                    diseaseMap.put(key, value);
                    // Key does not exist in diseaseMap
                    System.out.println("Key " + key + " does not exist in diseaseMap");
                }

            }
            return "copyTables";
        }
        model.addAttribute("checked", "There is nothing to change");
        return "copyTables";
    }
    @GetMapping("/role")
    private String copyRole(Model model) {
        Map<Integer, Integer> map = roleController.processRoleChanges();
        if (!roleMap.equals(map)) {
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();

                if (!roleMap.containsValue(value)) {
                    roleMap.put(key, value);
                    // Key does not exist in diseaseMap
                    System.out.println("Key " + key + " does not exist in roleMap");
                }

            }
            return "copyTables";
        }
        model.addAttribute("checkedRole", "There is nothing to change");
        return "copyTables";
    }
    @GetMapping("/family")
    private String copyFamily(Model model) {
        Map<Long, Long> map = familyController.processFamilyChanges();
        if (!familyMap.equals(map)) {
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                Long key = entry.getKey();
                Long value = entry.getValue();

                if (!familyMap.containsValue(value)) {
                    familyMap.put(key, value);
                    // Key does not exist in diseaseMap
                    System.out.println("Key " + key + " does not exist in familyMap");
                }

            }
            return "copyTables";
        }
        model.addAttribute("checkedFamily", "There is nothing to change");
        return "copyTables";
    }
    @GetMapping("/patient")
    private String copyPatient(Model model) {
        Map<Long, Long> map = patientController.processPatientChanges();
        if (!patientMap.equals(map)) {
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                Long key = entry.getKey();
                Long value = entry.getValue();

                if (!patientMap.containsValue(value)) {
                    patientMap.put(key, value);
                    // Key does not exist in diseaseMap
                    System.out.println("Key " + key + " does not exist in patientMap");
                }

            }
            return "copyTables";
        }
        model.addAttribute("checkedPatient", "There is nothing to change");
        return "copyTables";
    }
    @GetMapping("/meta-analyses")
    private String copyMetaAnalyses(Model model) {
        Map<Long, Long> map = metaAnalysisController.metaAnalysesChanges();
        if (!metaAnalysesMap.equals(map)) {
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                Long key = entry.getKey();
                Long value = entry.getValue();

                if (!metaAnalysesMap.containsValue(value)) {
                    metaAnalysesMap.put(key, value);
                    // Key does not exist in diseaseMap
                    System.out.println("Key " + key + " does not exist in meta-analysesMap");
                }

            }
            return "copyTables";
        }
        model.addAttribute("checkedMetaAnalyses", "There is nothing to change");
        return "copyTables";
    }
    @GetMapping("/fileUpload")
    private String copyFileUpload(Model model) {
        Map<Long, Long> map = fileUploadController.processFileUploadChanges();
        if (!fileUploadMap.equals(map)) {
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                Long key = entry.getKey();
                Long value = entry.getValue();

                if (!fileUploadMap.containsValue(value)) {
                    fileUploadMap.put(key, value);
                    // Key does not exist in diseaseMap
                    System.out.println("Key " + key + " does not exist in fileUploadMap");
                }

            }
            return "copyTables";
        }
        model.addAttribute("checkedFileUpload", "There is nothing to change");
        return "copyTables";
    }
    @GetMapping("/meta-study-relations")
    private String copyMetaStudyRelations(Model model) {
        Map<Long, Long> map = metaStudyRelationController.metaStudyRelationsChanges();
        if (!metaStudyRelation.equals(map)) {
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                Long key = entry.getKey();
                Long value = entry.getValue();

                if (!metaStudyRelation.containsValue(value)) {
                    metaStudyRelation.put(key, value);
                    // Key does not exist in diseaseMap
                    System.out.println("Key " + key + " does not exist in meta-study-relationsMap");
                }

            }
            return "copyTables";
        }
        model.addAttribute("checkedMetaStudyRelations", "There is nothing to change");
        return "copyTables";
    }
    @GetMapping("/patientsSymptoms")
    private String copyPatientsSymptoms(Model model) {
        Map<Long, Long> map = patientsSymptomsController.processPatientChanges();
        if (!patientsSymptomsMap.equals(map)) {
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                Long key = entry.getKey();
                Long value = entry.getValue();

                if (!patientsSymptomsMap.containsValue(value)) {
                    patientsSymptomsMap.put(key, value);
                    // Key does not exist in diseaseMap
                    System.out.println("Key " + key + " does not exist in patientSymptomsMap");
                }

            }
            return "copyTables";
        }
        model.addAttribute("checkedPatientsSymptoms", "There is nothing to change");
        return "copyTables";
    }
    @GetMapping("/gene")
    private String copyGene(Model model) {
        Map<Integer, Integer> map = geneController.processGeneChanges();
        if (!geneMap.equals(map)) {
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();

                if (!geneMap.containsValue(value)) {
                    geneMap.put(key, value);
                    // Key does not exist in diseaseMap
                    System.out.println("Key " + key + " does not exist in geneMap");
                }

            }
            return "copyTables";
        }
        model.addAttribute("checkedGene", "There is nothing to change");
        return "copyTables";
    }

    @GetMapping
    private String copy() {
        return "copyTables";
    }
}
