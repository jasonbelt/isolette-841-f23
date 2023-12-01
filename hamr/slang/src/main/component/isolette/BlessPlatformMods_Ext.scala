package isolette

import art.ArtNative_Ext
import org.sireum.S64._
import org.sireum._

import java.io.PrintStream

object BlessPlatformMods_Ext {
  var blessStart: S64 = s64"0"
  var beginCapture = F

  def modLogStream(): Unit = {
    // create a PrintStream that encapsulates whatever stream ArtNative_Ext.log is currently using
    val logStream = new PrintStream(ArtNative_Ext.logStream) {

      override def println(x: Object): Unit = {
        // things sent to ArtNative_Ext.log will always be a JSON object
        // so parse out the key/value pairs
        val p = org.sireum.Json.Parser.create(x.toString)
        p.parseObjectBegin()
        var entries = HashSMap.empty[String, String]()
        do {
          val key = p.parseObjectKeys(ISZ[String]("log", "title", "msg", "time"))
          val value = p.parseString()
          entries = entries + key ~> value
        } while(p.parseObjectNext())

        if (!beginCapture && ops.StringOps(entries.get("msg").get).contains("Begin execution")) {
          beginCapture = T
          blessStart = S64(entries.get("time").get).get
        }

        if (beginCapture) {
          // add a custom entry
          entries = entries + "bless_time" ~> (S64(entries.get("time").get).get - blessStart).string

          // or you could replace the time entry via the following
          //entries = entries + "time" ~> (S64(entries.get("time").get).get - blessStart).string
        }
        // convert back to a JSON object and print it to the console's output stream
        super.println(st"{ ${(for (e <- entries.entries) yield st"\"${e._1}\" : \"${e._2}\"", ", ")} }".render)
      }
    }

    // have ArtNative_Ext's log method use the custom stream
    ArtNative_Ext.logStream = logStream
  }
}
