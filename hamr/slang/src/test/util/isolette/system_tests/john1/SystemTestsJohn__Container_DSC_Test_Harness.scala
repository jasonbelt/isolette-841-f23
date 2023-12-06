// #Sireum

package isolette.system_tests.john1

import org.sireum._
import isolette._
import org.sireum.Random.Impl.Xoshiro256

// Distributed SlangCheck Test Harness for isolette.system_tests.john1.SystemTestsJohn__Container

@msig trait SystemTestsJohn__Container_DSC_Test_Harness
  extends Random.Gen.TestRunner[isolette.system_tests.john1.SystemTestsJohn__Container] {

  override def toCompactJson(o: isolette.system_tests.john1.SystemTestsJohn__Container): String = {
    return isolette.JSON.fromsystem_testsjohn1SystemTestsJohn__Container(o, T)
  }

  override def fromJson(json: String): isolette.system_tests.john1.SystemTestsJohn__Container = {
    isolette.JSON.tosystem_testsjohn1SystemTestsJohn__Container(json) match {
      case Either.Left(o) => return o
      case Either.Right(msg) => halt(msg.string)
    }
  }

  // you'll need to provide implementations for the following:

  // override def next(): isolette.system_tests.john1.SystemTestsJohn__Container = {}

  // override def test(o: isolette.system_tests.john1.SystemTestsJohn__Container): B = { }
}
