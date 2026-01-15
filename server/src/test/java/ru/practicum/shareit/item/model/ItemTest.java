package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest {

    @Test
    void testEqualsAndHashCode() {
        Item i1 = new Item();
        i1.setId(1L);
        i1.setName("Item");

        Item i2 = new Item();
        i2.setId(1L);
        i2.setName("Item");

        assertThat(i1).isEqualTo(i2);
        assertThat(i1.hashCode()).isEqualTo(i2.hashCode());
    }

    @Test
    void testToString() {
        Item item = new Item();
        item.setId(5L);

        assertThat(item.toString()).contains("Item");
    }
}