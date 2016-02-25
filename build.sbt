scalaVersion := "2.11.7"

libraryDependencies ++= {
  Seq(
    "com.typesafe.akka"   %% "akka-http-experimental"             % "2.4.2"   % "compile",
    "com.typesafe.akka"   %% "akka-http-spray-json-experimental"  % "2.4.2"   % "compile",
    "org.reactivemongo"   %% "reactivemongo"                      % "0.11.10" % "compile",
    "org.slf4j"           %  "slf4j-api"                          % "1.7.16"  % "compile",
    "org.scalatest"       %% "scalatest"                          % "2.2.6"   % "test"
  )
}
