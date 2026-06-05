package net.minecraftweb.client;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public class WebLauncher {

    // Hàm mồi nguyên bản để lấy thẻ Canvas
    @JSBody(script = "return document.getElementById('minecraft-canvas');")
    public static native JSObject getCanvasElement();

    // Hàm mồi cấu hình thông minh WebGL1 / WebGL2
    @JSBody(params = { "canvas" }, script = "" +
        "var gl = canvas.getContext('webgl2');" +
        "if (gl) {" +
        "    console.log('[Republiccraft] Phat hien phan cung manh: Kich hoat WebGL2');" +
        "    return { context: gl, version: 2 };" +
        "}" +
        "gl = canvas.getContext('webgl') || canvas.getContext('experimental-webgl');" +
        "if (gl) {" +
        "    console.log('[Republiccraft] Phat hien phan cung yeu (Intel HD): Kich hoat WebGL1 + Toi uu hoa');" +
        "    " +
        "    // 1. Kich hoat ANGLE_instanced_arrays (Ve hang loat)\n" +
        "    var instancedExt = gl.getExtension('ANGLE_instanced_arrays');" +
        "    if (instancedExt) gl.instancedExt = instancedExt;" +
        "    " +
        "    // 2. Kich hoat OES_element_index_uint (Vuot gioi han 65k dinh cho Chunk xa)\n" +
        "    gl.getExtension('OES_element_index_uint');" +
        "    " +
        "    // 3. Kich hoat EXT_texture_filter_anisotropic (Lam muot texture xa)\n" +
        "    gl.getExtension('EXT_texture_filter_anisotropic') || " +
        "    gl.getExtension('MOZ_EXT_texture_filter_anisotropic') || " +
        "    gl.getExtension('WEBKIT_EXT_texture_filter_anisotropic');" +
        "    " +
        "    return { context: gl, version: 1 };" +
        "}" +
        "return null;"
    )
    public static native JSObject initSmartContext(JSObject canvas);

    @JSBody(params = { "wrapper" }, script = "return wrapper ? wrapper.context : null;")
    public static native JSObject getContextFromWrapper(JSObject wrapper);

    @JSBody(params = { "wrapper" }, script = "return wrapper ? wrapper.version : 0;")
    public static native int getVersionFromWrapper(JSObject wrapper);

    // ==========================================
    // 🛠️ HÀM MỒI MỚI: BẮN LOG RA CONSOLE TRÌNH DUYỆT
    // ==========================================
    @JSBody(params = { "text" }, script = "console.log(text);")
    public static native void browserLog(String text);

    public static void main(String[] args) {
        if (!GLFW.glfwInit()) return;

        JSObject canvas = getCanvasElement();
        if (canvas == null) {
            browserLog("[Loi] Khong tim thay the <canvas id='minecraft-canvas'>!");
            return;
        }

        JSObject glWrapper = initSmartContext(canvas);
        if (glWrapper == null) {
            browserLog("[Loi] Trinh duyet/Phan cung qua cu, khong ho tro WebGL!");
            return;
        }

        // Dong bo hoa sang lop gia lap GL11
        GL11.glContext = getContextFromWrapper(glWrapper);
        GL11.glVersion = getVersionFromWrapper(glWrapper);

        // Bắn log trực tiếp ra Console của Trình duyệt thông qua hàm mồi
        browserLog("[He Thong] Khoi tao thanh cong! San sang chuyen quyen cho Minecraft Core.");
        
        // Gọi Minecraft Core bản Remastered mới nhất theo năm (Game Drops) của ông
        net.minecraft.client.Minecraft game = new net.minecraft.client.Minecraft("Republiccraft Remastered 26.1.2");
        game.run();
        
        // Test ve thu mot khung hinh mau nen gach cua Minecraft
        GL11.glClearColor(0.27f, 0.18f, 0.13f, 1.0f); 
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
}