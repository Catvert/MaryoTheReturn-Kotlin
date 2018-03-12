package be.catvert.pc.eca.actions

import be.catvert.pc.eca.Entity
import be.catvert.pc.eca.components.graphics.TextureComponent
import be.catvert.pc.eca.containers.EntityContainer
import be.catvert.pc.ui.Description
import be.catvert.pc.ui.UI

@Description("Permet d'appeler une action selon l'état de flip du TextureComponent")
class TextureFlipSwitcherAction(@UI var unFlipXAction: Action = EmptyAction(), @UI var flipXAction: Action = EmptyAction(), @UI var unFlipYAction: Action = EmptyAction(), @UI var flipYAction: Action = EmptyAction()) : Action() {
    override fun invoke(entity: Entity, container: EntityContainer) {
        entity.getCurrentState().getComponent<TextureComponent>()?.apply {
            if (flipX)
                flipXAction(entity, container)
            else
                unFlipXAction(entity, container)

            if (flipY)
                flipYAction(entity, container)
            else
                unFlipYAction(entity, container)
        }
    }
}