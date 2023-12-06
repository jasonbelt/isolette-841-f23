package isolette.system_tests.rst

import org.sireum._
import isolette._

object Regulate_Subsystem_Inputs_Container_ProfileI_Ext {
  def next(profile: Regulate_Subsystem_Inputs_Container_Profile): Option[Regulate_Subsystem_Inputs_Container] = {
    try {
      return Some(Regulate_Subsystem_Inputs_Container (
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
