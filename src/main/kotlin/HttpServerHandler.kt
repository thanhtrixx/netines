import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.DefaultFullHttpResponse
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uti.Log

class HttpServerHandler : SimpleChannelInboundHandler<HttpRequest>(), Log {

  override fun channelRead0(ctx: ChannelHandlerContext, request: HttpRequest) {
    CoroutineScope(Dispatchers.IO).launch {
      val response = DefaultFullHttpResponse(
        HttpVersion.HTTP_1_1,
        HttpResponseStatus.OK,
        Unpooled.wrappedBuffer("Hello".toByteArray())
      )
      response.headers().set("Content-Type", "text/plain")
      response.headers().set("Content-Length", response.content().readableBytes())
      ctx.writeAndFlush(response)
      ctx.close()
    }
  }
}
