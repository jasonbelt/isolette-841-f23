// #Sireum

package isolette.thermostat

import org.sireum._
import isolette._

object Thermostat_GumboX {

  /** Compute Entrypoint Contract
    *
    * assumes lower_is_not_higher_than_upper
    * @param api_lower_desired_tempWstatus incoming data port
    * @param api_upper_desired_tempWstatus incoming data port
    */
  @strictpure def compute_spec_lower_is_not_higher_than_upper_assume(api_lower_desired_tempWstatus: Isolette_Data_Model.TempWstatus_impl,
                                                                     api_upper_desired_tempWstatus: Isolette_Data_Model.TempWstatus_impl): B =
    api_lower_desired_tempWstatus.value <= api_upper_desired_tempWstatus.value

  /** CEP-T-Assm: Top-level assume contracts for manage_regulator_interface's compute entrypoint
    *
    * @param api_lower_desired_tempWstatus incoming data port
    * @param api_upper_desired_tempWstatus incoming data port
    */
  @strictpure def compute_CEP_T_Assm(
                                      api_lower_desired_tempWstatus: Isolette_Data_Model.TempWstatus_impl,
                                      api_upper_desired_tempWstatus: Isolette_Data_Model.TempWstatus_impl): B =
    compute_spec_lower_is_not_higher_than_upper_assume(api_lower_desired_tempWstatus, api_upper_desired_tempWstatus)

  /** CEP-Pre: Compute Entrypoint Pre-Condition for manage_regulator_interface
    *
    * @param api_current_tempWstatus incoming data port
    * @param api_lower_desired_tempWstatus incoming data port
    * @param api_regulator_mode incoming data port
    * @param api_upper_desired_tempWstatus incoming data port
    */
  @strictpure def compute_CEP_Pre(api_lower_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_impl,
                                  api_upper_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_impl,

                                   api_current_tempWstatus: Isolette_Data_Model.TempWstatus_impl,

                                   api_lower_desired_tempWstatus: Isolette_Data_Model.TempWstatus_impl,
                                   api_upper_desired_tempWstatus: Isolette_Data_Model.TempWstatus_impl): B =
    (// CEP-Assm: assume clauses of MRI & MHS
      compute_CEP_T_Assm(api_lower_desired_tempWstatus, api_upper_desired_tempWstatus) &


        // assumptions from MA adapted for incoming system ports
        api_upper_alarm_tempWstatus.value - api_lower_alarm_tempWstatus.value >= 1.0f &
        96.0f <= api_lower_alarm_tempWstatus.value & api_lower_alarm_tempWstatus.value <= 101.0f &
        97.0f <= api_upper_alarm_tempWstatus.value & api_upper_desired_tempWstatus.value <= 102.0f
      )

  /** CEP-Pre: Compute Entrypoint Pre-Condition for manage_regulator_interface via container
    *
    * @param pre Container holding the value of incoming ports and the pre-state values of state variables
    */
  @strictpure def compute_CEP_Pre_Container(pre: Thermostat_PreState_Container_P): B =
    compute_CEP_Pre(
      api_lower_alarm_tempWstatus = pre.api_lower_alarm_tempWstatus,
      api_upper_alarm_tempWstatus = pre.api_upper_alarm_tempWstatus,

      api_current_tempWstatus = pre.api_current_tempWstatus,

      api_lower_desired_tempWstatus = pre.api_lower_desired_tempWstatus,
      api_upper_desired_tempWstatus = pre.api_upper_desired_tempWstatus)
}
