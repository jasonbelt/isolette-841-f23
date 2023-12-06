package isolette.system_tests.john1

import art.ArtNative_Ext
import isolette.{Config_F32, InitialValues, RandomLib}
import org.sireum.Random.Gen64
import org.sireum.Random.Impl.Xoshiro256
import org.sireum._

// This file will not be overwritten so is safe to edit

class SystemTestsJohn_wSlangCheck extends SystemTestsJohn {

  val maxTests = 100
  var verbose: B = F

  for (profile <- getProfiles) {
    testProfile(profile)
  }

  def testProfile(profile: SystemTestsJohn_Profile_P): Unit = {
    for (i <- 0 until maxTests) {
      val testName = s"Profile \"${profile.name}\": John_$i"
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
                regulator1HPTestSchemaInject(
                  container = container,
                  prop = sysProp_NormalModeHeatOn _
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

  def getDefaultProfile: SystemTestsJohn_Profile_P = {
    return SystemTestsJohn_Profile_P(
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

  def getProfiles: MSZ[SystemTestsJohn_Profile_P] = {
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


  def next(profile: SystemTestsJohn_Profile_P): Option[SystemTestsJohn__Container] = {
    try {
      val lowerDesiredTempWStatus = profile.lowerDesiredTempWStatus.nextIsolette_Data_ModelTempWstatus_impl()
      val upperDesiredTempWStatus = profile.upperDesiredTempWStatus.nextIsolette_Data_ModelTempWstatus_impl()
      val currentTempWStatus = profile.upperDesiredTempWStatus.nextIsolette_Data_ModelTempWstatus_impl()
      val mode = profile.mode.nextIsolette_Data_ModelRegulator_ModeType()
      val internalFailure = profile.internalFailure.nextIsolette_Data_ModelFailure_Flag_impl()

      return Some(SystemTestsJohn__Container(lowerDesiredTempWStatus, upperDesiredTempWStatus, currentTempWStatus, mode, internalFailure))
    } catch {
      case e: AssertionError =>
        // SlangCheck was unable to satisfy a datatype's filter
        return None()
    }
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

}
