package isolette.system_tests.rst

import org.sireum._
import art.scheduling.static._
import art.Art
import isolette._

// Do not edit this file as it will be overwritten if SystemTestArtifactGen is rerun

class Example_Regulate_Subsystem_Outputs_Container_Test_wSlangCheck
  extends Regulate_Subsystem_Outputs_Container_SlangCheck {

  //===========================================================
  //  S c h e d u l a r     and    S t e p p e r     Configuration
  //===========================================================

  // note: this is overriding SystemTestSuite's 'def scheduler: Scheduler'
  //       abstract method
  //var scheduler: StaticScheduler = Schedulers.getStaticSchedulerH(MNone())
  var scheduler: StaticScheduler = Schedulers.getStaticScheduler(
    Schedulers.defaultStaticSchedule,
    Schedulers.defaultDomainToBridgeIdMap,
    Schedulers.threadNickNames,
    ISZCommandProvider(ISZ()))

  def compute(isz: ISZ[Command]): Unit = {
    scheduler = scheduler(commandProvider = ISZCommandProvider(isz :+ Stop()))

    Art.computePhase(scheduler)
  }

  override def beforeEach(): Unit = {

    // uncomment the following to disable the various guis and to suppress the log streams
    //disableLogsAndGuis()

    super.beforeEach()
  }

  //===========================================================
  //  S l a n g   C h e c k    Infrastructure
  //===========================================================

  val maxTests = 100
  var verbose: B = T

  val testMatrix: Map[String, TestRow] = Map.empty ++ ISZ(
    "testFamilyName" ~> TestRow(
      testMethod = NameProvider("Schema-Name", ((input_container: Any, output_container: Any) => T).asInstanceOf[(Any, Any) => B]),
      profile =getDefaultProfile,
      preStateCheck = ((container: Any) => T).asInstanceOf[Any => B],
      property = NameProvider("Property-Name", ((input_container: Any, output_container: Any) => T).asInstanceOf[(Any, Any) => B])
    )
  )

  for (testRow <- testMatrix.entries) {
    run(testRow._1, testRow._2)
  }

  def genTestName(testFamilyName: String, testRow: TestRow): String = {
    return s"${testFamilyName}: ${testRow.testMethod.name}: ${testRow.property.name}: ${testRow.profile.name}"
  }

  def run(testFamilyName: String, testRow: TestRow): Unit = {

    for (i <- 0 until maxTests) {
      val testName = s"${genTestName(testFamilyName, testRow)}_$i"
      this.registerTest(testName) {
        var retry: B = T
        var j: Z = 0

        while (j < testRow.profile.numTestVectorGenRetries && retry) {
          if (verbose && j > 0) {
            println(s"Retry $j:")
          }

          next(testRow.profile) match {
            case Some(container) =>
              if (!testRow.preStateCheck(container)) {
                // retry
              } else {
                testRow.testMethod.function(container, testRow.property.function)
                retry = F
              }
            case _ =>
          }
          j = j + 1
        }
      }
    }
  }
}