package isolette.system_tests.rst

import org.sireum._
import isolette._

// do not edit, auto-generated by s.SystemTestArtifactGen

object Regulate_Subsystem_Outputs_Container_UtilI_Ext {
  def getSeed: U64 = {
    val rand = new java.util.Random()
    rand.setSeed(rand.nextLong())
    return U64(rand.nextLong())
  }
}
