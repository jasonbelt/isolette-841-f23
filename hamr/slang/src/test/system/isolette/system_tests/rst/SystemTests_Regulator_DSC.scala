package isolette.system_tests.rst

import org.sireum._

// do not edit, auto-generated by p.Creator

object RunIt extends App {
  override def main(args: ISZ[String]): Z = {
    System.setProperty("TEST_FAMILY_NAME", "Normal_Heat_On")

    val instance = new SystemTests_Regulator_DSC()

    // simulate DSC calling next
    val container = instance.next()

    // simulate DSC calling test
    val result = instance.test(container)

    return if (result) 0 else 1
  }
}

class SystemTests_Regulator_DSC
  extends SystemTests_Regulator_wSlangCheck
    with Regulate_Subsystem_Inputs_Container_DSC_Test_Harness {

  override def next(): isolette.system_tests.rst.Regulate_Subsystem_Inputs_Container = {
    val testRow = testMatrix.get(getTestId()).get
    return Regulate_Subsystem_Inputs_Container_Profile.next(testRow.profile)
  }

  override def test(o: isolette.system_tests.rst.Regulate_Subsystem_Inputs_Container): B = {
    val testId = getTestId()
    val testRow = testMatrix.get(testId).get

    println(genTestName(testId, testRow))

    disableLogsAndGuis()

    super.beforeEach()

    if (!testRow.preStateCheck(o)) {
      println(s"Didn't pass pre state check ${o}")

      return T
    } else {

      val result = testRow.testMethod.function(o, testRow.property.function)

      this.afterEach()

      println(s"  ${result}")

      return result
    }
  }

  def getTestId(): String = {
    Os.prop("TEST_FAMILY_NAME") match {
      case Some(v) => return v
      case _ =>
        Os.env("TEST_FAMILY_NAME") match {
          case Some(v) => return v
          case _ =>
        }
    }
    halt("TEST_FAMILY_NAME not defined")
  }

  override def string: String = toString

  override def $clonable: Boolean = F

  override def $clonable_=(b: Boolean): org.sireum.$internal.MutableMarker = this

  override def $owned: Boolean = F

  override def $owned_=(b: Boolean): org.sireum.$internal.MutableMarker = this

  override def $clone: org.sireum.$internal.MutableMarker = this
}