package isolette.system_tests.rst

import org.sireum._
import isolette._

object Regulate_Subsystem_Outputs_Container_ProfileI_Ext {
  def next(profile: Regulate_Subsystem_Outputs_Container_Profile): Option[Regulate_Subsystem_Outputs_Container] = {
    try {
      return Some(Regulate_Subsystem_Outputs_Container (
        heat_control = profile.heat_control.nextIsolette_Data_ModelOn_OffType()))
    } catch {
      case e: AssertionError =>
        // SlangCheck was unable to satisfy a datatype's filter
        return None()
    }
  }
}