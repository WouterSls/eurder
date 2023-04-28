package com.switchfully.eurder.components.orderComponent;

import com.switchfully.eurder.components.itemComponent.Item;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "item_groups")
public class ItemGroup {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_group_seq")
        @SequenceGenerator(name = "item_group_seq", sequenceName = "item_group_seq", allocationSize = 1)
        private Long id;
        @OneToOne
        @JoinColumn(name = "fk_item_id")
        Item item;
        @Column(name = "amount_ordered")
        int amountOrdered;
        @Column(name = "shipping_date")
        LocalDate shippingDate;
        @ManyToOne(fetch = FetchType.LAZY)
        private Order fk_order;


        public ItemGroup (Item item, int amountOrdered, LocalDate shippingDate){
            this.item = item;
            this.amountOrdered = amountOrdered;
            this.shippingDate = shippingDate;
        }

        public ItemGroup(){

        }

    public int getAmountOrdered() {
        return amountOrdered;
    }

    public Item getItem() {
        return item;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public Long getId(){
            return id;
    }

    public void setFk_order(Order fk_order) {
        this.fk_order = fk_order;
    }

}
