package be.catvert.pc.actions

import be.catvert.pc.GameObject
import be.catvert.pc.containers.Level
import be.catvert.pc.utility.Constants
import be.catvert.pc.utility.Description
import be.catvert.pc.utility.ExposeEditor
import be.catvert.pc.utility.cast
import com.fasterxml.jackson.annotation.JsonCreator

@Description("Permet de changer la gravité du niveau")
class GravityAction(@ExposeEditor(min = 0f, max = 100f) var gravitySpeed: Int) : Action() {
    @JsonCreator private constructor() : this(Constants.defaultGravitySpeed)

    override fun invoke(gameObject: GameObject) {
        gameObject.container.cast<Level>()?.gravitySpeed = gravitySpeed
    }

    override fun toString() = super.toString() + " - gravity : $gravitySpeed"
}