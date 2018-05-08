package be.catvert.pc.eca.actions

import be.catvert.pc.eca.Entity
import be.catvert.pc.eca.containers.EntityContainer
import be.catvert.pc.eca.containers.Level
import be.catvert.pc.scenes.EditorScene
import be.catvert.pc.ui.UIImpl
import com.sun.org.glassfish.gmbal.Description
import imgui.ImGui

@Description("Permet de supprimer une entité")
class RemoveEntityAction : Action(), UIImpl {
    override fun invoke(entity: Entity, container: EntityContainer) {
        entity.removeFromParent()
    }

    override fun insertUI(label: String, entity: Entity, level: Level, editorUI: EditorScene.EditorUI) {
        ImGui.text("Il n'y a rien à configurer..")
    }
}