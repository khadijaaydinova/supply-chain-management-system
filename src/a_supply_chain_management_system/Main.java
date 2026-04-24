package a_supply_chain_management_system;

public class Main {

    public static void main(String[] args) {
        SupplyChainManager SCM = new SupplyChainManager();
        RawMaterial Cotton = new RawMaterial("Cotton", 6, 20);
        BuisnessEntity r1 = new RawMaterialProducer("Cotton Producer 01", Cotton, 300, 400, 20, 9000);
        SCM.addProducer((RawMaterialProducer)r1);

        System.out.println(r1.toString());

        System.out.println();
        System.out.println(r1.toString());

        System.out.println();
        System.out.println(r1.toString());

        Product phone1 = new Product("Phone", 300, 3);
        System.out.println(phone1.toString());




    }

}