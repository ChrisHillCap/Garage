name := "MacromeGarage"
 
version := "1.0" 
      
lazy val `macromegarage` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
scalaVersion := "2.11.11"

libraryDependencies ++= Seq( jdbc , cache , ws , specs2 % Test )

libraryDependencies ++= Seq(
  ws,
  "org.jsoup"              %  "jsoup"              % "1.11.2",
  "org.jsoup"              %  "jsoup"              % "1.11.2" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1"  % Test,
  "org.mockito"            %  "mockito-core"       % "2.13.0" % Test
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )
//unmanagedResourceDirectories in Assets <+= baseDirectory (_ / "target/web/uploads")

      