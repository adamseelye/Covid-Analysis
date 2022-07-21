import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object ui {
  // Command Line interfsce
  // Use in case web interface is not feasible

  // AS - moved spark connection here. deleted sparkQueries object.
  def cxn(): Unit = {
    val spark = SparkSession
      .builder
      .appName("Spark Queries")
      .master("spark://<hostaddr>:7077") // will prepare a server to use here
      .config("spark.master", "local[*]")
      .config("spark.driver.allowMultipleContexts", "true")
      .enableHiveSupport()
      .getOrCreate()
    Logger.getLogger("org").setLevel(Level.ERROR)
    println("created spark session")
  }

  // main function that will start the application
  def main (args: Array[String]): Unit = {
    cxn()
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
