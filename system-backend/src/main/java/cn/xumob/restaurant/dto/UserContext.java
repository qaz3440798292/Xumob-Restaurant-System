package cn.xumob.restaurant.dto;

public class UserContext {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setType(String type) {
        CONTEXT.set(type);
    }

    public static String getType() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

}
