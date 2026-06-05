package org.lwjgl.glfw;

import org.teavm.jso.JSBody;

public class GLFW {
    public static final int GLFW_TRUE = 1;
    public static final int GLFW_FALSE = 0;

    @JSBody(params = { "message" }, script = "console.log(message);")
    public static native void log(String message);

    public static boolean glfwInit() {
        log("[GLFW] Dang khoi tao he thong cua so trinh duyet...");
        return true;
    }

    public static long glfwCreateWindow(int width, int height, CharSequence title, long monitor, long share) {
        log("[GLFW] Da tao khung can doi cho game: " + width + "x" + height);
        return 1L;
    }

    public static boolean glfwWindowShouldClose(long window) { return false; }
    public static void glfwSwapBuffers(long window) {}
    public static void glfwPollEvents() {}
}