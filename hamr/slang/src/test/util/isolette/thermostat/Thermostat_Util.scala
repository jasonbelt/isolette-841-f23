// #Sireum

package isolette.thermostat

import org.sireum._
import isolette._

object Thermostat_Util {

  def injectIncomingSystemPorts(profile: Thermostat_PreState_Container_P): Unit = {
    val MMI_lat = Arch.isolette_single_sensor_Instance_thermostat_monitor_temperature_manage_monitor_interface.lower_alarm_tempWstatus
    val MMI_uat = Arch.isolette_single_sensor_Instance_thermostat_monitor_temperature_manage_monitor_interface.upper_alarm_tempWstatus

    val MMI_ct = Arch.isolette_single_sensor_Instance_thermostat_monitor_temperature_manage_monitor_interface.current_tempWstatus
    val MMM_ct = Arch.isolette_single_sensor_Instance_thermostat_monitor_temperature_manage_monitor_mode.current_tempWstatus
    val MA_ct = Arch.isolette_single_sensor_Instance_thermostat_monitor_temperature_manage_alarm.current_tempWstatus
    val MRM_ct = Arch.isolette_single_sensor_Instance_thermostat_regulate_temperature_manage_regulator_mode.current_tempWstatus
    val MHS_ct = Arch.isolette_single_sensor_Instance_thermostat_regulate_temperature_manage_heat_source.current_tempWstatus
    val MRI_ct = Arch.isolette_single_sensor_Instance_thermostat_regulate_temperature_manage_regulator_interface.current_tempWstatus

    val MRI_ldt = Arch.isolette_single_sensor_Instance_thermostat_regulate_temperature_manage_regulator_interface.lower_desired_tempWstatus
    val MRI_udt = Arch.isolette_single_sensor_Instance_thermostat_regulate_temperature_manage_regulator_interface.upper_desired_tempWstatus

    art.Art.insertInInfrastructurePort(MMI_lat.id, Isolette_Data_Model.TempWstatus_impl_Payload(profile.api_lower_alarm_tempWstatus))
    art.Art.insertInInfrastructurePort(MMI_uat.id, Isolette_Data_Model.TempWstatus_impl_Payload(profile.api_upper_alarm_tempWstatus))

    val ctp = Isolette_Data_Model.TempWstatus_impl_Payload(profile.api_current_tempWstatus)
    art.Art.insertInInfrastructurePort(MMI_ct.id, ctp)
    art.Art.insertInInfrastructurePort(MMM_ct.id, ctp)
    art.Art.insertInInfrastructurePort(MA_ct.id, ctp)
    art.Art.insertInInfrastructurePort(MRM_ct.id, ctp)
    art.Art.insertInInfrastructurePort(MHS_ct.id, ctp)
    art.Art.insertInInfrastructurePort(MRI_ct.id, ctp)

    art.Art.insertInInfrastructurePort(MRI_ldt.id, Isolette_Data_Model.TempWstatus_impl_Payload(profile.api_lower_desired_tempWstatus))
    art.Art.insertInInfrastructurePort(MRI_udt.id, Isolette_Data_Model.TempWstatus_impl_Payload(profile.api_upper_desired_tempWstatus))
  }
}
