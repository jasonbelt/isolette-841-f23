// #Sireum

package isolette.system_tests.rst

import org.sireum._
import isolette._

// do not edit, auto-generated by s.SystemTestArtifactGen

object Regulate_Subsystem_Outputs_Container_Profile {

  // a call to next may result in an AssertionError which is an indication that
  // SlangCheck was unable to satisfy a field's filter.  Consider using
  // nextH instead
  def next(profile: Regulate_Subsystem_Outputs_Container_Profile): Regulate_Subsystem_Outputs_Container = {
    return Regulate_Subsystem_Outputs_Container (
      heat_control = profile.heat_control.nextIsolette_Data_ModelOn_OffType()
    )
  }

  // nextH will return None() if SlangCheck is unable to satisfy a field's filter
  def nextH(profile: Regulate_Subsystem_Outputs_Container_Profile): Option[Regulate_Subsystem_Outputs_Container] = {
    return Regulate_Subsystem_Outputs_Container_ProfileI.next(profile)
  }

  def getDefaultProfile: Regulate_Subsystem_Outputs_Container_Profile = {
    return Regulate_Subsystem_Outputs_Container_Profile (
      name = "Default Regulate_Subsystem_Outputs_Container_Profile Profile",
      numTests = 100,
      numTestVectorGenRetries = 100,

      heat_control = Regulate_Subsystem_Outputs_Container_Util.freshRandomLib
    )
  }
}

@ext object Regulate_Subsystem_Outputs_Container_ProfileI {
  def next(profile: Regulate_Subsystem_Outputs_Container_Profile): Option[Regulate_Subsystem_Outputs_Container] = $
}

@record class Regulate_Subsystem_Outputs_Container_Profile (
  var name: String,
  var numTests: Z,
  var numTestVectorGenRetries: Z,

  var heat_control : RandomLib
)
