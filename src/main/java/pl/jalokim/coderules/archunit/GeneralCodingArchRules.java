package pl.jalokim.coderules.archunit;

import static com.tngtech.archunit.core.domain.JavaModifier.PRIVATE;
import static com.tngtech.archunit.core.domain.JavaModifier.PUBLIC;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static pl.jalokim.coderules.archunit.ArchRulesConstants.FACADE_SUFFIX;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

public class GeneralCodingArchRules {

    @ArchTest
    public final ArchRule facadeShouldHaveOnlyMethodsWithPublicOrPrivateModifier =
        methods().that().areDeclaredInClassesThat().haveSimpleNameEndingWith(FACADE_SUFFIX)
            .should().haveModifier(PUBLIC)
            .orShould().haveModifier(PRIVATE);
}
