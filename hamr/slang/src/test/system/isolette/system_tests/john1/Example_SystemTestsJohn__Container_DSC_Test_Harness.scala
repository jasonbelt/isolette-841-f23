package isolette.system_tests.john1

import org.sireum._

class Example_SystemTestsJohn__Container_DSC_Test_Harness
  extends Object
  with SystemTestsJohn__Container_DSC_Test_Harness {

  override def next(): isolette.system_tests.john1.SystemTestsJohn__Container = {
    halt("FYTD")
  }

  override def test(o: isolette.system_tests.john1.SystemTestsJohn__Container): B = {
    halt("FYTD")
  }


  override def string: String = toString

  override def $clonable: Boolean = F

  override def $clonable_=(b: Boolean): org.sireum.$internal.MutableMarker = this

  override def $owned: Boolean = F

  override def $owned_=(b: Boolean): org.sireum.$internal.MutableMarker = this

  override def $clone: org.sireum.$internal.MutableMarker = this
}
