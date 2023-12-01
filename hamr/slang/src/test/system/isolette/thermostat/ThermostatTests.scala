package isolette.thermostat

import org.sireum._
import art.scheduling.static._
import art.{Art, ArtNative_Ext}
import isolette.{Schedulers, SystemTestSuite}
import isolette._
// import isolette.Regulate.{Detect_Regulator_Failure_impl_thermostat_regulate_temperature_detect_regulator_failure_SystemTestAPI => nickname}
// import isolette.Monitor.{Manage_Monitor_Interface_impl_thermostat_monitor_temperature_manage_monitor_interface_SystemTestAPI => nickname}
// import isolette.Monitor.{Manage_Alarm_impl_thermostat_monitor_temperature_manage_alarm_SystemTestAPI => nickname}
// import isolette.Monitor.{Manage_Monitor_Mode_impl_thermostat_monitor_temperature_manage_monitor_mode_SystemTestAPI => nickname}
// import isolette.Monitor.{Detect_Monitor_Failure_impl_thermostat_monitor_temperature_detect_monitor_failure_SystemTestAPI => nickname}
// import isolette.Isolette.{operator_interface_thread_impl_operator_interface_oip_oit_SystemTestAPI => nickname}
// import isolette.Devices.{Temperature_Sensor_impl_temperature_sensor_cpi_thermostat_SystemTestAPI => nickname}
// import isolette.Devices.{Heat_Source_impl_heat_source_cpi_heat_controller_SystemTestAPI => nickname}

// This file will not be overwritten so is safe to edit

class ThermostatTests extends SystemTestSuite with Thermostat_GumboX_TestHarness_ScalaTest_Generator {

  // note: this is overriding SystemTestSuite's 'def scheduler: Scheduler'
  //       abstract method
  //var scheduler: StaticScheduler = Schedulers.getStaticSchedulerH(MNone())
  var scheduler: StaticScheduler = Schedulers.getStaticScheduler(
    Thermostat_Schedule.staticSchedule,
    Thermostat_Schedule.domainToBridgeIdMap,
    Thermostat_Schedule.threadNickNames,
    ISZCommandProvider(ISZ()))

  def compute(isz: ISZ[Command]): Unit = {
    scheduler = scheduler(commandProvider = ISZCommandProvider(isz :+ Stop()))

    Art.computePhase(scheduler)
  }

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

  // Suggestion: add the following import renamings of the components' SystemTestAPIs,
  //             replacing nickname with shortened versions that are easier to reference
  // import isolette.Regulate.{Manage_Regulator_Interface_impl_thermostat_regulate_temperature_manage_regulator_interface_SystemTestAPI => nickname}
  // import isolette.Regulate.{Manage_Heat_Source_impl_thermostat_regulate_temperature_manage_heat_source_SystemTestAPI => nickname}
  // import isolette.Regulate.{Manage_Regulator_Mode_impl_thermostat_regulate_temperature_manage_regulator_mode_SystemTestAPI => nickname}
  // import isolette.Regulate.{Detect_Regulator_Failure_impl_thermostat_regulate_temperature_detect_regulator_failure_SystemTestAPI => nickname}
  // import isolette.Monitor.{Manage_Monitor_Interface_impl_thermostat_monitor_temperature_manage_monitor_interface_SystemTestAPI => nickname}
  // import isolette.Monitor.{Manage_Alarm_impl_thermostat_monitor_temperature_manage_alarm_SystemTestAPI => nickname}
  // import isolette.Monitor.{Manage_Monitor_Mode_impl_thermostat_monitor_temperature_manage_monitor_mode_SystemTestAPI => nickname}
  // import isolette.Monitor.{Detect_Monitor_Failure_impl_thermostat_monitor_temperature_detect_monitor_failure_SystemTestAPI => nickname}
  // import isolette.Isolette.{operator_interface_thread_impl_operator_interface_oip_oit_SystemTestAPI => nickname}
  // import isolette.Devices.{Temperature_Sensor_impl_temperature_sensor_cpi_thermostat_SystemTestAPI => nickname}
  // import isolette.Devices.{Heat_Source_impl_heat_source_cpi_heat_controller_SystemTestAPI => nickname}

