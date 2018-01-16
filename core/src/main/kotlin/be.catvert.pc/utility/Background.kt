package be.catvert.pc.utility

import be.catvert.pc.Log
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.JsonReader
import com.fasterxml.jackson.annotation.JsonTypeInfo
import glm_.func.common.clamp
import java.io.FileReader
import kotlin.math.roundToInt

enum class BackgroundType {
    Standard, Parallax
}

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.WRAPPER_ARRAY)
sealed class Background(val type: BackgroundType) : Renderable

class StandardBackground(val backgroundFile: FileWrapper) : Background(BackgroundType.Standard) {
    private val background = ResourceManager.getTexture(backgroundFile.get())

    override fun render(batch: Batch) {
        batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    }
}

class ParallaxBackground(val parallaxDataFile: FileWrapper) : Background(BackgroundType.Parallax) {
    private data class Layer(val layer: TextureRegion, val applyYOffset: Boolean, val speed: Float, var x: Float = 0f)

    private val layers = mutableListOf<Layer>()

    private var lastCameraPos: Vector3? = null

    private var xOffset = 0
    private var yOffset = 0f

    init {
        try {
            val root = JsonReader().parse(FileReader(parallaxDataFile.get().path()))

            root["layers"].forEach {
                val layerFile = it.getString("file")
                val applyYOffset = it.getBoolean("applyYOffset")
                val speed = it.getFloat("speed")
                layers += Layer(TextureRegion(ResourceManager.getTexture(parallaxDataFile.get().parent().child(layerFile)).apply { setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat) }), applyYOffset, speed)
            }
        } catch (e: Exception) {
            Log.error(e) { "Une erreur s'est produite lors du chargement d'un fond d'écran parallax !" }
        }
    }

    fun updateXOffset(plusX: Int) {
        xOffset = Math.max(0, xOffset + plusX)

        layers.forEach {
            it.layer.regionWidth = Gdx.graphics.width + xOffset
        }
    }

    fun reset() {
        xOffset = 0
        yOffset = 0f
        layers.forEach {
            it.x = 0f
        }
    }

    fun updateCamera(camera: OrthographicCamera) {
        if (lastCameraPos != null && lastCameraPos != camera.position) {
            val deltaX = camera.position.x - lastCameraPos!!.x
            val deltaY = (camera.position.y - lastCameraPos!!.y) / 2f
            yOffset = Math.min(0f, yOffset - deltaY)

            layers.forEach {
                val move = (deltaX * it.speed)
                it.x = Math.min(0f, it.x - move)
                updateXOffset(move.roundToInt())
            }
        }
        lastCameraPos = camera.position.cpy()
    }

    override fun render(batch: Batch) {
        layers.forEach {
            batch.draw(it.layer, it.x, if (it.applyYOffset) yOffset else 0f, Gdx.graphics.width.toFloat() + xOffset, Gdx.graphics.height.toFloat())
        }
    }
}

