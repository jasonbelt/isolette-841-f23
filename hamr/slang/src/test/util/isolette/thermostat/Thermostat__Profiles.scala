// #Sireum
package isolette.thermostat

import org.sireum._
import isolette._

@record class Thermostat_Profile_P(
  val name: String,
  val numTests: Z, // number of tests to generate
  var numTestVectorGenRetries: Z, // number of test vector generation retries

  var api_lower_alarm_tempWstatus: RandomLib,
  var api_upper_alarm_tempWstatus: RandomLib,

  var api_current_tempWstatus: RandomLib,

  var api_lower_desired_tempWstatus: RandomLib,
  var api_upper_desired_tempWstatus: RandomLib)

