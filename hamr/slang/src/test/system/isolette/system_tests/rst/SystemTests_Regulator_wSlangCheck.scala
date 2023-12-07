package isolette.system_tests.rst

import org.sireum._
import art.scheduling.static._
import art.{Art}
import isolette.Isolette_Data_Model._
import isolette.Regulate.{Manage_Heat_Source_impl_thermostat_regulate_temperature_manage_heat_source_SystemTestAPI => RegMHS, Manage_Regulator_Interface_impl_thermostat_regulate_temperature_manage_regulator_interface_SystemTestAPI => RegMRI, Manage_Regulator_Mode_impl_thermostat_regulate_temperature_manage_regulator_mode_SystemTestAPI => RegMRM}
import isolette.{Config_F32, InitialValues, RandomLib, Schedulers, StaticSchedulerCust, SystemTestSuite}


// This file will not be overwritten so is safe to edit

class SystemTests_Regulator_wSlangCheck
  extends Regulate_Subsystem_Inputs_Container_SlangCheck {

  //===========================================================
  //  S c h e d u l a r     and    S t e p p e r     Configuration
  //
  //   Looks like default could be auto-generated and then
  //   customized by the developer.
  //===========================================================

  // note: this is overriding SystemTestSuite's 'def scheduler: Scheduler'
  //       abstract method
  //var scheduler: StaticScheduler = Schedulers.getStaticSchedulerH(MNone())
  var scheduler: StaticScheduler = Schedulers.getStaticScheduler(
    StaticSchedulerCust.staticSchedule,
    StaticSchedulerCust.domainToBridgeIdMap,
    StaticSchedulerCust.threadNickNames,
    ISZCommandProvider(ISZ()))

  def compute(isz: ISZ[Command]): Unit = {
    scheduler = scheduler(commandProvider = ISZCommandProvider(isz :+ Stop()))

    Art.computePhase(scheduler)
  }

  override def beforeEach(): Unit = {

    // uncomment the following to disable the various guis and to suppress the log streams
    disableLogsAndGuis()

    super.beforeEach()
  }

  //===========================================================
  //  S l a n g   C h e c k    Infrastructure
  //===========================================================

  val maxTests = 100
  var verbose: B = T

  val testMatrix: Map[String, TestRow] = Map.empty ++ ISZ(
    "Normal_Heat_On" ~> TestRow(
      NameProvider("Schema-Regulator_1HP_script_schema", (Regulator_1HP_script_schema _).asInstanceOf[(Any, Any) => B]),
      validRanges,
      (Regulate_Subsystem_Inputs_Container_GumboX.system_Pre_Container _).asInstanceOf[Any => B],
      NameProvider("Property-NormalModeHeatOn", (sysProp_NormalModeHeatOn _).asInstanceOf[(Any, Any) => B])
    ),
    "Normal_Heat_Off" ~> TestRow(
      NameProvider("Schema-Regulator_1HP_script_schema", (Regulator_1HP_script_schema _).asInstanceOf[(Any, Any) => B]),
      validRanges,
      (Regulate_Subsystem_Inputs_Container_GumboX.system_Pre_Container _).asInstanceOf[Any => B],
      NameProvider("Property-NormalModeHeatOff", (sysProp_NormalModeHeatOff _).asInstanceOf[(Any, Any) => B])
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
                assert(testRow.testMethod.function(container, testRow.property.function))
                retry = F
              }
            case _ =>
          }
          j = j + 1
        }
      }
    }
  }


  //-----------------------------------------------------------
  //  (Refactor into one method for each profile, then a
  //   "profile list" that indicates a particular set of
  //   profiles to run.
  //
  //   Developer-designed profile (derived from auto-generated template)
  //-----------------------------------------------------------

  def c32(low: Option[F32], high: Option[F32], ranLib: RandomLib): Config_F32 = {
    return ranLib.get_Config_F32(low = low, high = high)
  }

  def validRanges: Regulate_Subsystem_Inputs_Container_Profile = {
    var t = getDefaultProfile
    t = t(name = "Valid Ranges Profile")

    t.lowerDesiredTempWStatus.set_Config_F32(c32(
      low = Some(InitialValues.LOWER_DESIRED_TEMPERATURE_LOWER_RANGE),
      high = Some(InitialValues.LOWER_DESIRED_TEMPERATURE_UPPER_RANGE),
      ranLib = freshRandomLib))

    t.upperDesiredTempWStatus.set_Config_F32(c32(
      low = Some(InitialValues.UPPER_DESIRED_TEMPERATURE_LOWER_RANGE),
      high = Some(InitialValues.UPPER_DESIRED_TEMPERATURE_UPPER_RANGE),
      ranLib = freshRandomLib
    ))

    // tighten up currentTemp so that there's a better chance to get within MA's
    // 0.5 tolerance
    t.currentTempWStatus.set_Config_F32(c32(
      low = Some(90f),
      high = Some(110f),
      ranLib = freshRandomLib))

    t
  }

  //=============================================================
  //  Test Script Schemas
  //
  //   I believe that this could eventually be auto-generated from
  //   a higher-level language of trace patterns.   Ideally,
  //   the higher-level language would be shared by a Logika-based
  //   framework that generate Logika test scripts with the same
  //   abstract structure.
  //=============================================================

  def Regulator_1HP_script_schema(inject_container: Regulate_Subsystem_Inputs_Container,
                           prop: (Regulate_Subsystem_Inputs_Container, Regulate_Subsystem_Outputs_Container) => B
                          ): B = {

    // -------------------- trace prefix --------------------

    // ====== Initialization =====
    // run the system's initialization phase
    //  ... automatically generated basic on indication that this script is a "compute phase test script"

    Art.initializePhase(scheduler)

    // ====== Compute ======

    //  ----  r u n   t o   a r b i t r a r y    s y s t e m    s t a t e

    // Abstractly, run the system an arbitrary number of steps

    compute(ISZ(Hstep(2)))

    compute(ISZ(RunToThread("RegMRI")))

    // -------------------- inject / observe inputs --------------------
    //   ...eventually, auto-generated from higher-level specification of
    //      injection and observation vectors, along with temporal point
    //      declarations of when to inject and observe
    // -------------------------------------------------------------------

    // set system inputs that flow to Manage Regulator Interface
    RegMRI.put_concrete_inputs(
      // The following uses Scala's parameter name argument protocol
      // format: api_<port_name> = <value>
      api_upper_desired_tempWstatus = inject_container.upperDesiredTempWStatus, // value from parameter
      api_lower_desired_tempWstatus = inject_container.lowerDesiredTempWStatus, // value from parameter
      api_regulator_mode = inject_container.mode,
      api_current_tempWstatus = inject_container.currentTempWStatus) // value from parameter

    // set system inputs that flow to Manage Regulator Mode
    RegMRM.put_current_tempWstatus(inject_container.currentTempWStatus)
    RegMRM.put_internal_failure(inject_container.internalFailure)

    // set system inputs that flow to Manage Heat Source
    RegMHS.put_current_tempWstatus(inject_container.currentTempWStatus)

    // ---------------- trace steps ----------------------

    // run to end of current hyper-period - and check outputs of selected components
    compute(ISZ(Hstep(1)))

    // ------------------ observe output -- build output observation vector ------------------
    //   ...eventually, auto-generated from higher-level specification of
    //      injection and observation vectors, along with temporal point
    //      declarations of when to inject and observe
    // ---------------------------------------------------------------------------------------

    val api_heat_control = RegMHS.get_api_heat_control()
    val output_container = Regulate_Subsystem_Outputs_Container(api_heat_control)

    // ------------------ check property of collected observations ------------------
    //   ...eventually, auto-generated from higher-level specification of
    //      property evaluation
    // ---------------------------------------------------------------------------------------

    //assert(prop(inject_container, output_container))
    val ret = (prop(inject_container, output_container))

    // gracefully take system down
    Art.finalizePhase(scheduler)

    return ret
  }

  //=============================================================
  //  Properties
  //
  //    Could potentially be generated from GUMBO-like contracts.
  //    Semantically, properties are predicates on observation vectors,
  //    similar to how GUMBOX functions are predicates on component
  //    input/output/state vectors.
  //
  //=============================================================

  //-----------------------------
  //  Helper functions
  //-----------------------------

  def sysProp_RegulatorInputErrorCondition(container: Regulate_Subsystem_Inputs_Container): B = {
    return (container.upperDesiredTempWStatus.status == ValueStatus.Invalid
      | container.lowerDesiredTempWStatus.status == ValueStatus.Invalid
      | container.currentTempWStatus.status == ValueStatus.Invalid)
  }

  def sysProp_RegulatorErrorCondition(container: Regulate_Subsystem_Inputs_Container): B = {
    return (sysProp_RegulatorInputErrorCondition(container)
      | container.internalFailure.value == T)
  }

  //----------------------------------------------
  //  Property:  CT < LDT implies Heat-Control ON
  //    [high-level]
  //      In Normal mode, and in the absence of error-triggering inputs,
  //      If current temp is less than lower desired, then heat control shall be ON
  //
  //    [Precise property spec] To be done...
  //      The precise property would be expressed in terms of
  //        - declared observations (subsystem inputs and subsystem outputs)
  //        - temporal observation points (beginning and ending of hyper-period)
  //
  //    Note: Properties should be traceable to system requirements
  //----------------------------------------------

  def sysProp_NormalModeHeatOn(inputs_container: Regulate_Subsystem_Inputs_Container,

                               outputs_container: Regulate_Subsystem_Outputs_Container): B = {
    val triggerCondition = (!sysProp_RegulatorErrorCondition(inputs_container)
      & inputs_container.mode == Regulator_Mode.Normal_Regulator_Mode
      & inputs_container.currentTempWStatus.value < inputs_container.lowerDesiredTempWStatus.value)
    val desiredCondition = (outputs_container.heat_control == On_Off.Onn)
    return (triggerCondition ->: desiredCondition)
  }

  //----------------------------------------------
  //  Property:  CT > UDT implies Heat-Control Off
  //    [high-level]
  //      In Normal mode, and in the absence of error-triggering inputs,
  //      If current temp is greater than upper desired, then heat control shall be OFF
  //----------------------------------------------

  def sysProp_NormalModeHeatOff(
                                 inputs_container: Regulate_Subsystem_Inputs_Container,

                                 outputs_container: Regulate_Subsystem_Outputs_Container): B = {
    val triggerCondition = (!sysProp_RegulatorErrorCondition(inputs_container)
      & inputs_container.mode == Regulator_Mode.Normal_Regulator_Mode
      & inputs_container.currentTempWStatus.value > inputs_container.upperDesiredTempWStatus.value)
    val desiredCondition = (outputs_container.heat_control == On_Off.Off)
    return (triggerCondition ->: desiredCondition)
  }

}
