package models;

public class User {
    public String name;
    public String email;
    public String address;
    public Cart cart;
    public User( String name, String email, String address) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.cart = new Cart();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public Cart getCart() {
        return cart;
    }

}
