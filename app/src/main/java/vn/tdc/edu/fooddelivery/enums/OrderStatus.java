package vn.tdc.edu.fooddelivery.enums;

public enum OrderStatus {
    CHUA_XU_LY(1, "Chưa xử lý"),
    DANG_GIAO_HANG(2,"Đang giao hàng"),
    DA_GIAO(3, "Giao thành công"),
    DA_HUY(4, "Đã huỷ");

    private int status;

    private String name;

    OrderStatus(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }
}
