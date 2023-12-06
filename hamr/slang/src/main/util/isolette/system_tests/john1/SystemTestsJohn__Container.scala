// #Sireum
package isolette.system_tests.john1

import org.sireum._
import isolette.Isolette_Data_Model._

@datatype class SystemTestsJohn__Container(lowerDesiredTempWStatus: TempWstatus_impl,
                                           upperDesiredTempWStatus: TempWstatus_impl,

                                           currentTempWStatus: TempWstatus_impl,

                                           mode: Regulator_Mode.Type,

                                           internalFailure: Failure_Flag_impl)
