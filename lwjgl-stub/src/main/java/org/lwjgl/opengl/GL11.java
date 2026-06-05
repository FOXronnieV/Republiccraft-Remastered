package org.lwjgl.opengl;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public class GL11 {

    public static final int GL_COLOR_BUFFER_BIT = 0x00004000;
    public static final int GL_DEPTH_BUFFER_BIT = 0x00000100;
    public static final int GL_TRIANGLES = 0x0004;
    public static final int GL_UNSIGNED_INT = 0x1405; // Cần thiết cho OES_element_index_uint

    // Lưu trữ Context và phiên bản hiện tại
    public static JSObject glContext = null;
    public static int glVersion = 2; // Mặc định là 2, sẽ cập nhật khi khởi chạy

    @JSBody(params = { "gl", "r", "g", "b", "a" }, script = "if(gl) gl.clearColor(r, g, b, a);")
    private static native void nClearColor(JSObject gl, float r, float g, float b, float a);

    @JSBody(params = { "gl", "mask" }, script = "if(gl) gl.clear(mask);")
    private static native void nClear(JSObject gl, int mask);

    // Lệnh vẽ Instanced tương thích ngược cho cả WebGL1 và WebGL2
    @JSBody(params = { "gl", "version", "mode", "count", "type", "indicesOffset", "instanceCount" }, script = "" +
        "if(!gl) return;" +
        "if(version === 2) {" +
        "    gl.drawElementsInstanced(mode, count, type, indicesOffset, instanceCount);" +
        "} else if(gl.instancedExt) {" +
        "    gl.instancedExt.drawElementsInstancedANGLE(mode, count, type, indicesOffset, instanceCount);" +
        "}"
    )
    private static native void nDrawElementsInstanced(JSObject gl, int version, int mode, int count, int type, int indicesOffset, int instanceCount);

    public static void glClearColor(float red, float green, float blue, float alpha) {
        nClearColor(glContext, red, green, blue, alpha);
    }

    public static void glClear(int mask) {
        nClear(glContext, mask);
    }

    // Hàm mã nguồn Minecraft sẽ gọi để vẽ hàng ngàn block một lúc
    public static void glDrawElementsInstanced(int mode, int count, int type, int indicesOffset, int instanceCount) {
        nDrawElementsInstanced(glContext, glVersion, mode, count, type, indicesOffset, instanceCount);
    }
}