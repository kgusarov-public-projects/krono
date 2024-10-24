package org.kgusarov.krono.examples;

import org.kgusarov.krono.Krono;

public final class ExamplesJava {
    private ExamplesJava() {
    }

    public static void main(final String[] args) {
        final var krono = Krono.getEnCasual();
        final var result = krono.parse(
            """
                Yesterday I've read an interesting article about the history of the Roman Empire.
                It was written by a famous historian and it was published in 1999.
                
                The article was about the life of Julius Caesar and his role in the history of Rome.
                Julius Caesar was a great general and a brilliant politician who lived in the first century BC.
                12 July 100 BC – 15 March 44 BC, to be precise.
                
                A member of the First Triumvirate, Caesar led the Roman armies in the Gallic Wars before defeating
                his political rival Pompey in a civil war, and subsequently became dictator from 49 BC until
                his assassination in 44 BC. He played a critical role in the events that led to the demise of
                the Roman Republic and the rise of the Roman Empire.
                
                Many rulers in history became interested in the historiography of Caesar.
                Napoleon III wrote the scholarly work Histoire de Jules César, which was not finished.
                The second volume listed previous rulers interested in the topic.
                Charles VIII ordered a monk to prepare a translation of the Gallic Wars in 1480.
                
                Do not mistake him for caesar salad, though.
                
                A Caesar salad (also spelled Cesar, César and Cesare) is a green salad of romaine lettuce and
                croutons dressed with lemon juice (or lime juice), olive oil, eggs, Worcestershire sauce, anchovies,
                garlic, Dijon mustard, Parmesan and black pepper.
                The salad was created on July 4, 1924 by Caesar Cardini at Caesar's
                in Tijuana, Mexico, when the kitchen was overwhelmed and short on ingredients.
                It was originally prepared tableside, and it is still prepared tableside at the original venue.
                
                Tomorrow evening going to read something more.
                """
        );

        result.forEach(pr -> {
            final var prettyString = pr.toPrettyString();
            System.out.println(prettyString);
        });
    }
}
