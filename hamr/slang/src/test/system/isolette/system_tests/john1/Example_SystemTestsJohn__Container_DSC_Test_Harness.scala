package isolette.system_tests.john1

import isolette.Isolette_Data_Model.On_Off
import org.sireum._

// do not edit, auto-generated by p.Creator

object Example_SystemTestsJohn__Container_DSC_Test_Harness extends App {
  override def main(args: ISZ[String]): Z = {
    var result = T
    while (result) {
      val c = new Example_SystemTestsJohn__Container_DSC_Test_Harness
      val container = c.next()
      result = c.test(container)
      println(result)
    }
    return 0
  }
}
class Example_SystemTestsJohn__Container_DSC_Test_Harness
  extends SystemTestsJohn__Container_DSC_Test_Harness {

  val profiles: Map[String, SystemTestsJohn__Container_Profile] = Map.empty ++ IS(
    "default" ~> SystemTestsJohn__Container_Profile.getDefaultProfile
  )

  val props: Map[String, (Any, Any) => B] = Map.empty ++ IS(
    "default" ~>
      ((x: Any, y: Any) => T).asInstanceOf[(Any, Any) => B],
    "sysProp_NormalModeHeatOn" ~>
      (SystemTestsJohn.sysProp_NormalModeHeatOn _).asInstanceOf[(Any, Any) => B]
  )
  override def next(): isolette.system_tests.john1.SystemTestsJohn__Container = {
    return SystemTestsJohn__Container_Profile.next(profiles.get("default").get)
  }

  override def test(o: isolette.system_tests.john1.SystemTestsJohn__Container): B = {
    val prop = props.get("sysProp_NormalModeHeatOn").get

    val result = prop(o, On_Off.Off)

    return result
  }


  override def string: String = toString

  override def $clonable: Boolean = F

  override def $clonable_=(b: Boolean): org.sireum.$internal.MutableMarker = this

  override def $owned: Boolean = F

  override def $owned_=(b: Boolean): org.sireum.$internal.MutableMarker = this

  override def $clone: org.sireum.$internal.MutableMarker = this
}
