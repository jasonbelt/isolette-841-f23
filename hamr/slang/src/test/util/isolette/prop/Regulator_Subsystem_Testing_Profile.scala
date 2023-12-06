// #Sireum
package isolette.prop

import org.sireum._
import isolette.RandomLib

@record class Regulator_Subsystem_Testing_Profile(
                                       val name: String,
                                       val numTests: Z, // number of tests to generate
                                       var numTestVectorGenRetries: Z, // number of test vector generation retries

                                       var lowerDesiredTempWStatus: RandomLib,
                                       var upperDesiredTempWStatus: RandomLib,

                                       var currentTempWStatus: RandomLib,

                                       var mode: RandomLib,

                                       var internalFailure: RandomLib)
