package com.trendyol.kubernetesoperatorapi.base;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.Arrays;

import static org.mockito.Mockito.inOrder;

public class OrderTest extends BaseTest {

    protected InOrder inOrder;

    @BeforeEach
    void setUpInOrder() {
        inOrder = inOrder(Arrays.stream(this.getClass().getDeclaredFields())
                .filter(field -> (field.isAnnotationPresent(Mock.class) || field.isAnnotationPresent(Spy.class)))
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        return field.get(this);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).toArray());
    }
}
