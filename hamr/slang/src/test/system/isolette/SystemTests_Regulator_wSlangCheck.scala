package isolette

import art.scheduling.static._
import art.{Art, ArtNative_Ext}
import isolette.Isolette_Data_Model._
import isolette.Regulate.{Manage_Heat_Source_impl_thermostat_regulate_temperature_manage_heat_source_SystemTestAPI => RegMHS, Manage_Regulator_Interface_impl_thermostat_regulate_temperature_manage_regulator_interface_SystemTestAPI => RegMRI, Manage_Regulator_Mode_impl_thermostat_regulate_temperature_manage_regulator_mode_SystemTestAPI => RegMRM}
import isolette.prop.{GumboX_SystemTest, Regulator_Subsystem_Testing_Profile, Regulate_Subsystem_Inputs_Container, Regulate_Subsystem_Outputs_Container}
import org.sireum.Random.Gen64
import org.sireum.Random.Impl.Xoshiro256
import org.sireum._

// This file will not be overwritten so is safe to edit

class SystemTests_Regulator_wSlangCheck extends SystemTestSuite {

  //===========================================================
  //  ScalaTest Boiler Plate
  //
  //     -- looks like this would be auto-generated in a separate file
  //        which this class would inherit from
  //===========================================================

  override def beforeEach(): Unit = {
    // uncomment the following to disable the various guis
    System.setProperty("java.awt.headless", "true")

    // uncomment the following to suppress (or potentially redirect) ART's log stream
    art.ArtNative_Ext.logStream = new java.io.PrintStream(new java.io.OutputStream {
      override def write(b: Int): Unit = {}
    })

    // suppress the log stream
    ArtNative_Ext.logStream = new java.io.PrintStream(new java.io.OutputStream {
      override def write(b: Int): Unit = {}
    })

    // uncomment the following to suppress (or potentially redirect) the static scheduler's log stream
    art.scheduling.static.StaticSchedulerIO_Ext.logStream = new java.io.PrintStream(new java.io.OutputStream {
      override def write(b: Int): Unit = {}
    })

    super.beforeEach()
  }

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

  //===========================================================
  //  S l a n g   C h e c k    Infrastructure
  //===========================================================

  val maxTests = 100
  var verbose: B = F

  // Refactor:  Read list of Profile, Property pairs (instead of just the profile),
  //   or <Test Family/Scenario Name>, Property, Profile -- triple

  for (profile <- getProfiles) {
    // testProfile(profile)
    regulator_1HP_schema_runner("Normal Heat On", profile,sysProp_NormalModeHeatOn _)
    regulator_1HP_schema_runner("Normal Heat Off", profile,sysProp_NormalModeHeatOff _)
  }

  //------------------------------------------------
  //  Regulator  1HP  Inject/Observe Schema Runner
  //
  //   ..eventually auto-generated from a description
  //     of injections/observations at temporal trace points
  //------------------------------------------------

  def regulator_1HP_schema_runner(testfamilyname: String,
                                  profile: Regulator_Subsystem_Testing_Profile,
                                  prop: (Regulate_Subsystem_Inputs_Container, Regulate_Subsystem_Outputs_Container) =>
                                       B): Unit = {

    val propName: String = "Heat Control"  // ToDo: refactor so that this is passed as a parameter, or as a field in property

    for (i <- 0 until maxTests) {
      val testName = s"1HP: ${testfamilyname}: ${propName}:  Profile \"${profile.name}\"_$i"
      this.registerTest(testName) {
        var retry: B = T
        var j: Z = 0

        while (j < profile.numTestVectorGenRetries && retry) {
          if (verbose && j > 0) {
            println(s"Retry $j:")
          }

          next(profile) match {
            case Some(container) =>
              if (!GumboX_SystemTest.system_Pre_Container(container)) {
                // retry
              } else {
                Regulator_1HP_script_schema(
                  inject_container = container,
                  prop = prop
                )
                retry = F
              }
            case _ =>
          }
          j = j + 1
        }
      }
    }
  }

