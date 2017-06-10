package be.catvert.mtrktx.ecs.systems

import be.catvert.mtrktx.ecs.components.UpdateComponent
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray

/**
 * Created by arno on 04/06/17.
 */

class UpdateSystem() : BaseSystem() {
    private val updateMapper = ComponentMapper.getFor(UpdateComponent::class.java)

    private lateinit var entities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)

        entities = getEngine().getEntitiesFor(Family.all(UpdateComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        entities.forEach {
            if(updateMapper[it].active)
                updateMapper[it].update(deltaTime)
        }
    }
}