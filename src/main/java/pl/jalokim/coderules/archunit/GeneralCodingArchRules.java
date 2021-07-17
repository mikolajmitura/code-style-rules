package pl.jalokim.coderules.archunit;

import static com.tngtech.archunit.core.domain.JavaModifier.PRIVATE;
import static com.tngtech.archunit.core.domain.JavaModifier.PUBLIC;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static pl.jalokim.coderules.archunit.ArchRulesConstants.FACADE_SUFFIX;
import static pl.jalokim.coderules.archunit.ArchRulesConstants.MAPPER_SUFFIX;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Component;

public class GeneralCodingArchRules {

    @ArchTest
    public final ArchRule facadeShouldHaveOnlyMethodsWithPublicOrPrivateModifier =
        methods().that().areDeclaredInClassesThat().haveSimpleNameEndingWith(FACADE_SUFFIX)
            .should().haveModifier(PUBLIC)
            .orShould().haveModifier(PRIVATE);

    @ArchTest
    public final ArchRule mappersShouldNotBeSpringBeansButUtilClasses =
        classes().that().haveSimpleNameEndingWith(MAPPER_SUFFIX)
            .should().notBeAnnotatedWith(Component.class);
}