  def seedGen: Gen64 = Random.Gen64Impl(Xoshiro256.create)

  def freshRandomLib: RandomLib = RandomLib(Random.Gen64Impl(Xoshiro256.createSeed(seedGen.genU64())))

  //------------------------------------------------
  //  Test Vector Profiles
  //
  //   ..eventually auto-generated from a descriptor
  //     of injection vector
  //------------------------------------------------

  def getDefaultProfile: Regulator_Subsystem_Testing_Profile = {
    return Regulator_Subsystem_Testing_Profile(
      name = "Default Profile",
      numTests = 100,
      numTestVectorGenRetries = 100,

      lowerDesiredTempWStatus = freshRandomLib,
      upperDesiredTempWStatus = freshRandomLib,

      currentTempWStatus = freshRandomLib,

      mode = freshRandomLib,

      internalFailure = freshRandomLib
    )
  }

  //-----------------------------------------------------------
  //  (Refactor into one method for each profile, then a
  //   "profile list" that indicates a particular set of
  //   profiles to run.
  //
  //   Developer-designed profile (derived from auto-generated template)
  //-----------------------------------------------------------

  def getProfiles: MSZ[Regulator_Subsystem_Testing_Profile] = {
    def c32(low: Option[F32], high: Option[F32], ranLib: RandomLib): Config_F32 = {
      return ranLib.get_Config_F32(low = low, high = high)
    }

    val validRanges = getDefaultProfile
    validRanges.lowerDesiredTempWStatus.set_Config_F32(c32(
      low = Some(InitialValues.LOWER_DESIRED_TEMPERATURE_LOWER_RANGE),
      high = Some(InitialValues.LOWER_DESIRED_TEMPERATURE_UPPER_RANGE),
      ranLib = freshRandomLib))

    validRanges.upperDesiredTempWStatus.set_Config_F32(c32(
      low = Some(InitialValues.UPPER_DESIRED_TEMPERATURE_LOWER_RANGE),
      high = Some(InitialValues.UPPER_DESIRED_TEMPERATURE_UPPER_RANGE),
      ranLib = freshRandomLib
    ))

    // tighten up currentTemp so that there's a better chance to get within MA's
    // 0.5 tolerance
    validRanges.currentTempWStatus.set_Config_F32(c32(
      low = Some(90f),
      high = Some(110f),
      ranLib = freshRandomLib))

    //return MSZ(getDefaultProfile)
    return MSZ(validRanges)
  }

  //--------------------------------------------
  //  Looks like this could be auto-generated from injection vector description ??
  //--------------------------------------------

  def next(profile: Regulator_Subsystem_Testing_Profile): Option[Regulate_Subsystem_Inputs_Container] = {
    try {
      val lowerDesiredTempWStatus = profile.lowerDesiredTempWStatus.nextIsolette_Data_ModelTempWstatus_impl()
      val upperDesiredTempWStatus = profile.upperDesiredTempWStatus.nextIsolette_Data_ModelTempWstatus_impl()
      val currentTempWStatus = profile.upperDesiredTempWStatus.nextIsolette_Data_ModelTempWstatus_impl()
      val mode = profile.mode.nextIsolette_Data_ModelRegulator_ModeType()
      val internalFailure = profile.internalFailure.nextIsolette_Data_ModelFailure_Flag_impl()

      return Some(Regulate_Subsystem_Inputs_Container(lowerDesiredTempWStatus, upperDesiredTempWStatus, currentTempWStatus, mode, internalFailure))
    } catch {
      case e: AssertionError =>
        // SlangCheck was unable to satisfy a datatype's filter
        return None()
    }
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
                           prop: (Regulate_Subsystem_Inputs_Container, Regulate_Subsystem_Outputs_Container) =>
                             B
                          ): Unit = {

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

    assert(prop(inject_container, output_container))

    // gracefully take system down
    Art.finalizePhase(scheduler)
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
