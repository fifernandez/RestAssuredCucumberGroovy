package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        glue = {"support"},
        monochrome = true,
        //dryRun = false,
        plugin = {
                "json:build/reports/cucumber.json",
                "junit:build/reports/cucumber.xml",
                "html:build/reports/cucumber-report.html",
                "rerun:build/reports/rerun.txt",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        tags = ""
        )
public class TestRunner {
}
