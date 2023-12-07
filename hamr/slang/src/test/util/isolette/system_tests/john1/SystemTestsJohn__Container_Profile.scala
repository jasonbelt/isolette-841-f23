// #Sireum

package isolette.system_tests.john1

import org.sireum._
import isolette._

// do not edit, auto-generated by p.Creator

object SystemTestsJohn__Container_Profile {

  // a call to next may result in an AssertionError which is an indication that
  // SlangCheck was unable to satisfy a field's filter.  Consider using
  // nextH instead
  def next(profile: SystemTestsJohn__Container_Profile): SystemTestsJohn__Container = {
    return SystemTestsJohn__Container (
      lowerDesiredTempWStatus = profile.lowerDesiredTempWStatus.nextIsolette_Data_ModelTempWstatus_impl(),
      upperDesiredTempWStatus = profile.upperDesiredTempWStatus.nextIsolette_Data_ModelTempWstatus_impl(),
      currentTempWStatus = profile.currentTempWStatus.nextIsolette_Data_ModelTempWstatus_impl(),
      mode = profile.mode.nextIsolette_Data_ModelRegulator_ModeType(),
      internalFailure = profile.internalFailure.nextIsolette_Data_ModelFailure_Flag_impl()
    )
  }

  // nextH will return None() if SlangCheck is unable to satisfy a field's filter
  def nextH(profile: SystemTestsJohn__Container_Profile): Option[SystemTestsJohn__Container] = {
    return SystemTestsJohn__Container_ProfileI.next(profile)
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

@ext object SystemTestsJohn__Container_ProfileI {
  def next(profile: SystemTestsJohn__Container_Profile): Option[SystemTestsJohn__Container] = $
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
