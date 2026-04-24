package a_supply_chain_management_system;

import java.util.ArrayList;
import java.util.List;

public class ProductDesign {

    private String productName;
    private double productPrice;
    private List<RawMaterial> requiredMaterials;
    private String byproductName;
    private int byproductAmount;
    private double byproductCost;
    private Byproduct byproduct;

    public ProductDesign(String productName, double productPrice, String byproductName, int byproductAmount, double byproductCost) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.byproductName = byproductName;
        this.byproductAmount = byproductAmount;
        this.byproductCost = byproductCost;
        this.byproduct = new Byproduct(byproductName, byproductCost, byproductAmount);
        this.requiredMaterials = new ArrayList<>();
    }

    /**
     * Adds the Raw Material into the design.
     */
    public void addRequiredMaterial(RawMaterial material) {
        requiredMaterials.add(material);
    }

    //GETTERS
    /**
     * @return A list of required materials.
     */
    public List<RawMaterial> getMaterials() {
        return requiredMaterials;
    }

    /**
     * @return The Final Product.
     */
    public Product getOutput() {
        return new Product(productName, productPrice, 0);
    }

    public Byproduct getByproduct() {
        return byproduct;
    }
    public String getByproductName() { return byproductName; }
    public int getByproductAmount() {
        return byproductAmount;
    }
    public double getByproductCost() {
        return byproductCost;
    }
    public String getProductName() {
        return productName;
    }
    public double getProductPrice() {
        return productPrice;
    }
}