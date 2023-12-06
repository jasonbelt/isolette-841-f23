// #Sireum

package isolette.system_tests.john1

import org.sireum._
import isolette._
import org.sireum.Random.Impl.Xoshiro256

object SystemTestsJohn__Container_Util {

  def freshRandomLib: RandomLib = {
    return RandomLib(Random.Gen64Impl(Xoshiro256.createSeed(SystemTestsJohn__Container_UtilI.getSeed)))
  }
}

@ext object SystemTestsJohn__Container_UtilI {
  def getSeed: U64 = $
}
