package com.lamele.app.data

import com.lamele.app.data.local.DanmakuEntity
import com.lamele.app.data.local.FeedPostEntity
import com.lamele.app.data.local.FriendEntity
import com.lamele.app.data.local.SocialDao
import com.lamele.app.data.local.ToiletDao
import com.lamele.app.data.local.ToiletEntity
import com.lamele.app.data.local.ToiletReviewEntity
import com.lamele.app.data.local.TreeHoleEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExtrasRepository(
    private val toiletDao: ToiletDao,
    private val socialDao: SocialDao,
) {
    val toilets = toiletDao.observeToilets()
    fun reviewsFor(toiletId: Long) = toiletDao.observeReviews(toiletId)
    val feed = socialDao.observeFeed()
    val treeHoles = socialDao.observeTreeHoles()
    val danmaku = socialDao.observeDanmaku()
    val friends = socialDao.observeFriends()

    suspend fun insertToilet(t: ToiletEntity) = toiletDao.insert(t)
    suspend fun getToilet(id: Long) = toiletDao.getToilet(id)
    suspend fun insertReview(r: ToiletReviewEntity) = toiletDao.insertReview(r)
    suspend fun insertFeed(f: FeedPostEntity) = socialDao.insertFeed(f)
    suspend fun insertTreeHole(t: TreeHoleEntity) = socialDao.insertTreeHole(t)
    suspend fun insertDanmaku(d: DanmakuEntity) = socialDao.insertDanmaku(d)

    suspend fun seedIfNeeded() = withContext(Dispatchers.IO) {
        if (socialDao.friendCount() == 0) {
            listOf(
                FriendEntity(nickname = "摸鱼阿强", mockWeeklyCount = 5, mockPaidMinutes = 42),
                FriendEntity(nickname = "通畅学姐", mockWeeklyCount = 8, mockPaidMinutes = 30),
                FriendEntity(nickname = "带薪冠军老李", mockWeeklyCount = 4, mockPaidMinutes = 95),
            ).forEach { socialDao.insertFriend(it) }
            listOf(
                "兄弟，我也在蹲。",
                "隔壁在开闸，安心释放。",
                "这坑信号满格，建议收藏。",
                "公司欠我的，我从厕所拿回来。",
            ).forEach { t ->
                socialDao.insertDanmaku(DanmakuEntity(text = t))
            }
        }
    }

    suspend fun wipeAllExtras() = withContext(Dispatchers.IO) {
        socialDao.deleteAllFeeds()
        socialDao.deleteAllHoles()
        socialDao.deleteAllDanmaku()
        socialDao.clearFriends()
        toiletDao.deleteAllReviews()
        toiletDao.deleteAllToilets()
    }
}
