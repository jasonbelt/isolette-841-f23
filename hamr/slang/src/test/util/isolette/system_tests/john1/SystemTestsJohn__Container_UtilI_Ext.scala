package isolette.system_tests.john1

import org.sireum._
import isolette._

object SystemTestsJohn__Container_UtilI_Ext {
  def getSeed: U64 = {
    val rand = new java.util.Random()
    rand.setSeed(rand.nextLong())
    return U64(rand.nextLong())
  }
}
