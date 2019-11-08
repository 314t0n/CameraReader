package space.sentinel

import org.bytedeco.javacv.CanvasFrame
import org.slf4j.LoggerFactory
import space.sentinel.camera.CameraReader
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.*
import javax.swing.WindowConstants.EXIT_ON_CLOSE

fun main(args: Array<String>) {
    val logger = LoggerFactory.getLogger("Sentinel")

    val config = ConfigLoaderFactory().load()

    val camera = CameraReader(config)

    try {

        val canvas = createCanvas {
            camera.close()
        }

        camera
                .read()
//                .delayElements(Duration.ofMillis(100))
                .map {
                    Optional.ofNullable(it).map { canvas.showImage(it) }
                }
//                .subscribeOn(Schedulers.parallel())
                .subscribe()

        while (true)
            Thread.sleep(500)

    } catch (ex: Exception) {
        logger.error(ex.message)
    }
}

fun createCanvas(shutdown: () -> Unit): CanvasFrame {
    val canvas = CanvasFrame("Sentinel Camera View Util")
    canvas.defaultCloseOperation = EXIT_ON_CLOSE
    canvas.addWindowListener(Shutdown(shutdown))
    return canvas
}

class Shutdown(val shutdown: () -> Unit) : WindowAdapter() {
    override fun windowClosing(e: WindowEvent?) {
        shutdown()
    }
}