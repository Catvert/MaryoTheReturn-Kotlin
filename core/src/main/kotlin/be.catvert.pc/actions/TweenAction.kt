package be.catvert.pc.actions

import be.catvert.pc.GameObject
import be.catvert.pc.containers.Level
import be.catvert.pc.scenes.EditorScene
import be.catvert.pc.tweens.EmptyTween
import be.catvert.pc.tweens.Tween
import be.catvert.pc.tweens.TweenSystem
import be.catvert.pc.tweens.Tweens
import be.catvert.pc.utility.Constants
import be.catvert.pc.utility.CustomEditorImpl
import be.catvert.pc.utility.Description
import com.fasterxml.jackson.annotation.JsonCreator
import glm_.vec2.Vec2
import imgui.ImGui
import imgui.functionalProgramming
import kotlin.reflect.full.createInstance

@Description("Permet d'appliquer un/des tween(s) précis sur un game object")
class TweenAction(var tween: Tween) : Action(), CustomEditorImpl {
    @JsonCreator private constructor() : this(EmptyTween())

    override fun invoke(gameObject: GameObject) {
        TweenSystem.startTween(tween, gameObject)
    }

    private var addTweenTitle = "Ajouter un tween"
    private var addTweenCurrentTween: Tween? = null
    private var addTweenComboIndex = 0
    override fun insertImgui(label: String, gameObject: GameObject, level: Level, editorSceneUI: EditorScene.EditorSceneUI) {
        fun addBtnAddTween(currentTween: Tween?, counter: Int) {
            var openPopup = false // TODO bug?
            functionalProgramming.withId("tween add btn $counter") {
                if (ImGui.button("->", Vec2(20f, -1))) {
                    openPopup = true
                    addTweenCurrentTween = currentTween
                }
            }

            if (openPopup)
                ImGui.openPopup(addTweenTitle)
        }

        with(ImGui) {
            var currentTween: Tween? = tween

            var counter = 0
            while (currentTween != null) {
                if (counter == 0) {
                    addBtnAddTween(null, counter)
                    sameLine()
                }

                ++counter

                functionalProgramming.withGroup {
                    functionalProgramming.withId("tween $counter") {
                        currentTween!!.insertImgui("tween", gameObject, level, editorSceneUI)
                    }
                }
                sameLine()

                addBtnAddTween(currentTween, counter)

                currentTween = currentTween.nextTween

                if (currentTween != null) {
                    sameLine()
                }
            }

            functionalProgramming.popup(addTweenTitle) {
                functionalProgramming.withItemWidth(Constants.defaultWidgetsWidth) {
                    combo("tween", ::addTweenComboIndex, Tweens.values().map {
                        it.tween.simpleName?.removeSuffix("Tween") ?: "Nom introuvable"
                    })
                }

                if (button("Ajouter", Vec2(Constants.defaultWidgetsWidth, 0))) {
                    val tween = Tweens.values()[addTweenComboIndex].tween.createInstance()
                    addTweenCurrentTween?.apply {
                        this.nextTween = tween
                    } ?: run { this@TweenAction.tween = tween }
                    closeCurrentPopup()
                }
            }
        }
    }
}