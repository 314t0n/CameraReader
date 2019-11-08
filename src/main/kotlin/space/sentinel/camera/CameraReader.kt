package space.sentinel.camera

import com.typesafe.config.Config
import org.bytedeco.ffmpeg.global.avutil.AV_PIX_FMT_BGR24
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.FrameGrabber
import org.slf4j.LoggerFactory.getLogger
import reactor.core.publisher.Flux
import reactor.core.publisher.SynchronousSink

class CameraReader(config: Config) : AutoCloseable {

    private val logger = getLogger(this::class.java)
    private val grabber: FrameGrabber = FFmpegFrameGrabber(config.getString("camera.path"))

    init {
        logger.debug("Setup camera.")
        grabber.format = config.getString("camera.format")
        grabber.pixelFormat = AV_PIX_FMT_BGR24
        grabber.start()
    }

    fun read(): Flux<Frame> {
        return Flux.generate { synchronousSink: SynchronousSink<Frame> ->
            synchronousSink.next((grabber.grab()))
        }
    }

    override fun close() {
        logger.debug("Close.")
    }
}