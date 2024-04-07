package unk.beast.beastlottie;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.airbnb.lottie.AsyncUpdates;
import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieConfig;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieListener;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;
import com.airbnb.lottie.RenderMode;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.ReplForm;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

//@SuppressWarnings({"unused"})
public class BeastLottie extends AndroidNonvisibleComponent implements Animator.AnimatorListener, Animator.AnimatorPauseListener, ValueAnimator.AnimatorUpdateListener, LottieOnCompositionLoadedListener, LottieListener<Throwable> {
    private final Form form;
    private final LottieAnimationView animationView;
    private boolean ignoreSystemAnimationsDisabled = false;
    private boolean useCompositionFrameRate = false;
    private boolean useCacheComposition = true;
    private boolean outlineMasksAndMattes = false;
    private float minProgress = 0f;
    private float maxProgress = 0f;
    private String repeatMode = "Infinite";
    private String renderMode = "Automatic";
    private boolean safeMode = false;
    private String asyncUpdateMode = "Automatic";
    private boolean applyOpacityToLayers = false;

    public BeastLottie(ComponentContainer container) {
        super(container.$form());
        this.form = container.$form();
        this.animationView = new LottieAnimationView(form);
        this.animationView.addAnimatorListener(this);
        this.animationView.addAnimatorPauseListener(this);
        this.animationView.addAnimatorUpdateListener(this);
        this.animationView.addLottieOnCompositionLoadedListener(this);
        this.animationView.setFailureListener(this);
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
    @SimpleProperty(description = "Allows ignoring system animations settings, therefore allowing animations to run even if they are disabled.")
    public void IgnoreDisabledSystemAnimations(boolean ignore) {
        this.ignoreSystemAnimationsDisabled = ignore;
        this.animationView.setIgnoreDisabledSystemAnimations(ignore);
    }

    @SimpleProperty(description = "Returns if animations will be rendered ignoring the system animations settings, therefore allowing animations to run even if they are disabled.")
    public boolean IgnoreDisabledSystemAnimations() {
        return this.ignoreSystemAnimationsDisabled;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
    @SimpleProperty(description = "Lottie files can specify a target frame rate. By default, Lottie ignores it and re-renders on every frame. If that behavior is undesirable, you can set this to true to use the composition frame rate instead.")
    public void UseCompositionFrameRate(boolean use) {
        this.useCompositionFrameRate = use;
        this.animationView.setUseCompositionFrameRate(use);
    }

    @SimpleProperty(description = "Returns if the frame rate from Lottie files should be used.")
    public boolean UseCompositionFrameRate() {
        return this.useCompositionFrameRate;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
    @SimpleProperty(description = "Sets if paths should be merged for Kitkat and above.")
    public void MergePathsForKitKatAndAbove(boolean merge) {
        this.animationView.enableMergePathsForKitKatAndAbove(merge);
    }

    @SimpleProperty(description = "Returns if paths are being merged for Kitkat and above.")
    public boolean MergePathsForKitKatAndAbove() {
        return this.animationView.isMergePathsEnabledForKitKatAndAbove();
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
    @SimpleProperty(description = "Sets if animation should be clipped to composition bounds.")
    public void ClipToCompositionBounds(boolean clip) {
        this.animationView.setClipToCompositionBounds(clip);
    }

    @SimpleProperty(description = "Returns if animation is being clipped to composition bounds.")
    public boolean ClipToCompositionBounds() {
        return this.animationView.getClipToCompositionBounds();
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "True")
    @SimpleProperty(description = "Sets if animations should be cached.")
    public void CacheAnimations(boolean cache) {
        this.useCacheComposition = cache;
        this.animationView.setCacheComposition(cache);
    }

    @SimpleProperty(description = "Returns if animations are being cached.")
    public boolean CacheAnimations() {
        return this.useCacheComposition;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
    @SimpleProperty(description = "Enable this to debug slow animations by outlining masks and mattes. Do not leave this enabled in production.")
    public void OutlineMasksAndMattes(boolean outlined) {
        this.outlineMasksAndMattes = outlined;
        this.animationView.setOutlineMasksAndMattes(outlined);
    }

    @SimpleProperty(description = "Returns if animation is being clipped to composition bounds.")
    public boolean OutlineMasksAndMattes() {
        return this.outlineMasksAndMattes;
    }

    @SimpleProperty(description = "Returns whether or not any layers in this composition has masks.")
    public boolean HasMasks() {
        return this.animationView.hasMasks();
    }

    @SimpleProperty(description = "Returns whether or not any layers in this composition has a matte layer.")
    public boolean HasMatte() {
        return this.animationView.hasMatte();
    }

    @SimpleProperty(description = "Sets the minimum frame that the animation will start from when playing or looping.")
    public void MinFrame(int min) {
        this.animationView.setMinFrame(min);
    }

    @SimpleProperty(description = "Returns the minimum frame that the animation will start from when playing or looping.")
    public float MinFrame() {
        return this.animationView.getMinFrame();
    }

    @SimpleProperty(description = "Sets the minimum progress that the animation will start from when playing or looping.")
    public void MinProgress(float min) {
        this.minProgress = min;
        this.animationView.setMinProgress(min);
    }

    @SimpleProperty(description = "Returns the minimum progress that the animation will start from when playing or looping.")
    public float MinProgress() {
        return this.minProgress;
    }

    @SimpleProperty(description = "Sets the maximum frame that the animation will end at when playing or looping.")
    public void MaxFrame(int max) {
        this.animationView.setMaxFrame(max);
    }

    @SimpleProperty(description = "Returns the maximum frame that the animation will end at when playing or looping.")
    public float MaxFrame() {
        return this.animationView.getMaxFrame();
    }

    @SimpleProperty(description = "Sets the maximum progress that the animation will end at when playing or looping.")
    public void MaxProgress(float max) {
        this.maxProgress = max;
        this.animationView.setMaxProgress(max);
    }

    @SimpleProperty(description = "Returns the maximum progress that the animation will end at when playing or looping.")
    public float MaxProgress() {
        return this.maxProgress;
    }

    @SimpleProperty(description = "Sets the playback speed. If less than 0, the animation will play backwards.")
    public void Speed(float speed) {
        this.animationView.setSpeed(speed);
    }

    @SimpleProperty(description = "Returns the playback speed. If less than 0, the animation will play backwards.")
    public float Speed() {
        return this.animationView.getSpeed();
    }

    @SimpleProperty(description = "Specifies the repeat mode for animation.")
    public void RepeatMode(String repeatMode) {
        int animRepeatMode;
        if (repeatMode.equals("Infinite")) {
            animRepeatMode = LottieDrawable.INFINITE;
        } else if (repeatMode.equals("Restart")) {
            animRepeatMode = LottieDrawable.RESTART;
        } else {
            animRepeatMode = LottieDrawable.REVERSE;
        }
        this.animationView.setRepeatMode(animRepeatMode);
        this.repeatMode = repeatMode;
    }

    @SimpleProperty(description = "Returns the repeat mode being used.")
    public String RepeatMode() {
        return this.repeatMode;
    }

    @SimpleProperty(description = "Returns the Infinite repeat mode.")
    public String Infinite() {
        return "Infinite";
    }

    @SimpleProperty(description = "Returns the Restart repeat mode.")
    public String Restart() {
        return "Restart";
    }

    @SimpleProperty(description = "Returns the Restart repeat mode.")
    public String Reverse() {
        return "Reverse";
    }

    @SimpleProperty(description = "Sets the repeat count for the animation.")
    public void RepeatCount(int count) {
        this.animationView.setRepeatCount(count);
    }

    @SimpleProperty(description = "Returns the repeat count for the animation.")
    public int RepeatCount() {
        return this.animationView.getRepeatCount();
    }

    @SimpleProperty(description = "Returns infinite repeat count.")
    public int RepeatModeInfinite(){
        return ValueAnimator.INFINITE;
    }

    @SimpleProperty(description = "Returns reverse repeat count.")
    public int RepeatModeReverse(){
        return ValueAnimator.REVERSE;
    }

    @SimpleProperty(description = "Returns reverse repeat count.")
    public int RepeatModeRestart(){
        return ValueAnimator.RESTART;
    }
    @SimpleProperty(description = "Returns if the Lottie Animation View is animating.")
    public boolean IsAnimating() {
        return this.animationView.isAnimating();
    }

    @SimpleProperty(description = "Sets the path for Image Assets folder. This can come handy when your animations depend on images.")
    public void ImageAssetsFolder(String folder) {
        this.animationView.setImageAssetsFolder(folder);
    }

    @SimpleProperty(description = "Returns the path for Image Assets folder. This can come handy when your animations depend on images.")
    public String ImageAssetsFolder() {
        return this.animationView.getImageAssetsFolder() != null ? this.animationView.getImageAssetsFolder() : "";
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
    @SimpleProperty(description = "Sets if original image bounds should be maintained while rendering the animation.")
    public void MaintainOriginalImageBounds(boolean maintain) {
        this.animationView.setMaintainOriginalImageBounds(maintain);
    }

    @SimpleProperty(description = "Returns if original image bounds are being maintained while rendering the animation.")
    public boolean MaintainOriginalImageBounds() {
        return this.animationView.getMaintainOriginalImageBounds();
    }

    @SimpleProperty(description = "Sets the progress to the specified frame.")
    public void Frame(int frame) {
        this.animationView.setFrame(frame);
    }

    @SimpleProperty(description = "Returns the currently rendered frame.")
    public int Frame() {
        return this.animationView.getFrame();
    }

    @SimpleProperty(description = "Sets the animation progress.")
    public void Progress(float progress) {
        this.animationView.setProgress(progress);
    }

    @SimpleProperty(description = "Returns the animation progress.")
    public float Progress() {
        return this.animationView.getProgress();
    }

    @SimpleProperty(description = "Returns the duration of the Lottie animation.")
    public long Duration() {
        return this.animationView.getDuration();
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "True")
    @SimpleProperty(description = "If you are experiencing a device specific crash that happens during drawing, you can set this to true")
    public void SafeMode(boolean safe) {
        this.safeMode = safe;
        this.animationView.setSafeMode(safe);
    }

    @SimpleProperty(description = "Returns if the animation are being rendered in the safe mode to avoid crashes.")
    public boolean SafeMode() {
        return this.safeMode;
    }

    @SimpleProperty(description = "Sets the Render mode for Lottie.")
    public void RenderMode(String mode) {
        RenderMode renderMode;
        if (mode.equals("Hardware")) {
            renderMode = RenderMode.HARDWARE;
        } else if (mode.equals("Software")) {
            renderMode = RenderMode.SOFTWARE;
        } else {
            renderMode = RenderMode.AUTOMATIC;
        }
        this.renderMode = mode;
        this.animationView.setRenderMode(renderMode);
    }

    @SimpleProperty(description = "Returns the hardware render mode.")
    public String RenderModeHardware() {
        return "Hardware";
    }

    @SimpleProperty(description = "Returns the software render mode.")
    public String RenderModeSoftware() {
        return "Software";
    }

    @SimpleProperty(description = "Returns the automatic render mode.")
    public String RenderModeAutomatic() {
        return "Automatic";
    }

    @SimpleProperty(description = "Returns the currently set render mode for animations.")
    public String RenderMode() {
        return this.renderMode;
    }

    @SimpleProperty(description = "Sets the async update mode for Lottie.")
    public void AsyncUpdateMode(String mode) {
        AsyncUpdates asyncUpdates;
        if (mode.equals("Disabled")) {
            asyncUpdates = AsyncUpdates.DISABLED;
        } else if (mode.equals("Enabled")) {
            asyncUpdates = AsyncUpdates.ENABLED;
        } else {
            asyncUpdates = AsyncUpdates.AUTOMATIC;
        }
        this.asyncUpdateMode = mode;
        this.animationView.setAsyncUpdates(asyncUpdates);
    }

    @SimpleProperty(description = "Returns the disabled async update mode for Lottie.")
    public String AsyncUpdateModeDisabled() {
        return "Disabled";
    }

    @SimpleProperty(description = "Returns the enabled async update mode for Lottie.")
    public String AsyncUpdateModeEnabled() {
        return "Enabled";
    }

    @SimpleProperty(description = "Returns the automatic async update mode for Lottie.")
    public String AsyncUpdateModeAutomatic() {
        return "Automatic";
    }

    @SimpleProperty(description = "Returns the currently set async update mode.")
    public String AsyncUpdateMode() {
        return this.asyncUpdateMode;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
    @SimpleProperty(description = "Sets if opacity should be applied to animation layers.")
    public void ApplyOpacityToLayers(boolean apply) {
        this.applyOpacityToLayers = apply;
        this.animationView.setApplyingOpacityToLayersEnabled(apply);
    }

    @SimpleProperty(description = "Returns if opacity is being applied to animation layers.")
    public boolean ApplyOpacityToLayers() {
        return this.applyOpacityToLayers;
    }

    @SimpleProperty(description = "Sets if text should be clipped to bounding box.")
    public void ClipTextToBoundingBox(boolean clip) {
        this.animationView.setClipTextToBoundingBox(clip);
    }

    @SimpleProperty(description = "Returns if text is being clipped to bounding box.")
    public boolean ClipTextToBoundingBox() {
        return this.animationView.getClipTextToBoundingBox();
    }

    @SimpleProperty(description = "Returns if Async Updates are enabled.")
    public boolean AsyncUpdatesEnabled() {
        return this.animationView.getAsyncUpdatesEnabled();
    }

    @SimpleProperty(description = "Sets the layout for the Lottie Animation View.")
    public void Layout(AndroidViewComponent layout) {
        ViewGroup viewGroup = (ViewGroup) layout.getView();
        viewGroup.removeAllViews();
        viewGroup.addView(animationView);
    }

    @SimpleFunction(description = "Initializes Lottie for usage in the app.")
    public void InitializeLottie(boolean enablePathInterpolatorCache, boolean enableNetworkCache, boolean enableSystraceMarkers, String networkCacheDir) {
        Lottie.initialize(new LottieConfig.Builder()
                .setDisablePathInterpolatorCache(!enablePathInterpolatorCache)
                .setEnableNetworkCache(enableNetworkCache)
                .setEnableSystraceMarkers(enableSystraceMarkers)
                .setNetworkCacheDir(networkCacheDir.trim().isEmpty() ? form.getCacheDir() : new File(networkCacheDir))
                .build());
    }

    @SimpleFunction(description = "Cancels the currently playing animation (if any).")
    public void CancelAnimation() {
        this.animationView.cancelAnimation();
    }

    @SimpleFunction(description = "Pauses the currently playing animation (if any).")
    public void PauseAnimation() {
        this.animationView.pauseAnimation();
    }

    @SimpleFunction(description = "Resumes the currently playing animation (if any).")
    public void ResumeAnimation() {
        this.animationView.resumeAnimation();
    }

    @SimpleFunction(description = "Plays the lottie animation (if set).")
    public void PlayAnimation() {
        this.animationView.playAnimation();
    }

    @SimpleFunction(description = "Invalidates the Lottie animation view.")
    public void Invalidate() {
        this.animationView.invalidate();
    }

    @SimpleFunction(description = "Reverses the Animation speed.")
    public void ReverseAnimationSpeed() {
        this.animationView.reverseAnimationSpeed();
    }

    @SimpleFunction(description = "Loads Animation from Assets.")
    public void LoadFromAssets(String asset) {
        if (form instanceof ReplForm) {
            this.animationView.setAnimation(getStreamForCompanionAsset(asset), null);
        } else {
            this.animationView.setAnimation(asset);
        }
    }

    @SimpleFunction(description = "Loads Animation from Path.")
    public void LoadFromFilePath(String path) {
        try {
            this.animationView.setAnimation(new FileInputStream(path), null);
        } catch (Exception e) {
            ErrorOccurred(e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @SimpleFunction(description = "Loads Animation from provided JSON text.")
    public void LoadFromJson(String json) {
        this.animationView.setAnimationFromJson(json, null);
    }

    @SimpleFunction(description = "Loads Animation from provided URL.")
    public void LoadFromUrl(String url) {
        this.animationView.setAnimationFromUrl(url);
    }

    @SimpleEvent(description = "Event triggered when Lottie animation has started playing.")
    public void AnimationStarted() {
        EventDispatcher.dispatchEvent(this, "AnimationStarted");
    }

    @SimpleEvent(description = "Event triggered when Lottie animation has ended playing.")
    public void AnimationEnded() {
        EventDispatcher.dispatchEvent(this, "AnimationEnded");
    }

    @SimpleEvent(description = "Event triggered when Lottie animation has been cancelled while playing.")
    public void AnimationCancelled() {
        EventDispatcher.dispatchEvent(this, "AnimationCancelled");
    }

    @SimpleEvent(description = "Event triggered when Lottie animation has been repeated.")
    public void AnimationRepeated() {
        EventDispatcher.dispatchEvent(this, "AnimationRepeated");
    }

    @SimpleEvent(description = "Event triggered when Lottie animation has been paused.")
    public void AnimationPaused() {
        EventDispatcher.dispatchEvent(this, "AnimationPaused");
    }

    @SimpleEvent(description = "Event triggered when Lottie animation has been resumed.")
    public void AnimationResumed() {
        EventDispatcher.dispatchEvent(this, "AnimationResumed");
    }

    @SimpleEvent(description = "Event triggered when Lottie animation has been updated.")
    public void AnimationUpdated() {
        EventDispatcher.dispatchEvent(this, "AnimationUpdated");
    }

    @SimpleEvent(description = "Event triggered when Lottie composition has been loaded.")
    public void CompositionLoaded(float duration, float durationFrames, float startFrame, float endFrame, float frameRate, int maskAndMatteCount, boolean hasDashPattern, boolean hasImages) {
        EventDispatcher.dispatchEvent(this, "CompositionLoaded", duration, durationFrames, startFrame, endFrame, frameRate, maskAndMatteCount, hasDashPattern, hasImages);
    }

    @SimpleEvent(description = "Event triggered when an error occurred.")
    public void ErrorOccurred(String error, String log) {
        EventDispatcher.dispatchEvent(this, "ErrorOccurred", error, log);
    }

    @Override
    public void onAnimationStart(@NonNull Animator animation) {
        AnimationStarted();
    }

    @Override
    public void onAnimationEnd(@NonNull Animator animation) {
        AnimationEnded();
    }

    @Override
    public void onAnimationCancel(@NonNull Animator animation) {
        AnimationCancelled();
    }

    @Override
    public void onAnimationRepeat(@NonNull Animator animation) {
        AnimationRepeated();
    }

    @Override
    public void onAnimationPause(@NonNull Animator animation) {
        AnimationPaused();
    }

    @Override
    public void onAnimationResume(@NonNull Animator animation) {
        AnimationResumed();
    }

    @Override
    public void onAnimationUpdate(@NonNull ValueAnimator animation) {
        AnimationUpdated();
    }

    @Override
    public void onCompositionLoaded(LottieComposition lottieComposition) {
        float duration = lottieComposition != null ? lottieComposition.getDuration() : 0f;
        float durationFrames = lottieComposition != null ? lottieComposition.getDurationFrames() : 0f;
        float startFrame = lottieComposition != null ? lottieComposition.getStartFrame() : 0f;
        float endFrame = lottieComposition != null ? lottieComposition.getEndFrame() : 0f;
        float frameRate = lottieComposition != null ? lottieComposition.getFrameRate() : 0f;
        int maskAndMatteCount = lottieComposition != null ? lottieComposition.getMaskAndMatteCount() : 0;
        boolean hasDashPattern = lottieComposition != null && lottieComposition.hasDashPattern();
        boolean hasImages = lottieComposition != null && lottieComposition.hasImages();
        CompositionLoaded(duration, durationFrames, startFrame, endFrame, frameRate, maskAndMatteCount, hasDashPattern, hasImages);
    }

    private InputStream getStreamForCompanionAsset(String assetName) {
        try {
            String companionAssetsPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + form.getPackageName() + File.separator + "files" + File.separator + "assets" + File.separator;
            return new FileInputStream(companionAssetsPath + assetName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onResult(Throwable throwable) {
        ErrorOccurred(throwable.getMessage(), Log.getStackTraceString(throwable));
    }
}