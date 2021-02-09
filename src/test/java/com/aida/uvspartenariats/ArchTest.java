package com.aida.uvspartenariats;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.aida.uvspartenariats");

        noClasses()
            .that()
            .resideInAnyPackage("com.aida.uvspartenariats.service..")
            .or()
            .resideInAnyPackage("com.aida.uvspartenariats.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.aida.uvspartenariats.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
