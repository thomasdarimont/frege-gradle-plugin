package frege.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.process.internal.DefaultJavaExecAction
import org.gradle.process.internal.JavaExecAction

class FregeQuickCheckTask extends DefaultTask {

    // more options to consider:
/*
     Looks up quick check predicates in the given modules and tests them.

    [Usage:] java -cp fregec.jar frege.tools.Quick [ option ... ] modulespec ...

    Options:

    -    -v      print a line for each pedicate that passed
    -    -n num  run _num_ tests per predicate, default is 100
    -    -p pred1,pred2,... only test the given predicates
    -    -x pred1,pred2,... do not test the given predicates
    -    -l  just print the names of the predicates available.

    Ways to specify modules:

    - module  the module name (e.g. my.great.Module), will be lookup up in
              the current class path.
    - dir/    A directory path. The directory is searched for class files,
              and for each class files an attempt is made to load it as if
              the given directory was in the class path. The directory must
              be the root of the classes contained therein, otherwise the
              classes won't get loaded.
    - path-to.jar A jar or zip file is searched for class files, and for each
              class file found an attempt is made to load it as if the
              jar was in the class path.

     The number of passed/failed tests is reported. If any test failed or other
     errors occured, the exit code will be non zero.

     The code will try to heat up your CPU by running tests on all available cores.
     This should be faster on multi-core computers than running the tests
     sequentially. It makes it feasable to run more tests per predicate.

     */
    @TaskAction
    void runQuickCheck() {

        FileResolver fileResolver = getServices().get(FileResolver.class)
        JavaExecAction action = new DefaultJavaExecAction(fileResolver)
        action.setMain("frege.tools.Quick")
        action.setClasspath(project.files(project.configurations.testRuntime))

        project.configurations.testRuntime.each { println it }

        action.args("$project.buildDir/classes/main/") // test all in build dir and below build/classes/test/
        action.execute()
    }

}