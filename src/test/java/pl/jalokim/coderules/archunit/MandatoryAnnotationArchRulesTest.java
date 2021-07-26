package pl.jalokim.coderules.archunit;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchUnitRunner;
import org.junit.runner.RunWith;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "pl.jalokim.coderules.archunit.testing")
public class MandatoryAnnotationArchRulesTest extends MandatoryAnnotationArchRules {

}
