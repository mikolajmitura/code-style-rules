package pl.jalokim.coderules.archunit;

import static com.tngtech.archunit.core.domain.JavaModifier.PUBLIC;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static pl.jalokim.coderules.archunit.ArchRulesConstants.ENTITY_SUFFIX;
import static pl.jalokim.coderules.archunit.ArchRulesConstants.FACADE_SUFFIX;
import static pl.jalokim.coderules.archunit.ArchRulesConstants.REPOSITORY_SUFFIX;
import static pl.jalokim.coderules.archunit.ArchRulesConstants.REST_CONTROLLER_SUFFIX;
import static pl.jalokim.coderules.archunit.ArchRulesConstants.SERVICE_SUFFIX;

import com.querydsl.core.types.dsl.EntityPathBase;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

public class MandatoryAnnotationArchRules {

    private static final String ENUM_NOT_ANNOTATED_MSG_FORMAT = "Field '%s' in class: '%s' not annotated with @Enumerated(EnumType.STRING)";

    @ArchTest
    public final ArchRule facadeShouldBeAnnotatedWithTransactionalAndValidatedAnnotation =
        classes().that().haveSimpleNameEndingWith(FACADE_SUFFIX)
            .and().doNotHaveModifier(JavaModifier.ABSTRACT)
            .should().beAnnotatedWith(Transactional.class)
            .andShould().beAnnotatedWith(Validated.class);

    @ArchTest
    public final ArchRule serviceShouldBeAnnotatedWithValidatedAnnotation =
        classes().that().haveSimpleNameEndingWith(FACADE_SUFFIX)
            .and().doNotHaveModifier(JavaModifier.ABSTRACT)
            .should().beAnnotatedWith(Validated.class);

    @ArchTest
    public final ArchRule controllerShouldNotBeAnnotatedWithValidatedAnnotation =
        classes().that().haveSimpleNameEndingWith(REST_CONTROLLER_SUFFIX)
            .should().notBeAnnotatedWith(Validated.class);

    @ArchTest
    public final ArchRule controllerShouldBeAnnotatedWithApiAnnotation =
        classes().that().haveSimpleNameEndingWith(REST_CONTROLLER_SUFFIX)
            .should().beAnnotatedWith(Api.class);

    @ArchTest
    public final ArchRule publicControllerMethodsShouldBeAnnotatedWithApiOperationAnnotation =
        methods().that().areDeclaredInClassesThat().haveSimpleNameEndingWith(REST_CONTROLLER_SUFFIX)
            .and().haveModifier(PUBLIC)
            .should().beAnnotatedWith(ApiOperation.class);

    @ArchTest
    public final ArchRule restControllersShouldBeAnnotatedWithRestControllerAnnotation =
        classes().that().haveSimpleNameEndingWith(REST_CONTROLLER_SUFFIX)
            .should().beAnnotatedWith(RestController.class);

    @ArchTest
    public final ArchRule restControllersShouldHasRestControllerSuffix =
        classes().that().areAnnotatedWith(RestController.class)
            .should().haveSimpleNameEndingWith(REST_CONTROLLER_SUFFIX);

    @ArchTest
    public final ArchRule repositoriesShouldBeAnnotatedWithRepositoryAnnotation =
        classes().that().haveSimpleNameEndingWith(REPOSITORY_SUFFIX)
            .and().areNotAnnotatedWith(NoRepositoryBean.class)
            .should().beAnnotatedWith(Repository.class);

    @ArchTest
    public final ArchRule entitiesShouldBeAnnotatedWithEntityAnnotation =
        classes().that().haveSimpleNameEndingWith(ENTITY_SUFFIX)
            .and().areNotAssignableTo(EntityPathBase.class)
            .and().areNotAnnotatedWith(MappedSuperclass.class)
            .should().beAnnotatedWith(Entity.class);

    @ArchTest
    public final ArchRule servicesShouldBeAnnotatedWithServiceAnnotation =
        classes().that().haveSimpleNameEndingWith(SERVICE_SUFFIX)
            .and().areNotInterfaces()
            .and().doNotHaveModifier(JavaModifier.ABSTRACT)
            .should().beAnnotatedWith(Service.class);

    @ArchTest
    public final ArchRule enumFieldsInEntitiesShouldBeEnumeratedByString =
        fields().that(IS_ENUM_TYPE)
            .and().areDeclaredInClassesThat().haveSimpleNameEndingWith(ENTITY_SUFFIX)
            .and().areDeclaredInClassesThat().areAnnotatedWith(Entity.class)
            .should(BE_ENUMERATED_WITH_STRING);

    private static final DescribedPredicate<JavaField> IS_ENUM_TYPE = new DescribedPredicate<JavaField>("is enum type") {

        @Override
        public boolean apply(JavaField input) {
            return input.getRawType().isEnum();
        }
    };

    private static final ArchCondition<JavaField> BE_ENUMERATED_WITH_STRING = new ArchCondition<JavaField>("be annotated with @Enumerated(EnumType.STRING") {

        @Override
        @SuppressWarnings({"blockRcurly", "PMD.AccessorMethodGeneration"})
        public void check(JavaField item, ConditionEvents events) {
            Set<JavaAnnotation<JavaField>> annotations = item.getAnnotations();
            String fieldName = item.getName();
            String ownerName = item.getOwner().getName();
            String msg = String.format(ENUM_NOT_ANNOTATED_MSG_FORMAT, fieldName, ownerName);

            Optional<JavaAnnotation<JavaField>> foundEnumerated = annotations.stream()
                .filter(it -> Enumerated.class.getCanonicalName().equals(it.getRawType().getFullName()))
                .findFirst();

            if (foundEnumerated.isPresent()) {
                Object enumType = foundEnumerated.get().getProperties().get("value");
                if (EnumType.STRING.name().equals(enumType)) {
                    events.add(new SimpleConditionEvent(item, false, msg));
                }
            } else {
                events.add(new SimpleConditionEvent(item, false, msg));
            }
        }
    };
}
