package isolette

import org.sireum._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.{BeforeAndAfterEach, OneInstancePerTest}
import org.sireum.$internal.MutableMarker

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

abstract class SystemTestSuite extends AnyFunSuite with OneInstancePerTest with BeforeAndAfterEach with SystemTestSuiteSlang {

  override protected def beforeEach(): Unit = {
    beforeEachSlang()
  }

  override protected def afterEach(): Unit = {
    afterEachSlang()
  }

  override def string: String = toString()

  override def $clonable: Boolean = false

  override def $clonable_=(b: Boolean): MutableMarker = this

  override def $owned: Boolean = false

  override def $owned_=(b: Boolean): MutableMarker = this

  override def $clone: MutableMarker = this
}