object ui {
  def greeting(): Unit = {
    // this will either be the main ui or the CLI interface
    // with the main ui on the web
    println("COVID ANALYSIS")
    println("")
    println("Please visit <hostname:port> in your web browser")
  }

  // Command Line interfsce
  // Use in case web interface is not feasible

  // main function that will start the application
  def main (args: Array[String]): Unit = {
    sparkQueries.cxn()
    mainMenu()
  }

  // main menu
  def mainMenu(): Unit = {
    // add menu options where appropriat

    val input = scala.io.StdIn.readLine()
    input match {
      // make case statement based on input
      // statement to call queries function
      // will set up a case statement when or if that time comes

    queriesMenu()

    }

  }

  def queriesMenu(): Unit = {
    println("Please select a query")
    // add menu options where appropriate

    val input = scala.io.StdIn.readLine()
    input match {
      // create case statements for this menu
    }
  }
}
