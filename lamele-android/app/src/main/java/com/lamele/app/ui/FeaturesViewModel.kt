package com.lamele.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lamele.app.LameleApplication
import com.lamele.app.data.local.DanmakuEntity
import com.lamele.app.data.local.FeedPostEntity
import com.lamele.app.data.local.ToiletEntity
import com.lamele.app.data.local.ToiletReviewEntity
import com.lamele.app.data.local.TreeHoleEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FeaturesViewModel(app: Application) : AndroidViewModel(app) {
    private val lamele = app as LameleApplication
    private val ex = lamele.extrasRepository

    val game = lamele.gameStateRepository
    val prefsRepo = lamele.userPreferencesRepository

    val toilets = ex.toilets.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val feed = ex.feed.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val treeHoles = ex.treeHoles.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val danmaku = ex.danmaku.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val friends = ex.friends.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun reviewsFor(toiletId: Long) = ex.reviewsFor(toiletId)

    fun addReview(r: ToiletReviewEntity, done: () -> Unit = {}) {
        viewModelScope.launch {
            ex.insertReview(r)
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main.immediate) { done() }
        }
    }

    fun addToilet(t: ToiletEntity, onDone: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = ex.insertToilet(t)
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main.immediate) { onDone(id) }
        }
    }

    fun tryPurchaseCosmetic(cost: Int, cosmeticId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val ok = prefsRepo.spendPoopCoins(cost)
            if (ok) game.unlockCosmetic(cosmeticId)
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main.immediate) { onResult(ok) }
        }
    }

    fun feedPet(onMessage: (String) -> Unit) {
        viewModelScope.launch {
            val ok = prefsRepo.spendPoopCoins(10)
            if (ok) game.addPetXp(10)
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main.immediate) {
                onMessage(if (ok) "投喂成功，经验 +10" else "屎币不足，先去打卡攒币")
            }
        }
    }

    fun submitMiniGameScore(score: Int) {
        viewModelScope.launch { game.updateMiniGameScore(score) }
    }

    fun postFeed(content: String, anonymous: Boolean, linkedId: Long?) {
        viewModelScope.launch {
            ex.insertFeed(
                FeedPostEntity(
                    content = content,
                    tags = null,
                    isAnonymous = anonymous,
                    linkedRecordId = linkedId,
                ),
            )
        }
    }

    fun postTree(content: String, mood: String?) {
        viewModelScope.launch {
            ex.insertTreeHole(TreeHoleEntity(content = content, moodTag = mood))
        }
    }

    fun shootDanmaku(text: String) {
        viewModelScope.launch { ex.insertDanmaku(DanmakuEntity(text = text)) }
    }
}