  var verbose: B = F

  //test("Thermostat Test") {
  //  Art.initializePhase(scheduler)


  //}

  this.verbose = T

  override val failOnUnsatPreconditions = T

  val maxTests = 100


  for (profile <- this.getProfiles_P) {
    testThermostatCB_Profile_P(profile)
  }

  def testThermostatCB_Profile_P(profile: Thermostat_Profile_P): Unit = {
    for (i <- 0 until maxTests) {
      val testName = s"Profile \"${profile.name}\": testThermostatCB_$i"
      val escapedTestName = s"Profile \\\"${profile.name}\\\": testThermostatCB_$i"

      this.registerTest(testName) {
        var retry: B = T
        var j: Z = 0

        while(j < profile.numTestVectorGenRetries && retry) {

          if (verbose && j > 0) {
            println(s"Retry $j:")
          }

          next(profile) match {
            case Some(container) =>

              // TODO: add ability to add contracts at system level.  Those probably
              //       should relate (be refined by) the thread (and maybe process)
              //       level contracts.  For now the idea would be to add contracts
              //       for the system. Assume/Require clauses seem straight forward,
              //       guarantee/ensures not so much

              if (!Thermostat_GumboX.compute_CEP_Pre_Container(container)) {
                // retry
              } else {

                Art.initializePhase(scheduler)

                Thermostat_Util.injectIncomingSystemPorts(container)

                compute(ISZ(Hstep(1)))

                // gracefully take system down
                Art.finalizePhase(scheduler)
                retry = F
              }
            case _ =>

          }
          j = j + 1
        }

        if (retry) {
          if (failOnUnsatPreconditions) {
            fail ("Unable to satisfy precondition")
          } else {
            cprintln(T, "Unable to satisfy precondition")
          }
        }
      }
    }
  }

  override def getProfiles_P: MSZ[Thermostat_Profile_P] = {
    val validRanges = getDefaultProfile_P

    // helper method to customize a random lib's F32 config
    def c32(low: Option[F32], high: Option[F32], ranLib: RandomLib): Config_F32 = {
      return ranLib.get_Config_F32(low = low, high = high)
    }

    validRanges.api_lower_alarm_tempWstatus.set_Config_F32(c32(
      low = Some(InitialValues.LOWER_ALARM_TEMPERATURE_LOWER_RANGE),
      high = Some(InitialValues.LOWER_ALARM_TEMPERATURE_UPPER_RANGE),
      ranLib = freshRandomLib))

    validRanges.api_upper_alarm_tempWstatus.set_Config_F32(c32(
      low = Some(InitialValues.UPPER_ALARM_TEMPERATURE_LOWER_RANGE),
      high = Some(InitialValues.UPPER_ALARM_TEMPERATURE_UPPER_RANGE),
      ranLib = freshRandomLib))

    validRanges.api_lower_desired_tempWstatus.set_Config_F32(c32(
      low = Some(InitialValues.LOWER_DESIRED_TEMPERATURE_LOWER_RANGE),
      high = Some(InitialValues.LOWER_DESIRED_TEMPERATURE_UPPER_RANGE),
      ranLib = freshRandomLib))

    validRanges.api_upper_desired_tempWstatus.set_Config_F32(c32(
      low = Some(InitialValues.UPPER_DESIRED_TEMPERATURE_LOWER_RANGE),
      high = Some(InitialValues.UPPER_DESIRED_TEMPERATURE_UPPER_RANGE),
      ranLib = freshRandomLib))

    validRanges.api_current_tempWstatus.set_Config_F32(c32(
      low = Some(92f),
      high = Some(104f),
      ranLib = freshRandomLib))

    return MSZ(validRanges)
  }
}