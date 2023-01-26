fun main(args: Array<String>) {

  val port = if (args.isNotEmpty()) args[0].toInt() else 8080

  HttpServer(port).start()
}
