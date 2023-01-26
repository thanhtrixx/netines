import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.DefaultFullHttpResponse
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion
import io.netty.util.AsciiString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uti.Log

class HttpServerHandler : SimpleChannelInboundHandler<HttpRequest>(), Log {

  private val responseContent = "Hello from HttpServerHandler"
  private val CONTENT_TYPE = AsciiString.cached("Content-Type")
  private val CONTENT_TYPE_TEXT_PLAIN = AsciiString.cached("text/plain")
  private val CONTENT_LENGTH = AsciiString.cached("Content-Length")

  override fun channelRead0(ctx: ChannelHandlerContext, msg: HttpRequest) {
    CoroutineScope(Dispatchers.IO).launch {
      val response = DefaultFullHttpResponse(
        HttpVersion.HTTP_1_1,
        HttpResponseStatus.OK,
        Unpooled.wrappedBuffer(responseContent.toByteArray())
      )

      response.headers().set(CONTENT_TYPE, CONTENT_TYPE_TEXT_PLAIN)
      response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes())

      ctx.writeAndFlush(response)
      ctx.close()
    }
  }
}
