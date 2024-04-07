# Add any ProGuard configurations specific to this
# extension here.

-keep public class unk.beast.beastlottie.BeastLottie {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'unk/beast/beastlottie/repack'
-flattenpackagehierarchy
-dontpreverify
