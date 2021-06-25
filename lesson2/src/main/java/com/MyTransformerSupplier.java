package com;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.TransformerSupplier;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

final public class MyTransformerSupplier implements TransformerSupplier<String, String, KeyValue<String, String>> {

    private String someStoreName;

    public MyTransformerSupplier(String someStoreName) {
        this.someStoreName = someStoreName;
    }

    @Override
    public Transformer<String, String, KeyValue<String, String>> get() {
        return new Transformer<String, String, KeyValue<String, String>> () {

            private KeyValueStore<String, String> store;

            @Override
            public void init(ProcessorContext processorContext) {
                store = (KeyValueStore<String, String>) processorContext.getStateStore(someStoreName);
            }

            @Override
            public KeyValue<String, String> transform(String s, String s2) {
                store.put(s2, s2 + "_");
                return new KeyValue<>(s2, s2 + "_");
            }

            @Override
            public void close() {

            }
        };
    }
}
