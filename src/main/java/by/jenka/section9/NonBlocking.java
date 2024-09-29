package by.jenka.section9;

import java.util.concurrent.atomic.AtomicReference;

public class NonBlocking {

    public static void main(String[] args) {
        String oldName = "old name";
        String newName = "new name";

        AtomicReference<String> atomicReference = new AtomicReference<>(oldName);

        if (atomicReference.compareAndSet(oldName, newName)) {
            System.out.println("New value is " + atomicReference.get());
        } else {
            System.out.println("Nothing happened");
        }
    }
}
