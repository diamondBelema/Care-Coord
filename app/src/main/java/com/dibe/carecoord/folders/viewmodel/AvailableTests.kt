package com.dibe.carecoord.folders.viewmodel

import com.dibe.carecoord.folders.viewmodel.util.Test

object AvailableTests {

    val tests = listOf(
        // Haematology
        Test("D-Dimer", 8000, '₦', "Haematology"),
        Test("Blood Film for Malaria Parasite", 2000, '₦', "Haematology"),
        Test("Antinuclear Antibody (ANA)", 15000, '₦', "Haematology"),
        Test("Lupus Anticoagulant", 15000, '₦', "Haematology"),
        Test("Antiphospholipid Antibody", 15000, '₦', "Haematology"),
        Test("Complete Blood Count (CBC)", 5000, '₦', "Haematology"),
        Test("Hemoglobin A1c", 7000, '₦', "Haematology"),
        Test("Prothrombin Time (PT)", 6000, '₦', "Haematology"),
        Test("Activated Partial Thromboplastin Time (aPTT)", 6000, '₦', "Haematology"),

        // Clinical Chemistry
        Test("Serum Electrolytes (Na, K, Cl)", 5000, '₦', "Clinical Chemistry"),
        Test("Creatinine Clearance Test", 8000, '₦', "Clinical Chemistry"),
        Test("Ammonia", 8000, '₦', "Clinical Chemistry"),
        Test("CK Total", 5000, '₦', "Clinical Chemistry"),
        Test("Troponin I", 20000, '₦', "Clinical Chemistry"),
        Test("Troponin T", 20000, '₦', "Clinical Chemistry"),
        Test("Myoglobin", 15000, '₦', "Clinical Chemistry"),
        Test("Lipid Panel", 8000, '₦', "Clinical Chemistry"),
        Test("Liver Function Tests (LFTs)", 10000, '₦', "Clinical Chemistry"),
        Test("Bilirubin (Total and Direct)", 6000, '₦', "Clinical Chemistry"),
        Test("Calcium (Total and Ionized)", 6000, '₦', "Clinical Chemistry"),

        // Microbiology
        Test("Blood Culture", 12000, '₦', "Microbiology"),
        Test("Sputum Culture", 8000, '₦', "Microbiology"),
        Test("TB Culture", 15000, '₦', "Microbiology"),
        Test("Helicobacter Pylori Stool Antigen", 8000, '₦', "Microbiology"),
        Test("H. Pylori IgG", 6000, '₦', "Microbiology"),
        Test("Gonorrhea Culture", 6000, '₦', "Microbiology"),
        Test("HIV Viral Load", 50000, '₦', "Microbiology"),
        Test("Urine Culture", 6000, '₦', "Microbiology"),
        Test("Fungal Culture", 12000, '₦', "Microbiology"),
        Test("Molecular Testing for Infectious Diseases", 20000, '₦', "Microbiology"),

        // Hormone Assays
        Test("Insulin", 10000, '₦', "Hormone Assays"),
        Test("Parathyroid Hormone (PTH)", 15000, '₦', "Hormone Assays"),
        Test("ACTH (Adrenocorticotropic Hormone)", 20000, '₦', "Hormone Assays"),
        Test("DHEA-Sulfate", 10000, '₦', "Hormone Assays"),
        Test("Anti-Mullerian Hormone (AMH)", 25000, '₦', "Hormone Assays"),
        Test("FSH/LH Ratio", 15000, '₦', "Hormone Assays"),
        Test("Thyroid Stimulating Hormone (TSH)", 8000, '₦', "Hormone Assays"),
        Test("Free T4", 7000, '₦', "Hormone Assays"),
        Test("Free T3", 7000, '₦', "Hormone Assays"),

        // Histopathology
        Test("Bone Marrow Biopsy", 40000, '₦', "Histopathology"),
        Test("Liver Biopsy", 50000, '₦', "Histopathology"),
        Test("Kidney Biopsy", 50000, '₦', "Histopathology"),
        Test("Endometrial Biopsy", 30000, '₦', "Histopathology"),
        Test("Skin Biopsy", 15000, '₦', "Histopathology"),
        Test("Pathology Review", 20000, '₦', "Histopathology"),
        Test("Cytology (Pap Smear)", 10000, '₦', "Histopathology"),
        Test("Frozen Section", 25000, '₦', "Histopathology"),

        // Cancer Markers
        Test("BRCA1 & BRCA2 (Breast Cancer Gene)", 200000, '₦', "Cancer Markers"),
        Test("HER2/neu (Breast Cancer)", 100000, '₦', "Cancer Markers"),
        Test("Cancer Antigen (CA) 19-9 (Pancreatic)", 75000, '₦', "Cancer Markers"),
        Test("Cancer Antigen (CA) 50", 75000, '₦', "Cancer Markers"),
        Test("Neuron-Specific Enolase (NSE)", 50000, '₦', "Cancer Markers"),
        Test("Prostate-Specific Antigen (PSA)", 10000, '₦', "Cancer Markers"),
        Test("Alpha-Fetoprotein (AFP)", 30000, '₦', "Cancer Markers"),
        Test("Carcinoembryonic Antigen (CEA)", 25000, '₦', "Cancer Markers"),

        // Genetic Testing
        Test("Karyotyping", 60000, '₦', "Genetic Testing"),
        Test("Whole Genome Sequencing", 300000, '₦', "Genetic Testing"),
        Test("Carrier Screening for Cystic Fibrosis", 50000, '₦', "Genetic Testing"),
        Test("BRCA Gene Testing", 150000, '₦', "Genetic Testing"),
        Test("Apolipoprotein E Genotyping (for Alzheimer’s)", 60000, '₦', "Genetic Testing"),
        Test("Single Nucleotide Polymorphism (SNP) Testing", 200000, '₦', "Genetic Testing"),
        Test("Pharmacogenetic Testing", 100000, '₦', "Genetic Testing"),

        // Immunology
        Test("HLA-B27", 20000, '₦', "Immunology"),
        Test("Immunoglobulin E (IgE)", 5000, '₦', "Immunology"),
        Test("Anti-Double Stranded DNA (Anti-dsDNA)", 10000, '₦', "Immunology"),
        Test("Rheumatoid Factor (RF)", 10000, '₦', "Immunology"),
        Test("Complement C3 and C4", 8000, '₦', "Immunology"),
        Test("Anti-Smith Antibodies", 12000, '₦', "Immunology"),
        Test("Anti-Cyclic Citrullinated Peptide (Anti-CCP)", 20000, '₦', "Immunology"),

        // Infectious Diseases
        Test("COVID-19 PCR Test", 25000, '₦', "Infectious Diseases"),
        Test("COVID-19 Antibody Test", 8000, '₦', "Infectious Diseases"),
        Test("Dengue Fever Antigen", 10000, '₦', "Infectious Diseases"),
        Test("Zika Virus PCR", 20000, '₦', "Infectious Diseases"),
        Test("EBV Antibody (Epstein-Barr Virus)", 12000, '₦', "Infectious Diseases"),
        Test("Hepatitis A Antibody Test", 10000, '₦', "Infectious Diseases"),
        Test("Hepatitis B Surface Antigen (HBsAg)", 8000, '₦', "Infectious Diseases"),
        Test("Hepatitis C Antibody Test", 8000, '₦', "Infectious Diseases"),
        Test("Syphilis Screening (RPR/VDRL)", 5000, '₦', "Infectious Diseases"),

        // Toxicology
        Test("Drug Screening (Urine)", 10000, '₦', "Toxicology"),
        Test("Lead Level", 8000, '₦', "Toxicology"),
        Test("Arsenic Level", 8000, '₦', "Toxicology"),
        Test("Mercury Level", 8000, '₦', "Toxicology"),
        Test("Alcohol Breath Test", 2000, '₦', "Toxicology"),
        Test("Cannabis Testing (Blood)", 10000, '₦', "Toxicology"),
        Test("Cocaine Testing (Urine)", 8000, '₦', "Toxicology"),

        // Imaging & Diagnostic Tests
        Test("Ultrasound Scan", 15000, '₦', "Imaging"),
        Test("Pelvic Ultrasound", 4000, '₦', "Imaging"),
        Test("Abdominal Ultrasound", 4000, '₦', "Imaging"),
        Test("Prostate Ultrasound", 7000, '₦', "Imaging"),
        Test("Trans Vaginal Ultrasound Scan", 7000, '₦', "Imaging"),
        Test("Breast Scan", 10000, '₦', "Imaging"),
        Test("Ocular Scan", 10000, '₦', "Imaging"),
        Test("Soft Tissue Scan", 10000, '₦', "Imaging"),
        Test("Doppler Ultrasound", 20000, '₦', "Imaging"),
        Test("X-Ray", 10000, '₦', "Imaging"),
        Test("MRI", 70000, '₦', "Imaging"),
        Test("CT Scan", 60000, '₦', "Imaging"),
        Test("CT Angiography", 80000, '₦', "Imaging"),
        Test("Mammography", 25000, '₦', "Imaging"),
        Test("ECG", 10000, '₦', "Cardiology"),
        Test("Echocardiogram", 30000, '₦', "Cardiology"),
    )
    fun getAllTestNames(): String {
        return tests.joinToString(separator = ", ") { it.name }
    }
}