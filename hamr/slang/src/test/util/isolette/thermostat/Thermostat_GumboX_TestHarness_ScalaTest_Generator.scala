package isolette.thermostat

import org.sireum._
import org.sireum.Random.Gen64
import org.sireum.Random.Impl.Xoshiro256
import isolette._

trait Thermostat_GumboX_TestHarness_ScalaTest_Generator {
  // return a directory path if you want to serialize the report
  def serializeReportPath: Option[Os.Path] = None()

  def failOnUnsatPreconditions: B = F

  def seedGen: Gen64 = Random.Gen64Impl(Xoshiro256.create)

  def freshRandomLib: RandomLib = RandomLib(Random.Gen64Impl(Xoshiro256.createSeed(seedGen.genU64())))

  def getProfiles_P: MSZ[Thermostat_Profile_P]

  def getDefaultProfile_P: Thermostat_Profile_P = {
    return Thermostat_Profile_P(
      name = "Default Port Profile",
      numTests = 100,
      numTestVectorGenRetries = 100,

      api_lower_alarm_tempWstatus = freshRandomLib,
      api_upper_alarm_tempWstatus = freshRandomLib,

      api_current_tempWstatus = freshRandomLib,

      api_lower_desired_tempWstatus = freshRandomLib,
      api_upper_desired_tempWstatus = freshRandomLib
    )
  }

  def next(profile: Thermostat_Profile_P): Option[Thermostat_PreState_Container_P] = {
    try {
      val api_lower_alarm_tempWstatus = profile.api_lower_alarm_tempWstatus.nextIsolette_Data_ModelTempWstatus_impl()
      val api_upper_alarm_tempWstatus = profile.api_upper_alarm_tempWstatus.nextIsolette_Data_ModelTempWstatus_impl()

      val api_current_tempWstatus = profile.api_current_tempWstatus.nextIsolette_Data_ModelTempWstatus_impl()

      val api_lower_desired_tempWstatus = profile.api_lower_desired_tempWstatus.nextIsolette_Data_ModelTempWstatus_impl()
      val api_upper_desired_tempWstatus = profile.api_upper_desired_tempWstatus.nextIsolette_Data_ModelTempWstatus_impl()

      return Some(Thermostat_PreState_Container_P(
        api_lower_alarm_tempWstatus = api_lower_alarm_tempWstatus,
        api_upper_alarm_tempWstatus = api_upper_alarm_tempWstatus,

        api_current_tempWstatus = api_current_tempWstatus,

        api_lower_desired_tempWstatus = api_lower_desired_tempWstatus,
        api_upper_desired_tempWstatus = api_upper_desired_tempWstatus))
    } catch {
      case e: AssertionError =>
        // SlangCheck was unable to satisfy a datatype's filter
        return None()
    }
  }
}
