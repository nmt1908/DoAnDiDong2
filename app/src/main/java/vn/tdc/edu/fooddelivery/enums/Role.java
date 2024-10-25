package vn.tdc.edu.fooddelivery.enums;

public enum Role {
    ADMIN(1, "admin"),
    SHIPPER(2, "shipper"),
    CUSTOMER(3, "customer");

    private String role;
    private int id;

    Role(int id, String name) {
        this.role = name;
        this.id = id;
    }

    public String getName() {
        return role;
    }
    public int getId() {return id;}
}

