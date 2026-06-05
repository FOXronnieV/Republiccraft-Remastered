package net.minecraft.client;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.teavm.jso.JSBody;

public class Minecraft {
    private static Minecraft instance;
    private final String windowTitle;
    private boolean running = true;

    public Minecraft(String title) {
        this.windowTitle = title;
        instance = this;
    }

    public static Minecraft getInstance() {
        return instance;
    }

    // Gọi API của trình duyệt để tạo vòng lặp mượt mà 60 FPS không gây nghẽn luồng
    @JSBody(params = { "tickRunnable" }, script = "" +
        "var runTick = function() {" +
        "    if (tickRunnable.runTickField()) {" +
        "        tickRunnable.runTickMethod();" +
        "        window.requestAnimationFrame(runTick);" +
        "    }" +
        "};" +
        "window.requestAnimationFrame(runTick);"
    )
    private static native void scheduleNextTick(Minecraft instance);

    // Thằng TeaVM cần các hàm/biến này để JS gọi ngược lại (Bridge)
    public boolean runTickField() {
        return this.running;
    }

    public void runTickMethod() {
        this.loopTick();
    }

    public void run() {
        GLFW.log("[Minecraft Core] Dang khoi dong engine: " + windowTitle);
        // Thử nghiệm kết nối mạng ngay khi vào game (Ví dụ kết nối tới cổng proxy local 8080)
        net.minecraft.network.WebNetworkManager.connect("127.0.0.1", 8080);
        // Thay vì dùng vòng lặp while, ta đẩy cho trình duyệt quản lý loop
        scheduleNextTick(this);
    }

    // Khung hình xử lý thực tế của mỗi Frame
    private void loopTick() {
        if (GLFW.glfwWindowShouldClose(1L)) {
            this.running = false;
            return;
        }

        // Gọi hàm vẽ đồ họa
        render();

        // Bắt các sự kiện chuột/bàn phím
        GLFW.glfwPollEvents();
    }

    private void render() {
        // Xóa màn hình bằng màu nền đặc trưng (Màu đỏ gạch/nâu của Minecraft)
        GL11.glClearColor(0.27f, 0.18f, 0.13f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void stop() {
        this.running = false;
    }
}