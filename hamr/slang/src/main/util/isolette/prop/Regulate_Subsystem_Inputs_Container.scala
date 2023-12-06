// #Sireum
package isolette.prop

import org.sireum._
import isolette.Isolette_Data_Model._

@datatype class Regulate_Subsystem_Inputs_Container(lowerDesiredTempWStatus: TempWstatus_impl,
                                                    upperDesiredTempWStatus: TempWstatus_impl,

                                                    currentTempWStatus: TempWstatus_impl,

                                                    mode: Regulator_Mode.Type,

                                                    internalFailure: Failure_Flag_impl)

@datatype class Regulate_Subsystem_Outputs_Container(heat_control: On_Off.Type)
