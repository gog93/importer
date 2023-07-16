package org.ensembl.importer.controllers;

import lombok.RequiredArgsConstructor;
import org.ensembl.importer.entities.Patient;
import org.ensembl.importer.entities.PatientsSymptoms;
import org.ensembl.importer.entities.SequenceVariation;
import org.ensembl.importer.entities.Symptom;
import org.ensembl.importer.repositories.PatientsSymptomsRepository;
import org.ensembl.importer.repositories.SequenceVariationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/symptomsMerge")
public class SymptomsMergeController {
    private final SequenceVariationRepository sequenceVariationRepository;

    private final PatientsSymptomsRepository patientsSymptomsRepository;

    @GetMapping
    public String page() {
        return "index";
    }

    @GetMapping("/in")
    public ResponseEntity handleFormSubmission(@RequestParam("classNumber") String classNumber) {
        List<SequenceVariation> byGeneId = sequenceVariationRepository.findByGeneName(classNumber);
        Set<Patient> patients = new HashSet<>();
        Map<Symptom, Integer> symptoms = new LinkedHashMap<>();
        for (SequenceVariation s : byGeneId) {
            patients.add(s.getPatient());
        }
        checkTheFrequencyOfRecurrenceOfSymptoms(patients, symptoms);
        comparePatients(patients);
        return new ResponseEntity<>(HttpStatus.OK); // Redirect to a result page after processing the form

    }

    private void comparePatients(Set<Patient> patients) {
        Map<Patient, List<Symptom>> symptomGroups = new HashMap<>();

        for (Patient patient : patients) {
            List<PatientsSymptoms> byPatiant = patientsSymptomsRepository.findByPatientId(patient.getId());
            for (PatientsSymptoms s : byPatiant) {
                if (symptomGroups.containsKey(patient)) {
                    List<Symptom> symptomsForPatient = symptomGroups.get(patient);
                    symptomsForPatient.add(s.getSymptom());

                } else {
                    List<Symptom> symptomsForPatient = new ArrayList<>();
                    symptomsForPatient.add(s.getSymptom());
                    symptomGroups.put(patient, symptomsForPatient);
                }

            }

        }
        Set<PatientsSymptoms> patientsSymptoms = new HashSet<>();
        for (Patient patient1 : symptomGroups.keySet()) {
            List<Symptom> symptoms1 = symptomGroups.get(patient1);

            for (Patient patient2 : symptomGroups.keySet()) {
                if (patient1.equals(patient2)) {
                    continue; // Skip self-comparison
                }

                List<Symptom> symptoms2 = symptomGroups.get(patient2);
                if (symptoms2.containsAll(symptoms1) && symptoms2.size() != symptoms1.size()) {
                    // Find common symptoms between patients
                    List<Symptom> commonSymptoms = new ArrayList<>(symptoms2);
                    commonSymptoms.removeAll(symptoms1);

                    // If patients have common symptoms, update patient1's symptoms
                    if (!commonSymptoms.isEmpty()) {
                        for (int i = 0; i < commonSymptoms.size(); i++) {
                            PatientsSymptoms patientsSymptom = createNewPatientSymptom(patients, patient1, commonSymptoms.get(i));

                            patientsSymptoms.add(patientsSymptom);
                        }
                    }
                }
            }

        }
        patientsSymptomsRepository.saveAll(patientsSymptoms);

    }

    private void checkTheFrequencyOfRecurrenceOfSymptoms(Set<Patient> patients, Map<Symptom, Integer> symptoms) {
        int count = 0;
        for (Patient p : patients) {
            List<PatientsSymptoms> byPatiant = patientsSymptomsRepository.findByPatientId(p.getId());
            for (PatientsSymptoms s : byPatiant) {

                if (symptoms.containsKey(s.getSymptom())) {
                    int count2 = symptoms.get(s.getSymptom());
                    symptoms.put(s.getSymptom(), count2 + 1);
                } else {
                    symptoms.put(s.getSymptom(), count + 1);
                }
            }
        }
        Map<Patient, List<Symptom>> symptomGroups = new HashMap<>();

        for (Patient patient : patients) {
            List<PatientsSymptoms> byPatiant = patientsSymptomsRepository.findByPatientId(patient.getId());
            for (PatientsSymptoms s : byPatiant) {
                if (symptomGroups.containsKey(patient)) {
                    List<Symptom> symptomsForPatient = symptomGroups.get(patient);
                    symptomsForPatient.add(s.getSymptom());

                } else {
                    List<Symptom> symptomsForPatient = new ArrayList<>();
                    symptomsForPatient.add(s.getSymptom());
                    symptomGroups.put(patient, symptomsForPatient);
                }

            }

        }
        for (Patient p : patients) {
            List<Symptom> symptomsForPatient = symptomGroups.get(p);

            for (Symptom symptom : symptoms.keySet()) {
                if (symptomsForPatient != null) {
                    if (!symptomsForPatient.contains(symptom) && symptoms.get(symptom) >= patients.size() / 4) {
                        PatientsSymptoms patientsSymptoms = createNewPatientSymptom(patients, p, symptom);
                        patientsSymptomsRepository.save(patientsSymptoms);

                    }
                } else if (symptoms.get(symptom) >= patients.size() / 4) {
                    PatientsSymptoms patientsSymptoms = createNewPatientSymptom(patients, p, symptom);
                    patientsSymptomsRepository.save(patientsSymptoms);


                }
            }

        }
    }

    private Boolean patientSymptomsPresentCheck(Set<Patient> patients, Symptom symptom) {
        Map<Symptom, Map<Boolean, Integer>> presents = new LinkedHashMap<>();

        int count = 0;
        for (Patient patient : patients) {
            List<PatientsSymptoms> byPatiant = patientsSymptomsRepository.findByPatientId(patient.getId());
            for (PatientsSymptoms s : byPatiant) {
                if (presents.containsKey(s.getSymptom())) {
                    Map<Boolean, Integer> symptomsForPresent = presents.get(s.getSymptom());
                    Integer c = symptomsForPresent.get(s.getPresent());
                    if (c == null) {
                        symptomsForPresent.put(s.getPresent(), count + 1);
                    } else {
                        symptomsForPresent.put(s.getPresent(), c + 1);
                    }

                } else {
                    Map<Boolean, Integer> symptomsForPresent = new HashMap<>();
                    symptomsForPresent.put(s.getPresent(), count + 1);
                    presents.put(s.getSymptom(), symptomsForPresent);
                }

            }

        }
        Map<Boolean, Integer> booleanIntegerMap = presents.get(symptom);
        int a = booleanIntegerMap.get(true);
        int f = booleanIntegerMap.get(false);
        if (a >= f) {
            return true;
        } else {
            return false;
        }
    }

    private PatientsSymptoms createNewPatientSymptom(Set<Patient> patients, Patient patient, Symptom symptom) {
        Boolean aBoolean = patientSymptomsPresentCheck(patients, symptom);
        PatientsSymptoms patientsSymptoms = PatientsSymptoms.builder()
                .createdAt(LocalDateTime.now())
                .initial(false)
                .present(aBoolean)
                .updatedAt(LocalDateTime.now())
                .patient(patient)
                .symptom(symptom)
                .build();
        return patientsSymptoms;
    }
}
