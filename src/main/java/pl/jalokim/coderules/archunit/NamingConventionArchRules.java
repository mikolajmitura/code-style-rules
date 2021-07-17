package pl.jalokim.coderules.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static pl.jalokim.coderules.archunit.ArchRulesConstants.ENTITY_SUFFIX;
import static pl.jalokim.coderules.archunit.ArchRulesConstants.MAPPER_SUFFIX;
import static pl.jalokim.coderules.archunit.ArchRulesConstants.REPOSITORY_SUFFIX;
import static pl.jalokim.coderules.archunit.ArchRulesConstants.REST_CONTROLLER_SUFFIX;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import javax.persistence.Entity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("PMD.UnusedPrivateField")
public class NamingConventionArchRules {

    @ArchTest
    private final ArchRule restControllersShouldHaveRestControllerSuffix =
        classes().that().areAnnotatedWith(RestController.class)
            .should().haveSimpleNameEndingWith(REST_CONTROLLER_SUFFIX);

    @ArchTest
    private final ArchRule repositoriesShouldHaveRepositorySuffix =
        classes().that().areAnnotatedWith(Repository.class)
            .should().haveSimpleNameEndingWith(REPOSITORY_SUFFIX);

    @ArchTest
    private final ArchRule entitiesShouldHaveEntitySuffix =
        classes().that().areAnnotatedWith(Entity.class)
            .should().haveSimpleNameEndingWith(ENTITY_SUFFIX);

    @ArchTest
    private final ArchRule mapStructMappersShouldHaveMapperSuffix =
        classes().that().areAnnotatedWith(Mapper.class)
            .should().haveSimpleNameEndingWith(MAPPER_SUFFIX);
}
