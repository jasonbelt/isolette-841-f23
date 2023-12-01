## Initialization Properties

Given as an example:

* Outputs for Manage Heat Source are appropriately initialized

Zhaoxiang to do: 

* Outputs for Manage Regulator Interface are appropriately initialized

* Outputs for Manage Regulator Mode are appropriately initialized


## Compute Phase Properties

Given as example:

* [Heat control on] When the mode is normal,
  if the current temperature is less than the lower desired temperature,
  then the heat control shall be on.

Zhaoxiang To Do:  

* [Heat control off] When the mode is normal,
  if the current temperature is greater than the upper desired temperature,
  then the heat control shall be off.

* [Invalid temperature inputs lead to mode FAILED:] 
   - If the lower desired temperature status has an invalid status, then the heat control will be off
   - If the upper desired temperature status has an invalid status, then the heat control will be off
   - If the current temperature status has an invalid status, then the heat control will be off

To consider in the next phase of the work:

* [Heat control unchanged] When the mode is normal,
  if the current temperature is greater than or equal to the lower desired temperature,
  and the current temperature is less than or equal to the lower desired temperature,
  then the heater state is unchanged.

* [Heat control is off in FAILED state] When the subsystem mode is FAILED,
then heater state is OFF.


## To Discuss with Jason

* Stop() command - Jason mentioned that we should discuss this.  Do we need this? Should we try to modify the implementation so that it is not required?

* Observation theory [More of a John issue]: clarify the nature of observations that we are making.  Are we observing application port states or infrastructure port states?

* Do we have the capability to read the values of input ports in the post state?

* Consider the ability to test port input values after the communication dimension has been run after the initialization phase

* Deal with the timeout in Manage Regular Mode by moving to failure mode after a certain number of activitations of the MRM thread.   This is sufficient (don't need to 
have a notion of timer) because the thread is periodic and so we can calculate the time based on the number of activations.

* I believe we need to design some architecture for I/O devices, aligned with Jason's stuff, that allows system tests to set values coming from the environment (e.g., set the current temperature) and read values going to the environment (e.g., heat control)
 
 ## Inputs to Control in System Testing

 * Current Temperature (w status)

 * Upper Desired Temperature (w status)

 * Lower Desired Temperature (w status)

 * Regulator Internal Failure

 * Upper Alarm Temperature (w status)

 * Lower Alarm Temperature (w status)

 * Monitor Internal Failure

## Outputs to Monitor in System Testing

 * Heat Control

 * Alarm Control

 * Regular Status

 * Monitor Status

