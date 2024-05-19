package example

import zio.{ ZIO, ZLayer, ZIOAppDefault }
import zio.http.{ Middleware, Server, Routes }
import java.io.{ File }

object TestApp extends ZIOAppDefault:

    val staticContent = Routes.empty @@ Middleware.serveResources(zio.http.Path.empty / "resources")
                                     @@ Middleware.serveDirectory(zio.http.Path.empty / "directory", File("./src/main/resources"))

    val config = ZLayer.succeed(
        Server.Config.default.copy(
            responseCompression = Some(Server.Config.ResponseCompressionConfig.default),
        )
    )

    override def run = 
        Server
            .install(staticContent)
            .flatMap(port => ZIO.log(s"running on port: ${port}") *> ZIO.never)
            .provide(Server.live, config)
