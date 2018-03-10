package be.catvert.pc.factories


import be.catvert.pc.builders.TweenBuilder
import be.catvert.pc.eca.actions.RemoveEntityAction
import be.catvert.pc.eca.components.logics.LifeComponent
import be.catvert.pc.tweens.*

/**
 * Permet d'ajouter des tweens de base au jeu
 */
enum class TweenFactory(val tween: () -> Tween) {
    Empty({ EmptyTween() }),
    RemoveEntity({ TweenBuilder(AlphaTextureTween(0.5f, 0f), true).build(RemoveEntityAction()) }),
    ReduceSize({ TweenBuilder(ResizeTween(0.5f), true).build() }),
    DisableLifeComponent({ TweenBuilder(DisableComponentTween(LifeComponent::class.java, 0.5f)).build() });

    operator fun invoke() = tween()
}