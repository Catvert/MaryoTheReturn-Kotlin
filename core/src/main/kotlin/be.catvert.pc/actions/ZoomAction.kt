package be.catvert.pc.actions

import be.catvert.pc.GameObject
import be.catvert.pc.containers.Level
import be.catvert.pc.utility.ExposeEditor
import com.fasterxml.jackson.annotation.JsonCreator

class ZoomAction(@ExposeEditor(max = 2f) var zoom: Float) : Action {
    @JsonCreator private constructor(): this(1f)

    override fun invoke(gameObject: GameObject) {
        (gameObject.container as? Level)?.zoom = zoom
    }
}