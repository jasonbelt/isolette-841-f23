// #Sireum

package isolette.system_tests.john1

import org.sireum._
import isolette._


object SystemTestsJohn__Container_Profile {
  def next(profile: SystemTestsJohn__Container_Profile): SystemTestsJohn__Container = {
    return SystemTestsJohn__Container (
      lowerDesiredTempWStatus = profile.lowerDesiredTempWStatus.nextIsolette_Data_ModelTempWstatus_impl(),
      upperDesiredTempWStatus = profile.upperDesiredTempWStatus.nextIsolette_Data_ModelTempWstatus_impl(),
      currentTempWStatus = profile.currentTempWStatus.nextIsolette_Data_ModelTempWstatus_impl(),
      mode = profile.mode.nextIsolette_Data_ModelRegulator_ModeType(),
      internalFailure = profile.internalFailure.nextIsolette_Data_ModelFailure_Flag_impl()
    )
  }

  def getDefaultProfile: SystemTestsJohn__Container_Profile = {
    return SystemTestsJohn__Container_Profile (
      name = "Default SystemTestsJohn__Container_Profile Profile",
      numTests = 100,
      numTestVectorGenRetries = 100,

      lowerDesiredTempWStatus = SystemTestsJohn__Container_Util.freshRandomLib,
      upperDesiredTempWStatus = SystemTestsJohn__Container_Util.freshRandomLib,
      currentTempWStatus = SystemTestsJohn__Container_Util.freshRandomLib,
      mode = SystemTestsJohn__Container_Util.freshRandomLib,
      internalFailure = SystemTestsJohn__Container_Util.freshRandomLib
    )
  }
}

@record class SystemTestsJohn__Container_Profile (
  var name: String,
  var numTests: Z,
  var numTestVectorGenRetries: Z,

  var lowerDesiredTempWStatus : RandomLib,
  var upperDesiredTempWStatus : RandomLib,
  var currentTempWStatus : RandomLib,
  var mode : RandomLib,
  var internalFailure : RandomLib
)
