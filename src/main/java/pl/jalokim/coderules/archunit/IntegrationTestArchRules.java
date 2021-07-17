package pl.jalokim.coderules.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.transaction.annotation.Transactional;

public class IntegrationTestArchRules {

    @ArchTest
    public final ArchRule integrationTestClassesShouldNotBeAnnotatedWithTransactionalAnnotation = classes()
        .that()
        .haveSimpleNameEndingWith("IT")
        .should()
        .notBeAnnotatedWith(Transactional.class);

    @ArchTest
    public final ArchRule integrationTestMethodsShouldNotBeAnnotatedWithTransactionalAnnotation = methods()
        .that()
        .areDeclaredInClassesThat().haveSimpleNameEndingWith("IT")
        .should()
        .notBeAnnotatedWith(Transactional.class);
}
