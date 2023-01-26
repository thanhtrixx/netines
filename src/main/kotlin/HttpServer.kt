import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpServerCodec
import uti.Log

class HttpServer(
  private val port: Int = 8080
) : Log {

  private val bossGroup = NioEventLoopGroup(1)
  private val workerGroup = NioEventLoopGroup()
  private val channelInitializer = object : ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel) {
      val pipeline = ch.pipeline()
      pipeline.addLast("codec", HttpServerCodec())
      pipeline.addLast("handler", HttpServerHandler())
    }
  }

  fun start() {
    val bossGroup = NioEventLoopGroup(1)
    val workerGroup = NioEventLoopGroup()

    try {
      val b = ServerBootstrap()
      b.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel::class.java)
        .childHandler(channelInitializer)
        .option(ChannelOption.SO_BACKLOG, 128)
        .childOption(ChannelOption.SO_KEEPALIVE, true)

      val f = b.bind(port).sync()
      l.info("Server started at port $port")
      f.channel().closeFuture().sync()
    } finally {
      workerGroup.shutdownGracefully()
      bossGroup.shutdownGracefully()
    }
  }
}
