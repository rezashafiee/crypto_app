/**
 * Precompiled [com.tilda.kotlin.jvm.gradle.kts][Com_tilda_kotlin_jvm_gradle] script plugin.
 *
 * @see Com_tilda_kotlin_jvm_gradle
 */
public
class Com_tilda_kotlin_jvmPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Com_tilda_kotlin_jvm_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
