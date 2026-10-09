package com.example.util

/**
 * AdMob Configuration for CV Maker.
 *
 * Provides central management for AdMob Application ID and Ad Unit IDs.
 * By default, uses Google's official, guaranteed test ad unit IDs so that
 * the app never causes AdMob policy violations or account bans during development.
 *
 * When your production AdMob account is ready:
 * Simply replace these strings with your real AdMob Ad Unit IDs!
 */
object AdMobConfig {

    // 1. AdMob Application ID (Also configured in AndroidManifest.xml)
    // Format: ca-app-pub-XXXXXXXXXXXXXXXX~YYYYYYYYYY
    // Google sample App ID: ca-app-pub-3940256099942544~3347511713
    const val ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713"

    // 2. Banner Ad Unit ID (Used in bottom navigation bars across Home, My CV, and Export)
    // Google Test Banner: ca-app-pub-3940256099942544/6300978111
    var BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

    // 3. Native Advanced Ad Unit ID (Used for template-styled cards in the 2-column grid)
    // Google Test Native: ca-app-pub-3940256099942544/2247696110
    var NATIVE_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

    // 4. Interstitial Ad Unit ID (Post-splash launch interstitial)
    // Google Test Interstitial: ca-app-pub-3940256099942544/1033173712
    var INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

    // 5. Rewarded Ad Unit ID (Used when unlocking premium templates: Clarity, Tradition, Continental, Apex, Summit)
    // Google Test Rewarded: ca-app-pub-3940256099942544/5224354917
    var REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    /**
     * Call this helper if you provide keys at runtime or via BuildConfig
     */
    fun configureProductionIds(
        bannerId: String? = null,
        nativeId: String? = null,
        interstitialId: String? = null,
        rewardedId: String? = null
    ) {
        bannerId?.let { BANNER_AD_UNIT_ID = it }
        nativeId?.let { NATIVE_AD_UNIT_ID = it }
        interstitialId?.let { INTERSTITIAL_AD_UNIT_ID = it }
        rewardedId?.let { REWARDED_AD_UNIT_ID = it }
    }
}
