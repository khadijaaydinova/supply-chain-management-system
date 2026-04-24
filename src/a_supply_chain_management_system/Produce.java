package a_supply_chain_management_system;

public interface Produce{
    /**
     *Produces raw material/product in amount of 1.
     */
    void produce();
    /**
     * @param amount How many we want to produce.
     *Produces raw material/product in given amount.
     */
    void produce(int amount);
}
