package isolette.system_tests.john1

import org.sireum._
import isolette._

object SystemTestsJohn__Container_ProfileI_Ext {
  def next(profile: SystemTestsJohn__Container_Profile): Option[SystemTestsJohn__Container] = {
    try {
      return Some(SystemTestsJohn__Container (
        lowerDesiredTempWStatus = profile.lowerDesiredTempWStatus.nextIsolette_Data_ModelTempWstatus_impl(),
        upperDesiredTempWStatus = profile.upperDesiredTempWStatus.nextIsolette_Data_ModelTempWstatus_impl(),
        currentTempWStatus = profile.currentTempWStatus.nextIsolette_Data_ModelTempWstatus_impl(),
        mode = profile.mode.nextIsolette_Data_ModelRegulator_ModeType(),
        internalFailure = profile.internalFailure.nextIsolette_Data_ModelFailure_Flag_impl()))
    } catch {
      case e: AssertionError =>
        // SlangCheck was unable to satisfy a datatype's filter
        return None()
    }
  }
}
