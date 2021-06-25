package com.transformer;

import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.TransformerSupplier;
import org.apache.kafka.streams.processor.ProcessorContext;

public class MyTransformerSupplier implements TransformerSupplier {


    @Override
    public Transformer get() {
        return new Transformer() {
            @SuppressWarnings("unchecked")
            @Override
            public void init(ProcessorContext processorContext) {
                System.out.println(processorContext);
            }

            @Override
            public Object transform(Object o, Object o2) {
                return o;
            }

            @Override
            public void close() {

            }
        };
    }

}
