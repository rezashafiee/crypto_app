/**
 * Precompiled [com.tilda.android.library.gradle.kts][Com_tilda_android_library_gradle] script plugin.
 *
 * @see Com_tilda_android_library_gradle
 */
public
class Com_tilda_android_libraryPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Com_tilda_android_library_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
